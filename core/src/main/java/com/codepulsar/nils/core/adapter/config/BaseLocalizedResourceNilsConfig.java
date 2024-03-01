package com.codepulsar.nils.core.adapter.config;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;
import static com.codepulsar.nils.core.util.ParameterCheck.notNullEmptyOrBlank;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.config.LocalizedResourceConfig;
import com.codepulsar.nils.core.util.ParameterCheck;
/**
 * The {@link BaseLocalizedResourceNilsConfig} is a base implementation of {@link NilsConfig} and
 * {@link LocalizedResourceConfig}.
 *
 * <p>Adapter, uses both the {@link NilsConfig} and the {@link LocalizedResourceConfig} should use
 * this class as base class for their configuration, instead of implementing the interfaces
 * directly.
 *
 * @param <CFG> The class extending {@link BaseLocalizedResourceNilsConfig}.
 */
public abstract class BaseLocalizedResourceNilsConfig<CFG extends BaseNilsConfig<?>>
    extends BaseNilsConfig<CFG> implements LocalizedResourceConfig {

  private final Module owner;
  private final String baseFileExtension;
  private String baseFileName;
  private boolean fallbackActive = true;
  /**
   * Create a new instance.
   *
   * @param owner The owner of the resource file.
   * @param defaultBaseFileExtension The default file extension for the base file.
   */
  protected BaseLocalizedResourceNilsConfig(Module owner, String defaultBaseFileExtension) {
    this.owner = notNull(owner, "owner");
    var fileExtension = notNullEmptyOrBlank(defaultBaseFileExtension, "defaultBaseFileExtension");
    if (!fileExtension.startsWith(".")) {
      fileExtension = "." + fileExtension;
    }
    this.baseFileExtension = fileExtension;
    this.baseFileName = "nls/translation" + fileExtension;
  }

  @Override
  public Module getOwner() {
    return owner;
  }

  @Override
  public String getBaseFileName() {
    return baseFileName;
  }
  /**
   * Sets the base name for the localized files.
   *
   * <p>The name can include the paths. It can contain a file extension.
   *
   * <p>See for information about the default value in the concrete implementation class.
   *
   * @param baseFileName The base name of the localized files.
   * @return This config object.
   * @see #getBaseFileName()
   */
  @SuppressWarnings("unchecked")
  public CFG baseFileName(String baseFileName) {
    this.baseFileName =
        ParameterCheck.notNullEmptyOrBlank(baseFileName, "baseFileName", NILS_CONFIG);
    if (baseFileName.lastIndexOf(".") < 0) {
      this.baseFileName += this.baseFileExtension;
    }
    return (CFG) this;
  }

  @Override
  public boolean isFallbackActive() {
    return fallbackActive;
  }
  /**
   * Sets the flag, if a fallback to other resource files is active.
   *
   * @param fallback {@code true} if active, else {@code false}.
   * @see #isFallbackActive()
   */
  @SuppressWarnings("unchecked")
  public CFG fallbackActive(boolean fallback) {
    this.fallbackActive = fallback;
    return (CFG) this;
  }
}
