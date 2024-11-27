package com.codepulsar.nils.adapter.snakeyaml;

import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;

/**
 * Configuration for the {@link SnakeYamlAdapter} implementation.
 *
 * <p>The default base file name is the package of the Class resolved in {@link #init(Class)} or
 * {@link #init(Object)} + "translation.yaml".
 */
public class SnakeYamlAdapterConfig
    extends BaseLocalizedResourceNilsConfig<SnakeYamlAdapterConfig> {

  private SnakeYamlAdapterConfig(Class<?> owner) {
    super(owner, ".yaml");
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return SnakeYamlAdapterFactory.class;
  }

  /**
   * Create a <code>SnakeYamlAdapterConfig</code> from a class as reference.
   *
   * <p><em>Note:</em> The {@code Class} will be used for resolving and accessing the translation
   * files.
   *
   * @param owner A Class
   * @return The created SnakeYamlAdapterConfig.
   */
  public static SnakeYamlAdapterConfig init(Class<?> owner) {
    notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return new SnakeYamlAdapterConfig(owner);
  }

  /**
   * Create a <code>SnakeYamlAdapterConfig</code> from an object as reference.
   *
   * <p><em>Note:</em> The object's class will be used for resolving and accessing the translation
   * files.
   *
   * @param owner An object
   * @return The created SnakeYamlAdapterConfig.
   */
  public static SnakeYamlAdapterConfig init(Object owner) {
    notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return init(owner.getClass());
  }
}
