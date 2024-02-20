package com.codepulsar.nils.core.adapter.util;

import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import com.codepulsar.nils.core.util.ParameterCheck;
/**
 * The {@link MapTranslationRetriever} is an implementation of the {@link TranslationRetriever} for
 * translations that are structures as {@code Map} of {@code String} to {@code Object}, which are
 * used for examples in json or yaml serializers.
 */
public class MapTranslationRetriever implements TranslationRetriever {
  private final Map<String, Object> translations;
  /**
   * Create a new instance.
   *
   * @param translations The Map with the keys and translations
   */
  public MapTranslationRetriever(Map<String, Object> translations) {
    this.translations = ParameterCheck.notNull(translations, "translations");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<String> retrieve(String key) {
    if (key == null) {
      return Optional.empty();
    }
    StringTokenizer keyParts = new StringTokenizer(key, ".");
    Map<String, Object> latest = translations;
    while (keyParts.hasMoreTokens()) {
      String part = keyParts.nextToken();
      Object value = latest.get(part);
      if (value == null) {
        return Optional.empty();
      } else if (value instanceof String) {
        if (!keyParts.hasMoreTokens()) {
          return Optional.of((String) value);
        } else {
          return Optional.empty();
        }
      } else {
        if (value instanceof Map) {
          if (keyParts.hasMoreTokens()) {
            latest = (Map<String, Object>) value;
          } else {
            return Optional.empty();
          }
        } else {
          return Optional.of(value.toString());
        }
      }
    }
    return Optional.empty();
  }
}
