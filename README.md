# NILS - A Java NLS library

_NILS_ is a lightweight Java library for NLS (aka "national language support", "localization", "internationalization").

It enables developers to integrate NLS into their Java projects easily.

## Getting started 

### Requirements

The library requires

* **Java 11** or higher.
* The [SLF4J Api](https://mvnrepository.com/artifact/org.slf4j/slf4j-api) for logging.

### Include

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
  
  public void translateUI() {
    var nls = NLS_FACTORY.nls(); // (2)
    var customerName = nls.get("customer.name"); // (3)
    var pageXOfY = nls.get("page_counter", 3, 5); // (4)
  }
}
```

* The example creates a `NilsFactory` using resource bundles located at "nls/translation.properties" (1).
* In the method `translateUI()` a new NLS object is requested for the default locale (2).
* Than a call for the translation with the key "customer.name" and no arguments is done (3).
* Lastly a 2nd call for a translation with two arguments is done(4).

The next example shows, if you use NLS directly for a specific locale (here German).

```java
public class SimpleUse {

  private static final NLS NLS_DE =
      NilsFactory.init(ResourceBundleAdapterConfig.init(SimpleUse.class)).nls(Locale.GERMAN);
  
  ...
  
  public void translateUI() {
    var customerName = NLS_DE.get("customer.name");
    var pageXOfY = NLS_DE.get("page_counter", 3, 5);
  }
}
```

## Further reading

### Missing Translations

_NILS_ provides two behaviours if translations could not be found:

1. Return the key with some characters around it.
2. Throw an exception, that the translation could not be found.

The first one is the default case. If a translation key was not found the translation surrounded by `[` and `]` will be returned (i. e. `[persons.name]`).

The `NilsConfig` provides methods to configure this: 

* `escapeIfMissing(false)` : A flag, if an exception should be thrown, if the translation could not be found. `false` means throw an exception.
* `escapePattern("@{0}")` : Escape a missing exception in the format `@persons.name`. The default is `[{0}]`. The escapePattern must contain the string "{0}". 

### Different locales

_NILS_ can handle with different locales at a time. The `NilsFactory` provides access to different NLS objects.

* `nls()` : Returns an NLS object for the default locale of the used JVM.
* `nls(Locale)` : Returns an NLS object for a specific locale.
* `nls(String)` : Returns an NLS object, where the locale is reolved by its language code.

### Translation format for arguments

If you want to use arguments in a translated value, the value must be in the format "Here is the first {0} replacement". Each arguments is represented by a "{..}" section in the translation, starting with 0 for the first argument.

The argument replacement is done by the `MessageFormat` class of the JRE. Look there for more information about the formatting.

### Caveats / hacks

#### NILS and Java's module-info.java

If you use _NILS_ with a module based application and want to use the resource bundles of your application you must give access to the folder containing the resource bundles using the `open` keyword.

```
module xyz {
  export my.package;
  open lang;
}
```

In the example lang is the folder containing the resource files.

### Extending

In general _NILS_ is an abstraction layer to NLS. The access to the translation is encapsulated.

Currently there is only one implementation for this layer: ResourceBundles.

If you need another kind of translation source you can implement your own Adapter. (Maybe for JSON or reading from a database).

To extend NILS you must implement the following classes (located in the package `com.codepulsar.nils.adapter`):

* `Adapter`: The Adapter is the concrete implementation, accessing your translation source. 
* `AdapterConfig`: The configuration for the adapter. Is used by the `NilsFactory`.
* `AdapterFactory`: A factory using the AdapterConfig to create a new Adapter object. The Factory must provide a parameterless default constructor.

Beside these interfaces NILS provides the abstract class `BaseAdapter`. It is recommended that Adapters should extend from this class, because these base class handles common cases like the resolution of missing keys, argument replacements in translations etc.

The test package provides a really simple implementation for a Adapter using a fix Map. (see `com.codepulsar.nils.testadapter`).

## Contribution

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
