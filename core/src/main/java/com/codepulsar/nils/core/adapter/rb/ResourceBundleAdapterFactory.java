package com.codepulsar.nils.core.adapter.rb;

import java.util.Locale;

import com.codepulsar.nils.api.adapter.AdapterConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
/** The factory for the {@link ResourceBundleAdapter}. */
public class ResourceBundleAdapterFactory implements AdapterFactory<ResourceBundleAdapter> {

  @Override
  public ResourceBundleAdapter create(AdapterConfig config, Locale locale) {
    return new ResourceBundleAdapter(config, locale);
  }
}
