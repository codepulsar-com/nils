package com.codepulsar.nils.core.testadapter;

import java.io.InputStream;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseLocalizedResourceAdapter;
import com.codepulsar.nils.core.adapter.util.LocalizedResourceResolver;

public class BaseLocalizedResourceAdapterTestee
    extends BaseLocalizedResourceAdapter<BaseLocalizedResourceAdapterTestee, NilsConfig<?>> {

  public BaseLocalizedResourceAdapterTestee(
      AdapterContext<BaseLocalizedResourceAdapterTestee> context) {
    super(context);
  }

  @Override
  protected InputStream resolveInputStream(String resource) {
    return null;
  }

  @Override
  protected void initTranslations(LocalizedResourceResolver resolver) {}
}
