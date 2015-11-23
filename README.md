# Amplify

Respectfully request feedback in your Android app.

<!-- TODO: add example image or gif here -->

# Project Status

<a href="https://travis-ci.org/stkent/amplify"><img src="https://travis-ci.org/stkent/amplify.svg"></a> [![Coverage Status](https://coveralls.io/repos/stkent/amplify/badge.svg?branch=master&service=github)](https://coveralls.io/github/stkent/amplify?branch=master) [![Download](https://api.bintray.com/packages/stkent/android-libraries/amplify/images/download.svg)](https://bintray.com/stkent/android-libraries/amplify/_latestVersion)

# Getting Started

In your `build.gradle`:

    dependencies {
        compile 'com.github.stkent:amplify:0.1.0'
    }
    
In your `Application` class:

    public class ExampleApplication extends Application {
        @Override public void onCreate() {
            super.onCreate();
            AmplifyStateTracker.get(this)
                    .configureWithDefaults()
                    .setLogLevel(BuildConfig.DEBUG ? Logger.LogLevel.DEBUG : Logger.LogLevel.NONE);
        }
    }

In your xml layout(s):

    <com.github.stkent.amplify.views.AmplifyView
        android:id="@+id/amplifyView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

In the corresponding `Activity`/`Fragment`/`View` class (typically presented immediately after a successfully-completed 'transaction'):

    final AmplifyView amplifyView = (AmplifyView) findViewById(R.id.amplifyView);
    
    ...
    
    AmplifyStateTracker.get(this).promptIfReady(amplifyView);

# Configuring

## Default Rules

## Default Prompt UI

# Customizing

## Custom Events

## Custom Rules

## Custom Prompt UI

# Contributing

## Conventions

Code style and correctness is also enforced by several checkers. Running the Gradle command

    ./gradlew amplify:check
    
will perform all the standard `check` tasks, as well as noting any violations detected by:

- [FindBugs™](http://findbugs.sourceforge.net/);
- [PMD](https://pmd.github.io/);
- [checkstyle](http://checkstyle.sourceforge.net/).

The Travis CI pull request build will fail if any violations are detected; these violations will be reported as top-level comments on the corresponding pull request.

## Running Tests

Execute the library unit test suite using the Gradle command:

    ./gradlew amplify:testRelease
    
The Travis CI pull request build will fail if any test fails.

## Generating Inline Licenses

Automatically generate license headers in new source files using the Gradle command:

    ./gradlew licenseFormat
    
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
