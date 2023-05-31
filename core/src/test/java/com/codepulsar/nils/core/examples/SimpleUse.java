package com.codepulsar.nils.core.examples;

import java.util.Locale;

import com.codepulsar.nils.core.NLS;
import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.NilsFactory;
import com.codepulsar.nils.core.adapter.rb.ResourceBundleAdapterConfig;

public class SimpleUse {

  private static final NilsFactory NLS_FACTORY =
      NilsFactory.init(ResourceBundleAdapterConfig.init(SimpleUse.class));

  private static final NLS NLS_DE =
      NilsFactory.init(ResourceBundleAdapterConfig.init(SimpleUse.class)).nls(Locale.GERMAN);

  @SuppressWarnings("unused")
  public void access() {
    var nls = NLS_FACTORY.nls();

    var customerName_dft = nls.get("customer.name");
    var pageXOfY_dft = nls.get("page_counter", 3, 5);

    var customerName = NLS_DE.get("customer.name");
    var pageXOfY = NLS_DE.get("page_counter", 3, 5);
  }

  public void config1() {
    NilsConfig config =
        NilsConfig.init(ResourceBundleAdapterConfig.init(SimpleUse.class)).escapeIfMissing(false);
    NilsFactory.init(config);
  }

  public void config2() {
    NilsConfig config =
        NilsConfig.init(ResourceBundleAdapterConfig.init(SimpleUse.class)).escapePattern("@{0}");
    NilsFactory.init(config);
  }
}
