# amplify

Respectfully request feedback in your Android app.

<!-- TODO: add example image or gif here -->

<a href="https://travis-ci.org/stkent/amplify"><img src="https://travis-ci.org/stkent/amplify.svg" /></a> <a href="https://bintray.com/stkent/android-libraries/amplify/"><img src="https://img.shields.io/bintray/v/stkent/android-libraries/amplify.svg" /></a> <a href="http://www.detroitlabs.com/"><img src="https://img.shields.io/badge/Sponsor-Detroit%20Labs-000000.svg" /></a>

# Table Of Contents

- [Introduction](#introduction)
- [How It Works](#how-it-works)
- [Getting Started](#getting-started)
- [Configuring](#configuring)
- [Customizing](#customizing)
- [License](#license)

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

# Library Structure

_amplify_ consists of two main components:

- An **event-tracking engine**, responsible for tracking occurrences of significant events and evaluating rules based on these events to determine appropriate times to ask for feedback;

- A custom **inline feedback prompt** that guides users through the flow depicted below to determine the appropriate outlet for their feedback:

<br />

<p align="center">
	<img src="https://raw.githubusercontent.com/stkent/amplify/master/assets/flow.png" width="60%" />
</p>

<br />

These components are designed to complement each other, and combining them as described in the [Getting Started](#getting-started) section below is the easiest way to integrate _amplify_ into your application. However, it is perfectly possible to couple the event-tracking engine with your own prompt flow and UI if desired. (This could be useful if you have highly-customized requirements for the prompt flow.) If you pursue this route, we would encourage you to ensure your prompting mechanism still reflects the principles outlined in the [Introduction](#introduction).

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
