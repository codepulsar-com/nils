package com.codepulsar.nils.core.adapter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.testadapter.BaseLocalizedResourceAdapterTestee;
import com.codepulsar.nils.core.testadapter.BaseLocalizedResourceAdapterTesteeConfig;
import com.codepulsar.nils.core.testadapter.BaseLocalizedResourceAdapterTesteeFactory;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class BaseLocalizedResourceAdapterTest {

  private Locale current;
  private AdapterContext<BaseLocalizedResourceAdapterTestee> context;

  @BeforeEach
  public void defineDefault() {
    current = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
    context =
        new AdapterContext<BaseLocalizedResourceAdapterTestee>()
            .factory(new BaseLocalizedResourceAdapterTesteeFactory());
  }

  @AfterEach
  public void resetLocale() {
    Locale.setDefault(current);
  }

  @Test
  public void nullContext() {
    // Act / Assert
    assertThatThrownBy(() -> new BaseLocalizedResourceAdapterTestee(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context' cannot be null.");
  }

  @Test
  public void nullLocale() {
    // Arrange
    Locale locale = null;
    var config = BaseLocalizedResourceAdapterTesteeConfig.init(this);
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new BaseLocalizedResourceAdapterTestee(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context.locale' cannot be null.");
  }

  @Test
  public void nullConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    NilsConfig<?> config = null;
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new BaseLocalizedResourceAdapterTestee(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context.config' cannot be null.");
  }

  @Test
  public void invalidConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    NilsConfig<?> config = new StaticAdapterConfig();
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new BaseLocalizedResourceAdapterTestee(context))
        .isInstanceOf(NilsConfigException.class)
        .hasMessageContaining("NILS-004")
        .hasMessageContaining("The provided AdapterConfig")
        .hasMessageContaining("does not implement");
  }
}
