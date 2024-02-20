package com.codepulsar.nils.adapter.snakeyaml;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.util.ParameterCheck;
/** The factory for the {@link SnakeYamlAdapter}. */
public class SnakeYamlAdapterFactory implements AdapterFactory<SnakeYamlAdapter> {

  @Override
  public SnakeYamlAdapter create(NilsConfig<?> config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");
    if (!(config instanceof SnakeYamlAdapterConfig)) {
      throw new NilsConfigException(
          "The provided AdapterConfig (%s) is not of type %s",
          config, SnakeYamlAdapterConfig.class.getName());
    }
    var adapterContext =
        new AdapterContext<SnakeYamlAdapter>().config(config).locale(locale).factory(this);
    return new SnakeYamlAdapter(adapterContext);
  }
}
