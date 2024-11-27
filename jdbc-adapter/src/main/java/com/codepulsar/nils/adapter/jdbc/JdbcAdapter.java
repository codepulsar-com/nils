package com.codepulsar.nils.adapter.jdbc;

import static com.codepulsar.nils.core.error.ErrorTypes.ADAPTER_ERROR;
import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;
import static com.codepulsar.nils.core.util.ParameterCheck.notNullEmptyOrBlank;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.adapter.jdbc.utils.JdbcErrorTypes;
import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.AdapterContext;
import com.codepulsar.nils.core.adapter.util.FallbackAdapterHandler;

/** An {@link Adapter} implementation for accessing a database via JDBC to get the translations. */
public class JdbcAdapter implements Adapter {
  private static final Logger LOG = LoggerFactory.getLogger(JdbcAdapter.class);

  private final ReentrantLock lock = new ReentrantLock();
  private final AdapterContext<JdbcAdapter> adapterContext;
  private final Locale locale;
  private final JdbcAdapterConfig adapterConfig;
  private final Map<String, Optional<String>> cache = new HashMap<>();
  private boolean fallbackPossible = true;
  private String selectStatement;
  private final FallbackAdapterHandler<JdbcAdapter> fallbackAdapterHandler;
  private LocalDateTime nextReset;

  protected JdbcAdapter(AdapterContext<JdbcAdapter> context) {
    this.adapterContext = notNull(context, "context");
    notNull(context.getConfig(), "context.config");
    notNull(context.getLocale(), "context.locale");
    if (!(context.getConfig() instanceof JdbcAdapterConfig)) {
      throw ADAPTER_ERROR
          .asException()
          .message("The provided AdapterConfig (%s) does not implement %s.")
          .args(context.getConfig(), JdbcAdapter.class.getName())
          .go();
    }
    adapterConfig = (JdbcAdapterConfig) context.getConfig();
    locale = context.getLocale();
    this.fallbackAdapterHandler =
        new FallbackAdapterHandler<>(adapterContext, adapterConfig.getRootLocale());
    checkConnectionConfig();
    initSelectStatement();
    initFallbackAvailable();
    nextReset = LocalDateTime.now().plusSeconds(adapterConfig.getCacheTimeout());
  }

  @Override
  public Optional<String> getTranslation(String key) {
    lock.lock();
    try {
      notNullEmptyOrBlank(key, "key");
      resetCache();
      var value = cache.computeIfAbsent(key, this::resolveTranslation);

      if (value.isPresent()) {
        return value;
      }

      if (fallbackPossible) {
        value = fallbackAdapterHandler.getFallbackAdapter().getTranslation(key);
      }

      return value;
    } finally {
      lock.unlock();
    }
  }

  private void resetCache() {
    if (adapterConfig.getCacheTimeout() < 0L) {
      return;
    }

    if (LocalDateTime.now().isAfter(nextReset)) {
      cache.clear();
      nextReset = LocalDateTime.now().plusSeconds(adapterConfig.getCacheTimeout());
    }
  }

  private Optional<String> resolveTranslation(String key) {
    initByDriverName();

    try (var con =
            DriverManager.getConnection(
                adapterConfig.getUrl(), adapterConfig.getUsername(), adapterConfig.getPassword());
        var statement = con.prepareStatement(selectStatement)) {

      statement.setString(1, key);
      statement.setString(2, locale.toString());

      try (var resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.ofNullable(resultSet.getString(adapterConfig.getValueField()));
        }
      }
    } catch (SQLException e) {
      LOG.error(e.getMessage(), e);
      throw JdbcErrorTypes.SQL_EXCEPTION.asException().cause(e).args(e.getMessage()).go();
    }
    return Optional.empty();
  }

  private void initByDriverName() {
    var driverClass = adapterConfig.getDriverClass();
    if (driverClass != null && !driverClass.isBlank()) {
      try {
        Class.forName(driverClass);
      } catch (ClassNotFoundException e) {
        LOG.error(e.getMessage(), e);
        throw JdbcErrorTypes.MISSING_JDBC_DRIVER.asException().cause(e).go();
      }
    }
  }

  private void checkConnectionConfig() {
    try {
      notNullEmptyOrBlank(adapterConfig.getUrl(), "url", nilsException(CONFIG_ERROR));
      notNullEmptyOrBlank(adapterConfig.getUsername(), "username", nilsException(CONFIG_ERROR));
    } catch (NilsException e) {
      LOG.error(e.getMessage(), e);
      throw JdbcErrorTypes.INCOMPLETE_CONNECTION_DATA.asException().cause(e).go();
    }
  }

  private void initSelectStatement() {
    var table =
        adapterConfig.getSchema() != null
            ? adapterConfig.getSchema() + "." + adapterConfig.getTableName()
            : adapterConfig.getTableName();

    selectStatement =
        String.format(
            "SELECT %1$s FROM %2$s WHERE %3$s = ? AND %4$s = ?",
            adapterConfig.getValueField(),
            table,
            adapterConfig.getKeyField(),
            adapterConfig.getLocaleField());
  }

  private void initFallbackAvailable() {
    this.fallbackPossible =
        adapterConfig.isFallbackActive() && (!adapterConfig.getRootLocale().equals(locale));
  }
}
