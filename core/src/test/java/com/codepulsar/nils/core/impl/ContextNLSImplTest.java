package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.config.SuppressableErrorTypes.ALL;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.MISSING_TRANSLATION;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NLS_PARAMETER_CHECK;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.api.NLS;
import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.handler.TranslationFormatter;
import com.codepulsar.nils.core.testadapter.StaticAdapter;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;
import com.codepulsar.nils.core.testdata.Dummy;

public class ContextNLSImplTest {

  private static final String DATA_PROVIDER =
      "com.codepulsar.nils.core.impl.ContextNLSImplTestDataProvider";

  @Test
  public void constructor_nullParent() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    NLS parent = null;

    // Act / Assert
    assertThatThrownBy(() -> new ContextNLSImpl(parent, config, "context"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'parent' cannot be null.");
  }

  @Test
  public void constructor_nullConfig() {
    // Arrange
    NilsConfig config = null;
    var parent = createParent();

    // Act / Assert
    assertThatThrownBy(() -> new ContextNLSImpl(parent, config, "context"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  public void constructor_emptyContext() {
    // Arrange
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent();

    // Act / Assert
    assertThatThrownBy(() -> new ContextNLSImpl(parent, config, " "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context' cannot be empty or blank.");
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_string_getByKey_invalid")
  public void string_getByKey_invalid(String key, String errMsg) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_string_getByKey_invalid_suppressed")
  public void string_getByKey_invalid_suppressed(String key, String expected) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(key);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void string_getByKey_found() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get("simple");

    // Assert
    assertThat(result).isEqualTo("A simple translation (Context)");
    assertThat(result).isEqualTo(parent.get("context.simple"));
  }

  @Test
  public void string_getByKey_notFound_escaping_errorType_MISSING_TRANSLATION() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(MISSING_TRANSLATION);
    var parent = createParent(config);

    var underTest = new ContextNLSImpl(parent, config, "context");
    // Act
    var result = underTest.get("not.found");

    // Assert
    assertThat(result).isEqualTo("[context.not.found]");
    assertThat(result).isEqualTo(parent.get("context.not.found"));
  }

  @Test
  public void string_getByKey_notFound_exception_errorType_none() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'context.not.found' and locale 'en_US'.");

    // Act / Assert same behavior as parent
    assertThatThrownBy(() -> parent.get("context.not.found"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'context.not.found' and locale 'en_US'.");
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_string_getByKeyAndArgs_invalid")
  public void string_getByKeyAndArgs_invalid(String key, Object[] args, String errMsg) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, args))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_string_getByKeyAndArgs_invalid_suppressed")
  public void string_getByKeyAndArgs_invalid_suppressed(
      String key, Object[] args, String expected) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(key, args);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void string_getByKeyAndArgs_nullArgs() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get("with_args", (Object[]) null);

    // Assert
    assertThat(result).isEqualTo("A {0} with {1} (Context).");
    assertThat(result).isEqualTo(parent.get("context.with_args", (Object[]) null));
  }

  @Test
  public void string_getByKeyAndArgs_emptyArgs() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get("with_args", new Object[] {});

    // Assert
    assertThat(result).isEqualTo("A {0} with {1} (Context).");
    assertThat(result).isEqualTo(parent.get("context.with_args", new Object[] {}));
  }

  @Test
  public void string_getByKeyAndArgs_found() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get("with_args", "First", 200L);

    // Assert
    assertThat(result).isEqualTo("A First with 200 (Context).");
    assertThat(result).isEqualTo(parent.get("context.with_args", "First", 200L));
  }

  @Test
  public void string_getByKeyAndArgs_notFound_escaping() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(ALL);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get("not.found", "with a value");

    // Assert
    assertThat(result).isEqualTo("[context.not.found]");
    assertThat(result).isEqualTo(parent.get("context.not.found", "with a value"));
  }

  @Test
  public void string_getByKeyAndArgs_notFound_exception() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'context.not.found' and locale 'en_US'.");

    // Act / Assert same behavior as parent
    assertThatThrownBy(() -> parent.get("context.not.found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'context.not.found' and locale 'en_US'.");
  }

  @Test
  public void string_getByKeyAndArgs_found_stringformat() {
    // Arrange
    var config =
        NilsConfig.init(new StaticAdapterConfig())
            .translationFormatter(TranslationFormatter.STRING_FORMAT);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get("with_args_string_format", "First", 200L);

    // Assert
    assertThat(result).isEqualTo("A First with 200 (Context).");
    assertThat(result).isEqualTo(parent.get("context.with_args_string_format", "First", 200L));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_class_getByKey_invalid")
  public void class_getByKey_invalid(Class<?> key, String subKey, String errMsg) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, subKey))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_class_getByKey_invalid_suppressed")
  public void class_getByKey_invalid_suppressed(Class<?> key, String subKey, String expected) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(key, subKey);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void class_getByKey_foundSimpleClassnameResolver() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    assertThat(config.getClassPrefixResolver()).isEqualTo(ClassPrefixResolver.SIMPLE_CLASSNAME);

