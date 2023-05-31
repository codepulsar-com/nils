package com.codepulsar.nils.core.testadapter;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;

public class StaticAdapterConfig implements AdapterConfig {

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return StaticAdapterFactory.class;
  }
}
