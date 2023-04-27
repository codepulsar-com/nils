package com.codepulsar.nils.adapter;

import java.util.Locale;

import com.codepulsar.nils.NilsConfig;

public interface AdapterFactory<A extends Adapter> {

  A create(NilsConfig config, Locale locale);
}
