# NILS - A Java NLS library

_NILS_ is a lightweight Java library for NLS (aka "national language support", "localization", "internationalization").

It enables developers to integrate NLS into their Java projects easily.

Currently there are adapters for using with 

* Java Resource Bundles (as part of `nils-core`. See below for usage).
* Using JSON files (see [Gson Adapter](docs/gson-adapter.md) for usage).
* Using YAML files (see [SnakeYaml Adapter](docs/snakeyaml-adapter.md) for usage).

## Getting started 

### Requirements

The core library requires

* **Java 11** or higher.
* The [SLF4J Api](https://mvnrepository.com/artifact/org.slf4j/slf4j-api) for logging.

Existings adapters can depend more libraries. See [Documentation](docs/index.md) section 'Existing Adapters'. 

### Include

The easiest way to include _NILS_ is via as dependency in you build tool like Maven or Gradle.

You can find the current versions in the [Maven central repository](https://mvnrepository.com/artifact/com.codepulsar.nils).

Maven:

```
<dependency>
  <groupId>com.codepulsar.nils</groupId>
  <artifactId>nils-core</artifactId>
  <version>1.2.0</version>
</dependency>
```

Gradle:

```
implementation group: 'com.codepulsar.nils', name: 'nils-core', version: '1.2.0'
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
