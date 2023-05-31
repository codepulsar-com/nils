package com.codepulsar.nils.testadapter;

import com.codepulsar.nils.adapter.AdapterConfig;
import com.codepulsar.nils.adapter.AdapterFactory;

public class StaticAdapterConfig implements AdapterConfig {

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return StaticAdapterFactory.class;
  }
}
