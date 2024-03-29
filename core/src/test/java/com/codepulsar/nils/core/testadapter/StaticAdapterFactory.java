package com.codepulsar.nils.core.testadapter;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;

public class StaticAdapterFactory implements AdapterFactory<StaticAdapter> {

  @Override
  public StaticAdapter create(NilsConfig<?> config, Locale locale) {
    return new StaticAdapter();
  }
}
