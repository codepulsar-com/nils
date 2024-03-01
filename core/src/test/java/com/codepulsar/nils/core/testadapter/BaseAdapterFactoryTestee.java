package com.codepulsar.nils.core.testadapter;

import java.util.Optional;

import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.core.adapter.AdapterContext;

public class BaseAdapterFactoryTestee implements Adapter {

  public BaseAdapterFactoryTestee(AdapterContext<BaseAdapterFactoryTestee> context) {}

  @Override
  public Optional<String> getTranslation(String key) {
    return Optional.empty();
  }
}
