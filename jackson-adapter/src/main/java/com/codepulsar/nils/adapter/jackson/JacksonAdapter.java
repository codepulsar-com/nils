package com.codepulsar.nils.adapter.jackson;

import static com.codepulsar.nils.adapter.jackson.utils.JacksonErrorTypes.CORRUPT_FILE_ERROR;
import static com.codepulsar.nils.core.error.ErrorTypes.IO_ERROR;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.adapter.jackson.utils.ObjectMapperFactory;
import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseLocalizedResourceAdapter;
import com.codepulsar.nils.core.adapter.util.LocalizedResourceResolver;
import com.codepulsar.nils.core.adapter.util.MapTranslationRetriever;
import com.fasterxml.jackson.databind.ObjectMapper;
/** An {@link Adapter} implementation using for JSON and YAML files for the translations. */
public class JacksonAdapter
    extends BaseLocalizedResourceAdapter<JacksonAdapter, JacksonAdapterConfig<?>> {
  private static final Logger LOG = LoggerFactory.getLogger(JacksonAdapter.class);

  public JacksonAdapter(AdapterContext<JacksonAdapter> context) {
    super(context);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void initTranslations(LocalizedResourceResolver resolver) {
    ObjectMapper objectMapper = resolveObjectMapper();
    try (var fileReader = new InputStreamReader(resolver.resolve(), StandardCharsets.UTF_8); ) {
      Map<String, Object> translations = objectMapper.readValue(fileReader, Map.class);
      translation = new MapTranslationRetriever(translations);
      LOG.debug("Translation for locale {} read.", locale);
    } catch (NilsException e) {
      throw e;
    } catch (Exception e) {
      throw CORRUPT_FILE_ERROR
          .asException()
          .message("Error reading file '%s'.")
          .args(resolver.getUsedResourceName())
          .cause(e)
          .go();
    } finally {
      resolver.close();
    }
    this.resourceName = resolver.getUsedResourceName();
  }

  private ObjectMapper resolveObjectMapper() {
    var pos = adapterConfig.getBaseFileName().lastIndexOf(".");
    var fileExtension = this.adapterConfig.getBaseFileName().substring(pos);
    return new ObjectMapperFactory().resolve(fileExtension);
  }

  @Override
  protected InputStream resolveInputStream(String resource) {
    try {
      var owner = adapterConfig.getOwnerModule();
      return owner.getResourceAsStream(resource);
    } catch (IOException e) {
      LOG.error("Error getting resource {}.", e, resource);
      throw new NilsException(IO_ERROR, "Error getting resource %s", e, resource);
    }
  }
}
