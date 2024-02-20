package com.codepulsar.nils.adapter.jackson;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseNilsConfig;
import com.codepulsar.nils.core.util.ParameterCheck;
/** Configuration for the {@link JacksonAdapter} implementation for {@code json} files. */
public class JacksonAdapterJsonConfig extends BaseNilsConfig<JacksonAdapterJsonConfig>
    implements JacksonAdapterConfig<JacksonAdapterJsonConfig> {

  private Module owner;

  private String baseFileName = "nls/translation.json";

  private JacksonAdapterJsonConfig(Module owner) {
    this.owner = owner;
  }
  /**
   * Gets the owner module for the nls support.
   *
   * @return A Module object.
   */
  @Override
  public Module getOwner() {
    return owner;
  }

  // TODO useFallback (cascade for not found keys in the current locale)

  /**
   * Gets the base name of the json files.
   *
   * <p>The name can include the paths, but must not contains the file extension <code>.json</code>,
   * <code>.yaml</code>.
   *
   * <p>The default value is <code>nls/translation</code>.
   *
   * @return The base name of the json files.
   * @see #baseFileName(String)
   */
  @Override
  public String getBaseFileName() {
    return baseFileName;
  }

  /**
   * Sets the base name of the nls files.
   *
   * <p>The name can include the paths, but must contains the file extension.
   *
   * <p>The default value is <code>nls/translation.json</code>.
   *
   * @param baseFileName The base name of the json files.
   * @return This config object.
   * @see #getBaseFileName()
   */
  public JacksonAdapterJsonConfig baseFileName(String baseFileName) {
    this.baseFileName =
        ParameterCheck.notNullEmptyOrBlank(baseFileName, "baseFileName", NILS_CONFIG);
    if (baseFileName.lastIndexOf(".") < 0) {
      this.baseFileName += ".json";
    }
    return this;
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
    ParameterCheck.notNull(owner, "owner", NILS_CONFIG);
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
    ParameterCheck.notNull(owner, "owner", NILS_CONFIG);
    return init(owner.getClass());
  }
}
