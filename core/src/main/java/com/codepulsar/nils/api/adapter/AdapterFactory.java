package com.codepulsar.nils.api.adapter;

import java.util.Locale;

import com.codepulsar.nils.api.NilsConfig;
/**
 * An <strong>AdapterFactory</strong> creates an new object of an {@link Adapter} using the {@link
 * AdapterConfig} and <code>Locale</code>, if necessary.
 *
 * @param <A> A type parameter for the Adapter implementation class.
 */
public interface AdapterFactory<A extends Adapter> {
  /**
   * Creates a new {@link Adapter} instance, using the {@link AdapterConfig} and <code>Locale</code>
   * , if necessary.
   *
   * @param config An {@link AdapterConfig} object.
   * @param locale A <code>Locale</code> object.
   * @return The created {@link Adapter} instance.
   */
  A create(NilsConfig<?> config, Locale locale);
}
