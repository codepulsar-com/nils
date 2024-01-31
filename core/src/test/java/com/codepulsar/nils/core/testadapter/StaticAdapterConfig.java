package com.codepulsar.nils.core.testadapter;

import com.codepulsar.nils.api.adapter.AdapterConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;

public class StaticAdapterConfig implements AdapterConfig {

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return StaticAdapterFactory.class;
  }
}
