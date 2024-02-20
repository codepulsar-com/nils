package com.codepulsar.nils.core.adapter.util;

import java.util.Optional;

import com.codepulsar.nils.core.adapter.BaseLocalizedResourceAdapter;
/**
 * The {@link TranslationRetriever} is used from the {@link BaseLocalizedResourceAdapter} and
 * adapter implementations using {@link BaseLocalizedResourceAdapter} as base class.
 *
 * <p>Depended on the type of resource files different strategies to get the translation for a key
 * is necessary. These different strategies must implements this interface.
 *
 * @see MapTranslationRetriever
 */
public interface TranslationRetriever {
  /**
   * Retrieve a translation for a key.
   *
   * @param key The key for the translation.
   * @return The translation or empty if not found.
   */
  Optional<String> retrieve(String key);
}
