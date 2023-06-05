package com.codepulsar.nils.core.adapter;

import java.util.Locale;
/**
 * An <tt>AdapterFactory</tt> creates an new object of an {@link Adapter} using the {@link
 * AdapterConfig} and {@link Locale}, if necessary.
 *
 * @param <A> A type parameter for the Adapter implementation class.
 */
public interface AdapterFactory<A extends Adapter> {
  /**
   * Creates a new {@link Adapter} instance, using the {@link AdapterConfig} and {@link Locale}, if
   * necessary.
   *
   * @param config An {@link AdapterConfig} object.
   * @param locale A {@link Locale} object.
   * @return The created {@link Adapter} instance.
   */
  A create(AdapterConfig config, Locale locale);
}
