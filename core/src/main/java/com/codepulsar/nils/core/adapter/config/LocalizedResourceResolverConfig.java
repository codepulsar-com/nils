package com.codepulsar.nils.core.adapter.config;

import com.codepulsar.nils.core.adapter.Adapter;
import com.codepulsar.nils.core.adapter.util.LocalizedResourceResolver;

/**
 * The interface <strong>LocalizedResourceResolverConfig</strong> defines methods used by the {@link
 * LocalizedResourceResolver}.
 *
 * <p><em>Note:</em> This interface is normally internally used by {@link Adapter} implementations.
 */
public interface LocalizedResourceResolverConfig {
  /**
   * Gets the base name of the resource files.
   *
   * <p>The name can include the paths and may contains a file extension, depending on the {@link
   * Adapter} implementation.
   *
   * @return The base name of the resource files.
   */
  String getBaseFileName();
}
