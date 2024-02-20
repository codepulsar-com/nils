package com.codepulsar.nils.adapter.snakeyaml;

import static com.codepulsar.nils.adapter.snakeyaml.utils.SnakeYamlErrorTypes.CORRUPT_FILE_ERROR;
import static com.codepulsar.nils.core.error.ErrorTypes.IO_ERROR;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseLocalizedResourceAdapter;
import com.codepulsar.nils.core.adapter.util.LocalizedResourceResolver;
import com.codepulsar.nils.core.adapter.util.MapTranslationRetriever;
/** An {@link Adapter} implementation using YAML files for the translations. */
public class SnakeYamlAdapter
    extends BaseLocalizedResourceAdapter<SnakeYamlAdapter, SnakeYamlAdapterConfig> {
  private static final Logger LOG = LoggerFactory.getLogger(SnakeYamlAdapter.class);

  public SnakeYamlAdapter(AdapterContext<SnakeYamlAdapter> context) {
    super(context);
  }

  @Override
  protected void initTranslations(LocalizedResourceResolver resolver) {
    var loaderOptions = new LoaderOptions();
    loaderOptions.setAllowDuplicateKeys(false);
    var yaml = new Yaml(loaderOptions);
    try (var fileReader = new InputStreamReader(resolver.resolve(), StandardCharsets.UTF_8); ) {
      Map<String, Object> translations = yaml.load(fileReader);
      translation = new MapTranslationRetriever(translations);
      LOG.debug("Translation for locale {} read.", locale);
    } catch (NilsException e) {
      // Just re-throw NilsExceptions
      throw e;
    } catch (Exception e) {
      throw CORRUPT_FILE_ERROR
          .asException()
          .message("Error reading YAML file '%s'.")
          .args(resolver.getUsedResourceName())
          .cause(e)
          .go();
    } finally {
      resolver.close();
    }
    this.resourceName = resolver.getUsedResourceName();
  }

  @Override
  protected InputStream resolveInputStream(String resource) {
    try {
      var owner = adapterConfig.getOwner();
      return owner.getResourceAsStream(resource);
    } catch (IOException e) {
      LOG.error("Error getting resource {}.", e, resource);
      throw new NilsException(IO_ERROR, "Error getting resource %s.", e, resource);
    }
  }
}
