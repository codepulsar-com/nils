package com.codepulsar.nils.core.adapter.rb;

import java.util.Locale;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;
/** The factory for the {@link ResourceBundleAdapter}. */
public class ResourceBundleAdapterFactory implements AdapterFactory<ResourceBundleAdapter> {

  @Override
  public ResourceBundleAdapter create(NilsConfig config, Locale locale) {
    return new ResourceBundleAdapter(config, locale);
  }
}
