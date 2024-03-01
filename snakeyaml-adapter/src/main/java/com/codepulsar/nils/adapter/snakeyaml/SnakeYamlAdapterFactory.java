package com.codepulsar.nils.adapter.snakeyaml;

import java.util.List;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseAdapterFactory;
/** The factory for the {@link SnakeYamlAdapter}. */
public class SnakeYamlAdapterFactory extends BaseAdapterFactory<SnakeYamlAdapter> {

  @Override
  protected List<Class<? extends NilsConfig<?>>> getValidAdapterConfigClasses() {
    return List.of(SnakeYamlAdapterConfig.class);
  }

  @Override
  protected SnakeYamlAdapter createAdapter(AdapterContext<SnakeYamlAdapter> context) {
    return new SnakeYamlAdapter(context);
  }
}
