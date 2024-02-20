package com.codepulsar.nils.adapter.gson;

import static com.codepulsar.nils.adapter.gson.utils.GsonErrorTypes.CORRUPT_FILE_ERROR;
import static com.codepulsar.nils.core.error.ErrorTypes.IO_ERROR;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.BaseLocalizedResourceAdapter;
import com.codepulsar.nils.core.adapter.util.LocalizedResourceResolver;
import com.codepulsar.nils.core.adapter.util.MapTranslationRetriever;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
/** An {@link Adapter} implementation using JSON files for the translations. */
public class GsonAdapter extends BaseLocalizedResourceAdapter<GsonAdapter, GsonAdapterConfig> {
  private static final Logger LOG = LoggerFactory.getLogger(GsonAdapter.class);

  public GsonAdapter(AdapterContext<GsonAdapter> context) {
    super(context);
  }

  @Override
  protected void initTranslations(LocalizedResourceResolver resolver) {
    var gson = new GsonBuilder().create();
    try (var fileReader = new InputStreamReader(resolver.resolve(), StandardCharsets.UTF_8);
        var jsonReader = new JsonReader(fileReader); ) {
      Map<String, Object> translations = gson.fromJson(jsonReader, Map.class);
      translation = new MapTranslationRetriever(translations);
      LOG.debug("Translation for locale {} read.", locale);
    } catch (JsonIOException | JsonSyntaxException | IOException e) {
      throw CORRUPT_FILE_ERROR
          .asException()
          .message("Error reading JSON file '%s'.")
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
