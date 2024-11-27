package com.codepulsar.nils.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.NilsFactory;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.testdata.Dummy;

public class ResourceBundleIntegrationTest {
  private ResourceBundleAdapterConfig adapterConfig;

  private Locale current;

  @BeforeEach
  void setup() {
    current = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
    adapterConfig = ResourceBundleAdapterConfig.init(this).baseFileName("test/integration");
  }

  @AfterEach
  void resetLocale() {
    Locale.setDefault(current);
  }

  @Test
  void string_getByKey_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var underTest = NilsFactory.init(adapterConfig).nls(locale);

    // Act
    var value = underTest.get("simple");

    // Assert
    assertThat(value).isEqualTo("A simple translation");
  }

  @Test
  void string_getByKey() {
    // Arrange
    var locale = Locale.GERMAN;
    var underTest = NilsFactory.init(adapterConfig).nls(locale);

    // Act
    var value = underTest.get("simple");

    // Assert
    assertThat(value).isEqualTo("Eine einfache Übersetzung");
  }

  @Test
  void string_getByKey_fallback() {
    // Arrange
    var locale = Locale.GERMAN;
    var underTest = NilsFactory.init(adapterConfig).nls(locale);

    // Act
    var value = underTest.get("fallback");

    // Assert
    assertThat(value).isEqualTo("A fallback translation");
  }

  @Test
  void string_getByKey_notFound_escaping() {
    // Arrange
    var locale = Locale.ENGLISH;
    var underTest = NilsFactory.init(adapterConfig.suppressErrors(true)).nls(locale);

    // Act
    var value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo("[not.found]");
  }

  @Test
  void string_getByKey_notFound_exception() {
    // Arrange
    var locale = Locale.ENGLISH;
    var underTest = NilsFactory.init(adapterConfig.suppressErrors(false)).nls(locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: Could not find a translation for key 'not.found' and locale 'en'.");
  }

  @Test
  void string_getByKeyAndArgs_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var underTest = NilsFactory.init(adapterConfig).nls(locale);

    // Act
    var value = underTest.get("with_args", "First", 200L);

    // Assert
    assertThat(value).isEqualTo("A First with 200.");
  }

  @Test
  void class_getByKey_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var underTest = NilsFactory.init(adapterConfig).nls(locale);

    // Act
    var value = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(value).isEqualTo("Attribute");
  }

  @Test
  void class_getByKey_notFound_escaping_changed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = adapterConfig.escapePattern(">>{0}<<").suppressErrors(true);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act
    var value = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(value).isEqualTo(">>Dummy.not_found<<");
  }

  @Test
  void resolveKeyForOtherIncludeTag() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = adapterConfig.includeTag("[include]");
    var adapter = new ResourceBundleAdapter(adapterConfig, locale);
    var underTest = NilsFactory.init(config).nls(locale);

    // Actual
    assertThat(adapter.getTranslation("data.Message.[include]").isPresent()).isTrue();
    assertThat(adapter.getTranslation("data.Message.buttons.ok").isPresent()).isFalse();
    assertThat(adapter.getTranslation("data.AlternativeMessage.buttons.ok").isPresent()).isTrue();

    // Act
    String translation = underTest.get("data.Message.buttons.ok");

    // Assert
    assertThat(translation).isEqualTo("Ok (AlternativeMessage)");
  }

  @Test
  void resolveValueWithMoreLevels() {
    // Arrange
    var locale = Locale.ENGLISH;
    var adapter = new ResourceBundleAdapter(adapterConfig, locale);
    var underTest = NilsFactory.init(adapterConfig).nls(locale);

    // Actual
    assertThat(adapter.getTranslation("Level2._include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Level1._include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Level1.value").isPresent()).isFalse();
    assertThat(adapter.getTranslation("Level0.value").isPresent()).isTrue();

    // Act
    var translation = underTest.get("Level2.value");

    // Assert
    assertThat(translation).isEqualTo("Value (Level0)");
  }

  @Test
  void circularInclude() {
    // Arrange
    var locale = Locale.ENGLISH;
    var adapter = new ResourceBundleAdapter(adapterConfig, locale);
    var underTest = NilsFactory.init(adapterConfig).nls(locale);

    // Actual
    assertThat(adapter.getTranslation("Cycle1._include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Cycle2._include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Cycle1.value").isPresent()).isFalse();
    assertThat(adapter.getTranslation("Cycle2.value").isPresent()).isFalse();

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("Cycle1.value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-002: Found circular include on 'Cycle2'.");
  }
}
