package com.codepulsar.nils.core.adapter.util;

import java.util.Locale;
import java.util.Objects;

import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.core.adapter.AdapterContext;

public class FallbackAdapterHandler<A extends Adapter> {

  /** An {@link Adapter} object using if the translation was not found by these adapter. */
  private A fallbackAdapter;

  /** The {@link AdapterContext} of the adapter. */
  private final AdapterContext<A> adapterContext;

  private Locale rootLocale;

  public FallbackAdapterHandler(AdapterContext<A> adapterContext) {
    this.adapterContext = adapterContext;
  }

  public FallbackAdapterHandler(AdapterContext<A> adapterContext, Locale rootLocale) {
    this(adapterContext);
    this.rootLocale = Objects.requireNonNull(rootLocale, "'rootLocale' cannot be null.");
  }
  /**
   * Gets the {@link Adapter} for the fallback to another {@code Locale}.
   *
   * @return An {@link Adapter} object.
   */
  public A getFallbackAdapter() {
    if (fallbackAdapter == null) {
      var locale = adapterContext.getLocale();
      var variant = locale.getVariant();
      var country = locale.getCountry();
      var language = locale.getLanguage();
      if (variant.isEmpty() && !country.isEmpty() && !language.isEmpty()) {
        country = "";
      } else if (variant.isEmpty() && country.isEmpty() && !language.isEmpty()) {
        language = "";
      }
      Locale parent = new Locale(language, country);
      if ("".equals(language) && rootLocale != null) {
        parent = rootLocale;
      }

      fallbackAdapter = adapterContext.getFactory().create(adapterContext.getConfig(), parent);
    }

    return fallbackAdapter;
  }
}
