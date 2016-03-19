# Issue Tracking

Issues are tracked using [GitHub Issues](https://github.com/stkent/amplify/issues). Please review all tag types to understand issue categorization.

Always review all existing issues before opening a new one. If you would like to work on an existing issue, please comment to that effect and assign yourself to the issue.

# Conventions

Code committed to this project must pass selected style and correctness checks provided by:

- [FindBugs™](http://findbugs.sourceforge.net/);
- [PMD](https://pmd.github.io/);
- [checkstyle](http://checkstyle.sourceforge.net/).

This helps us focus on content when reviewing contributions.

You can run these checks locally by executing the following Gradle command:

```shell
./gradlew :amplify:check
```

Travis CI runs the same checks for each pull request and marks the build as failing if any check does not pass. Detailed information about every detected violation will be automatically posted to the conversation for that pull request. Violation detection and reporting is handled by the [Gnag](https://github.com/btkelly/gnag) Gradle plugin.

## Class Member Order

- static fields (public, protected, private)
- static methods (public, protected, private)
- instance fields (public, protected, private)
- constructors
- instance methods (overrides, public, protected, private)

Run the library unit test suite by executing the Gradle command:

```shell
./gradlew :amplify:testRelease
```

The Travis CI pull request build will fail if any test fails.

# Generating Inline Licenses

Before opening a pull request, you must generate license headers in any new source files by executing the Gradle command:

```shell
./gradlew licenseFormat
```

The Travis CI pull request build will fail if any source file is missing this generated header.

# Updating README Table Of Contents

If your work includes structural changes to the project README (adding/removing/changing/reordering any section headers), you must regenerate the README's table of contents using [DocToc](https://github.com/thlorenz/doctoc). After installing DocToc, execute the provided script to regenerate the table of contents with sensible defaults:

```shell
./update_toc.sh
```
