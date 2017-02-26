# Change Log

## v2.0.0

_2017-02-26_

- [Added] Ability to specify feedback collection methods via `Amplify.setPositiveFeedbackCollectors` and `Amplify.setCriticalFeedbackCollectors`. Prior to this release, positive feedback was always collected via the Google Play Store, and critical feedback via email. This expanded functionality replaces the more limited functionality previously offered by `Amplify.setFeedbackEmailAddress`, `Amplify.setFeedbackEmailContentProvider`, and `Amplify.setPackageName` and those methods have therefore been removed.
- [Added] Ability to specify multiple feedback collection methods in priority order. If the highest priority collector is unable to collect feedback on the current device, the next highest priority collector is tried, etc.
- [Added] Built-in support for collecting feedback via the Amazon App Store. See the new `AmazonAppStoreFeedbackCollector` class.
- [Changed] Default feedback email template now includes more information about the user device.
- [Changed] Tweaked the example app to make it feel like it belongs to this project (new name and icon!).
- [Internal] Simplified internal models and updated them to be more representative of the problem space rather than of the solution space. This internal restructuring was significant, but not much should leak into consumer code. If you notice any changes not captured in this CHANGELOG entry that have affected your consumption of the library, please open an issue that describes the change and its impact.

## v1.5.1

_2017-01-02_

- Fix crash that could occur when restoring saved prompt view state.

## v1.5.0

_2016-06-07_

- **NOTE**: The method `Amplify.setLogLevel(LogLevel)` has been removed. Logging is now enabled by calling `Amplify.setLogger(ILogger)` before any other configuration occurs.
- The subject line and pre-filled body for critical feedback emails can now be customized.

## v1.4.0

_2016-06-06_

- Prompt views being rendered by Android Studio's Layout Editor Preview no longer cause an `IllegalStateException` (issue #152);
- Users can now customize more attributes of the `DefaultLayoutPromptView` in both xml and code:
  - Text size;
  - Button border width;
  - Button corner radius.

## v1.3.1

_2016-04-12_

- `BasePromptView` subclasses now properly restore all state after configuration changes:
  - Fixed crash on orientation change when attempting to unpack saved state (issue #146; affects v1.2.0 and v1.3.0);
  - Fixed `BasePromptView` configuration being reset to default on orientation change (issue #148).

## v1.3.0

_2016-04-06_

- A new initialization method, `Amplify.initSharedInstance(Application, String)`, has been added. The second `String` parameter represents the name of the `SharedPreferences` instance that will be used to store and retrieve tracking data. `Amplify.initSharedInstance(Application)` works exactly as before, with tracking data stored in the default `SharedPreferences` instance named `"AMPLIFY_SHARED_PREFERENCES_NAME"`.
- Subclasses of `BasePromptView` (which include the packaged `DefaultLayoutPromptView` and `CustomLayoutPromptView` classes) can now be configured to automatically fade out the displayed thanks view after a delay. The length of this delay may be specified using the xml attribute `prompt_view_thanks_display_time_ms` or by calling the method `setThanksDisplayTimeMs(int)` when building a  `BasePromptViewConfig` instance programmatically. The default behavior is for the thanks view to display indefinitely.
- Fixed the base prompt view config not being saved/restored during configuration changes.

## v1.2.0

_2016-03-31_

- Subclasses of `BasePromptView` (which include the packaged `DefaultLayoutPromptView` and `CustomLayoutPromptView` classes) now properly save and restore state through orientation changes. This change induced several other structural adjustments.
- **NOTE**: The method `Amplify.get(Context)` has been replaced by two separate methods:
	- `Amplify.initSharedInstance(Application)`, which should be called when performing initial library configuration in your custom `Application` subclass;
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
