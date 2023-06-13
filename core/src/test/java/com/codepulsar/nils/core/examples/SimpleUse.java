package com.codepulsar.nils.core.examples;

import java.util.Locale;

import com.codepulsar.nils.core.NLS;
import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.NilsFactory;
import com.codepulsar.nils.core.adapter.rb.ResourceBundleAdapterConfig;
import com.codepulsar.nils.core.config.SuppressableErrorTypes;

public class SimpleUse {

  private static final NilsFactory NLS_FACTORY =
      NilsFactory.init(ResourceBundleAdapterConfig.init(SimpleUse.class));

  private static final NLS NLS_DE =
      NilsFactory.init(ResourceBundleAdapterConfig.init(SimpleUse.class)).nls(Locale.GERMAN);

  @SuppressWarnings("unused")
  public void translate() {
    var nls = NLS_FACTORY.nls(); // (2)

    var customerName = nls.get("customer.name"); // (3)
    var pageXOfY = nls.get("page_counter", 3, 5); // (4)
    var street = nls.get(Address.class, "street"); // (5)
  }

  @SuppressWarnings("unused")
  public void translateDE() {
    var customerName = NLS_DE.get("customer.name");
    var pageXOfY = NLS_DE.get("page_counter", 3, 5);
    var street = NLS_DE.get(Address.class, "street");
  }

  // TODO Update public docu
  public void config1() {
    NilsConfig config =
        NilsConfig.init(ResourceBundleAdapterConfig.init(SimpleUse.class))
            .suppressErrors(
                SuppressableErrorTypes.INCLUDE_LOOP_DETECTED,
                SuppressableErrorTypes.MISSING_TRANSLATION);
    NilsFactory.init(config);
  }

  public void config2() {
    NilsConfig config =
        NilsConfig.init(ResourceBundleAdapterConfig.init(SimpleUse.class)).escapePattern("@{0}");
    NilsFactory.init(config);
  }
}
