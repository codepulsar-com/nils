# Documentation

## Using different locales

_NILS_ can handle with different locales at a time. The `NilsFactory` provides access to different NLS objects.

* `nls()` : Returns an NLS object of the default locale used by the JVM.
* `nls(Locale)` : Returns an NLS object for a specific locale.
* `nls(String)` : Returns an NLS object, where the locale is reolved by its language code.

## Translations with arguments

Using the one of the methods `NLS.get(String key, Object... args)` or `NLS.get(Class<?> key, String subKey, Object... args)` the passed args will be used to format the translation value (if the value is correct).

_NILS_ comes with two implementation using formatting: 

* `TranslationFormatter.MESSAGE_FORMAT` : This implementation uses `MessageFormat.format()`
* `TranslationFormatter.STRING_FORMAT` : This implementation uses `String.format()`

The default is the MESSAGE_FORMAT varient (as offened used by resource bundles).

The NilsConfig has an option to change this: `translationFormatter(TranslationFormatter)`.

You can also implement your own TranslationFormatter and set it in the options.

An exception could be thrown if the translation format is invalid. You can suppress this error by using the `SuppressableErrorTypes.TRANSLATION_FORMAT_ERROR` in the NilsConfig. 

## Missing Translations

_NILS_ provides two behaviours if translations could not be found:

1. Return the key with some characters around it.
2. Throw an exception, that the translation could not be found.

The first one is the default case. If a translation key was not found the translation surrounded by `[` and `]` will be returned (i. e. `[persons.name]`).

The `NilsConfig` provides methods to configure this: 

* `suppressErrors(SuppressableErrorTypes.MISSING_TRANSLATION)` : Sets a flag, if an exception should be thrown. If the flag `MISSING_TRANSLATION` is set, no exception is thrown. The default is that an exception is thrown.
* `escapePattern("@{0}")` : Escape a missing exception in the format `@persons.name`. The default is `[{0}]`. The escapePattern must contain the string "{0}". 

## Suppress errors

During development or testing it is useful to get exceptions if for example a translation is missing.

On a production system a more relaxed way is sometimes better for the end user instead of getting an
error page because of a missing translation.


_NILS_ provides therefore a way to suppress certain error types. If one type is suppressed the user gets the escaped version of the requested translation (like '[Person.name]'). The error itself is logged via slf4j.

The `NilsConfig` provides with the method `suppressErrors(ErrorType, ...)` a way to configure the behaviour.

The class `SuppressableErrorTypes` defines the error types that can be suppressed. The following types are available:


| Type                    | Description
| ----------------------- | --------------------
| `ALL`                   | Suppress all error that can be suppressed (see other types).
| `NONE`                  | No error is suppressed.
| `INCLUDE_LOOP_DETECTED` | Include other keys leads into a loop.
| `MISSING_TRANSLATION`   | The translation source does not provide a translation for the requested key.
| `NLS_PARAMETER_CHECK`   |  The parameter call at the NLS interface is invalid.
| `TRANSLATION_FORMAT_ERROR` | The value of a translation could not be formatted with the used TranslationFormatter.
 
The default is `NONE`.

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

Employee.@include = Person      # (1)
Employee.surname = Family name  # (2)
```

* (1) This line include all keys from Person. If your application request for example `Employee.firstname` NILS will do a lookup and return the value from `Person.firstname`.
* (2) Employee overwrites the key for `surname`. So `Employee.surname` will return 'Family name' instead of 'Surname'.

If you want to include more that one base you can add the values semicolon separated:

```
Employee.@include = Person;com.myproject.BaseClass
```

The ordering is important. The first look up will be taken on `Person`. The person has not a translation for the requested key, `com.myproject.BaseClass` will be called.

The `NilsConfig` provides a method to configure the include tag:

* `includeTag(String)`: Set the tag used for including other keys. The default is `@include`.

## Class name resolving

The `NLS` interface provides two methods using a `Class` as prefix for a translation key:

* `get(Class<?> key, String subKey)`
* `get(Class<?> key, String subKey, Object... args)` 

The default behaviour is to use the simple name of a class. Therefore xyz.dummy.DummyClass will be `DummyClass` and a call of `nls.get(DummyClass.class, "test")` would be the translation key `DummyClass.test`.

How a class name is resolved is configurable in the NilsConfig using the method `nilsConfig.classPrefixResolver(ClassPrefixResolver)` by setting a ClassPrefixResolver implementation.

The _NILS_ provides two implementations:

* `ClassPrefixResolver.SIMPLE_CLASSNAME` : Using the simple name of a class (the default).
* `ClassPrefixResolver.FQN_CLASSNAME` : Using the full qualified name of a class (xyz.dummy.DummyClass will be `xyz.dummy.DummyClass`).

But own resolver could be implemented and used.

## Formats

_NILS_ provides different Formatter class in the `Locale` of a NLS object, for formatting or parsing dates, times, dates with time or different number formats.

You can get access to the formats be calling `NLS.getFormats()`.

The default format for dates, times and dates with time will be medium (i.e. Jul 25, 2023). In the NilsConfig there is the method `dateFormatStyle(FormatStyle)` where the format style of all date related formatter can be changed globally.

## Context based NLS

Sometimes translation are needed in a specific context.

Often these leads (i.e. using resource bundles or so) in code that duplicated parts of the translation key like:

```
nls.get("page.options.darkmode");
nls.get("page.options.showInbox");
```

We could say we are in the context 'page.options'.

_NILS_ provides a way to get only translations in a context. From an existing NLS object or from the NilsFactory. This could shorten the code as followed:

```
var contextNls = nls.context("page.options");
contextNls.get("darkmode");
contextNls.get("showInbox");
```

See: the `NLS.context`-methods and the `NilsFactory.context`-methods for more information.

## Caveats / hacks

### NILS and Java's module-info.java

If you use _NILS_ with a module based application and want to use the resource bundles of your application you must give access to the folder containing the resource bundles using the `open` keyword.

```
module xyz {
  export my.package;
  requires com.codepulsar.nils.core;
  open nls;
}
```

In the example `nls` is the folder containing the resource files.

## Existing Adapters

_NILS_ is an abstraction layer to NLS. The access to the translation is encapsulated by adapters.

Currently there are two implementations for adapters provided by _NILS_ :

* For ResourceBundles: Use the class `ResourceBundleAdapter` from `nils-core`.
* For JSON files: Use the class `GsonAdapter` from `nils-gson-adapter`. See [Gson Adapter](gson-adapter.md) for more information.

## Extending NILS

### Writing your own adapter

If you need another kind of translation source (as the existing ones) you can implement your own Adapter. (Maybe for reading translation from a database).

To extend NILS you must implement the following classes (located in the package `com.codepulsar.nils.core.adapter`):

* `Adapter`: The Adapter is the concrete implementation, accessing your translation source. 
* `AdapterConfig`: The configuration for the adapter. Is used by the `NilsFactory`.
* `AdapterFactory`: A factory using the AdapterConfig to create a new Adapter object. The Factory must provide a parameterless default constructor.

The tests in the core module provides a really simple implementation for a Adapter using a fix Map storing the translations (see `com.codepulsar.nils.core.testadapter`).
