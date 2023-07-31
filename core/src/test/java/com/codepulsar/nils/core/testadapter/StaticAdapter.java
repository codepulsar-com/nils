package com.codepulsar.nils.core.testadapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.codepulsar.nils.core.adapter.Adapter;

public class StaticAdapter implements Adapter {

  private final Map<String, String> translations;

  public StaticAdapter() {
    this.translations = new HashMap<>();
    translations.putAll(
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
            "Attribute (StaticResolver)"));

    translations.putAll(
        Map.of(
            "context.simple",
            "A simple translation (Context)",
            "context.with_args",
            "A {0} with {1} (Context).",
            "context.with_args_string_format",
            "A %1$s with %2$d (Context).",
            "context.Dummy.attribute",
            "Attribute (Context / Dummy)",
            "context.com.codepulsar.nils.core.testdata.Dummy.attribute",
            "Attribute (Context / Dummy FQN)",
            "context.Dummy.with_args",
            "A {0} with {1} (Context / Dummy).",
            "context.sub_context.attribute",
            "Attribute (Context / Subcontext)"));
  }

  @Override
  public Optional<String> getTranslation(String key) {
    return Optional.ofNullable(translations.get(key));
  }
}
