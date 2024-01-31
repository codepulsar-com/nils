package com.codepulsar.nils.adapter.jackson;

import static com.codepulsar.nils.adapter.jackson.utils.JacksonErrorTypes.CORRUPT_FILE_ERROR;
import static com.codepulsar.nils.core.error.ErrorType.IO_ERROR;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.adapter.jackson.utils.ObjectMapperFactory;
import com.codepulsar.nils.core.adapter.Adapter;
import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.util.LocalizedResourceResolver;
import com.codepulsar.nils.core.error.NilsConfigException;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.util.ParameterCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
/** An {@link Adapter} implementation using for JSON and YAML files for the translations. */
public class JacksonAdapter implements Adapter {
  private static final Logger LOG = LoggerFactory.getLogger(JacksonAdapter.class);

  private final JacksonAdapterConfig adapterConfig;
  private final Locale locale;
  private String resourceName;
  private boolean fallbackAvailable = true;
  private JacksonAdapter fallbackAdapter;

  private Map<String, Object> translations = Map.of();

  public JacksonAdapter(AdapterConfig config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");
    if (!(config instanceof JacksonAdapterConfig)) {
      throw new NilsConfigException(
          String.format(
              "The provided AdapterConfig (%s) is not of type %s",
              config, JacksonAdapterConfig.class.getName()));
    }
    this.adapterConfig = (JacksonAdapterConfig) config;
    this.locale = locale;
    initTranslations();
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
        } else if (value instanceof List) {
          if (keyParts.hasMoreTokens()) {
            List<Map<String, Object>> innerList = (List<Map<String, Object>>) value;
            Map<String, Object> next = new HashMap<>();
            innerList.forEach(c -> next.putAll(c));
            latest = next;
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

  @SuppressWarnings("unchecked")
  private void initTranslations() {

    ObjectMapper objectMapper = resolveObjectMapper();
    var resolver = new LocalizedResourceResolver(adapterConfig, locale, this::resolveInputStream);
    try (var fileReader = new InputStreamReader(resolver.resolve(), StandardCharsets.UTF_8); ) {
      translations = objectMapper.readValue(fileReader, Map.class);
      LOG.debug("Translation for locale {} read.", locale);
    } catch (NilsException e) {
      throw e;
    } catch (Exception e) {
      throw new NilsException(
          CORRUPT_FILE_ERROR, "Error reading file '" + resolver.getUsedResourceName() + "'.", e);
    } finally {
      resolver.close();
    }
    this.resourceName = resolver.getUsedResourceName();
    this.fallbackAvailable =
        !locale.equals(new Locale("")) || !resourceName.endsWith(adapterConfig.getBaseFileName());
  }

  private JacksonAdapter getFallbackAdapter() {
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
      fallbackAdapter = new JacksonAdapterFactory().create(adapterConfig, parent);
    }

    return fallbackAdapter;
  }

  private ObjectMapper resolveObjectMapper() {
    var pos = adapterConfig.getBaseFileName().lastIndexOf(".");
    var fileExtension = this.adapterConfig.getBaseFileName().substring(pos);
    return new ObjectMapperFactory().resolve(fileExtension);
  }

  private InputStream resolveInputStream(String resource) {
    try {
      var owner = adapterConfig.getOwner();
      return owner.getResourceAsStream(resource);
    } catch (IOException e) {
      LOG.error("Error getting resource {}.", e, resource);
      throw new NilsException(IO_ERROR, "Error getting resource " + resource + ".", e);
    }
  }
}
