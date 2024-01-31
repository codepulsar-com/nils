package com.codepulsar.nils.adapter.snakeyaml;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;

import com.codepulsar.nils.api.adapter.AdapterConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.LocalizedResourceResolverConfig;
import com.codepulsar.nils.core.util.ParameterCheck;

/** Configuration for the {@link SnakeYamlAdapter} implementation. */
public class SnakeYamlAdapterConfig implements AdapterConfig, LocalizedResourceResolverConfig {

  private Module owner;

  private String baseFileName = "nls/translation.yaml";

  private SnakeYamlAdapterConfig(Module owner) {
    this.owner = owner;
  }
  /**
   * Gets the owner module for the nls support.
   *
   * @return A Module object.
   */
  public Module getOwner() {
    return owner;
  }

  // TODO useFallback (cascade for not found keys in the current locale)

  /**
   * Gets the base name of the yaml files.
   *
   * <p>The name can include the paths with the file extension <code>.yaml</code>.
   *
   * <p>The default value is <code>nls/translation.yaml</code>.
   *
   * @return The base name of the yaml files.
   * @see #baseFileName(String)
   */
  @Override
  public String getBaseFileName() {
    return baseFileName;
  }
  /**
   * Sets the base name of the yaml files.
   *
   * <p>The name can include the paths. It can contain the file extension <code>.yaml</code>.
   *
   * <p>The default value is <code>nls/translation.yaml</code>.
   *
   * @param baseFileName The base name of the yaml files.
   * @return This config object.
   * @see #getBaseFileName()
   */
  public SnakeYamlAdapterConfig baseFileName(String baseFileName) {
    this.baseFileName =
        ParameterCheck.notNullEmptyOrBlank(baseFileName, "baseFileName", NILS_CONFIG);
    if (baseFileName.lastIndexOf(".") < 0) {
      this.baseFileName += ".yaml";
    }

    return this;
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
    ParameterCheck.notNull(owner, "owner", NILS_CONFIG);
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
    ParameterCheck.notNull(owner, "owner", NILS_CONFIG);
    return init(owner.getClass());
  }
}
