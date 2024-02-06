package com.codepulsar.nils.core.impl;

import java.text.MessageFormat;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.util.ParameterCheck;

class NLSKeyUtil {

  private final NilsConfig<?> config;
  private final ClassPrefixResolver classPrefixResolver;

  NLSKeyUtil(NilsConfig<?> config) {
    this.config = ParameterCheck.notNull(config, "config");
    this.classPrefixResolver = config.getClassPrefixResolver();
  }

  public String buildMissingKey(String key) {
    return MessageFormat.format(config.getEscapePattern(), key);
  }

  public String resolveKeyPrefix(Class<?> key) {
    return classPrefixResolver.resolve(key);
  }
}
