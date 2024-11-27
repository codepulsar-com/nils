package com.codepulsar.nils.adapter.rb;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.core.util.ParameterCheck;

/** An {@link Adapter} implementation using Java ResourceBundles for the translations. */
public class ResourceBundleAdapter implements Adapter {
  private final ResourceBundle bundle;
  private final ResourceBundleAdapterConfig adapterConfig;

  public ResourceBundleAdapter(NilsConfig<?> config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");

    this.adapterConfig = (ResourceBundleAdapterConfig) config;
    var resourceBundle = adapterConfig.getBaseFileName().replace(".properties", "");
    this.bundle = ResourceBundle.getBundle(resourceBundle, locale, adapterConfig.getOwner());
  }

  @Override
  public Optional<String> getTranslation(String key) {
    try {
      return Optional.of(bundle.getString(key));
    } catch (MissingResourceException ex) {
      return Optional.empty();
    }
  }
}
