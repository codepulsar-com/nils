package com.codepulsar.nils.adapter.snakeyaml;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;

/**
 * Configuration for the {@link SnakeYamlAdapter} implementation.
 *
 * <p>The default base file name is {@code nls/translation.yaml}.
 */
public class SnakeYamlAdapterConfig
    extends BaseLocalizedResourceNilsConfig<SnakeYamlAdapterConfig> {

  private SnakeYamlAdapterConfig(Module owner) {
    super(owner, ".yaml");
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return SnakeYamlAdapterFactory.class;
  }
  /**
   * Create a <code>SnakeYamlAdapterConfig</code> from a class as reference.
   *
   * <p><em>Note:</em> The class will be used to resolve the module the class is located in.
   *
   * @param owner A Class
   * @return The created SnakeYamlAdapterConfig.
   */
  public static SnakeYamlAdapterConfig init(Class<?> owner) {
    notNull(owner, "owner", NILS_CONFIG);
    return new SnakeYamlAdapterConfig(owner.getModule());
  }

  /**
   * Create a <code>SnakeYamlAdapterConfig</code> from an object as reference.
   *
   * <p><em>Note:</em> The object will be used to resolve the module the object class is located in.
   *
   * @param owner An object
   * @return The created SnakeYamlAdapterConfig.
   */
  public static SnakeYamlAdapterConfig init(Object owner) {
    notNull(owner, "owner", NILS_CONFIG);
    return init(owner.getClass());
  }
}
