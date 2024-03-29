package com.codepulsar.nils.adapter.jackson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseLocalizedResourceAdapter;

public class JacksonAdapterYamlTest {

  private Locale current;

  private AdapterContext<JacksonAdapter> context;

  @BeforeEach
  public void defineDefault() {
    current = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
    context = new AdapterContext<JacksonAdapter>().factory(new JacksonAdapterFactory());
  }

  @AfterEach
  public void resetLocale() {
    Locale.setDefault(current);
  }

  @Test
  public void nullLocale() {
    // Arrange
    Locale locale = null;
    var config = JacksonAdapterYamlConfig.init(this);
    context.config(config).locale(locale);

    // Act / Assert
    assertThatThrownBy(() -> new JacksonAdapter(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context.locale' cannot be null.");
  }

  @Test
  public void nullConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig<?> config = null;
    context.config(config).locale(locale);

    // Act / Assert
    assertThatThrownBy(() -> new JacksonAdapter(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context.config' cannot be null.");
  }

  @Test
  public void defaultConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this);
    context.config(config).locale(locale);

    // Act
    var underTest = new JacksonAdapter(context);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest).isInstanceOf(BaseLocalizedResourceAdapter.class);
  }

  @Test
  public void baseFileNameForYaml() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/existing.yaml");
    context.config(config).locale(locale);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void invalidBaseFileName() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/non_existing");
    context.config(config).locale(locale);

    // Act / Assert
    assertThatThrownBy(() -> new JacksonAdapter(context))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-008: Could not find a resource for baseFilename 'test/non_existing.yaml'.");
  }

  @Test
  public void fallbackFile() {
    // Arrange
    var locale = Locale.ITALIAN;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/existing.yaml");
    context.config(config).locale(locale);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void translateKeyFound_nestedKeys() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this);
    context.config(config).locale(locale);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("deep");
    // Assert
    assertThat(value).isEmpty();

    // Act
    value = underTest.getTranslation("deep.deepkey3");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Key 3");

    // Act
    value = underTest.getTranslation("deep.deeper.deeperkey4");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Key 4");

    // Act
    value = underTest.getTranslation("deep.non_existing");
    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void translateKeyFound1() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/existing.yaml");
    context.config(config).locale(locale);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void translateKeyFound2() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/existing.yaml");
    context.config(config).locale(locale);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Ich bin übersetzt!");
  }

  @Test
  public void translateKeyFallback() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/existing.yaml");
    context.config(config).locale(locale);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.fallback");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm a fallback!");
  }

  @Test
  public void translateKeyFallbackNotFound() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/existing.yaml");
    context.config(config).locale(locale);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.fallback_notfound");

    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void translateKeyFallback_fallbackInactive() {
    // Arrange
    var locale = Locale.GERMAN;
    var config =
        JacksonAdapterYamlConfig.init(this).baseFileName("test/existing").fallbackActive(false);
    context.locale(locale).config(config);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.fallback");

    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void translateKeyNotFound() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/existing.yaml");
    context.config(config).locale(locale);
    var underTest = new JacksonAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me.butImNotThere");
    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void corruptJsonFile() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/corrupt.yaml");
    context.config(config).locale(locale);

    // Act / Assert
    assertThatThrownBy(() -> new JacksonAdapter(context))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-201: Error reading file '/test/corrupt.yaml'.");
  }

  @Test
  public void nonJsonFile() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/non_yaml.yaml");
    context.config(config).locale(locale);

    // Act / Assert
    assertThatThrownBy(() -> new JacksonAdapter(context))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-201: Error reading file '/test/non_yaml.yaml'.");
  }
}
