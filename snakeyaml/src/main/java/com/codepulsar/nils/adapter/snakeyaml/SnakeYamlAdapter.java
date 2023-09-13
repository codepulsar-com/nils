package com.codepulsar.nils.adapter.snakeyaml;

import static com.codepulsar.nils.adapter.snakeyaml.utils.SnakeYamlErrorTypes.CORRUPT_FILE_ERROR;

import java.io.IOException;
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
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import com.codepulsar.nils.adapter.snakeyaml.utils.YamlStreamResolver;
import com.codepulsar.nils.core.adapter.Adapter;
import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.error.NilsConfigException;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.util.ParameterCheck;
/** An {@link Adapter} implementation using YAML files for the translations. */
public class SnakeYamlAdapter implements Adapter {
  private static final Logger LOG = LoggerFactory.getLogger(SnakeYamlAdapter.class);

  private final SnakeYamlAdapterConfig adapterConfig;
  private final Locale locale;
  private final String resourceName;
  private boolean fallbackAvailable = true;
  private SnakeYamlAdapter fallbackAdapter;

  private Map<String, Object> translations = Map.of();

  public SnakeYamlAdapter(AdapterConfig config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");
    if (!(config instanceof SnakeYamlAdapterConfig)) {
      throw new NilsConfigException(
          String.format(
              "The provided AdapterConfig (%s) is not of type %s",
              config, SnakeYamlAdapterConfig.class.getName()));
    }
    this.adapterConfig = (SnakeYamlAdapterConfig) config;
    this.locale = locale;
    var loaderOptions = new LoaderOptions();
    loaderOptions.setAllowDuplicateKeys(false);
    Yaml yaml = new Yaml(loaderOptions);
    var resolver = new YamlStreamResolver();
    try (var fileReader =
        new InputStreamReader(resolver.resolve(adapterConfig, locale), StandardCharsets.UTF_8); ) {
      translations = yaml.load(fileReader);
      LOG.debug("Translation for locale {} read.", locale);
    } catch (NilsException e) {
      // Just re-throw NilsExceptions
      throw e;
    } catch (Exception e) {
      throw new NilsException(
          CORRUPT_FILE_ERROR, "Error reading YAML file '" + resolver.getUsedResource() + "'.", e);
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
            || !resourceName.endsWith(adapterConfig.getBaseFileName() + ".yaml");
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
        if (value instanceof List) {
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

  private SnakeYamlAdapter getFallbackAdapter() {
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
      fallbackAdapter = new SnakeYamlAdapterFactory().create(adapterConfig, parent);
    }

    return fallbackAdapter;
  }
}
