package com.codepulsar.nils.testadapter;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.codepulsar.nils.NilsConfig;
import com.codepulsar.nils.adapter.BaseAdapter;

public class StaticAdapter extends BaseAdapter {

  private Map<String, String> translations =
      Map.of("simple", "A simple translation", "with_args", "A {0} with {1}.");

  public StaticAdapter(NilsConfig config, Locale locale) {
    super(config, locale);
  }

  @Override
  protected Optional<String> resolveTranslation(String key) {
    return Optional.ofNullable(translations.get(key));
  }
}
