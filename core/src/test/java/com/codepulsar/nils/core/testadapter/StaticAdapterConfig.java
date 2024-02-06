package com.codepulsar.nils.core.testadapter;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseNilsConfig;

public class StaticAdapterConfig extends BaseNilsConfig<StaticAdapterConfig> {

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return StaticAdapterFactory.class;
  }
}
