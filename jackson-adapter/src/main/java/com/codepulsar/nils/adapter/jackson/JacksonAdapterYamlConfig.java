package com.codepulsar.nils.adapter.jackson;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;
/**
 * Configuration for the {@link JacksonAdapter} implementation for {@code yaml} files.
 *
 * <p>The default base file name is {@code nls/translation.yaml}.
 */
public class JacksonAdapterYamlConfig
    extends BaseLocalizedResourceNilsConfig<JacksonAdapterYamlConfig>
    implements JacksonAdapterConfig<JacksonAdapterYamlConfig> {

  private JacksonAdapterYamlConfig(Module owner) {
    super(owner, ".yaml");
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return JacksonAdapterFactory.class;
  }
  /**
   * Create a {@linkplain JacksonAdapterYamlConfig} from a class as reference.
   *
   * <p><em>Note:</em> The class will be used to resolve the module the class is located in.
   *
   * @param owner A Class
   * @return The created {@linkplain JacksonAdapterYamlConfig}.
   */
  public static JacksonAdapterYamlConfig init(Class<?> owner) {
    notNull(owner, "owner", NILS_CONFIG);
    return new JacksonAdapterYamlConfig(owner.getModule());
  }

  /**
   * Create a {@linkplain JacksonAdapterYamlConfig} from an object as reference.
   *
   * <p><em>Note:</em> The object will be used to resolve the module the object class is located in.
   *
   * @param owner An object
   * @return The created {@linkplain JacksonAdapterYamlConfig}.
   */
  public static JacksonAdapterYamlConfig init(Object owner) {
    notNull(owner, "owner", NILS_CONFIG);
    return init(owner.getClass());
  }
}
