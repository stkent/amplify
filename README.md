<p align="center">
	<img src="assets/logo.png" width="150px" />
</p>

# amplify

Respectfully request feedback in your Android app.

<a href="https://travis-ci.org/stkent/amplify"><img src="https://travis-ci.org/stkent/amplify.svg" /></a> [ ![Download](https://api.bintray.com/packages/stkent/android-libraries/amplify/images/download.svg) ](https://bintray.com/stkent/android-libraries/amplify/_latestVersion) <a href="http://www.detroitlabs.com/"><img src="https://img.shields.io/badge/Sponsor-Detroit%20Labs-000000.svg" /></a> [![Coverage Status](https://coveralls.io/repos/github/stkent/amplify/badge.svg?branch=main)](https://coveralls.io/github/stkent/amplify?branch=main) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-amplify-green.svg?style=true)](https://android-arsenal.com/details/1/3290)

# Guide

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [Development Status](#development-status)
- [Introduction](#introduction)
- [Library Structure](#library-structure)
- [Getting Started](#getting-started)
  - [Default Behavior](#default-behavior)
- [Configuring](#configuring)
  - [Prompt Timing](#prompt-timing)
  - [Prompt UI](#prompt-ui)
  - [Feedback Collection](#feedback-collection)
- [Customizing](#customizing)
  - [Prompt Timing](#prompt-timing-1)
  - [Prompt UI](#prompt-ui-1)
  - [Feedback Emails](#feedback-emails)
- [Debug Builds](#debug-builds)
  - [Logging](#logging)
  - [Package Name Override](#package-name-override)
  - [Visibility](#visibility)
- [Resetting](#resetting)
- [Case Studies](#case-studies)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Development Status

**Maintained**

- Not currently under active development.
- Active development may resume in the future.
- Bug reports will be triaged and fixed. No guarantees are made regarding fix timelines.
- Feature requests will be triaged. No guarantees are made regarding acceptance or implementation timelines.
- Pull requests from external contributors are not currently being accepted.

# Introduction

_amplify_ focuses on helping Android developers prompt their users for feedback at the right times and in the right way. Inspired by [Circa News](https://medium.com/circa/the-right-way-to-ask-users-to-review-your-app-9a32fd604fca), we built this library based on the following principles: 

- **No interruptions.** The inline prompt we provide can be inserted right into your view hierarchy and customized to complement your existing UI. Users are free to interact with the prompt as much or as little as they like. This approach shows respect for your users and preserves the app flow you have carefully crafted.

- **No nagging.** _amplify_ intelligently tracks significant events to make sure your users are only prompted for feedback at appropriate times.

- **Maximum impact.** When users indicate they are willing to provide feedback, we direct them to the highest-impact outlet:

    - Users with positive feedback are asked to leave a quick rating or review in the Google Play Store, improving the rating and discoverability of your app. All of these ratings and reviews reflect genuine user experiences - _amplify_ just makes it easier for happy customers to choose to share their appreciation.

    - Users with critical feedback are instead asked to send a more detailed email that will automatically include pertinent app and device information. This gives you an opportunity to engage these users in a more meaningful dialogue, allowing you to better understand and accommodate their feedback.

- **Easy to integrate.** Default prompt timing rules allow you to get up and running as quickly as possible.

- **Easy to customize.** Use both built-in and custom events to create a collection of prompt timing rules. Tweak the provided inline prompt UI via XML or in code.

# Library Structure

_amplify_ consists of two main elements:

- A **prompt timing calculator**, represented by the `Amplify` class, responsible for:

    - verifying that the current device provides the required feedback channels (the Google Play Store + at least one email application);

    - tracking occurrences of significant events and evaluating rules based on these events to determine appropriate times to ask for feedback (we occasionally refer to this portion of the library as the **event-tracking engine**);

- Customizable **inline feedback prompts**, represented by the `DefaultLayoutPromptView` and `CustomLayoutPromptView` classes, that guide users through the flow depicted below to determine the appropriate outlet for their feedback:

<br />

<p align="center">
	<img src="https://raw.githubusercontent.com/stkent/amplify/main/assets/flow.png" width="60%" />
</p>

| Positive feedback flow            | Critical feedback flow            |
|-----------------------------------|-----------------------------------|
| <img src="https://raw.githubusercontent.com/stkent/amplify/main/assets/positive-feedback.gif" /> | <img src="https://raw.githubusercontent.com/stkent/amplify/main/assets/critical-feedback.gif" /> |

<br />

These components are designed to complement each other, and combining them as described in the [Getting Started](#getting-started) section below is the easiest way to integrate _amplify_ into your application. However, it is perfectly possible to couple the prompt timing calculator with your own prompt flow and UI if desired. (This could be useful if you have highly-customized requirements for the prompt flow.) If you pursue this route, we would encourage you to ensure your prompting mechanism still reflects the principles outlined in the [Introduction](#introduction).

# Getting Started

<ol>
  <li>Specify <em>amplify</em> as a dependency in your <code>build.gradle</code> file:</li>
</ol>

```groovy
dependencies {
    implementation("com.github.stkent:amplify:2.2.3")
}
```

<ol start="2">
  <li>Initialize the shared <code>Amplify</code> instance in your custom <code><a href="http://developer.android.com/reference/android/app/Application.html">Application</a></code> subclass, supplying feedback collectors that determine where positive and critical feedback should be directed:</li>
</ol>

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .applyAllDefaultRules();
    }
    
}
```

<ol start="3">
  <li>Add a <code>DefaultLayoutPromptView</code> instance to all XML layouts in which you may want to prompt the user for their feedback:</li>
</ol>

```xml
<com.github.stkent.amplify.prompt.DefaultLayoutPromptView
    android:id="@+id/prompt_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

<ol start="4">
  <li>Get the shared <code>Amplify</code> instance and call its <code>promptIfReady</code> method when appropriate, passing in your <code>DefaultLayoutPromptView</code> instance:</li>
</ol>

```java
public class ExampleActivity extends Activity {

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        /*
         * Be careful: don't re-prompt after a configuration change!
         * The provided prompt view classes handle saving and restoring their state.
         * Perform this check in onCreateView or onViewCreated if using a Fragment.
         */
        if (savedInstanceState == null) {
            DefaultLayoutPromptView promptView
                    = (DefaultLayoutPromptView) findViewById(R.id.prompt_view);

            Amplify.getSharedInstance().promptIfReady(promptView);
        }
    }

}
```

That's it! The prompt timing calculator will evaluate the default rules each time `promptIfReady` is called, and instruct the `PromptView` to automatically update its state based on the result. If the user chooses to interact with the prompt, the sequence of questions asked is also automatically managed. If the user decides to give feedback, _amplify_ will handle opening the appropriate Google Play Store page or email client with pre-populated details.

## Default Behavior

The convenience method `applyAllDefaultRules` initializes the prompt timing calculator with a collection of sensible default rules. With these rules applied, we only prompt for feedback when:

- **The Google Play Store is available.** If a user's device won't allow them to provide feedback, we never ask for it. (We believe that a high enough percentage of devices are capable of sending email that a similar check for the availability of an email application is unnecessary.)

- **It has been more than a week since a new version of your app was installed.** We like to give users some time to settle in and explore the changes made in the latest update before asking them their opinion... but not so much time that their valuable first impressions are forgotten!

- **It has been more than a week since your app last crashed.** There are much better ways to collect detailed crash information than via user feedback. We're big fans of [Fabric/Crashlytics](https://fabric.io/kits/android/crashlytics). To save users from spending time reporting crashes that we are already aware of and fixing, we avoid asking for feedback right after a crash has occurred.

- **The user has never previously provided positive feedback.** We strive to constantly improve our apps' functionality and stability. If we do our job right, there's little to be gained by prompting satisfied users for feedback repeatedly. If we decide to significantly overhaul our app (either internally or externally), we will [reset](#resetting) the prompt timing calculator to capture feedback from our entire user base again.

- **The user has not provided critical feedback for this version of the application already.** Since it's unlikely that we'll be able to address any critical feedback received without releasing an update, we won't re-prompt a user who already provided insights into the current version of the app.

- **The user has not actively declined to provide feedback for this version of the application.** If a user has already actively indicated they are not interested in providing feedback for the current version of the app, we won't ask again before the next update is shipped. (Note that 'actively indicated' here means a user deliberately declined to provide feedback. This rule does not apply to users who have seen but did not interact with the prompt in any way.)

More information on how to apply your own collection of rules is available in the [Configuring](#configuring) section. Building custom rules is covered in the [Customizing](#customizing) section.

# Configuring

## Prompt Timing

_amplify_ calculates prompt timing based on two types of rule.

### Environment-Based Rules

These rules are based on the environment/device in which the embedding application is currently running. For example, they may query whether or not the current device is capable of handling a specific [`Intent`](http://developer.android.com/reference/android/content/Intent.html).

_amplify_ is packaged with the following environment-based rules:

- `GooglePlayStoreRule`: verifies whether or not the Google Play Store is installed on the current device.
- `AmazonAppStoreRule`: verifies whether or not the Amazon App Store is installed on the current device.

Environment-based rules can be applied by calling the `addEnvironmentBasedRule` method when configuring your `Amplify` instance. For example:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .addEnvironmentBasedRule(new GooglePlayStoreRule()); // Prompt never shown if Google Play Store not installed.
    }
    
}
```

### Event-Based Rules

These rules are based on tracked events that occur within the embedding application. Different dimensions of these events can be tracked (time of first/most recent occurrence, total number of occurrences, etc.)

The **times** of the following special events are automatically tracked whenever _amplify_ is enabled:

- original app install (note: this can pre-date the time at which _amplify_ was added to your application!);
- last app update time;
- last app crash time;

Rules related to each of these events can be configured using the dedicated configuration methods `setInstallTimeCooldownDays`, `setLastUpdateTimeCooldownDays`, and `setLastCrashTimeCooldownDays`. For example:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .setInstallTimeCooldownDays(14)   // Prompt not shown within two weeks of initial install.
               .setLastUpdateTimeCooldownDays(7) // Prompt not shown within one week of most recent update.
               .setLastCrashTimeCooldownDays(7); // Prompt not shown within one week of most recent crash.
    }
    
}
```

The following events are also automatically reported to the shared `Amplify` instance whenever you use the `promptIfReady` method to show your prompt:

- prompt was shown;
- user indicated a positive opinion of the app;
- user indicated a critical opinion of the app;
- user agreed to give feedback (either positive or critical);
- user declined to give feedback (either positive or critical);
- user agreed to give positive feedback;
- user agreed to give critical feedback;
- user declined to give positive feedback;
- user declined to give critical feedback;
- thanks view was shown;
- prompt was auto-dismissed.

To apply rules based on these events, use the configuration methods `addTotalEventCountRule`, `addFirstEventTimeRule`, `addLastEventTimeRule`, `addLastEventVersionCodeRule`, and `addLastEventVersionNameRule`. The method you select will determine which dimension of the event is tracked using `SharedPreferences`. Each method accepts two parameters:

- the event to be tracked; in this case, one of the events defined in the `PromptViewEvent` enum;
- the event-based rule to be applied to that tracked dimension.

_amplify_ is packaged with the following event-based rules:

- `CooldownDaysRule`: checks whether enough time has elapsed since the last occurrence of this event.
- `MaximumCountRule`: checks whether this event has occurred fewer than N times, for some number N.
- `MinimumCountRule`: checks whether this event has occurred at least N times, for some number N.
- `VersionCodeChangedRule`: checks whether this event has already occurred for the current version _code_ of the embedding application.
- `VersionNameChangedRule`: checks whether this event has already occurred for the current version _name_ of the embedding application.
- `WarmupDaysRule`: checks whether enough time has elapsed since the first occurrence of this event.

An example configuration that leverage these rules is below:

TODO: add a few more example rules to this snippet!

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .addTotalEventCountRule(PromptInteractionEvent.USER_GAVE_POSITIVE_FEEDBACK,
                        new MaximumCountRule(1)) // Never ask the user for feedback again if they already responded positively.
    }
    
}
```                

## Prompt UI

_amplify_ provides two configurable prompt UIs. The test app associated with this project includes examples of these prompts with:

- no custom configuration applied;
- distinct colors and strings set on every attribute in XML;
- distinct colors and strings set on every attribute in code;

| Test app UI; use me to explore!  |
|----------------------------------|
| <img src="https://raw.githubusercontent.com/stkent/amplify/main/assets/app.png" width="50%" /> |

### Default Layout

**Use this if you are happy with the basic layout of the prompt shown above, but need to customize colors or wording!**

Provided by the `DefaultLayoutPromptView` class. The basic layouts of the questions and thanks presented to users of the embedding application are fixed, but the most important elements of those layouts (colors and text) are fully customizable. This prompt view can also be configured to auto-dismiss the thanks view after a given delay in milliseconds. The full set of available XML configuration hooks is shown below (remember to use the `app` XML namespace when setting these properties!):

```xml
<com.github.stkent.amplify.prompt.DefaultLayoutPromptView
    android:id="@+id/prompt_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:prompt_view_user_opinion_question_title="Custom Title"
    app:prompt_view_user_opinion_question_subtitle="Custom Subtitle"
    app:prompt_view_user_opinion_question_positive_button_label="Custom Button Label"
    app:prompt_view_user_opinion_question_negative_button_label="Custom Button Label"
    app:prompt_view_positive_feedback_question_title="Custom Title"
    app:prompt_view_positive_feedback_question_subtitle="Custom Subtitle"
    app:prompt_view_positive_feedback_question_positive_button_label="Custom Button Label"
    app:prompt_view_positive_feedback_question_negative_button_label="Custom Button Label"
    app:prompt_view_critical_feedback_question_title="Custom Title"
    app:prompt_view_critical_feedback_question_subtitle="Custom Subtitle"
    app:prompt_view_critical_feedback_question_positive_button_label="Custom Button Label"
    app:prompt_view_critical_feedback_question_negative_button_label="Custom Button Label"
    app:prompt_view_thanks_title="Custom Title"
    app:prompt_view_thanks_subtitle="Custom Subtitle"
    app:prompt_view_foreground_color="@color/custom_foreground_color"
    app:prompt_view_background_color="@color/custom_background_color"
    app:prompt_view_title_text_color="@color/custom_title_text_color"
    app:prompt_view_subtitle_text_color="@color/custom_subtitle_text_color"
    app:prompt_view_positive_button_text_color="@color/custom_positive_button_text_color"
    app:prompt_view_positive_button_background_color="@color/custom_positive_button_background_color"
    app:prompt_view_positive_button_border_color="@color/custom_positive_button_border_color"
    app:prompt_view_negative_button_text_color="@color/custom_negative_button_text_color"
    app:prompt_view_negative_button_background_color="@color/custom_negative_button_background_color"
    app:prompt_view_negative_button_border_color="@color/custom_negative_button_border_color"
    app:prompt_view_thanks_display_time_ms="2000"
    app:prompt_view_text_size="@dimen/prompt_view_text_size_large"
    app:prompt_view_button_border_width="@dimen/prompt_view_button_border_width"
    app:prompt_view_button_corner_radius="@dimen/prompt_view_button_corner_radius" />
```

All attributes are optional. The most important are `prompt_view_foreground_color` and `prompt_view_background_color`. All other color attributes default to one of these two colors, so most use-cases can probably be supported by setting one or both of these attributes only.

It is also possible to configure this layout in code. To do so, users apply a `BasePromptViewConfig` and/or a `DefaultLayoutPromptViewConfig` to the view. Each configuration type can be constructed using a builder, which allows only the desired attributes to be overridden. Below shows an example in which every possible attribute is configured this way:

```java
DefaultLayoutPromptView promptView = (DefaultLayoutPromptView) findViewById(R.id.prompt_view);

final BasePromptViewConfig basePromptViewConfig
    = new BasePromptViewConfig.Builder()
        .setUserOpinionQuestionTitle("Custom Title")
        .setUserOpinionQuestionSubtitle("Custom Subtitle")
        .setUserOpinionQuestionPositiveButtonLabel("Custom Button Label")
        .setUserOpinionQuestionNegativeButtonLabel("Custom Button Label")
        .setPositiveFeedbackQuestionTitle("Custom Title")
        .setPositiveFeedbackQuestionSubtitle("Custom Subtitle")
        .setPositiveFeedbackQuestionPositiveButtonLabel("Custom Button Label")
        .setPositiveFeedbackQuestionNegativeButtonLabel("Custom Button Label")
        .setCriticalFeedbackQuestionTitle("Custom Title")
        .setCriticalFeedbackQuestionSubtitle("Custom Subtitle")
        .setCriticalFeedbackQuestionPositiveButtonLabel("Custom Button Label")
        .setCriticalFeedbackQuestionNegativeButtonLabel("Custom Button Label")
        .setThanksDisplayTimeMs(2000)
        .build();

final DefaultLayoutPromptViewConfig defaultLayoutPromptViewConfig
    = new DefaultLayoutPromptViewConfig.Builder()
        .setForegroundColor(Color.parseColor("#FF0000"))
        .setBackgroundColor(Color.parseColor("#FF9900"))
        .setTitleTextColor(Color.parseColor("#33FF00"))
        .setSubtitleTextColor(Color.parseColor("#00FFFF"))
        .setPositiveButtonTextColor(Color.parseColor("#CC00FF"))
        .setPositiveButtonBackgroundColor(Color.parseColor("#3300FF"))
        .setPositiveButtonBorderColor(Color.parseColor("#0066FF"))
        .setNegativeButtonTextColor(Color.parseColor("#FFFF00"))
        .setNegativeButtonBackgroundColor(Color.parseColor("#FF0000"))
        .setNegativeButtonBorderColor(Color.parseColor("#999999"))
        .setCustomTextSizePx(getResources()
            .getDimensionPixelSize(R.dimen.prompt_view_text_size_large))
        .setButtonBorderWidthPx(getResources()
            .getDimensionPixelSize(R.dimen.prompt_view_button_border_width))
        .setButtonCornerRadiusPx(getResources()
            .getDimensionPixelSize(R.dimen.prompt_view_button_corner_radius))
        .build();

promptView.applyBaseConfig(basePromptViewConfig);
promptView.applyConfig(defaultLayoutPromptViewConfig);
```

### Custom Layout

**Use this if you need to provide a structurally different prompt layout, require custom fonts, etc.**

Provided by the `CustomLayoutPromptView` class. You provide the basic layouts to use, and any customization of the default strings you require. This prompt view can also be configured to auto-dismiss the thanks view after a given delay in milliseconds. The full set of available XML configuration hooks is shown below (remember to use the `app` XML namespace when setting these properties!):

```xml
<com.github.stkent.amplify.prompt.CustomLayoutPromptView
    android:id="@+id/prompt_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:prompt_view_question_layout="@layout/include_amplify_question_layout"
    app:prompt_view_thanks_layout="@layout/include_amplify_thanks_layout"
    app:prompt_view_user_opinion_question_title="Custom Title"
    app:prompt_view_user_opinion_question_subtitle="Custom Subtitle"
    app:prompt_view_user_opinion_question_positive_button_label="Custom Button Label"
    app:prompt_view_user_opinion_question_negative_button_label="Custom Button Label"
    app:prompt_view_positive_feedback_question_title="Custom Title"
    app:prompt_view_positive_feedback_question_subtitle="Custom Subtitle"
    app:prompt_view_positive_feedback_question_positive_button_label="Custom Button Label"
    app:prompt_view_positive_feedback_question_negative_button_label="Custom Button Label"
    app:prompt_view_critical_feedback_question_title="Custom Title"
    app:prompt_view_critical_feedback_question_subtitle="Custom Subtitle"
    app:prompt_view_critical_feedback_question_positive_button_label="Custom Button Label"
    app:prompt_view_critical_feedback_question_negative_button_label="Custom Button Label"
    app:prompt_view_thanks_title="Custom Title"
    app:prompt_view_thanks_subtitle="Custom Subtitle"
    app:prompt_view_thanks_display_time_ms="2000" />
```

The `prompt_view_question_layout` attribute is **required** and subject to some additional requirements (listed below). All other attributes are optional. If `prompt_view_thanks_layout` is not provided, the prompt will automatically dismiss at the end of every flow. If it is provided, the user will see the thanks view whenever they agree to give feedback.

#### Included Question Layout Requirements

The layout referenced by `prompt_view_question_layout` _must_ include:

- A `TextView` subclass with id `amplify_title_text_view`;
- A `TextView` subclass with id `amplify_positive_button`;
- A `TextView` subclass with id `amplify_negative_button`.

If a view is found with an appropriate button id but it is _not_ a `TextView` subclass, the library will gracefully no-op when trying to set the button text.

The layout referenced by `prompt_view_question_layout` _may_ include:

   - A `TextView` subclass with id `amplify_subtitle_text_view`;
 
#### Included Thanks Layout Requirements 

The layout referenced by `prompt_view_thanks_layout ` _must_ include:

- A `TextView` subclass with id `amplify_title_text_view`.

The layout referenced by `prompt_view_thanks_layout ` _may_ include:

- A `TextView` subclass with id `amplify_subtitle_text_view`.

As before, it is also possible to configure the `CustomLayoutPromptView` in code. To do so, users apply a `CustomLayoutPromptViewConfig`, and optionally a `BasePromptViewConfig`, to the view. Below shows an example in which every possible attribute is configured this way:

```java
CustomLayoutPromptView promptView = (CustomLayoutPromptView) findViewById(R.id.prompt_view);

final BasePromptViewConfig basePromptViewConfig
    = new BasePromptViewConfig.Builder()
        .setUserOpinionQuestionTitle("Custom Title")
        .setUserOpinionQuestionSubtitle("Custom Subtitle")
        .setUserOpinionQuestionPositiveButtonLabel("Custom Button Label")
        .setUserOpinionQuestionNegativeButtonLabel("Custom Button Label")
        .setPositiveFeedbackQuestionTitle("Custom Title")
        .setPositiveFeedbackQuestionSubtitle("Custom Subtitle")
        .setPositiveFeedbackQuestionPositiveButtonLabel("Custom Button Label")
        .setPositiveFeedbackQuestionNegativeButtonLabel("Custom Button Label")
        .setCriticalFeedbackQuestionTitle("Custom Title")
        .setCriticalFeedbackQuestionSubtitle("Custom Subtitle")
        .setCriticalFeedbackQuestionPositiveButtonLabel("Custom Button Label")
        .setCriticalFeedbackQuestionNegativeButtonLabel("Custom Button Label")
        .setThanksDisplayTimeMs(2000)
        .build();

final CustomLayoutPromptViewConfig customLayoutPromptViewConfig
    = new CustomLayoutPromptViewConfig(
        R.layout.custom_question_view, R.layout.custom_thanks_view);

promptView.applyBaseConfig(basePromptViewConfig);
promptView.applyConfig(customLayoutPromptViewConfig);
```

### Listening For Prompt-Related Events

It may sometimes be useful to know when prompt-related events occur. For example, you may want to:

- track user interactions with the prompt view using your preferred analytics suite;
- adjust other UI elements when the prompt view is shown/hidden.

To achieve this, pass an `IEventListener` instance to the `addPromptEventListener` method of your prompt view. An example implementation demonstrating these use-cases is given below:

```java
DefaultLayoutPromptView promptView = (DefaultLayoutPromptView) findViewById(R.id.prompt_view);

promptView.addPromptEventListener(new IEventListener() {
    @Override
    public void notifyEventTriggered(@NonNull final IEvent event) {
        AnalyticsTracker.reportPromptEvent(event);
    
        if (event == PromptViewEvent.PROMPT_SHOWN) {
            relatedView.setVisibility(View.VISIBLE);
        } else if (event == PromptViewEvent.PROMPT_DISMISSED) {
            relatedView.setVisibility(View.GONE);
        }
    }
});

Amplify.getSharedInstance().promptIfReady(promptView);
```

## Feedback Collection

The collection of both positive and critical feedback is handled by implementations of the `IFeedbackCollector` interface. _amplify_ is packaged with the following complete implementations:

- `GooglePlayStoreFeedbackCollector`: collects feedback via the Google Play Store.
- `AmazonAppStoreFeedbackCollector`: collects feedback via the Amazon App Store.
- `DefaultEmailFeedbackCollector`: collects feedback via email. Must be passed one or more recipient email addresses when constructed.

It is possible to specify more than one feedback collector for a given feedback type. For example, the code snippet below will first attempt to collect positive feedback via the Amazon App Store; if that fails (because the Amazon App Store is not available), it will automatically and invisibly fall back to collecting positive feedback via the Google Play Store:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(
                       new AmazonAppStoreFeedbackCollector(),
                       new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .applyAllDefaultRules();
    }
    
}
```

# Customizing

## Prompt Timing

### Applying Custom Environment-Based Rules

A new custom environment-based rule can be added by implementing the `IEnvironmentBasedRule` interface and passing an instance of this implementation to the `Amplify` instance method `addEnvironmentBasedRule`:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .addEnvironmentBasedRule(new MyCustomEnvironmentBasedRule());
    }
    
}
```

### Tracking Custom Events

A new custom event can be tracked by implementing the `IEvent` interface, registering this event with a corresponding (default or custom) `IEventBasedRule` using one of the following methods:

- `addTotalEventCountRule`;
- `addFirstEventTimeRule`;
- `addLastEventTimeRule`;
- `addLastEventVersionCodeRule`;
- `addLastEventVersionNameRule`,

and then notifying the `Amplify` instance of occurrences of this event using the `notifyEventTriggered` method:

```java
Amplify.getSharedInstance().notifyEventTriggered(new MyCustomEvent());
```

As before, the dimension of the event that will be tracked is dictated by which registration method is called.

### Applying Custom Event-Based Rules

A new custom event can be tracked by implementing the `IEventBasedRule<T>` interface, and registering a (default or custom) `IEvent` with this custom `IEventBasedRule` using one of the following methods:

- `addTotalEventCountRule`;
- `addFirstEventTimeRule`;
- `addLastEventTimeRule`;
- `addLastEventVersionCodeRule`;
- `addLastEventVersionNameRule`.

The generic type `T` must be one of: `Integer`, `Long`, or `String`. The type you select will depend on which tracked event aspect (time, count, etc.) you wish to apply this check to.

## Prompt UI

**Reminder: use the provided `DefaultLayoutPromptView` and `CustomLayoutPromptView` classes whenever possible!**

To provide fully-custom views for each phase of the typical prompt flow, implement the `IPromptView` interface and pass an instance of this implementation to the `promptIfReady` method. Your custom class should create and save a `PromptPresenter` instance in any constructors - this presenter will be used to communicate to your prompt which state it should display. See the `BasePromptView` class for a sample implementation in which:

- all questions are assumed to share a common view structure;
- prompt state is preserved through configuration changes (non-trivial!).

To provide a totally custom experience in which _amplify_ does not manage the prompt/rating/feedback UI flows at all, replace any calls to `promptIfReady` with calls to `shouldPrompt`. This method will evaluate all rules and provide a boolean that indicates whether every provided rule is currently satisfied. You may then use this hook to begin your own feedback request flow. Again, if you choose this route be aware that you are responsible for maintaining prompt state through orientation changes (if desired).

## Feedback Emails

To customize the feedback email template, extend either `DefaultEmailFeedbackCollector` or `BaseEmailFeedbackCollector` and implement the `getSubjectLine` and/or `getBody` methods, then pass an instance of your subclass to the shared `Amplify` instance during configuration:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new CustomEmailFeedbackCollector("someone@example.com"))
               .applyAllDefaultRules();
    }
    
}
```

# Debug Builds

The delayed nature of _amplify_ prompts can make it hard to test effectively when integrated. We provide the following capabilities to help with this.

## Logging

By default, logging is disabled. To enable it, call the static method `Amplify.setLogger` _before_ initializing the shared `Amplify` instance. The provided `AndroidLogger` class will be an appropriate choice for most users. If you instead wish to route _amplify_ logs into an existing logging framework, you may supply your own implementation of the `ILogger` interface here instead.

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.setLogger(new AndroidLogger());
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .applyAllDefaultRules();
    }
    
}
```

## Package Name Override

By default, the `GooglePlayStoreFeedbackCollector` and `AmazonAppStoreFeedbackCollector` will search their stores for the app whose package name matches the running application. If your app's build variants do not all share a single package name (perhaps to allow simultaneous installation), _amplify_  will fail to load the appropriate Google Play Store or Amazon App Store page in non-release builds. To fix this, pass your release build package name to the appropriate `IFeedbackCollector` instance during library initialization. For example:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        String releasePackageName = "my.release.package.name";
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector(releasePackageName))
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .setPackageName("my.release.package.name");
    }
    
}
```

## Visibility

To force the prompt to ignore any environment or event-based rules and instead appear every time, call `setAlwaysShow(true)` when configuring the shared `Amplify` instance. For example, the code snippet below would result in the prompt appearing every time in debug builds _only_ (useful when configuring or testing the prompt):

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .setAlwaysShow(BuildConfig.DEBUG);
    }
    
}
```

# Resetting

All data tracked by _amplify_ is stored in a single [`SharedPreferences`](http://developer.android.com/reference/android/content/SharedPreferences.html) instance. By default, this instance is named `"AMPLIFY_SHARED_PREFERENCES_NAME"`. However, it is possible to configure _amplify_ to use a different `SharedPreferences` instance during initialization:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        String sharedPrefsName = "my-custom-shared-prefs-name";
        
        Amplify.initSharedInstance(this, sharedPrefsName)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .applyAllDefaultRules();
    }
    
}
```

Supplying a custom shared preferences name in this manner will effectively 'reset' the library (since the new `SharedPreferences` instance will not contain any existing tracked data). This can be useful if you release a major app update and wish to prompt _all_ your users for feedback again.

The same mechanism can be used to keep debug and release data separate (this does not affect your application's end users, but can be useful during app development and testing!):

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        String sharedPrefsName;

        if (BuildConfig.DEBUG) {
            sharedPrefsName = "debug-shared-prefs-name";
        } else {
            sharedPrefsName = Constants.DEFAULT_BACKING_SHARED_PREFERENCES_NAME;
        }

        Amplify.initSharedInstance(this, sharedPrefsName)
               .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
               .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector("someone@example.com"))
               .applyAllDefaultRules();
    }
    
}
```

# Case Studies

Early versions of _amplify_ are integrated in apps with state-wide and national audiences, with over 200,000 installs combined. After integrating _amplify_, our data indicates that the number of Play Store reviews received increases by a factor of **5x-10x**, and the number of feedback emails received **doubles**. We present screenshots showing example implementations below:

| Styled default layout |
|-----------------------|
| <img src="https://raw.githubusercontent.com/stkent/amplify/main/assets/dte.png" /> |

# License

    Copyright 2015 Stuart Kent
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
