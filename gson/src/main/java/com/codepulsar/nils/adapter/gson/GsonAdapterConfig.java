package com.codepulsar.nils.adapter.gson;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.LocalizedResourceResolverConfig;
import com.codepulsar.nils.core.util.ParameterCheck;

/** Configuration for the {@link GsonAdapter} implementation. */
public class GsonAdapterConfig implements AdapterConfig, LocalizedResourceResolverConfig {

  private Module owner;

  private String baseFileName = "nls/translation.json";

  private GsonAdapterConfig(Module owner) {
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
   * Gets the base name of the json files.
   *
   * <p>The name can include the paths, but must not contains the file suffix <code>.json</code>.
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
   * Sets the base name of the json files.
   *
   * <p>The name can include the paths, but must not contains the file suffix <code>.json</code>.
   *
   * <p>The default value is <code>nls/translation</code>.
   *
   * @param baseFileName The base name of the json files.
   * @return This config object.
   * @see #getBaseFileName()
   */
  public GsonAdapterConfig baseFileName(String baseFileName) {
    this.baseFileName =
        ParameterCheck.notNullEmptyOrBlank(baseFileName, "baseFileName", NILS_CONFIG);
    if (baseFileName.lastIndexOf(".") < 0) {
      this.baseFileName += ".json";
    }
    return this;
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
    ParameterCheck.notNull(owner, "owner", NILS_CONFIG);
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
    ParameterCheck.notNull(owner, "owner", NILS_CONFIG);
    return init(owner.getClass());
  }
}
