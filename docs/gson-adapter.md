# Gson Adapter

## Include

The easiest way to include _NILS - Gson Adapter_ is via as dependency in you build tool like Maven or Gradle.

You can find the current versions in the [Maven central repository](https://mvnrepository.com/artifact/com.codepulsar.nils).
Maven:

```
<dependency>
  <groupId>com.codepulsar.nils</groupId>
  <artifactId>nils-gson-adapter</artifactId>
  <version>1.2.0</version>
</dependency>
```

Gradle:

```
implementation group: 'com.codepulsar.nils', name: 'nils-gson-adapter', version: '1.2.0'
```

### Using

After you have included the jar into your project you must create a `NilsFactory` with a `GsonAdapterConfig` to get access to the NLS support.

```java
public class SimpleUse {

  private static final NilsFactory NLS_FACTORY =
      NilsFactory.init(GsonAdapterConfig.init(SimpleUse.class)); // (1)
  
  ...
  
  public void translate() {
    var nls = NLS_FACTORY.nls(); // (2)

    var customerName = nls.get("customer.name"); // (3)
  }
}
```

* (1) The example creates a `NilsFactory` using the GsonAdapter. The JSON files are located at "nls/translation.json", "nls/translation_en.json" etc.
* (2) Get a new NLS object in the method you require the translation.
* (3) Example call for the translation with the key "customer.name" and no arguments.

The corresponding JSON file `nls/translation.json`can look like:

```json
{
  "customer": {
    "name" : "Name",
    "street": "Street"
  }
}
```

## Include Translations

The Gson Adapter can also provides the inclusion of already defined keys, like the Resource Bundle.

A corresponding JSON file could look like;


```json
{ "Person" : {
    "surname" : "Surname",
    "firstname" : "First name",
    "birthday" : "Birthday"
  },
  "Employee" : {
    "@include" : "Person",      # (1)
    "surname" : "Family name"   # (2)
  }
}
```

* (1) This line include all keys from Person. If your application request for example `Employee.firstname` NILS will do a lookup and return the value from `Person.firstname`.
* (2) Employee overwrites the key for `surname`. So `Employee.surname` will return 'Family name' instead of 'Surname'.

If you want to include more that one base you can add the values semicolon separated:

```json
...
  "Employee" : {
    "@include" : "Person;com.myproject.BaseClass",
    ...
  }
...
```

The ordering is important. The first look up will be taken on `Person`. The person has not a translation for the requested key, `com.myproject.BaseClass` will be called.

The `NilsConfig` provides a method to configure the include tag:

* `includeTag(String)`: Set the tag used for including other keys. The default is `@include`.
