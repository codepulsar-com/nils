package com.codepulsar.nils.api.adapter.config;

import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.core.adapter.util.LocalizedResourceResolver;

/**
 * The interface <strong>LocalizedResourceConfig</strong> defines methods used by the {@link
 * LocalizedResourceResolver}.
 *
 * <p><em>Note:</em> This interface is normally internally used by {@link Adapter} implementations.
 */
public interface LocalizedResourceConfig {
  /**
   * Gets the base name for the localized files.
   *
   * <p>The name can include the paths with a file extension.
   *
   * <p>See for information about the default value in the concrete implementation class.
   *
   * @return The base name of the localized files.
   * @see #baseFileName(String)
   */
  String getBaseFileName();
  /**
   * Gets the flag, if a fallback to other resource files is active.
   *
   * <p>In case it is set to {@code true} a fallback to other localized resources ("de_DE" &gt; "de"
   * &gt; "") will be performed.
   *
   * @return {@code true} if active, else {@code false}.
   */
  boolean isFallbackActive();
  /**
   * Gets the owner module for the nls support.
   *
   * @return A Module object.
   */
  Module getOwner();
}
