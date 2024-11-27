package com.codepulsar.nils.adapter.gson;

import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;

/**
 * Configuration for the {@link GsonAdapter} implementation.
 *
 * <p>The default base file name is {@code nls/translation.json}.
 */
public class GsonAdapterConfig extends BaseLocalizedResourceNilsConfig<GsonAdapterConfig> {

  private GsonAdapterConfig(Module owner) {
    super(owner, ".json");
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return GsonAdapterFactory.class;
  }

  /**
   * Create a <code>GsonAdapterConfig</code> from a class as reference.
   *
   * <p><em>Note:</em> The class will be used to resolve the module the class is located in.
   *
   * @param owner A Class
   * @return The created GsonAdapterConfig.
   */
  public static GsonAdapterConfig init(Class<?> owner) {
    notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return new GsonAdapterConfig(owner.getModule());
  }

  /**
   * Create a <code>GsonAdapterConfig</code> from an object as reference.
   *
   * <p><em>Note:</em> The object will be used to resolve the module the object class is located in.
   *
   * @param owner An object
   * @return The created GsonAdapterConfig.
   */
  public static GsonAdapterConfig init(Object owner) {
    notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return init(owner.getClass());
  }
}
