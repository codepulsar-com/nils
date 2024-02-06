package com.codepulsar.nils.core.adapter.util;

import static com.codepulsar.nils.core.error.ErrorTypes.IO_ERROR;
import static com.codepulsar.nils.core.error.ErrorTypes.MISSING_FILE_EXTENSION_ERROR;
import static com.codepulsar.nils.core.error.ErrorTypes.MISSING_RESOURCE_FILE_ERROR;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle.Control;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.config.LocalizedResourceResolverConfig;
import com.codepulsar.nils.core.util.ParameterCheck;
/**
 * The class <strong>LocalizedResourceResolver</strong> is used to get a resource for a specific
 * <code>Locale</code>.
 *
 * <p>If a resource is not available for a locale it will use the next level.
 *
 * <p>Example:
 *
 * <ul>
 *   <li>BaseFileName is <code>/nls/translation.properties</code>
 *   <li>Locale is <code>de_DE</code>
 * </ul>
 *
 * The resolution will try to find the following files:
 *
 * <pre>
 * /nls/translation_de_DE.properties
 * /nls/translation_de.properties
 * /nls/translation.properties
 * </pre>
 *
 * The first that was found will be returned as <code>InputStream</code>.
 */
public class LocalizedResourceResolver implements AutoCloseable {

  private static final Logger LOG = LoggerFactory.getLogger(LocalizedResourceResolver.class);
  private final LocalizedResourceResolverConfig config;
  private final Locale locale;
  private Function<String, InputStream> resourceToInputStreamResolver;
  private String baseFileName;
  private String fileExtension;

  private InputStream inputStream;
  private String usedResourceName;

  /**
   * Create a new <strong>LocalizedResourceResolver</strong>.
   *
   * @param resolverConfig A {@link LocalizedResourceResolverConfig} object.
   * @param locale The target Locale
   * @param resourceToInputStreamResolver A Function that returns an InputStream based on a resource
   *     name.
   */
  public LocalizedResourceResolver(
      LocalizedResourceResolverConfig resolverConfig,
      Locale locale,
      Function<String, InputStream> resourceToInputStreamResolver) {
    this.config = ParameterCheck.notNull(resolverConfig, "resolverConfig");
    this.locale = ParameterCheck.notNull(locale, "locale");
    this.resourceToInputStreamResolver =
        ParameterCheck.notNull(resourceToInputStreamResolver, "resourceToInputStreamResolver");
    initBaseFilename(resolverConfig.getBaseFileName());
  }

  /**
   * Resolve the InputStream and return if found.
   *
   * @return The resolved InputStream.
   */
  public InputStream resolve() {

    List<String> resourceNames = resolveResourceNames();

    for (String resource : resourceNames) {
      try {
        inputStream = resourceToInputStreamResolver.apply(resource);
        if (inputStream != null) {
          LOG.debug("Found resource {}. Returning the stream object.", resource);
          usedResourceName = resource;
          return inputStream;
        }
      } catch (Exception e) {
        LOG.error("Error getting resource {}.", e, resource);
        throw new NilsException(IO_ERROR, "Error getting resource '" + resource + "'.", e);
      }
    }
    throw new NilsException(
        MISSING_RESOURCE_FILE_ERROR,
        "Could not find a resource for baseFilename '" + config.getBaseFileName() + "'.");
  }

  /**
   * Get the name of the used resource.
   *
   * @return A String with the resource name.
   */
  public String getUsedResourceName() {
    return usedResourceName;
  }

  private List<String> resolveResourceNames() {
    Control ctl = Control.getControl(Control.FORMAT_DEFAULT);
    List<Locale> locales = ctl.getCandidateLocales(baseFileName, locale);
    return locales
        .stream()
        .map(l -> ctl.toBundleName(baseFileName, l) + fileExtension)
        .collect(Collectors.toList());
  }

  private void initBaseFilename(String filename) {
    String normalized = filename.startsWith("/") ? filename : "/" + filename;
    int lastDot = normalized.lastIndexOf(".");
    if (lastDot < 0) {
      throw new NilsException(
          MISSING_FILE_EXTENSION_ERROR,
          "Could not resolve file extension on baseFileName '" + filename + "'.");
    }
    baseFileName = normalized.substring(0, lastDot);
    fileExtension = normalized.substring(lastDot);
  }

  @Override
  public void close() {
    if (inputStream != null) {

      try {
        inputStream.close();
      } catch (IOException e) {
        LOG.warn(
            "Error closing inputStream of resource '"
                + usedResourceName
                + "'. Reason: "
                + e.getMessage(),
            e);
      }
    }
  }
}
