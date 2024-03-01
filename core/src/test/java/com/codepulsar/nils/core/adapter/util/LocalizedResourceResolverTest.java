package com.codepulsar.nils.core.adapter.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.adapter.config.LocalizedResourceConfig;
import com.codepulsar.nils.api.error.NilsException;

public class LocalizedResourceResolverTest {

  @Test
  public void ctor() {
    // Arrange
    var resolverConfig = new LocalizedResourceResolverConfigTestImpl("test/resource.properties");
    var locale = Locale.ITALY;
    Function<String, InputStream> resourceToInputStreamResolver = this::getInputStream;

    // Act
    var underTest =
        new LocalizedResourceResolver(resolverConfig, locale, resourceToInputStreamResolver);

    // Assert
    assertThat(underTest).isNotNull();
  }

  @Test
  public void ctor_parameterConfigIsNull() {
    // Arrange
    LocalizedResourceConfig resolverConfig = null;
    var locale = Locale.FRANCE;
    Function<String, InputStream> resourceToInputStreamResolver = this::getInputStream;

    // Act / Assert
    assertThatThrownBy(
            () ->
                new LocalizedResourceResolver(
                    resolverConfig, locale, resourceToInputStreamResolver))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'resolverConfig' cannot be null.");
  }

  @Test
  public void ctor_parameterLocaleIsNull() {
    // Arrange
    var resolverConfig = new LocalizedResourceResolverConfigTestImpl("test/resource.properties");
    Locale locale = null;
    Function<String, InputStream> resourceToInputStreamResolver = this::getInputStream;

    // Act / Assert
    assertThatThrownBy(
            () ->
                new LocalizedResourceResolver(
                    resolverConfig, locale, resourceToInputStreamResolver))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void ctor_parameterResourceToInputStreamResolverIsNull() {
    // Arrange
    var resolverConfig = new LocalizedResourceResolverConfigTestImpl("test/resource.properties");
    var locale = Locale.CANADA;
    Function<String, InputStream> resourceToInputStreamResolver = null;

    // Act / Assert
    assertThatThrownBy(
            () ->
                new LocalizedResourceResolver(
                    resolverConfig, locale, resourceToInputStreamResolver))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'resourceToInputStreamResolver' cannot be null.");
  }

  @Test
  public void resolve_foundForLocale() {
    // Arrange
    var resolverConfig = new LocalizedResourceResolverConfigTestImpl("test/resource.properties");
    var locale = Locale.GERMANY;
    Function<String, InputStream> resourceToInputStreamResolver = this::getInputStream;
    try (var underTest =
        new LocalizedResourceResolver(resolverConfig, locale, resourceToInputStreamResolver)) {

      // Act
      var result = underTest.resolve();
      assertThat(result).isNotNull();
      assertThat(underTest.getUsedResourceName()).isEqualTo("/test/resource_de_DE.properties");
    }
  }

  @Test
  public void resolve_fallbackForLocale() {
    // Arrange
    var resolverConfig = new LocalizedResourceResolverConfigTestImpl("test/resource.properties");
    var locale = Locale.UK;
    Function<String, InputStream> resourceToInputStreamResolver = this::getInputStream;
    try (var underTest =
        new LocalizedResourceResolver(resolverConfig, locale, resourceToInputStreamResolver)) {

      // Act
      var result = underTest.resolve();
      assertThat(result).isNotNull();
      assertThat(underTest.getUsedResourceName()).isEqualTo("/test/resource_en.properties");
    }
  }

  @Test
  public void resolve_errorResolvingResource() {
    // Arrange
    var resolverConfig = new LocalizedResourceResolverConfigTestImpl("test/resource.properties");
    var locale = Locale.GERMANY;
    Function<String, InputStream> resourceToInputStreamResolver =
        s -> {
          throw new RuntimeException("Test Error");
        };
    try (var underTest =
        new LocalizedResourceResolver(resolverConfig, locale, resourceToInputStreamResolver)) {

      // Act / Assert
      assertThatThrownBy(() -> underTest.resolve())
          .isInstanceOf(NilsException.class)
          .hasMessage("NILS-007: Error getting resource '/test/resource_de_DE.properties'.");
      assertThat(underTest.getUsedResourceName()).isNull();
    }
  }

  @Test
  public void resolve_noResourceFound() {
    // Arrange
    var resolverConfig =
        new LocalizedResourceResolverConfigTestImpl("test/not_existing.properties");
    var locale = Locale.GERMANY;
    Function<String, InputStream> resourceToInputStreamResolver = this::getInputStream;
    try (var underTest =
        new LocalizedResourceResolver(resolverConfig, locale, resourceToInputStreamResolver)) {

      // Act / Assert
      assertThatThrownBy(() -> underTest.resolve())
          .isInstanceOf(NilsException.class)
          .hasMessage(
              "NILS-008: Could not find a resource for baseFilename 'test/not_existing.properties'.");
      assertThat(underTest.getUsedResourceName()).isNull();
    }
  }

  @Test
  public void initBaseFilename_noFileExtensionFound() {
    // Arrange
    var resolverConfig = new LocalizedResourceResolverConfigTestImpl("test/resource");
    var locale = Locale.ITALY;
    Function<String, InputStream> resourceToInputStreamResolver = this::getInputStream;

    // Act / Assert
    assertThatThrownBy(
            () ->
                new LocalizedResourceResolver(
                    resolverConfig, locale, resourceToInputStreamResolver))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-009: Could not resolve file extension on baseFileName 'test/resource'.");
  }

  private InputStream getInputStream(String s) {
    try {
      return getClass().getModule().getResourceAsStream(s);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private class LocalizedResourceResolverConfigTestImpl implements LocalizedResourceConfig {

    private String baseFileName;

    public LocalizedResourceResolverConfigTestImpl(String baseFileName) {
      this.baseFileName = baseFileName;
    }

    @Override
    public String getBaseFileName() {
      return baseFileName;
    }

    @Override
    public Module getOwner() {
      return getClass().getModule();
    }

    @Override
    public boolean isFallbackActive() {
      return true;
    }
  }
}
