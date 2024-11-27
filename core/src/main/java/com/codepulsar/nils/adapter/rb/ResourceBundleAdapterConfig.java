package com.codepulsar.nils.adapter.rb;

import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;
import com.codepulsar.nils.core.util.ParameterCheck;

/** Configuration for the {@link ResourceBundleAdapter} implementation. */
public class ResourceBundleAdapterConfig
    extends BaseLocalizedResourceNilsConfig<ResourceBundleAdapterConfig> {
  private static Logger LOG = LoggerFactory.getLogger(ResourceBundleAdapterConfig.class);

  private ResourceBundleAdapterConfig(Module owner) {
    super(owner, "properties");
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return ResourceBundleAdapterFactory.class;
  }

  /**
   * {@inheritDoc}
   *
   * <p><strong>Note: always {@code true} for ResourceBundles.<strong>
   */
  @Override
  public boolean isFallbackActive() {
    return super.isFallbackActive();
  }

  /**
   * {@inheritDoc}
   *
   * <p><strong>Note: always {@code true} for ResourceBundles.<strong>
   */
  @Override
  public ResourceBundleAdapterConfig fallbackActive(boolean fallback) {
    LOG.warn("Call of fallbackActive() will be ignored for ResourceBundles.");
    return super.fallbackActive(true);
  }

  /**
   * Create a <code>ResourceBundleAdapterConfig</code> from a class as reference.
   *
   * <p><em>Note:</em> The class will be used to resolve the module the class is located in.
   *
   * @param owner A Class
   * @return The created ResourceBundleAdapterConfig.
   */
  public static ResourceBundleAdapterConfig init(Class<?> owner) {
    ParameterCheck.notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return new ResourceBundleAdapterConfig(owner.getModule());
  }

  /**
   * Create a <code>ResourceBundleAdapterConfig</code> from an object as reference.
   *
   * <p><em>Note:</em> The object will be used to resolve the module the object class is located in.
   *
   * @param owner An object
   * @return The created ResourceBundleAdapterConfig.
   */
  public static ResourceBundleAdapterConfig init(Object owner) {
    ParameterCheck.notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return init(owner.getClass());
  }
}
