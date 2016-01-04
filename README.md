# Amplify

Respectfully request feedback in your Android app.

<!-- TODO: add example image or gif here -->

<a href="https://travis-ci.org/stkent/amplify"><img src="https://travis-ci.org/stkent/amplify.svg"></a>

# Introduction

## What

## Why

# Getting Started

(1) Specify Amplify as a dependency in your `build.gradle` file:

```groovy
dependencies {
    compile 'com.github.stkent:amplify:0.1.0'
}
```
    
(2) Initialize the state tracker in your `Application` class:

```java
public class ExampleApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        AmplifyStateTracker.get(this).configureWithDefaults()
    }
    
}
```

(3) Add an `AmplifyView` instance to all xml layouts in which you may want to prompt the user for their feedback:

```xml
<com.github.stkent.amplify.views.AmplifyView
    android:id="@+id/amplifyView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

(4) Call the state tracker's `promptIfReady` method when appropriate, passing in your `AmplifyView` instance:

```java
AmplifyView amplifyView = (AmplifyView) findViewById(R.id.amplifyView);

AmplifyStateTracker.get(context).promptIfReady(amplifyView);
```

# Library structure

TODO: Here we need some discussion of the fact that this library essentially comes in two pieces:

- the state-tracking portion
- the default prompt views that enforce a particular flow (and also allow for automatic tracking of prompt-related event occurrences.)

---

The `AmplifyStateTracker` singleton has two main responsibilities:

- tracking occurrences of registered events;
- evaluating checks based on (a) the application's 'environment', and (b) each registered event, that collectively determine whether or not the user should be shown your feedback prompt. If any check fails, the user will not be prompted.

Calling `configureWithDefaults()` on the `AmplifyStateTracker` instance registers a number of events and checks and provides a sensible baseline configuration.

# Configuring

## Default Environment Checks

The `GooglePlayStoreIsAvailableCheck` check will pass if the Google Play Store is available on a user's device, and will fail otherwise.

## Default Events

### Feedback Prompt Events

These events are associated with user actions related to the feedback prompt UI.

- User agreed to provide positive feedback
- User agreed to provide critical feedback
- User declined to provide positive feedback
- User declined to provide critical feedback

### Application-level Events

- Application installed
- Application updated
- Application crashed

These (pseudo-)events are associated with application-level actions.

## Default Event Checks

When calling `configureWithDefaults` on the `AmplifyStateTracker` instance, the following checks are registered by default:

- the `GooglePlayStoreIsAvailableCheck`, which will block all prompts if the Google Play Store is not available.

## Default Prompt UI

# Customizing

## Custom Environment Checks

A new custom environment check can be added by implementing the `IEnvironmentCheck` interface and passing an instance of the implementation to the `AmplifyStateTracker` instance method `addEnvironmentCheck()`.

## Custom Events

A new custom event can be tracked by implementing the `ITrackableEvent` interface and passing an instance of the implementation to one of the following `AmplifyStateTracker` instance methods:

- `trackTotalEventCount()`
- `trackFirstEventTime()`
- `trackLastEventTime()`
- `trackLastEventVersion()`

You will also need to provide an event check when calling any of these methods. The data passed to this paired event check is linked to the particular tracking method called. For example, if you register an event using the `trackTotalEventCount()` method, the corresponding event check will be called with integer values that represent the number of event occurrences to date.

## Custom Event Checks

A new custom event check can be created by implementing the `IEventCheck<T>` interface. The generic type `T` must be one of: `Integer`, `Long`, or `String`. The type you select will depend on which tracked event aspect (time, count, etc.) you wish to apply this check to.

## Custom Prompt UI

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
