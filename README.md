# amplify

Respectfully request feedback in your Android app.

<!-- TODO: add example image or gif here -->

<a href="https://travis-ci.org/stkent/amplify"><img src="https://travis-ci.org/stkent/amplify.svg" /></a> <a href="https://bintray.com/stkent/android-libraries/amplify/"><img src="https://img.shields.io/bintray/v/stkent/android-libraries/amplify.svg" /></a> <a href="http://www.detroitlabs.com/"><img src="https://img.shields.io/badge/Sponsor-Detroit%20Labs-000000.svg" /></a>

# Table Of Contents

- [Introduction](#introduction)

# Introduction

_amplify_ focuses on helping Android developers prompt their users for feedback at the right times and in the right way. Inspired by [Circa News](https://medium.com/circa/the-right-way-to-ask-users-to-review-your-app-9a32fd604fca), we built this library based on the following principles: 

#### No interruptions

The inline prompt we provide can be inserted right into your view hierarchy and customized to complement your existing UI. Users are free to interact with the prompt as much or as little as they like. This approach shows respect for your users and preserves the app flow you have carefully crafted.

#### No nagging

_amplify_ intelligently tracks significant events to make sure your users are only prompted for feedback at appropriate times.

#### Maximum impact

When users indicate they are willing to provide feedback, we direct them to the highest-impact outlet:

- Users with positive feedback are asked to leave a quick rating or review in the Google Play Store, improving the rating and discoverability of your app. All of these ratings and reviews reflect genuine user experiences - _amplify_ just makes it easier for happy customers to choose to express their appreciation.

- Users with critical feedback are instead asked to send a more detailed email that will automatically include pertinent app and device information. This gives you an opportunity to engage these users in a more meaningful dialogue, allowing you to better understand and accommodate their feedback.

#### Easy to integrate

Default prompt timing rules allow you to get up and running as quickly as possible.

#### Easy to customize

Use both built-in and custom events to create a collection of prompt timing rules. Tweak the provided inline prompt UI via xml or in code.

# How It Works

_amplify_ consists of two main components:

- An **event-tracking engine**, responsible for tracking occurrences of significant events and evaluating rules based on these events to determine appropriate times to ask for feedback;

- **Inline prompt UI** that guides users through the flow depicted below to determine the appropriate outlet for their feedback:

<br />

<p align="center">
	<img src="https://raw.githubusercontent.com/stkent/amplify/master/assets/flow.png" width="60%" />
</p>

<br />

# Getting Started

(1) Specify Amplify as a dependency in your `build.gradle` file:

```groovy
dependencies {
    compile 'com.github.stkent:amplify:{latest-version}'
}
```
    
(2) Initialize the shared `Amplify` instance in your custom `Application` subclass:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Amplify.get(this).configureWithDefaults();
    }
    
}
```

(3) Add a `PromptView` instance to all xml layouts in which you may want to prompt the user for their feedback:

```xml
<com.github.stkent.amplify.views.PromptView
    android:id="@+id/prompt_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

(4) Call the state tracker's `promptIfReady` method when appropriate, passing in your `PromptView` instance:

```java
PromptView promptView = (PromptView) findViewById(R.id.prompt_view);

Amplify.get(context).promptIfReady(promptView);
```

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

When calling `configureWithDefaults` on the `Amplify` instance, the following checks are registered by default:

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

A new custom event check can be created by implementing the `IEventCheck<T>` interface. The generic type `T` must be one of: `Integer`, `Long`, or `String`. The type you select will depend on which tracked event aspect (time, count, etc.) you wish to apply this check to.

## Prompt UI

# Contributing

## Issue Tracking

Library issues are tracked using [GitHub Issues](https://github.com/stkent/amplify/issues). Please review all tag types to understand issue categorization.

Always review open issues before opening a new one. If you would like to work on an existing issue, please comment to that effect and assign yourself to the issue.

## Conventions

Code committed to this project must pass selected style and correctness checks provided by:

- [FindBugs™](http://findbugs.sourceforge.net/);
- [PMD](https://pmd.github.io/);
- [checkstyle](http://checkstyle.sourceforge.net/).

This helps us focus on content only when reviewing contributions.

You can run these checks locally by executing the following Gradle command:

```shell
./gradlew amplify:check
```

Travis CI runs the same checks for each pull request and marks the build as failing if any check does not pass. Detailed information about every detected violation will be automatically posted to the conversation for that pull request. Violation detection and reporting is handled by the [Gnag](https://github.com/btkelly/gnag) Gradle plugin.

## Running Tests

Run the library unit test suite by executing the Gradle command:

```shell
./gradlew amplify:testRelease
```

The Travis CI pull request build will fail if any test fails.

## Generating Inline Licenses

Before opening a pull request, you must generate license headers in any new source files by executing the Gradle command:

```shell
./gradlew licenseFormat
```

The Travis CI pull request build will fail if any source file is missing this generated header.

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
