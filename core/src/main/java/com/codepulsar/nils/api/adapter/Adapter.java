package com.codepulsar.nils.api.adapter;

import java.util.Optional;

import com.codepulsar.nils.adapter.rb.ResourceBundleAdapter;
import com.codepulsar.nils.api.NilsConfig;
/**
 * An <strong>Adapter</strong> provides an implementation for a specific kind to access localization
 * information (i. e. from ResourceBundles).
 *
 * <p>An adapter can have further configuration provided by an implementation of the {@link
 * NilsConfig} interface.
 *
 * <p>NILS can so easily extended by implementing an own Adapter for an own purpose.
 *
 * <p>A default implementation is the {@link ResourceBundleAdapter} using Java's <code>
 * ResourceBundle</code> to resolve a translation.
 *
 * @see NilsConfig
 * @see ResourceBundleAdapter
 */
public interface Adapter {
  /**
   * Resolve the translation value from the adapter implementation.
   *
   * @param key The translation key
   * @return The value or an empty <code>Optional</code>
   */
  Optional<String> getTranslation(String key);
}
