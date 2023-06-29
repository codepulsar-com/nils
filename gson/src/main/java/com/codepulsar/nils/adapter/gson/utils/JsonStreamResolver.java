package com.codepulsar.nils.adapter.gson.utils;

import static com.codepulsar.nils.adapter.gson.utils.GsonErrorTypes.IO_ERROR;
import static com.codepulsar.nils.adapter.gson.utils.GsonErrorTypes.MISSING_FILE_ERROR;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle.Control;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.adapter.gson.GsonAdapterConfig;
import com.codepulsar.nils.core.error.NilsException;

public class JsonStreamResolver implements AutoCloseable {
  private static final Logger LOG = LoggerFactory.getLogger(JsonStreamResolver.class);
  private InputStream inputStream;
  private String usedResource;

  public InputStream resolve(GsonAdapterConfig config, Locale locale) {
    List<String> resourceNames = resolveResourceNames(config.getBaseFileName(), locale);

    Module module = config.getOwner();

    for (String resource : resourceNames) {
      try {
        inputStream = module.getResourceAsStream(resource);

        if (inputStream != null) {
          LOG.debug("Found resource {}. Returning the stream object.", resource);
          usedResource = resource;
          return inputStream;
        }
      } catch (IOException e) {
        LOG.error("Error getting resource {}.", e, resource);
        throw new NilsException(IO_ERROR, "Error getting resource " + resource + ".", e);
      }
    }
    throw new NilsException(
        MISSING_FILE_ERROR,
        "Could not find a resource for baseFilename '" + config.getBaseFileName() + "'.");
  }

  public String getUsedResource() {
    return usedResource;
  }

  private List<String> resolveResourceNames(String baseFilename, Locale locale) {
    String normalized = baseFilename.startsWith("/") ? baseFilename : "/" + baseFilename;

    Control ctl = Control.getControl(Control.FORMAT_DEFAULT);
    List<Locale> locales = ctl.getCandidateLocales(normalized, locale);
    return locales
        .stream()
        .map(l -> ctl.toBundleName(normalized, l) + ".json")
        .collect(Collectors.toList());
  }

  @Override
  public void close() throws IOException {
    if (inputStream != null) {
      inputStream.close();
    }
  }
}
