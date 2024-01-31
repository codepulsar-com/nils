package com.codepulsar.nils.core.adapter.rb;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.adapter.AdapterConfig;
import com.codepulsar.nils.core.error.NilsConfigException;
import com.codepulsar.nils.core.util.ParameterCheck;
/** An {@link Adapter} implementation using Java ResourceBundles for the translations. */
public class ResourceBundleAdapter implements Adapter {
  private static final Logger LOG = LoggerFactory.getLogger(ResourceBundleAdapter.class);
  private final ResourceBundle bundle;
  private final ResourceBundleAdapterConfig adapterConfig;

  public ResourceBundleAdapter(AdapterConfig config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");
    if (!(config instanceof ResourceBundleAdapterConfig)) {
      throw new NilsConfigException(
          String.format(
              "The provided AdapterConfig (%s) is not of type %s",
              config, ResourceBundleAdapterConfig.class.getName()));
    }
    this.adapterConfig = (ResourceBundleAdapterConfig) config;
    this.bundle =
        ResourceBundle.getBundle(
            adapterConfig.getResourcesBundleName(), locale, adapterConfig.getOwner());
  }

  @Override
  public Optional<String> getTranslation(String key) {
    try {
      return Optional.of(bundle.getString(key));
    } catch (MissingResourceException ex) {
      LOG.warn(
          "Cannot find key '{}' in resource file '{}'.",
          key,
          adapterConfig.getResourcesBundleName());
      return Optional.empty();
    }
  }
}
