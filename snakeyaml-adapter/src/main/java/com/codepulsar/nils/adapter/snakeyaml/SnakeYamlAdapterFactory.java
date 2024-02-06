package com.codepulsar.nils.adapter.snakeyaml;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
/** The factory for the {@link SnakeYamlAdapter}. */
public class SnakeYamlAdapterFactory implements AdapterFactory<SnakeYamlAdapter> {

  @Override
  public SnakeYamlAdapter create(NilsConfig<?> config, Locale locale) {
    return new SnakeYamlAdapter(config, locale);
  }
}
