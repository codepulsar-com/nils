package com.codepulsar.nils.adapter.rb;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
/** The factory for the {@link ResourceBundleAdapter}. */
public class ResourceBundleAdapterFactory implements AdapterFactory<ResourceBundleAdapter> {

  @Override
  public ResourceBundleAdapter create(NilsConfig<?> config, Locale locale) {
    return new ResourceBundleAdapter(config, locale);
  }
}
