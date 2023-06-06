# NILS - A Java NLS library

_NILS_ is a lightweight Java library for NLS (aka "national language support", "localization", "internationalization").

It enables developers to integrate NLS into their Java projects easily.

## Getting started 

### Requirements

The library requires

* **Java 11** or higher.
* The [SLF4J Api](https://mvnrepository.com/artifact/org.slf4j/slf4j-api) for logging.

### Include

**NO RELEASE SO FAR. STILL IN DEVELOPMENT**

You can include _NILS_ from the release page or even easier as dependency if you use a build tool like Maven or Gradle.

Maven:

```
Not published yet.
```

Gradle:

```
Not published yet.
```

### Using

After you have included the jar into your project you must create a `NilsFactory` to get access to the NLS support.

```java
public class SimpleUse {

  private static final NilsFactory NLS_FACTORY =
      NilsFactory.init(ResourceBundleAdapterConfig.init(SimpleUse.class)); // (1)
  
  ...
  
  public void translate() {
    var nls = NLS_FACTORY.nls(); // (2)

    var customerName = nls.get("customer.name"); // (3)
    var pageXOfY = nls.get("page_counter", 3, 5); // (4)
    var street = nls.get(Address.class, "street"); // (5)
  }
}
```

* (1) The example creates a `NilsFactory` using resource bundles located at "nls/translation.properties".
* (2) Get a new NLS object in the method you require the translation.
* (3) Example call for the translation with the key "customer.name" and no arguments.
* (4) Example call for the translation "page_counter" with two arguments.
* (5) Example for a class and its attribute "street".

The next example shows, if you use NLS directly for a specific locale (here German).

```java
public class SimpleUse {

  private static final NLS NLS_DE =
      NilsFactory.init(ResourceBundleAdapterConfig.init(SimpleUse.class)).nls(Locale.GERMAN);
  
  ...
  
  public void translateDE() {
    var customerName = NLS_DE.get("customer.name");
    var pageXOfY = NLS_DE.get("page_counter", 3, 5);
    var street = NLS_DE.get(Address.class, "street");
  }
}
```

## Further reading

See the [Documentation](docs/index.md) for more information.

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
