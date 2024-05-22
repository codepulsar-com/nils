package com.codepulsar.nils.adapter.jdbc;

import java.util.List;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseAdapterFactory;
/** The factory for the {@link JdbcAdapter}. */
public class JdbcAdapterFactory extends BaseAdapterFactory<JdbcAdapter> {

  @Override
  protected List<Class<? extends NilsConfig<?>>> getValidAdapterConfigClasses() {
    return List.of(JdbcAdapterConfig.class);
  }

  @Override
  protected JdbcAdapter createAdapter(AdapterContext<JdbcAdapter> context) {
    return new JdbcAdapter(context);
  }
}
