package com.codepulsar.nils.adapter.rb;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.NilsConfig;
import com.codepulsar.nils.adapter.BaseAdapter;
/** A NLS adapter implementation using Java ResourceBundles for the translations. */
public class ResourceBundleAdapter extends BaseAdapter {
  private static final Logger LOG = LoggerFactory.getLogger(ResourceBundleAdapter.class);
  private final ResourceBundle bundle;
  private final ResourceBundleAdapterConfig adapterConfig;

  public ResourceBundleAdapter(NilsConfig config, Locale locale) {
    super(config, locale);
    if (!(config.getAdapterConfig() instanceof ResourceBundleAdapterConfig)) {
      throw new IllegalArgumentException(
          String.format(
              "The provided AdapterConfig (%s) is not of type %s",
              config.getAdapterConfig(), ResourceBundleAdapterConfig.class.getName()));
    }
    this.adapterConfig = (ResourceBundleAdapterConfig) config.getAdapterConfig();
    this.bundle =
        ResourceBundle.getBundle(
            adapterConfig.getResourcesBundleName(), locale, adapterConfig.getOwner());
  }

  @Override
  protected Optional<String> resolveTranslation(String key) {
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
