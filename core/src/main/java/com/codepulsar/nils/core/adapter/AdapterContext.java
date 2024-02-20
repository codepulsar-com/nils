package com.codepulsar.nils.core.adapter;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.adapter.AdapterFactory;
/**
 * The {@link AdapterContext} can be used by adapter implementations.
 *
 * <p>The class sum up the config and the locale and get access to its {@link AdapterFactory} (i.e.
 * for fallback).
 *
 * @param <A> The adapter
 */
public class AdapterContext<A extends Adapter> {

  private NilsConfig<?> config;
  private Locale locale;
  private AdapterFactory<A> factory;

  /**
   * Gets the {@link AdapterFactory}.
   *
   * @return An {@link AdapterFactory}.
   */
  public AdapterFactory<A> getFactory() {
    return factory;
  }
  /**
   * Sets the {@link AdapterContext}.
   *
   * @param factory An {@link AdapterFactory}
   * @return This object.
   */
  public AdapterContext<A> factory(AdapterFactory<A> factory) {
    this.factory = factory;
    return this;
  }

  /**
   * Gets the {@link NilsConfig}.
   *
   * @return A {@link NilsConfig}
   */
  public NilsConfig<?> getConfig() {
    return config;
  }
  /**
   * Sets a {@link NilsConfig}.
   *
   * @param config A {@link NilsConfig}
   * @return This object.
   */
  public AdapterContext<A> config(NilsConfig<?> config) {
    this.config = config;
    return this;
  }

  /**
   * Gets the {code Locale}.
   *
   * @return A {@code Locale}
   */
  public Locale getLocale() {
    return locale;
  }
  /**
   * Sets the {code Locale}.
   *
   * @param locale A {@code Locale}
   * @return This object
   */
  public AdapterContext<A> locale(Locale locale) {
    this.locale = locale;
    return this;
  }
}
