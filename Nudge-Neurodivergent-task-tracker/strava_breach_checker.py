#!/usr/bin/env python3
"""
Strava account breach checker.

This tool checks the email address used for a Strava account against
XposedOrNot's public breach database. It does not ask for, collect, or test
Strava passwords or access tokens.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from dataclasses import dataclass
from typing import Any, Iterable
from urllib.error import HTTPError, URLError
from urllib.parse import quote
from urllib.request import Request, urlopen


API_BASE_URL = "https://api.xposedornot.com/v1/check-email/"
USER_AGENT = "StravaBreachChecker/1.0"
EMAIL_PATTERN = re.compile(r"^[^@\s]+@[^@\s]+\.[^@\s]+$")


class BreachCheckError(RuntimeError):
    """A user-facing error from the breach-check request."""


@dataclass(frozen=True)
class BreachReport:
    """Normalized breach results returned by the data provider."""

    email: str
    breaches: tuple[str, ...]
    source: str = "XposedOrNot"

    @property
    def found(self) -> bool:
        return bool(self.breaches)


def validate_email(value: str) -> str:
    """Normalize and validate an email address before sending it to the API."""
    email = value.strip().lower()
    if not EMAIL_PATTERN.fullmatch(email):
        raise ValueError("Please enter a valid email address, such as name@example.com.")
    return email


def mask_email(email: str) -> str:
    """Return a privacy-preserving display version of an email address."""
    local, domain = email.split("@", 1)
    if len(local) <= 2:
        masked_local = local[0] + "*" * max(1, len(local) - 1)
    else:
        masked_local = local[0] + "*" * (len(local) - 2) + local[-1]
    return f"{masked_local}@{domain}"


def _as_breach_names(value: Any) -> Iterable[str]:
    """Extract names from the API's list-based or object-based response shapes."""
    if isinstance(value, list):
        for item in value:
            if isinstance(item, str) and item.strip():
                yield item.strip()
            elif isinstance(item, list):
                yield from _as_breach_names(item)
            elif isinstance(item, dict):
                yield from _as_breach_names(item)
    elif isinstance(value, dict):
        for key in ("breach", "name", "title", "domain"):
            item = value.get(key)
            if isinstance(item, str) and item.strip():
                yield item.strip()
                return


def parse_breach_response(payload: Any) -> tuple[str, ...]:
    """Normalize the provider response and remove duplicate names."""
    if not isinstance(payload, dict):
        raise BreachCheckError("The breach service returned an unexpected response.")

    candidates: list[str] = []
    for key in ("breaches", "BreachesSummary", "breach", "data"):
        if key in payload:
            candidates.extend(_as_breach_names(payload[key]))

    deduplicated: list[str] = []
    seen: set[str] = set()
    for name in candidates:
        comparison_key = name.casefold()
        if comparison_key not in seen:
            seen.add(comparison_key)
            deduplicated.append(name)
    return tuple(sorted(deduplicated, key=str.casefold))


def check_email(email: str, timeout: int = 15) -> BreachReport:
    """Check an email address against the public breach-data endpoint."""
    normalized_email = validate_email(email)
    url = API_BASE_URL + quote(normalized_email, safe="")
    request = Request(
        url,
        headers={
            "Accept": "application/json",
            "User-Agent": USER_AGENT,
        },
        method="GET",
    )

    try:
        with urlopen(request, timeout=timeout) as response:
            payload = json.loads(response.read().decode("utf-8"))
    except HTTPError as error:
        if error.code == 404:
            return BreachReport(email=normalized_email, breaches=())
        if error.code in (429, 500, 502, 503, 504):
            raise BreachCheckError(
                "The breach service is temporarily unavailable. Please try again later."
            ) from error
        raise BreachCheckError(
            f"The breach service rejected the request (HTTP {error.code})."
        ) from error
    except (URLError, TimeoutError) as error:
        raise BreachCheckError(
            "Could not reach the breach service. Check your connection and try again."
        ) from error
    except (json.JSONDecodeError, UnicodeDecodeError) as error:
        raise BreachCheckError(
            "The breach service returned invalid data. Please try again later."
        ) from error

    return BreachReport(
        email=normalized_email,
        breaches=parse_breach_response(payload),
    )


def print_report(report: BreachReport, show_email: bool = False) -> None:
    """Print a clear, privacy-conscious report."""
    displayed_email = report.email if show_email else mask_email(report.email)
    print("\n" + "=" * 64)
    print("STRAVA ACCOUNT BREACH CHECK")
    print("=" * 64)
    print(f"Email checked: {displayed_email}")
    print(f"Data source:   {report.source}")

    if report.found:
        print("\nRESULT: This email appears in known breach data.")
        print(f"Known breach sources found: {len(report.breaches)}")
        print("\nReported sources:")
        for breach in report.breaches:
            print(f"  • {breach}")
        print("\nRecommended next steps:")
        print("  1. Change your Strava password and any other reused password.")
        print("  2. Turn on two-factor authentication where available.")
        print("  3. Review Strava sessions, connected apps, and recent activity.")
        print("  4. Treat unexpected emails or reset links as suspicious.")
    else:
        print("\nRESULT: No known breach matches were found for this email.")
        print("This is not proof that the account is safe: breach databases")
        print("can be incomplete, delayed, or unable to identify every incident.")

    print("\nImportant: this checks the login email against public breach data.")
    print("The email is sent to the listed breach-data provider to perform the check.")
    print("It does not test your Strava password, log in to Strava, or prove")
    print("that Strava itself was the source of a breach.")
    print("=" * 64)


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        description=(
            "Check the email used for a Strava account against known breach data. "
            "Never enter a Strava password or access token."
        )
    )
    parser.add_argument(
        "--email",
        help="The email address used for Strava. If omitted, you will be prompted.",
    )
    parser.add_argument(
        "--show-email",
        action="store_true",
        help="Show the full email in the report instead of masking it.",
    )
    parser.add_argument(
        "--json",
        action="store_true",
        dest="as_json",
        help="Print machine-readable JSON instead of the formatted report.",
    )
    parser.add_argument(
        "--timeout",
        type=int,
        default=15,
        help="Network timeout in seconds (default: 15).",
    )
    return parser


def prompt_for_email() -> str:
    print("Strava Account Breach Checker")
    print("This tool only needs the email address used for Strava.")
    print("Never enter your Strava password, recovery code, or access token.")
    return input("\nEmail used for Strava: ")


def main(argv: list[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)
    if args.timeout < 1:
        parser.error("--timeout must be at least 1 second.")

    raw_email = args.email if args.email is not None else prompt_for_email()
    try:
        report = check_email(raw_email, timeout=args.timeout)
    except ValueError as error:
        print(f"\nInput error: {error}", file=sys.stderr)
        return 2
    except BreachCheckError as error:
        print(f"\nCheck failed: {error}", file=sys.stderr)
        return 1

    if args.as_json:
        print(
            json.dumps(
                {
                    "email": report.email if args.show_email else mask_email(report.email),
                    "breaches_found": report.found,
                    "breach_count": len(report.breaches),
                    "breaches": list(report.breaches),
                    "source": report.source,
                    "note": (
                        "A result is not proof of a Strava-specific breach; "
                        "no result is not proof of safety."
                    ),
                },
                indent=2,
            )
        )
    else:
        print_report(report, show_email=args.show_email)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())