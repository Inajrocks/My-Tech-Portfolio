import os
import secrets

from flask import Flask, jsonify, request
from werkzeug.security import generate_password_hash


app = Flask(__name__)
app.secret_key = os.environ.get("FLASK_SECRET_KEY", os.urandom(32))

EQUIPMENT_PRICES = {
    "Bulldozer": 500,
    "Excavator": 450,
    "Crane": 800,
}


USERS_DB = {
	"admin": {
		"password_hash": generate_password_hash(os.environ.get("ADMIN_PASSWORD", secrets.token_urlsafe(24))),
		"role": "admin",
		"api_key": os.environ.get("ADMIN_API_KEY", secrets.token_urlsafe(32)),
	},
	"alice": {
		"password_hash": generate_password_hash(os.environ.get("ALICE_PASSWORD", secrets.token_urlsafe(24))),
		"role": "user",
		"api_key": os.environ.get("ALICE_API_KEY", secrets.token_urlsafe(32)),
	},
	"bob": {
		"password_hash": generate_password_hash(os.environ.get("BOB_PASSWORD", secrets.token_urlsafe(24))),
		"role": "user",
		"api_key": os.environ.get("BOB_API_KEY", secrets.token_urlsafe(32)),
	},
	"charlie": {
		"password_hash": generate_password_hash(os.environ.get("CHARLIE_PASSWORD", secrets.token_urlsafe(24))),
		"role": "guest",
		"api_key": os.environ.get("CHARLIE_API_KEY", secrets.token_urlsafe(32)),
	},
}


def require_api_key():
	"""Return the username for a valid Bearer token, otherwise None."""
	authorization = request.headers.get("Authorization", "")
	scheme, _, token = authorization.partition(" ")
	if scheme.lower() != "bearer" or not token:
		return None

	for username, user_data in USERS_DB.items():
		if token == user_data["api_key"]:
			return username
	return None


def require_role(username, required_role):
	"""Enforce the required role using least-privilege checks."""
	user_data = USERS_DB.get(username)
	return bool(user_data and user_data["role"] == required_role)


@app.route("/api/v1/rentals", methods=["GET"])
def get_rentals():
	username = require_api_key()
	if username is None:
		return jsonify({"status": "error", "message": "Authentication required"}), 401

	return jsonify({"status": "success", "data": []})


@app.route("/api/v1/admin/users", methods=["GET"])
def admin_get_all_users():
	username = require_api_key()
	if username is None:
		return jsonify({"status": "error", "message": "Authentication required"}), 401
	if not require_role(username, "admin"):
		return jsonify({"status": "error", "message": "Forbidden"}), 403

	return jsonify({"status": "success", "data": list(USERS_DB)})
