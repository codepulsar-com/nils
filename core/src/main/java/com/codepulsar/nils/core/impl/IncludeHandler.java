package com.codepulsar.nils.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.error.ErrorTypes;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.util.ParameterCheck;

class IncludeHandler {

  private static final Logger LOG = LoggerFactory.getLogger(IncludeHandler.class);
  private final Function<String, Optional<String>> keyResolver;
  private final NilsConfig nilsConfig;

  IncludeHandler(NilsConfig nilsConfig, Function<String, Optional<String>> keyResolver) {
    this.nilsConfig = nilsConfig;
    this.keyResolver = keyResolver;
  }

  public Optional<String> findKey(String request) {
    ParameterCheck.notNullEmptyOrBlank(request, "request");
    return findKeyInHierachie(request, new ArrayList<String>());
  }

  private Optional<String> findKeyInHierachie(String request, List<String> visitedIncludes) {
    Optional<Entry<String, String>> includedKeys = resolveIncludeKeys(request);
    if (includedKeys.isEmpty()) {
      return Optional.empty();
    }

    Entry<String, String> entry = includedKeys.get();
    String requestIncludeBase = entry.getKey().replace(getIncludeSuffix(), "");
    String includePart = request.replace(requestIncludeBase, "");
    StringTokenizer tokenizer = new StringTokenizer(entry.getValue(), ";");
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken().trim();
      if (visitedIncludes.contains(token)) {
        throw new NilsException(
            ErrorTypes.INCLUDE_LOOP_DETECTED, "Found circular include on '%s'.", token);
      }
      visitedIncludes.add(token);
      String includedKey = token + "." + includePart;
      Optional<String> value = keyResolver.apply(includedKey);
      if (value.isPresent()) {
        LOG.trace("Found matching value for '{}' at '{}'.", request, includedKey);
        return value;
      }
      Optional<String> hierachialValue = findKeyInHierachie(includedKey, visitedIncludes);
      if (hierachialValue.isPresent()) {
        LOG.trace("Found matching value for '{}' at '{}'.", request, includedKey);
        return hierachialValue;
      }
    }
    return Optional.empty();
  }

  protected Optional<Entry<String, String>> resolveIncludeKeys(String request) {

    int dot = request.lastIndexOf(".");
    if (dot < 0) {
      return Optional.empty();
    }
    String requestPart = request.substring(0, dot);
    String requestPartWithSuffix = requestPart + "." + getIncludeSuffix();
    Optional<String> includeKey = keyResolver.apply(requestPartWithSuffix);
    if (includeKey.isPresent()) {
      LOG.trace("Found include at '{}'.", requestPartWithSuffix);
      return Optional.of(Map.entry(requestPartWithSuffix, includeKey.get()));
    }
    return resolveIncludeKeys(requestPart);
  }

  private String getIncludeSuffix() {
    return nilsConfig.getIncludeTag();
  }
}
