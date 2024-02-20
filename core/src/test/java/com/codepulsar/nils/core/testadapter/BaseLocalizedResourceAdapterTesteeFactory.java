package com.codepulsar.nils.core.testadapter;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.AdapterContext;

public class BaseLocalizedResourceAdapterTesteeFactory
    implements AdapterFactory<BaseLocalizedResourceAdapterTestee> {

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
