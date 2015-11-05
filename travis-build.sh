#!/bin/bash
set -ev

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
	./gradlew clean amplify:check amplify:test example:assembleDebug
else
	./gradlew clean amplify:checkAndReport amplify:test example:assembleDebug -PgitHubAuthToken="${PR_BOT_AUTH_TOKEN}" -PgitHubIssueNumber="${TRAVIS_PULL_REQUEST}"
fi