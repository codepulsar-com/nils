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
   * Gets the base name of the resource files.
   *
   * <p>The name can include the paths and may contains a file extension, depending on the {@link
   * Adapter} implementation.
   *
   * @return The base name of the resource files.
   */
  String getBaseFileName();

  /**
   * Gets the owner module for the nls support.
   *
   * @return A Module object.
   */
  Module getOwner();
}
