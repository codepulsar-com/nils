# NILS - A Java NLS library

_NILS_ is a lightweight Java library for NLS (aka "national language support", "localization", "internationalization").

It enables developers to integrate NLS into their Java projects easily.

The documentation can be found at [https://docs.codepulsar.com/nils/docs/latest/index.html](https://docs.codepulsar.com/nils/latest/index.html).

## Contributing

Contribution are welcome! Please check the issues, if there is/was already a ticket for your bug, suggestions etc. If you have code changes please create a pull request.

The author decides, if the pull request is integrated or rejected.

To contribute code, please note:

* New behaviour or fixes must be covered a Junit test. If no Junit test is possible please explain in the pull request why it is not possible.
* Public accessable classes and methods, abstract methods of abstract classes must provide a JavaDoc comment, explaining the behaviour of the class and/or method. 
  Internal classes or methods could have a JavaDoc.
* The project and code language is English.
* The code is formatted using the style of the google code format (See [Google Java Format](https://github.com/google/google-java-format)). 

New or changed code must meet these requirements. 

## Versioning

This project uses [Semantic Versioning](https://semver.org/).

## Design decisions

### Lifetime of deprecated APIs

Starting with version 3.x a lifetime of deprecated APIs is introduced.

Deprecated marked APIs will stay at least for _2_ further main versions in the code base.

As example: Code, that was marked as deprecated in version 3 will, stay at least until version 5 in the code base.

The use of deprecated APIs should give developers time to adopt their code to the changed API of _NILS_.

### Package names contains main version number

Starting with version 4 the package names will contain the main version number in their names.

The base will be `com.codepulsar.nils<Version>`. For version 4 the package names will be `com.codepulsar.nils4.api` or `com.codepulsar.nils4.adapter.jackson` etc.

Translation libraries like _NILS_ provide low level features. A library using _NILS_ for their translation should be easily included into other project without worring about the used version of _NILS_.

So a library A (using _NILS 3_ ) and a library B (using _NILS 4_ ) must be used side by side without introducing incompatibilities due to _NILS_ .

The down side is, that an upgrade to a newer version of _NILS_ leads into code changes (referencing to the _new_ package name). But we see more advantage in this approach.
