# Documentation

## Using different locales

_NILS_ can handle with different locales at a time. The `NilsFactory` provides access to different NLS objects.

* `nls()` : Returns an NLS object of the default locale used by the JVM.
* `nls(Locale)` : Returns an NLS object for a specific locale.
* `nls(String)` : Returns an NLS object, where the locale is reolved by its language code.

## Translation format for arguments

If you want to use arguments in a translated value, the value must be in the format like "Here is the first {0} replacement. Here the 2nd {1}.". Each arguments is represented by a "{..}" in the translation, starting with 0 for the first argument.

The argument replacement is done by the `MessageFormat` class of the JRE. Look there for more information about the further formatting rules.

## Missing Translations

_NILS_ provides two behaviours if translations could not be found:

1. Return the key with some characters around it.
2. Throw an exception, that the translation could not be found.

The first one is the default case. If a translation key was not found the translation surrounded by `[` and `]` will be returned (i. e. `[persons.name]`).

The `NilsConfig` provides methods to configure this: 

* `escapeIfMissing(false)` : A flag, if an exception should be thrown, if the translation could not be found. `false` means throw an exception.
* `escapePattern("@{0}")` : Escape a missing exception in the format `@persons.name`. The default is `[{0}]`. The escapePattern must contain the string "{0}". 

## Include Translations

_NILS_ provides a simple way to include translations from already defined keys.

Let's asume you have a class person with the attribute `surname`, `firstName` and `birthday`. Also you have an inherited class called Employee.
Normally you must define all keys in a ResourceBundle like

```
Person.surname = Surname
Person.firstname = First name
Person.birthday = Birthday

Employee.surname = Surname
Employee.firstname = First name
Employee.birthday = Birthday
```

With _NILS_ you can include the keys from the Person easyly by using the include tag at the Employee.

```
Person.surname = Surname
Person.firstname = First name
Person.birthday = Birthday

Employee@include = Person       # (1)
Employee.surname = Family name  # (2)
```

* (1) This line include all keys from Person. If your application request for example `Employee.firstname` NILS will do a lookup and return the value from `Person.firstname`.
* (2) Employee overwrites the key for `surname`. So `Employee.surname` will return 'Family name' instead of 'Surname'.

If you want to include more that one base you can add the values semicolon separated:

```
Employee@include = Person;com.myproject.BaseClass
```

The ordering is important. The first look up will be taken on `Person`. The person has not a translation for the requested key, `com.myproject.BaseClass` will be called.

The `NilsConfig` provides a method to configure the include tag:

* `includeTag(String)`: Set the tag used for including other keys. The default is `@include`.

## Caveats / hacks

### NILS and Java's module-info.java

If you use _NILS_ with a module based application and want to use the resource bundles of your application you must give access to the folder containing the resource bundles using the `open` keyword.

```
module xyz {
  export my.package;
  open lang;
}
```

In the example lang is the folder containing the resource files.

## Extending NILS

### Writing your own adapter

_NILS_ is an abstraction layer to NLS. The access to the translation is encapsulated by adapters.

Currently there is only one implementation for an adapter: ResourceBundles.

If you need another kind of translation source you can implement your own Adapter. (Maybe for JSON or reading from a database).

To extend NILS you must implement the following classes (located in the package `com.codepulsar.nils.core.adapter`):

* `Adapter`: The Adapter is the concrete implementation, accessing your translation source. 
* `AdapterConfig`: The configuration for the adapter. Is used by the `NilsFactory`.
* `AdapterFactory`: A factory using the AdapterConfig to create a new Adapter object. The Factory must provide a parameterless default constructor.

The tests in the core module provides a really simple implementation for a Adapter using a fix Map storing the translations (see `com.codepulsar.nils.core.testadapter`).
