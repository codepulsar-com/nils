package com.codepulsar.nils.core.adapter;

/**
 * An <strong>AdapterConfig</strong> provides further configuration values for an {@link Adapter}
 * implementation.
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
