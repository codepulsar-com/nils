package com.codepulsar.nils.adapter.jackson;

import java.util.Locale;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;
/** The factory for the {@link JacksonAdapter}. */
public class JacksonAdapterFactory implements AdapterFactory<JacksonAdapter> {

  @Override
  public JacksonAdapter create(AdapterConfig config, Locale locale) {
    return new JacksonAdapter(config, locale);
  }
}
