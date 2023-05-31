package com.codepulsar.nils.core.adapter;

import java.util.Locale;

import com.codepulsar.nils.core.NilsConfig;

public interface AdapterFactory<A extends Adapter> {

  A create(NilsConfig config, Locale locale);
}
