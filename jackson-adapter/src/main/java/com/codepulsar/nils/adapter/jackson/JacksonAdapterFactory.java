package com.codepulsar.nils.adapter.jackson;

import java.util.List;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseAdapterFactory;
/** The factory for the {@link JacksonAdapter}. */
public class JacksonAdapterFactory extends BaseAdapterFactory<JacksonAdapter> {

  @Override
  protected List<Class<? extends NilsConfig<?>>> getValidAdapterConfigClasses() {
    return List.of(JacksonAdapterJsonConfig.class, JacksonAdapterYamlConfig.class);
  }

  @Override
  protected JacksonAdapter createAdapter(AdapterContext<JacksonAdapter> context) {
    return new JacksonAdapter(context);
  }
}
