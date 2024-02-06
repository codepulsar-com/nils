package com.codepulsar.nils.adapter.gson;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
/** The factory for the {@link GsonAdapter}. */
public class GsonAdapterFactory implements AdapterFactory<GsonAdapter> {

  @Override
  public GsonAdapter create(NilsConfig<?> config, Locale locale) {
    return new GsonAdapter(config, locale);
  }
}
