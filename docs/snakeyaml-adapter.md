# Gson Adapter

## Include

The easiest way to include _NILS - SnakeYaml Adapter_ is via as dependency in you build tool like Maven or Gradle.

You can find the current versions in the [Maven central repository](https://mvnrepository.com/artifact/com.codepulsar.nils).
Maven:

```
<dependency>
  <groupId>com.codepulsar.nils</groupId>
  <artifactId>nils-snakeyaml-adapter</artifactId>
  <version>1.3.0-SNAPSHOT</version>
</dependency>
```

Gradle:

```
implementation group: 'com.codepulsar.nils', name: 'nils-snakeyaml-adapter', version: '1.2.0'
```

### Using

After you have included the jar into your project you must create a `NilsFactory` with a `SnakeYamlAdapterConfig` to get access to the NLS support.

```java
public class SimpleUse {

  private static final NilsFactory NLS_FACTORY =
      NilsFactory.init(SnakeYamlAdapterConfig.init(SimpleUse.class)); // (1)
  
  ...
  
  public void translate() {
    var nls = NLS_FACTORY.nls(); // (2)

    var customerName = nls.get("customer.name"); // (3)
  }
}
```

* (1) The example creates a `NilsFactory` using the GsonAdapter. The JSON files are located at "nls/translation.yaml", "nls/translation_en.yaml" etc.
* (2) Get a new NLS object in the method you require the translation.
* (3) Example call for the translation with the key "customer.name" and no arguments.

The corresponding JSON file `nls/translation.json`can look like:

```yaml
customer: 
- name: Name
- street: Street
```

## Include Translations

The SnakeYaml Adapter can also provides the inclusion of already defined keys, like the Resource Bundle.

A corresponding YAML file could look like;


```yaml
Person:
- surname: Surname
- firstname: First name
- birthday: Birthday
Employee:
- _include: Person      # (1)
- surname : Family name   # (2)
```

* (1) This line include all keys from Person. If your application request for example `Employee.firstname` NILS will do a lookup and return the value from `Person.firstname`.
* (2) Employee overwrites the key for `surname`. So `Employee.surname` will return 'Family name' instead of 'Surname'.

If you want to include more that one base you can add the values semicolon separated:

```yaml
...
Employee:
- _include : Person;com.myproject.BaseClass
...
```

The ordering is important. The first look up will be taken on `Person`. The person has not a translation for the requested key, `com.myproject.BaseClass` will be called.

You must set the property includeTag of the `NilsConfig` because YAML doesn't support the default include tag(`@include`). Its recommended to use `_include` instead.

```java
var config = NilsConfig.init(adapterConfig).includeTag("_include");
var adapter = new SnakeYamlAdapter(adapterConfig, locale);
var underTest = NilsFactory.init(config).nls(locale);
```
