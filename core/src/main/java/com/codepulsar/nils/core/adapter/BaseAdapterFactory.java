package com.codepulsar.nils.core.adapter;

import static com.codepulsar.nils.core.error.ErrorTypes.ADAPTER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.NilsFactory;
import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.util.ParameterCheck;

/**
 * The {@linkplain BaseAdapterFactory} is a base implementation for {@link AdapterFactory}s.
 *
 * <p>The class provides common checks and caches the create {@link Adapter} objects for faster
 * access (ie. multiple calls from the {@link NilsFactory} with the same {@code Locale} or if a
 * {@link Adapter} is called as fallback.
 *
 * @param <A> The type of the {@link Adapter}
 */
public abstract class BaseAdapterFactory<A extends Adapter> implements AdapterFactory<A> {

  private Map<Locale, Adapter> cache = new HashMap<>();

  @SuppressWarnings("unchecked")
  @Override
  public A create(NilsConfig<?> config, Locale locale) {
    ParameterCheck.notNull(config, "config");
    ParameterCheck.notNull(locale, "locale");
    checkAdapterConfig(config);

    return (A)
        cache.computeIfAbsent(
            locale,
            k -> {
              var adapterContext = new AdapterContext<A>().config(config).locale(k).factory(this);
              return createAdapter(adapterContext);
            });
  }

  protected void checkAdapterConfig(NilsConfig<?> config) {
    boolean found = false;
    for (Class<? extends NilsConfig<?>> configClass : getValidAdapterConfigClasses()) {
      if (configClass.isInstance(config)) {
        found = true;
        break;
      }
    }

    if (!found) {
      var classNames =
          getValidAdapterConfigClasses().stream()
              .map(Class::getName)
              .collect(Collectors.joining(","));
      throw ADAPTER_ERROR
          .asException()
          .message("The provided AdapterConfig (%s) is not of the expected type (%s).")
          .args(config, classNames)
          .go();
    }
  }

  /**
   * Gets the classes for valid {@link NilsConfig}s for the factory.
   *
   * @return The Class of the valid {@link NilsConfig}.
   */
  protected abstract List<Class<? extends NilsConfig<?>>> getValidAdapterConfigClasses();

  /**
   * Create a concrete {@link Adapter} object.
   *
   * <p><em>Note:</em>The method is only called if no adapter for the requested {@code Locale} was
   * found.
   *
   * @param context An {@link AdapterContext} object
   * @return The newly created {@link Adapter}.
   */
  protected abstract A createAdapter(AdapterContext<A> context);
}
