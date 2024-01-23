package com.codepulsar.nils.adapter.jackson.utils;

import static com.codepulsar.nils.adapter.jackson.utils.JacksonErrorTypes.MISSING_DEPENDENCY;

import com.codepulsar.nils.core.error.ErrorType;
import com.codepulsar.nils.core.error.NilsException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {

  public ObjectMapper resolve(String fileExtension) {
    if (".yaml".equalsIgnoreCase(fileExtension) || ".yml".equalsIgnoreCase(fileExtension)) {
      return new ObjectMapper(getYamlFactory());
    }
    // Default case for JSON files
    return new ObjectMapper();
  }

  private JsonFactory getYamlFactory() {
    var className = "com.fasterxml.jackson.dataformat.yaml.YAMLFactory";
    Class<?> clazz = null;
    try {
      clazz = Class.forName(className);
    } catch (Throwable ex) {
      throw new NilsException(
          MISSING_DEPENDENCY,
          "The dependency 'com.fasterxml.jackson.dataformat:jackson-dataformat-yaml' is missing for using a YAML file.");
    }

    try {
      return (JsonFactory) clazz.getConstructor().newInstance();
    } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
      throw new NilsException(
          ErrorType.ADAPTER_ERROR,
          String.format("Could not create instance of class '%s'.", className),
          e);
    }
  }
}
