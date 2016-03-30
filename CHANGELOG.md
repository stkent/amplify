# Change Log

## v1.2.0

_2016-03_

- Subclasses of `BasePromptView` (which include the packaged `DefaultLayoutPromptView` and `CustomLayoutPromptView` classes) now properly save and restore state through orientation changes. This change induced several other structural adjustments.
- **NOTE**: The method `Amplify.get(Context)` has been replaced by two separate methods:
	- `Amplify.initialize(Application)`, which should be called when performing initial library configuration in your custom `Application` subclass;
	- `Amplify.getSharedInstance()`, which can be called thereafter to obtain a reference to your configured `Amplify` instance.
- **NOTE**: The methods `Amplify.promptIfReady(Activity, IPromptView)` and `Amplify.promptIfReady(Activity, IPromptView, IEventListener<PromptViewEvent>)` have been replaced by the method `Amplify.promptIfReady(IPromptView)`. Users who wish to listen for promp-related events should now register an `IEventListener` implementation with their `PromptView` before calling `Amplify.promptIfReady`. This can be accomplished using the `BasePromptView.
(IEventListener)` method.
- **NOTE**: Any user-provided implementations of `IPromptView` that wish to properly save and restore state through orientation changes should mimic the implementation used in the `BasePromptView` class. (It is expected that almost all library users will be using a packaged subclass of `BasePromptView`, and can therefore safely ignore this.)
- **NOTE**: Any user-provided implementations of `IPromptView` must prepare an associated `PromptPresenter` instance in their constructors, rather than receiving an instance via the (now-removed) `IPromptView.setPresenter(IPromptPresenter)` method. Library users can refer to the `BasePromptView` class as an example.
- Fixed memory leak that occurred during device configuration changes.

The README has been updated to reflect all of the above changes.

## v1.1.0

_2016-03-20_

- `VersionChangedRule` has been removed and replaced with `VersionNameChangedRule` and `VersionCodeChangedRule` to allow library consumers to decide which dimension of the embedding application version to track. Events of these types are registered using the new methods `addLastEventVersionNameRule` and `addLastEventVersionCodeRule` respectively. `applyAllDefaultRules` now uses `VersionNameChangedRule` internally for consistency with _amplify_ v1.0.0.
- Added new minimum count rule. When this rule is applied, the feedback prompt will only be shown if the associated event has occurred at _least_ a given number of times.
- Moved the `GooglePlayStoreRule` into the correct package (`com.github.stkent.amplify.tracking.rules`).

## v1.0.0

_2016-03-18_

Initial public release!

- **NOTE**: Upgrading to this version (or later) of _amplify_ from a pre-release version will reset all tracking information for certain built-in events (app crashed; user gave/declined to give positive/critical feedback). We will endeavor to avoid this in all post-1.0.0 builds.