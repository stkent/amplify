# amplify

Respectfully request feedback in your Android app.

<a href="https://travis-ci.org/stkent/amplify"><img src="https://travis-ci.org/stkent/amplify.svg" /></a> <a href="https://bintray.com/stkent/android-libraries/amplify/"><img src="https://img.shields.io/bintray/v/stkent/android-libraries/amplify.svg" /></a> <a href="http://www.detroitlabs.com/"><img src="https://img.shields.io/badge/Sponsor-Detroit%20Labs-000000.svg" /></a>

# Table Of Contents

- [Introduction](#introduction)
- [Library Structure](#library-structure)
- [Getting Started](#getting-started)
    - [Default Behavior](#default-behavior)
- [Configuring](#configuring)
- [Customizing](#customizing)
- [Case Studies](#case-studies)
- [License](#license)

# Introduction

_amplify_ focuses on helping Android developers prompt their users for feedback at the right times and in the right way. Inspired by [Circa News](https://medium.com/circa/the-right-way-to-ask-users-to-review-your-app-9a32fd604fca), we built this library based on the following principles: 

- **No interruptions.** The inline prompt we provide can be inserted right into your view hierarchy and customized to complement your existing UI. Users are free to interact with the prompt as much or as little as they like. This approach shows respect for your users and preserves the app flow you have carefully crafted.

- **No nagging.** _amplify_ intelligently tracks significant events to make sure your users are only prompted for feedback at appropriate times.

- **Maximum impact.** When users indicate they are willing to provide feedback, we direct them to the highest-impact outlet:

    - Users with positive feedback are asked to leave a quick rating or review in the Google Play Store, improving the rating and discoverability of your app. All of these ratings and reviews reflect genuine user experiences - _amplify_ just makes it easier for happy customers to choose to share their appreciation.

    - Users with critical feedback are instead asked to send a more detailed email that will automatically include pertinent app and device information. This gives you an opportunity to engage these users in a more meaningful dialogue, allowing you to better understand and accommodate their feedback.

- **Easy to integrate.** Default prompt timing rules allow you to get up and running as quickly as possible.

- **Easy to customize.** Use both built-in and custom events to create a collection of prompt timing rules. Tweak the provided inline prompt UI via xml or in code.

# Library Structure

_amplify_ consists of two main components:

- A **prompt timing calculator**, represented by the `Amplify` class, responsible for:

    - verifying that the current device provides the required feedback channels (the Google Play Store + at least one email application);

    - tracking occurrences of significant events and evaluating rules based on these events to determine appropriate times to ask for feedback (we occasionally refer to this portion of the library as the **event-tracking engine**);

- A custom **inline feedback prompt**, represented by the `PromptView` class, that guides users through the flow depicted below to determine the appropriate outlet for their feedback:

<br />

<p align="center">
	<img src="https://raw.githubusercontent.com/stkent/amplify/master/assets/flow.png" width="60%" />
</p>

<br />

These components are designed to complement each other, and combining them as described in the [Getting Started](#getting-started) section below is the easiest way to integrate _amplify_ into your application. However, it is perfectly possible to couple the prompt timing calculator with your own prompt flow and UI if desired. (This could be useful if you have highly-customized requirements for the prompt flow.) If you pursue this route, we would encourage you to ensure your prompting mechanism still reflects the principles outlined in the [Introduction](#introduction).

# Getting Started

<ol>
  <li>Specify <em>amplify</em> as a dependency in your <code>build.gradle</code> file:</li>
</ol>

```groovy
dependencies {
    compile 'com.github.stkent:amplify:{latest-version}'
}
```

<ol start="2">
  <li>Initialize the shared <code>Amplify</code> instance in your custom <code><a href="http://developer.android.com/reference/android/app/Application.html">Application</a></code> subclass:</li>
</ol>

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.get(this)
               .setFeedbackEmailAddress("someone@example.com")
               .applyAllDefaultRules();
    }
    
}
```

<ol start="3">
  <li>Add a <code>PromptView</code> instance to all xml layouts in which you may want to prompt the user for their feedback:</li>
</ol>

```xml
<com.github.stkent.amplify.views.PromptView
    android:id="@+id/prompt_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

<ol start="4">
  <li>Call the state tracker's <code>promptIfReady</code> method when appropriate, passing in your <code>PromptView</code> instance:</li>
</ol>

```java
PromptView promptView = (PromptView) findViewById(R.id.prompt_view);

Amplify.get(context).promptIfReady(promptView);
```

That's it! The prompt timing calculator will evaluate the default rules each time `promptIfReady` is called, and instruct the `PromptView` to automatically update its visibility based on the result. If the user chooses to interact with the prompt, the sequence of questions asked is also automatically managed by the `PromptView`.

## Default Behavior

The convenience method `applyAllDefaultRules` initializes the prompt timing calculator with a collection of sensible default rules. With these rules applied, we only prompt for feedback when:

- **The Google Play Store is available.** If a user's device won't allow them to provide feedback, we never ask for it. (We believe that a high enough percentage of devices are capable of sending email that a similar check for the availability of an email application is unnecessary.)

- **It has been more than a week since a new version of your app was installed.** We like to give users some time to settle in and explore the changes made in the latest update before asking them their opinion... but not so much time that their valuable first impressions are forgotten!

- **It has been more than a week since your app last crashed.** There are much better ways to collect detailed crash information than via user feedback. We're big fans of [Fabric/Crashlytics](https://fabric.io/kits/android/crashlytics). To save users from spending time reporting crashes that we are already aware of and fixing, we avoid asking for feedback right after a crash has occurred.

- **The user has never previously provided positive feedback.** We strive to constantly improve our apps' functionality and stability. If we do our job right, there's little to be gained by prompting satisfied users for feedback repeatedly. If we decide to significantly overhaul our app (either internally or externally), we will reset the prompt timing calculator to capture feedback from our entire userbase again. // TODO: link to a section that explains how to do this.

- **The user has not provided critical feedback for this version of the application already.** Since it's unlikely that we'll be able to address any critical feedback received without releasing an update, we won't re-prompt a user who already provided insights into the current version of the app.

- **The user has not actively declined to provide feedback for this version of the application.** If a user has already actively indicated they are not interested in providing feedback for the current version of the app, we won't ask again before the next update is shipped. (Note that 'actively indicated' here means a user deliberately declined to provide feedback. This rule does not apply to users who have seen but did not interact with the prompt in any way.)

More information on how to apply your own collection of rules is available in the [Configuring](#configuring) section. Building custom rules is covered in the [Customizing](#customizing) section.

# Configuring

## Event Tracking

### Default Environment Checks

The `GooglePlayStoreIsAvailableCheck` check will pass if the Google Play Store is available on a user's device, and will fail otherwise.

### Default Events

#### Feedback Prompt Events

These events are associated with user actions related to the feedback prompt UI.

- User agreed to provide positive feedback
- User agreed to provide critical feedback
- User declined to provide positive feedback
- User declined to provide critical feedback

#### Application-level Events

- Application installed
- Application updated
- Application crashed

These (pseudo-)events are associated with application-level actions.

### Default Event Checks

When calling `applyAllDefaultRules` on the `Amplify` instance, the following checks are registered by default:

- the `GooglePlayStoreIsAvailableCheck`, which will block all prompts if the Google Play Store is not available.

## Prompt UI

# Customizing

## Event Tracking

### Custom Environment Checks

A new custom environment check can be added by implementing the `IEnvironmentCheck` interface and passing an instance of the implementation to the `Amplify` instance method `addEnvironmentCheck()`.

### Custom Events

A new custom event can be tracked by implementing the `ITrackableEvent` interface and passing an instance of the implementation to one of the following `Amplify` instance methods:

- `trackTotalEventCount()`
- `trackFirstEventTime()`
- `trackLastEventTime()`
- `trackLastEventVersion()`

You will also need to provide an event check when calling any of these methods. The data passed to this paired event check is linked to the particular tracking method called. For example, if you register an event using the `trackTotalEventCount()` method, the corresponding event check will be called with integer values that represent the number of event occurrences to date.

### Custom Event Checks

A new custom event check can be created by implementing the `IEventBasedRule<T>` interface. The generic type `T` must be one of: `Integer`, `Long`, or `String`. The type you select will depend on which tracked event aspect (time, count, etc.) you wish to apply this check to.

## Prompt UI

# Case Studies



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
