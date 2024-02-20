package com.codepulsar.nils.adapter.jackson;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.config.LocalizedResourceConfig;

public interface JacksonAdapterConfig<C extends JacksonAdapterConfig<?>>
    extends NilsConfig<C>, LocalizedResourceConfig {}
