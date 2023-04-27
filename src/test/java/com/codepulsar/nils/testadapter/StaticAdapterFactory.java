package com.codepulsar.nils.testadapter;

import java.util.Locale;

import com.codepulsar.nils.NilsConfig;
import com.codepulsar.nils.adapter.AdapterFactory;

public class StaticAdapterFactory implements AdapterFactory<StaticAdapter> {

  @Override
  public StaticAdapter create(NilsConfig config, Locale locale) {
    return new StaticAdapter(config, locale);
  }
}
