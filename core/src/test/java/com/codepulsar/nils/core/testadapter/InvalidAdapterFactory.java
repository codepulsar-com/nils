package com.codepulsar.nils.core.testadapter;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.AdapterContext;

public class InvalidAdapterFactory implements AdapterFactory<BaseLocalizedResourceAdapterTestee> {

  public InvalidAdapterFactory(String para) {}

  @Override
  public BaseLocalizedResourceAdapterTestee create(NilsConfig<?> config, Locale locale) {
    var adapterContext =
        new AdapterContext<BaseLocalizedResourceAdapterTestee>()
            .config(config)
            .locale(locale)
            .factory(this);
    return new BaseLocalizedResourceAdapterTestee(adapterContext);
  }
}
