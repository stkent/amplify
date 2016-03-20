# Change Log

## v1.1.0

_2016-03-20_

- `VersionChangedRule` has been removed and replaced with `VersionNameChangedRule` and `VersionCodeChangedRule` to allow library consumers to decide which dimension of the embedding application version to track. Events of these types are registered using the new methods `addLastEventVersionNameRule` and `addLastEventVersionCodeRule` respectively. `applyAllDefaultRules` now uses `VersionNameChangedRule` internally for consistency with _amplify_ v1.0.0.
- Added new minimum count rule. When this rule is applied, the feedback prompt will only be shown if the associated event has occurred at _least_ a given number of times.
- Moved the `GooglePlayStoreRule` into the correct package (`com.github.stkent.amplify.tracking.rules`).

## v1.0.0

_2016-03-18_

- Initial public release!
- **NOTE**: Upgrading to this version (or later) of _amplify_ from a pre-release version will reset all tracking information for certain built-in events (app crashed; user gave/declined to give positive/critical feedback). We will endeavor to avoid this in all post-1.0.0 builds.