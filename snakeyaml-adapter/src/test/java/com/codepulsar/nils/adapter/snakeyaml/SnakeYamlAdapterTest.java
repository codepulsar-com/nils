package com.codepulsar.nils.adapter.snakeyaml;

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

public class SnakeYamlAdapterTest {

  private Locale current;
  private AdapterContext<SnakeYamlAdapter> context;

  @BeforeEach
  public void defineDefault() {
    current = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
    context = new AdapterContext<SnakeYamlAdapter>().factory(new SnakeYamlAdapterFactory());
  }

  @AfterEach
  public void resetLocale() {
    Locale.setDefault(current);
  }

  @Test
  public void nullLocale() {
    // Arrange
    Locale locale = null;
    var config = SnakeYamlAdapterConfig.init(this);
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context.locale' cannot be null.");
  }

  @Test
  public void nullConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig<?> config = null;
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context.config' cannot be null.");
  }

  @Test
  public void defaultConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this);
    context.locale(locale).config(config);

    // Act
    var underTest = new SnakeYamlAdapter(context);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest).isInstanceOf(BaseLocalizedResourceAdapter.class);
  }

  @Test
  public void invalidBaseFileName() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/non_existing");
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(context))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-008: Could not find a resource for baseFilename 'test/non_existing.yaml'.");
  }

  @Test
  public void fallbackFile() {
    // Arrange
    var locale = Locale.ITALIAN;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    context.locale(locale).config(config);
    var underTest = new SnakeYamlAdapter(context);

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
    var config = SnakeYamlAdapterConfig.init(this);
    context.locale(locale).config(config);
    var underTest = new SnakeYamlAdapter(context);

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
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    context.locale(locale).config(config);
    var underTest = new SnakeYamlAdapter(context);

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
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    context.locale(locale).config(config);
    var underTest = new SnakeYamlAdapter(context);

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
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    context.locale(locale).config(config);
    var underTest = new SnakeYamlAdapter(context);

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
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    context.locale(locale).config(config);
    var underTest = new SnakeYamlAdapter(context);

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
        SnakeYamlAdapterConfig.init(this).baseFileName("test/existing").fallbackActive(false);
    context.locale(locale).config(config);
    var underTest = new SnakeYamlAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.fallback");

    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void translateKeyNotFound() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    context.locale(locale).config(config);
    var underTest = new SnakeYamlAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me.butImNotThere");
    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void corruptYamlFile() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/corrupt");
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(context))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-150: Error reading YAML file '/test/corrupt.yaml'.");
  }

  @Test
  public void nonYamlFile() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/non_yaml");
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(context))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-150: Error reading YAML file '/test/non_yaml.yaml'.");
  }
}
