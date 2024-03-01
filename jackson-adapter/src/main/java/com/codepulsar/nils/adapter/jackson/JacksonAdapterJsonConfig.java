package com.codepulsar.nils.adapter.jackson;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;
/**
 * Configuration for the {@link JacksonAdapter} implementation for {@code json} files.
 *
 * <p>The default base file name is {@code nls/translation.json}.
 */
public class JacksonAdapterJsonConfig
    extends BaseLocalizedResourceNilsConfig<JacksonAdapterJsonConfig>
    implements JacksonAdapterConfig<JacksonAdapterJsonConfig> {

  private JacksonAdapterJsonConfig(Module owner) {
    super(owner, ".json");
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return JacksonAdapterFactory.class;
  }
  /**
   * Create a {@linkplain JacksonAdapterJsonConfig} from a class as reference.
   *
   * <p><em>Note:</em> The class will be used to resolve the module the class is located in.
   *
   * @param owner A Class
   * @return The created {@linkplain JacksonAdapterJsonConfig}.
   */
  public static JacksonAdapterJsonConfig init(Class<?> owner) {
    notNull(owner, "owner", NILS_CONFIG);
    return new JacksonAdapterJsonConfig(owner.getModule());
  }

  /**
   * Create a {@linkplain JacksonAdapterJsonConfig} from an object as reference.
   *
   * <p><em>Note:</em> The object will be used to resolve the module the object class is located in.
   *
   * @param owner An object
   * @return The created {@linkplain JacksonAdapterJsonConfig}.
   */
  public static JacksonAdapterJsonConfig init(Object owner) {
    notNull(owner, "owner", NILS_CONFIG);
    return init(owner.getClass());
  }
}
