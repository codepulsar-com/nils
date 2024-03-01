package com.codepulsar.nils.core.testadapter;

import java.util.List;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseAdapterFactory;

public class BaseAdapterFactoryTesteeFactory extends BaseAdapterFactory<BaseAdapterFactoryTestee> {

  @Override
  protected List<Class<? extends NilsConfig<?>>> getValidAdapterConfigClasses() {
    return List.of(BaseAdapterFactoryTesteeConfig.class);
  }

  @Override
  protected BaseAdapterFactoryTestee createAdapter(
      AdapterContext<BaseAdapterFactoryTestee> context) {
    return new BaseAdapterFactoryTestee(context);
  }
}
