import json
import unittest
from unittest.mock import patch

import strava_breach_checker as checker


class StravaBreachCheckerTests(unittest.TestCase):
    def test_validate_email_normalizes_case_and_spaces(self):
        self.assertEqual(
            checker.validate_email("  Athlete@Example.COM "),
            "athlete@example.com",
        )

    def test_validate_email_rejects_password_like_input(self):
        with self.assertRaises(ValueError):
            checker.validate_email("my-strava-password")

    def test_mask_email_keeps_domain_and_small_local_part_private(self):
        self.assertEqual(checker.mask_email("athlete@example.com"), "a*****e@example.com")
        self.assertEqual(checker.mask_email("a@example.com"), "a*@example.com")

    def test_parse_breach_response_supports_public_response_shape(self):
        payload = {"breaches": [["Twitter", "Twitter", "Strava"]]}
        self.assertEqual(
            checker.parse_breach_response(payload),
            ("Strava", "Twitter"),
        )

    def test_parse_breach_response_supports_summary_objects(self):
        payload = {
            "BreachesSummary": [
                {"breach": "Example Service"},
                {"name": "Another Service"},
            ]
        }
        self.assertEqual(
            checker.parse_breach_response(payload),
            ("Another Service", "Example Service"),
        )

    @patch("strava_breach_checker.urlopen")
    def test_check_email_builds_request_and_returns_report(self, mock_urlopen):
        class FakeResponse:
            def __enter__(self):
                return self

            def __exit__(self, *_args):
                return False

            def read(self):
                return json.dumps({"breaches": [["Example Service"]]}).encode()

        mock_urlopen.return_value = FakeResponse()
        report = checker.check_email("athlete@example.com")
        request = mock_urlopen.call_args.args[0]

        self.assertEqual(report.breaches, ("Example Service",))
        self.assertIn("athlete%40example.com", request.full_url)
        self.assertEqual(mock_urlopen.call_args.kwargs["timeout"], 15)


if __name__ == "__main__":
    unittest.main()