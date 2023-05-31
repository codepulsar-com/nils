package com.codepulsar.nils.core.adapter;

/**
 * An <tt>AdapterConfig</tt> provides further configuration values for an <tt>Adapter</tt>
 * implementation
 *
 * @see Adapter
 */
public interface AdapterConfig {
  /**
   * Class of the {@link AdapterFactory}.
   *
   * <p>the class must have an parameter less constructor.
   *
   * @return The Class of the AdapterFactory.
   */
  Class<? extends AdapterFactory<?>> getFactoryClass();
}
