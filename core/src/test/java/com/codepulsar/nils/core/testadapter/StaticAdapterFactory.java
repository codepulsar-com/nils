package com.codepulsar.nils.core.testadapter;

import java.util.Locale;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;

public class StaticAdapterFactory implements AdapterFactory<StaticAdapter> {

  @Override
  public StaticAdapter create(NilsConfig config, Locale locale) {
    return new StaticAdapter(config, locale);
  }
}
