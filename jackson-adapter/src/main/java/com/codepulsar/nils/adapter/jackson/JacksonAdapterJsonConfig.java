package com.codepulsar.nils.adapter.jackson;

import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;

/**
 * Configuration for the {@link JacksonAdapter} implementation for {@code json} files.
 *
 * <p>The default base file name is the package of the Class resolved in {@link #init(Class)} or
 * {@link #init(Object)} + "translation.json".
 */
public class JacksonAdapterJsonConfig
    extends BaseLocalizedResourceNilsConfig<JacksonAdapterJsonConfig>
    implements JacksonAdapterConfig<JacksonAdapterJsonConfig> {

  private JacksonAdapterJsonConfig(Class<?> owner) {
    super(owner, ".json");
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return JacksonAdapterFactory.class;
  }

  /**
   * Create a {@linkplain JacksonAdapterJsonConfig} from a class as reference.
   *
   * <p><em>Note:</em> The {@code Class} will be used for resolving and accessing the translation
   * files.
   *
   * @param owner A Class
   * @return The created {@linkplain JacksonAdapterJsonConfig}.
   */
  public static JacksonAdapterJsonConfig init(Class<?> owner) {
    notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return new JacksonAdapterJsonConfig(owner);
  }

  /**
   * Create a {@linkplain JacksonAdapterJsonConfig} from an object as reference.
   *
   * <p><em>Note:</em> The object's class will be used for resolving and accessing the translation
   * files.
   *
   * @param owner An object
   * @return The created {@linkplain JacksonAdapterJsonConfig}.
   */
  public static JacksonAdapterJsonConfig init(Object owner) {
    notNull(owner, "owner", nilsException(CONFIG_ERROR));
    return init(owner.getClass());
  }
}