    // Act
    var result = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(result).isEqualTo("Attribute (Context / Dummy)");
  }

  @Test
  public void class_getByKey_foundFqnClassnameResolver() {
    // Arrange
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig())
            .classPrefixResolver(ClassPrefixResolver.FQN_CLASSNAME);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(result).isEqualTo("Attribute (Context / Dummy FQN)");
  }

  @Test
  public void class_getByKey_notFound_escaping() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(MISSING_TRANSLATION);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(result).isEqualTo("[context.Dummy.not_found]");
    assertThat(result).isEqualTo(parent.get("context.Dummy.not_found"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_class_getByKeyAndArgs_invalid")
  public void class_getByKeyAndArgs_invalid(
      Class<?> key, String subKey, Object[] args, String errMsg) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, subKey, args))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_class_getByKeyAndArgs_invalid_suppressed")
  public void class_getByKeyAndArgs_invalid_suppressed(
      Class<?> key, String subKey, Object[] args, String expected) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(key, subKey, args);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void class_getByKeyAndArgs_nullArgs() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(Dummy.class, "with_args", (Object[]) null);

    // Assert
    assertThat(result).isEqualTo("A {0} with {1} (Context / Dummy).");
  }

  @Test
  public void class_getByKeyAndArgs_emptyArgs() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(Dummy.class, "with_args", new Object[] {});

    // Assert
    assertThat(result).isEqualTo("A {0} with {1} (Context / Dummy).");
  }

  @Test
  public void class_getByKeyAndArgs_found() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);

    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(Dummy.class, "with_args", "First", 200L);

    // Assert
    assertThat(result).isEqualTo("A First with 200 (Context / Dummy).");
  }

  @Test
  public void class_getByKeyAndArgs_notFound_escaping() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(MISSING_TRANSLATION);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.get(Dummy.class, "not_found", "with a value");

    // Assert
    assertThat(result).isEqualTo("[context.Dummy.not_found]");
    assertThat(result).isEqualTo(parent.get("context.Dummy.not_found", "with a value"));
  }

  @Test
  public void class_getByKeyAndArgs_notFound_exception() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(Dummy.class, "not_found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'context.Dummy.not_found' and locale 'en_US'.");

    // Act / Assert same behavior as parent
    assertThatThrownBy(() -> parent.get("context.Dummy.not_found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'context.Dummy.not_found' and locale 'en_US'.");
  }

  @Test
  public void context_string() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.context("sub_context");

    // Assert
    assertThat(result).isNotNull();

    // Act
    var call = result.get("attribute");

    // Assert
    assertThat(call).isEqualTo("Attribute (Context / Subcontext)");
    assertThat(call).isEqualTo(underTest.get("sub_context.attribute"));
    assertThat(call).isEqualTo(parent.get("context.sub_context.attribute"));
  }

  @Test
  public void context_class() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.context(Dummy.class);

    // Assert
    assertThat(result).isNotNull();

    // Act
    var call = result.get("attribute");

    // Assert
    assertThat(call).isEqualTo("Attribute (Context / Dummy)");
    assertThat(call).isEqualTo(underTest.get("Dummy.attribute"));
    assertThat(call).isEqualTo(parent.get("context.Dummy.attribute"));
  }

  @Test
  public void getFormats() {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig());
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.getFormats();
    // Assert
    assertThat(result).isNotNull();
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_context_string_invalid")
  public void context_string_invalid_exception(String context, String errorMsg) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.context(context))
        .isInstanceOf(NilsException.class)
        .hasMessage(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_context_string_invalid")
  public void context_string_invalid_suppressed(String context, String errorMsg) {
    // Arrange
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.context(context);

    // Assert
    assertThat(result).isEqualTo(underTest);
  }

  @Test
  public void context_class_invalid_exception() {
    // Arrange
    Class<?> context = null;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act / Assert
    assertThatThrownBy(() -> underTest.context(context))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-003: Parameter 'context' cannot be null.");
  }

  @Test
  public void context_class_invalid_suppressed() {
    // Arrange
    Class<?> context = null;

    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    var parent = createParent(config);
    var underTest = new ContextNLSImpl(parent, config, "context");

    // Act
    var result = underTest.context(context);

    // Assert
    assertThat(result).isEqualTo(underTest);
  }

  private NLS createParent() {
    var config = NilsConfig.init(new StaticAdapterConfig());
    return createParent(config);
  }

  private NLS createParent(NilsConfig config) {
    var locale = Locale.US;
    return new NLSImpl(new StaticAdapter(), config, locale);
  }
}
