package com.codepulsar.nils.adapter.gson;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;
import com.codepulsar.nils.core.util.ParameterCheck;
// TODO docs
public class GsonAdapterConfig implements AdapterConfig {

  private Module owner;

  private String jsonBaseFileName = "nls/translation";

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

  public String getBaseFileName() {
    return jsonBaseFileName;
  }
  // TODO docu: without .json
  public GsonAdapterConfig baseFileName(String jsonBaseFileName) {
    this.jsonBaseFileName =
        ParameterCheck.notNullEmptyOrBlank(jsonBaseFileName, "jsonBaseFileName", NILS_CONFIG);
    return this;
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return GsonAdapterFactory.class;
  }

  public static GsonAdapterConfig init(Class<?> owner) {
    ParameterCheck.notNull(owner, "owner", NILS_CONFIG);
    return new GsonAdapterConfig(owner.getModule());
  }

  public static GsonAdapterConfig init(Object owner) {
    ParameterCheck.notNull(owner, "owner", NILS_CONFIG);
    return init(owner.getClass());
  }
}
