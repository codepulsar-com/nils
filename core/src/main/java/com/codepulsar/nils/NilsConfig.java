package com.codepulsar.nils;

import java.text.MessageFormat;

import com.codepulsar.nils.adapter.AdapterConfig;
import com.codepulsar.nils.util.ParameterCheck;
/** The configuration of the Nils library. */
public class NilsConfig {
  private AdapterConfig adapterConfig;

  private boolean escapeIfMissing = true;

  private String escapePattern = "[{0}]";

  private NilsConfig(AdapterConfig adapterConfig) {
    this.adapterConfig = adapterConfig;
  }
  /**
   * Gets the {@link AdapterConfig} object.
   *
   * @return The {@link AdapterConfig} object.
   */
  public AdapterConfig getAdapterConfig() {
    return adapterConfig;
  }

  /**
   * Gets the flag if the translation key should be escaped instead of throwing an exception if a
   * translation could not be by the adapter.
   *
   * <p>The default value is <code>true</code>.
   *
   * @return The value of the flag.
   * @see #escapeIfMissing(boolean)
   */
  public boolean isEscapeIfMissing() {
    return escapeIfMissing;
  }

  /**
   * Gets the escape pattern if a translation is missing, but no exception should be thrown.
   *
   * <p>The escape pattern must contain <tt>{0}</tt>.
   *
   * @return The pattern.
   * @see #isEscapeIfMissing()
   * @see #escapePattern(String)
   */
  public String getEscapePattern() {
    return escapePattern;
  }

  /**
   * Sets the flag, if the translation key should be escaped instead of throwing an exception if an
   * translation could not be found.
   *
   * <p>The default value is <code>true</code>.
   *
   * @param escapeIfMissing The value of the flag.
   * @return This config object.
   * @see #isEscapeIfMissing()
   */
  public NilsConfig escapeIfMissing(boolean escapeIfMissing) {
    this.escapeIfMissing = escapeIfMissing;
    return this;
  }

  /**
   * Sets the escape pattern if a translation is missing, but no exception should be thrown.
   *
   * <p>The escape pattern must contain <tt>{0}</tt>.
   *
   * @param escapePattern the pattern
   * @return This config object.
   * @see #isEscapeIfMissing()
   * @see #getEscapePattern()
   */
  public NilsConfig escapePattern(String escapePattern) {
    ParameterCheck.notNullEmptyOrBlank(escapePattern, "escapePattern");

    this.escapePattern = checkEscapePattern(escapePattern);
    return this;
  }

  private String checkEscapePattern(String pattern) {
    if (!pattern.contains("{0}")) {
      throw new IllegalArgumentException(
          "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\".");
    }
    if (pattern.contains("'{0}'")) {
      throw new IllegalArgumentException(
          "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\".");
    }
    try {
      MessageFormat.format(pattern, "TEST");
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException(
          "Parameter 'escapePattern' is invalid: " + ex.getMessage());
    }
    return pattern;
  }
  /**
   * Create a <code>NilsConfig</code> from an {@link AdapterConfig}.
   *
   * @param adapterConfig An {@link AdapterConfig} object.
   * @return The created NilsConfig.
   */
  public static NilsConfig init(AdapterConfig adapterConfig) {
    ParameterCheck.notNull(adapterConfig, "adapterConfig");
    return new NilsConfig(adapterConfig);
  }
}
