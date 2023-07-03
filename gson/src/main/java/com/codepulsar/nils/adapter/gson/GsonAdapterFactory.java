package com.codepulsar.nils.adapter.gson;

import java.util.Locale;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;
/** The factory for the {@link GsonAdapter}. */
public class GsonAdapterFactory implements AdapterFactory<GsonAdapter> {

  @Override
  public GsonAdapter create(AdapterConfig config, Locale locale) {
    return new GsonAdapter(config, locale);
  }
}
