package com.codepulsar.nils.adapter.snakeyaml;

import java.util.Locale;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;
/** The factory for the {@link SnakeYamlAdapter}. */
public class SnakeYamlAdapterFactory implements AdapterFactory<SnakeYamlAdapter> {

  @Override
  public SnakeYamlAdapter create(AdapterConfig config, Locale locale) {
    return new SnakeYamlAdapter(config, locale);
  }
}
