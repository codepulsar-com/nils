package com.codepulsar.nils.core.adapter;

import com.codepulsar.nils.core.NLS;
import com.codepulsar.nils.core.adapter.rb.ResourceBundleAdapter;
/**
 * An <tt>Adapter</tt> provides an implementation for a specific kind to access localization
 * information (i. e. from ResourceBundles).
 *
 * <p>An adapter can have further configuration provided by an implementation of the {@link
 * AdapterConfig} interface.
 *
 * <p>NILS can so easily extended by implementing an own Adapter for an own purpose.
 *
 * <p>A default implementation is the {@link ResourceBundleAdapter} using Java's
 * <tt>ResourceBundle</tt> to resolve a translation.
 *
 * @see AdapterConfig
 * @see ResourceBundleAdapter
 */
public interface Adapter extends NLS {}
