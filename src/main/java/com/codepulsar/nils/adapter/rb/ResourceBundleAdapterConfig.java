package com.codepulsar.nils.adapter.rb;

import com.codepulsar.nils.adapter.AdapterConfig;
import com.codepulsar.nils.adapter.AdapterFactory;
import com.codepulsar.nils.util.ParameterCheck;
/** Configuration for the {@link ResourceBundleAdapter} implementation. */
public class ResourceBundleAdapterConfig implements AdapterConfig {

  private Module owner;
  private String resourcesBundleName = "nls/translation";

  private ResourceBundleAdapterConfig(Module owner) {
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
  /**
   * Gets the name of the resource bundle.
   *
   * <p>The name can include the paths, but must not contains the file suffix <tt>.properties.</tt>.
   *
   * <p>The default value is <code>nls/translation</code>.
   *
   * @return The name of the resource bundle.
   * @see #resourcesBundleName(String)
   */
  public String getResourcesBundleName() {
    return resourcesBundleName;
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return ResourceBundleAdapterFactory.class;
  }

  /**
   * Sets the name of the resource bundle.
   *
   * <p>The name can include the paths, but must not contains the file suffix <tt>.properties.</tt>.
   *
   * <p>The default value is <code>nls/translation</code>.
   *
   * @param resourcesBundleName The name of the resource bundle.
   * @return This config object.
   * @see #getResourcesBundleName()
   */
  public ResourceBundleAdapterConfig resourcesBundleName(String resourcesBundleName) {
    this.resourcesBundleName =
        ParameterCheck.notNullEmptyOrBlank(resourcesBundleName, "resourcesBundleName");
    return this;
  }

  /**
   * Create a <code>ResourceBundleAdapterConfig</code> from a class as reference.
   *
   * <p><em>Note:</em> The class will be used to resolve the module the class is located in.
   *
   * @param owner A Class
   * @return The created NilsConfig.
   */
  public static ResourceBundleAdapterConfig init(Class<?> owner) {
    ParameterCheck.notNull(owner, "owner");
    return new ResourceBundleAdapterConfig(owner.getModule());
  }

  /**
   * Create a <code>ResourceBundleAdapterConfig</code> from an object as reference.
   *
   * <p><em>Note:</em> The object will be used to resolve the module the object class is located in.
   *
   * @param owner An object
   * @return The created NilsConfig.
   */
  public static ResourceBundleAdapterConfig init(Object owner) {
    ParameterCheck.notNull(owner, "owner");
    return init(owner.getClass());
  }
}
