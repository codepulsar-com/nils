package com.codepulsar.nils.adapter.rb;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.util.ParameterCheck;
/** The factory for the {@link ResourceBundleAdapter}. */
public class ResourceBundleAdapterFactory implements AdapterFactory<ResourceBundleAdapter> {

  @Override
  public ResourceBundleAdapter create(NilsConfig<?> config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");

    if (!(config instanceof ResourceBundleAdapterConfig)) {
      throw new NilsConfigException(
          "The provided AdapterConfig (%s) is not of type %s",
          config, ResourceBundleAdapterConfig.class.getName());
    }
    return new ResourceBundleAdapter(config, locale);
  }
}
