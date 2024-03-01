package com.codepulsar.nils.core.testadapter;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseNilsConfig;

public class BaseAdapterFactoryTesteeConfig extends BaseNilsConfig<BaseAdapterFactoryTesteeConfig> {

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return BaseLocalizedResourceAdapterTesteeFactory.class;
  }
}
