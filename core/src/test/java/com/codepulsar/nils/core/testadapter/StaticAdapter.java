package com.codepulsar.nils.core.testadapter;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.codepulsar.nils.core.adapter.Adapter;
import com.codepulsar.nils.core.adapter.AdapterConfig;

public class StaticAdapter implements Adapter {

  private Map<String, String> translations =
      Map.of(
          "simple",
          "A simple translation",
          "with_args",
          "A {0} with {1}.",
          "Dummy.attribute",
          "Attribute",
          "Dummy.with_args",
          "A {0} with {1}.");

  public StaticAdapter(AdapterConfig config, Locale locale) {}

  @Override
  public Optional<String> getTranslation(String key) {
    return Optional.ofNullable(translations.get(key));
  }
}
