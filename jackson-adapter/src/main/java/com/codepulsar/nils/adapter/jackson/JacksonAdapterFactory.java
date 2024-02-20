package com.codepulsar.nils.adapter.jackson;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.util.ParameterCheck;
/** The factory for the {@link JacksonAdapter}. */
public class JacksonAdapterFactory implements AdapterFactory<JacksonAdapter> {

  @Override
  public JacksonAdapter create(NilsConfig<?> config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");

    if (!(config instanceof JacksonAdapterConfig)) {
      throw new NilsConfigException(
          "The provided AdapterConfig (%s) is not of type %s",
          config, JacksonAdapterConfig.class.getName());
    }
    var adapterContext =
        new AdapterContext<JacksonAdapter>().config(config).locale(locale).factory(this);
    return new JacksonAdapter(adapterContext);
  }
}
