package com.codepulsar.nils.adapter.gson;

import static com.codepulsar.nils.adapter.gson.utils.GsonErrorTypes.CORRUPT_JSON_FILE_ERROR;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.adapter.gson.utils.JsonStreamResolver;
import com.codepulsar.nils.core.adapter.Adapter;
import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.error.NilsConfigException;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.util.ParameterCheck;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
/** An {@link Adapter} implementation using JSON files for the translations. */
public class GsonAdapter implements Adapter {
  private static final Logger LOG = LoggerFactory.getLogger(GsonAdapter.class);

  private final GsonAdapterConfig adapterConfig;
  private final Locale locale;
  private final String resourceName;
  private boolean fallbackAvailable = true;
  private GsonAdapter fallbackAdapter;

  private Map<String, Object> translations = Map.of();

  public GsonAdapter(AdapterConfig config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");
    if (!(config instanceof GsonAdapterConfig)) {
      throw new NilsConfigException(
          String.format(
              "The provided AdapterConfig (%s) is not of type %s",
              config, GsonAdapterConfig.class.getName()));
    }
    this.adapterConfig = (GsonAdapterConfig) config;
    this.locale = locale;
    Gson gson = new GsonBuilder().create();
    var resolver = new JsonStreamResolver();
    try (var fileReader =
            new InputStreamReader(resolver.resolve(adapterConfig, locale), StandardCharsets.UTF_8);
        var jsonReader = new JsonReader(fileReader); ) {
      translations = gson.fromJson(jsonReader, Map.class);
      LOG.debug("Translation for locale {} read.", locale);
    } catch (JsonIOException | JsonSyntaxException | IOException e) {
      throw new NilsException(
          CORRUPT_JSON_FILE_ERROR,
          "Error reading JSON file '" + resolver.getUsedResource() + "'.",
          e);
    } finally {
      try {
        resolver.close();
      } catch (IOException e) {
        LOG.debug("Error closing resource {}.", resolver.getUsedResource(), e);
      }
    }
    this.resourceName = resolver.getUsedResource();
    this.fallbackAvailable =
        !locale.equals(new Locale(""))
            || !resourceName.endsWith(adapterConfig.getBaseFileName() + ".json");
  }

  @Override
  public Optional<String> getTranslation(String key) {
    ParameterCheck.notNullEmptyOrBlank(key, "key");
    var value = retrieveTranslationFromCurrentData(key);

    if (value.isPresent()) {
      return value;
    }

    if (fallbackAvailable) {
      value = getFallbackAdapter().getTranslation(key);
    }

    return value;
  }

  @SuppressWarnings("unchecked")
  private Optional<String> retrieveTranslationFromCurrentData(String key) {
    StringTokenizer keyParts = new StringTokenizer(key, ".");
    Map<String, Object> latest = translations;
    while (keyParts.hasMoreTokens()) {
      String part = keyParts.nextToken();
      Object value = latest.get(part);
      if (value == null) {
        return Optional.empty();
      } else if (value instanceof String) {
        if (!keyParts.hasMoreTokens()) {
          return Optional.of((String) value);
        } else {
          return Optional.empty();
        }
      } else {
        if (value instanceof Map) {
          if (keyParts.hasMoreTokens()) {
            latest = (Map<String, Object>) value;
          } else {
            return Optional.empty();
          }
        } else {
          return Optional.of(value.toString());
        }
      }
    }
    return Optional.empty();
  }

  private GsonAdapter getFallbackAdapter() {
    if (fallbackAdapter == null) {
      var variant = locale.getVariant();
      var country = locale.getCountry();
      var language = locale.getLanguage();
      if (variant.isEmpty() && !country.isEmpty() && !language.isEmpty()) {
        country = "";
      } else if (variant.isEmpty() && country.isEmpty() && !language.isEmpty()) {
        language = "";
      }
      Locale parent = new Locale(language, country);
      fallbackAdapter = new GsonAdapterFactory().create(adapterConfig, parent);
    }

    return fallbackAdapter;
  }
}
