package com.codepulsar.nils.core.testadapter;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseNilsConfig;

public class TestFactoryAdapterConfig extends BaseNilsConfig<TestFactoryAdapterConfig> {

  private Class<? extends AdapterFactory<?>> factoryClass;

  public TestFactoryAdapterConfig(Class<? extends AdapterFactory<?>> factoryClass) {
    this.factoryClass = factoryClass;
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return factoryClass;
  }
}
