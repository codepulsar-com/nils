package com.codepulsar.nils.core.testadapter;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.api.adapter.config.LocalizedResourceConfig;
import com.codepulsar.nils.core.adapter.config.BaseNilsConfig;

public class BaseLocalizedResourceAdapterTesteeConfig
    extends BaseNilsConfig<BaseLocalizedResourceAdapterTesteeConfig>
    implements LocalizedResourceConfig {

  private Module owner;

  private String baseFileName = "nls/translation.test";

  private BaseLocalizedResourceAdapterTesteeConfig(Module owner) {
    this.owner = owner;
  }

  @Override
  public Module getOwner() {
    return owner;
  }

  @Override
  public String getBaseFileName() {
    return baseFileName;
  }

  public BaseLocalizedResourceAdapterTesteeConfig baseFileName(String baseFileName) {
    this.baseFileName = baseFileName;
    return this;
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return BaseLocalizedResourceAdapterTesteeFactory.class;
  }

  public static BaseLocalizedResourceAdapterTesteeConfig init(Class<?> owner) {
    return new BaseLocalizedResourceAdapterTesteeConfig(owner.getModule());
  }

  public static BaseLocalizedResourceAdapterTesteeConfig init(Object owner) {
    return init(owner.getClass());
  }
}
