package com.codepulsar.nils.core.testadapter;

import java.util.Map;
import java.util.Optional;

import com.codepulsar.nils.core.adapter.Adapter;

public class StaticAdapter implements Adapter {

  private Map<String, String> translations =
      Map.of(
          "simple",
          "A simple translation",
          "with_args",
          "A {0} with {1}.",
          "with_args_string_format",
          "A %1$s with %2$d.",
          "Dummy.attribute",
          "Attribute",
          "Dummy.with_args",
          "A {0} with {1}.",
          "com.codepulsar.nils.core.testdata.Dummy.attribute",
          "Attribute (FQN)",
          "StaticClassResolver.attribute",
          "Attribute (StaticResolver)");

  @Override
  public Optional<String> getTranslation(String key) {
    return Optional.ofNullable(translations.get(key));
  }
}
