package com.codepulsar.nils.adapter.rb;

import java.util.List;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseAdapterFactory;
/** The factory for the {@link ResourceBundleAdapter}. */
public class ResourceBundleAdapterFactory extends BaseAdapterFactory<ResourceBundleAdapter> {

  @Override
  protected List<Class<? extends NilsConfig<?>>> getValidAdapterConfigClasses() {
    return List.of(ResourceBundleAdapterConfig.class);
  }

  @Override
  protected ResourceBundleAdapter createAdapter(AdapterContext<ResourceBundleAdapter> context) {
    return new ResourceBundleAdapter(context.getConfig(), context.getLocale());
  }
}
