package com.codepulsar.nils.adapter.jackson;

import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;

/**
 * Configuration for the {@link JacksonAdapter} implementation for {@code yaml} files.
 *
 * <p>*
 *
 * <p>The default base file name is the package of the Class resolved in {@link #init(Class)} or
 * {@link #init(Object)} + "translation.yaml".
 */
public class JacksonAdapterYamlConfig
    extends BaseLocalizedResourceNilsConfig<JacksonAdapterYamlConfig>
    implements JacksonAdapterConfig<JacksonAdapterYamlConfig> {

  private JacksonAdapterYamlConfig(Class<?> owner) {
    super(owner, ".yaml");
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return JacksonAdapterFactory.class;
  }

  /**
   * Create a {@linkplain JacksonAdapterYamlConfig} from a class as reference.
   *
   * <p><em>Note:</em> The {@code Class} will be used for resolving and accessing the translation
   * files.
   *
   * @param owner A Class
   * @return The created {@linkplain JacksonAdapterYamlConfig}.
   */
  public static JacksonAdapterYamlConfig init(Class<?> owner) {
    notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return new JacksonAdapterYamlConfig(owner);
  }

  /**
   * Create a {@linkplain JacksonAdapterYamlConfig} from an object as reference.
   *
   * <p><em>Note:</em> The object's class will be used for resolving and accessing the translation
   * files.
   *
   * @param owner An object
   * @return The created {@linkplain JacksonAdapterYamlConfig}.
   */
  public static JacksonAdapterYamlConfig init(Object owner) {
    notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return init(owner.getClass());
  }
}
