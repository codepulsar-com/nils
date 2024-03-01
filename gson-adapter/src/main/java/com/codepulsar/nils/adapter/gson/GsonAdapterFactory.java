package com.codepulsar.nils.adapter.gson;

import java.util.List;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseAdapterFactory;
/** The factory for the {@link GsonAdapter}. */
public class GsonAdapterFactory extends BaseAdapterFactory<GsonAdapter> {

  @Override
  protected List<Class<? extends NilsConfig<?>>> getValidAdapterConfigClasses() {
    return List.of(GsonAdapterConfig.class);
  }

  @Override
  protected GsonAdapter createAdapter(AdapterContext<GsonAdapter> context) {
    return new GsonAdapter(context);
  }
}
