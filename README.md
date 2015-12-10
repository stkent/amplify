# Amplify

Respectfully request feedback in your Android app.

<!-- TODO: add example image or gif here -->

# Project Status

<a href="https://travis-ci.org/stkent/amplify"><img src="https://travis-ci.org/stkent/amplify.svg"></a> [![Coverage Status](https://coveralls.io/repos/stkent/amplify/badge.svg?branch=master&service=github)](https://coveralls.io/github/stkent/amplify?branch=master) [![Download](https://api.bintray.com/packages/stkent/android-libraries/amplify/images/download.svg)](https://bintray.com/stkent/android-libraries/amplify/_latestVersion)

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

# Configuring

## Default Events

## Default Rules

## Default Prompt UI

# Customizing

## Custom Events

## Custom Rules

## Custom Prompt UI

# Contributing

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

Execute the library unit test suite using the Gradle command:

```shell
./gradlew amplify:testRelease
```

The Travis CI pull request build will fail if any test fails.

## Generating Inline Licenses

Automatically generate license headers in new source files using the Gradle command:

```shell
./gradlew licenseFormat
```

The Travis CI pull request build will fail if any source files is missing this generated header.

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
