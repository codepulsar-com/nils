package com.codepulsar.nils.adapter.gson;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.util.ParameterCheck;
/** The factory for the {@link GsonAdapter}. */
public class GsonAdapterFactory implements AdapterFactory<GsonAdapter> {

  @Override
  public GsonAdapter create(NilsConfig<?> config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");

    if (!(config instanceof GsonAdapterConfig)) {
      throw new NilsConfigException(
          "The provided AdapterConfig (%s) is not of type %s",
          config, GsonAdapterConfig.class.getName());
    }
    var adapterContext =
        new AdapterContext<GsonAdapter>().config(config).locale(locale).factory(this);
    return new GsonAdapter(adapterContext);
  }
}
