#!/bin/bash
set -ev

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
	./gradlew clean check coveralls
else
	./gradlew clean checkAndReport coveralls -PgitHubAuthToken="${PR_BOT_AUTH_TOKEN}" -PgitHubIssueNumber="${TRAVIS_PULL_REQUEST}"
fi