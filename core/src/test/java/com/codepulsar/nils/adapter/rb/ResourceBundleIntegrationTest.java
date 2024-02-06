package com.codepulsar.nils.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.NilsFactory;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.testdata.Dummy;

public class ResourceBundleIntegrationTest {
  private ResourceBundleAdapterConfig adapterConfig;

  private Locale current;

  @BeforeEach
  public void defineDefault() {
    current = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
  }

  @BeforeEach
  public void setup() {
    adapterConfig = ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/integration");
  }

  @AfterEach
  public void resetLocale() {
    Locale.setDefault(current);
  }

  @Test
  public void string_getByKey_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act
    var value = underTest.get("simple");

    // Assert
    assertThat(value).isEqualTo("A simple translation");
  }

  @Test
  public void string_getByKey() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = NilsConfig.init(adapterConfig);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act
    var value = underTest.get("simple");

    // Assert
    assertThat(value).isEqualTo("Eine einfache Übersetzung");
  }

  @Test
  public void string_getByKey_fallback() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = NilsConfig.init(adapterConfig);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act
    var value = underTest.get("fallback");

    // Assert
    assertThat(value).isEqualTo("A fallback translation");
  }

  @Test
  public void string_getByKey_notFound_escaping() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig).suppressErrors(true);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act
    var value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo("[not.found]");
  }

  @Test
  public void string_getByKey_notFound_exception() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig).suppressErrors(false);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: Could not find a translation for key 'not.found' and locale 'en'.");
  }

  @Test
  public void string_getByKeyAndArgs_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act
    var value = underTest.get("with_args", "First", 200L);

    // Assert
    assertThat(value).isEqualTo("A First with 200.");
  }

  @Test
  public void class_getByKey_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act
    var value = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(value).isEqualTo("Attribute");
  }

  @Test
  public void class_getByKey_notFound_escaping_changed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig).escapePattern(">>{0}<<").suppressErrors(true);
    var underTest = NilsFactory.init(config).nls(locale);

    // Act
    var value = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(value).isEqualTo(">>Dummy.not_found<<");
  }

  @Test
  public void resolveKeyForOtherIncludeTag() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig).includeTag("[include]");
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
  public void resolveValueWithMoreLevels() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig);
    var adapter = new ResourceBundleAdapter(adapterConfig, locale);
    var underTest = NilsFactory.init(config).nls(locale);

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
  public void circularInclude() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(adapterConfig);
    var adapter = new ResourceBundleAdapter(adapterConfig, locale);
    var underTest = NilsFactory.init(config).nls(locale);

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
