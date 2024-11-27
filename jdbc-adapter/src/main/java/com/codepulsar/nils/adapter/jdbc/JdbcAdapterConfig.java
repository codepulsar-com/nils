package com.codepulsar.nils.adapter.jdbc;

import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;
import static com.codepulsar.nils.core.util.ParameterCheck.notNullEmptyOrBlank;

import java.util.Locale;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.adapter.config.BaseNilsConfig;
import com.codepulsar.nils.core.util.ParameterCheck;

/** Configuration for the {@link JdbcAdapter} implementation. */
public class JdbcAdapterConfig extends BaseNilsConfig<JdbcAdapterConfig> {

  private String url;
  private String username;
  private String password = "";
  private String driverClass;
  private boolean fallbackActive = true;
  private Locale rootLocale = new Locale("");

  private String schema = null;
  private String tableName = "NILS_TRANSLATION";
  private String localeField = "NLS_LOCALE";
  private String keyField = "NLS_KEY";
  private String valueField = "NLS_VALUE";

  private long cacheTimeout = -1L;

  /**
   * Get the database URL.
   *
   * <p><em>Note: This value is mandatory.</em>
   *
   * @return The URL to the database.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Set the database URL.
   *
   * <p><em>Note: This value is mandatory.</em>
   *
   * @param url The URL to the database.
   * @return This config object.
   * @see #getUrl()
   */
  protected JdbcAdapterConfig url(String url) {
    this.url = notNullEmptyOrBlank(url, "url", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the user name to connect to the database.
   *
   * <p><em>Note: This value is mandatory.</em>
   *
   * @return A user name.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the user name to connect to the database.
   *
   * <p><em>Note: This value is mandatory.</em>
   *
   * @param username A user name.
   * @return This config object.
   * @see #getUsername()
   */
  protected JdbcAdapterConfig username(String username) {
    this.username = notNullEmptyOrBlank(username, "username", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the password to connect to the database.
   *
   * <p><em>Note: This value can be empty.</em>
   *
   * @return A String
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the password to connect to the database.
   *
   * <p><em>Note: This value can be empty.</em>
   *
   * @param password A String
   * @return This config object.
   * @see #getPassword()
   */
  protected JdbcAdapterConfig password(String password) {
    this.password =
        ParameterCheck.notNull(password, "password", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the driver class name of the JDBC driver implementation.
   *
   * <p><em>Note: This value is optional. It must only be configured if the automatic resolution of
   * the driver class is not possible.</em>
   *
   * @return The full qualified name of the JDBC driver class.
   */
  public String getDriverClass() {
    return driverClass;
  }

  /**
   * Set the driver class name of the JDBC driver implementation.
   *
   * <p><em>Note: This value is optional. It must only be configured if the automatic resolution of
   * the driver class is not possible.</em>
   *
   * @param driverClass The full qualified name of the JDBC driver class.
   * @return This config object.
   * @see #getDriverClass()
   */
  public JdbcAdapterConfig driverClass(String driverClass) {
    this.driverClass =
        notNullEmptyOrBlank(driverClass, "driverClass", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the database schema the translation table is located in.
   *
   * <p><em>Note: This value is only neccessary if the scheme must be defined to access the
   * translation table.</em>
   *
   * @return The database schema.
   */
  public String getSchema() {
    return schema;
  }

  /**
   * Set the database schema the translation table is located in.
   *
   * <p><em>Note: This value is only neccessary if the scheme must be defined to access the
   * translation table.</em>
   *
   * @param schema The database schema.
   * @return This config object.
   * @see #getSchema()
   */
  public JdbcAdapterConfig schema(String schema) {
    this.schema = notNullEmptyOrBlank(schema, "schema", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the name of the translation table.
   *
   * <p>The default value is {@code NILS_TRANSLATION}.
   *
   * @return The name of the translation table.
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * Set the name of the translation table.
   *
   * <p>The default value is {@code NILS_TRANSLATION}.
   *
   * @param tableName The name of the translation table.
   * @return This config object.
   * @see #getTableName()
   */
  public JdbcAdapterConfig tableName(String tableName) {
    this.tableName =
        notNullEmptyOrBlank(tableName, "tableName", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the name of the locale field in the translation table.
   *
   * <p>The default value is {@code LOCALE}.
   *
   * @return The name of the locale field.
   */
  public String getLocaleField() {
    return localeField;
  }

  /**
   * Set the name of the locale field in the translation table.
   *
   * <p>The default value is {@code LOCALE}.
   *
   * @param localeField The name of the locale field.
   * @return This config object
   * @see #getLocaleField()
   */
  public JdbcAdapterConfig localeField(String localeField) {
    this.localeField =
        notNullEmptyOrBlank(localeField, "localeField", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the name of the translation key field in the translation table.
   *
   * <p>The default value is {@code KEY_}.
   *
   * @return The name of the key field.
   */
  public String getKeyField() {
    return keyField;
  }

  /**
   * Set the name of the translation key field in the translation table.
   *
   * <p>The default value is {@code KEY_}.
   *
   * @param keyField The name of the key field.
   * @return This config object.
   * @see #getKeyField()
   */
  public JdbcAdapterConfig keyField(String keyField) {
    this.keyField = notNullEmptyOrBlank(keyField, "keyField", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the name of the translation value field in the translation table.
   *
   * <p>The default value is {@code VALUE_}.
   *
   * @return The name of the value field.
   */
  public String getValueField() {
    return valueField;
  }

  /**
   * Set the name of the translation value field in the translation table.
   *
   * <p>The default value is {@code VALUE_}.
   *
   * @param valueField The name of the value field.
   * @return This config object.
   * @see #getValueField()
   */
  public JdbcAdapterConfig valueField(String valueField) {
    this.valueField =
        notNullEmptyOrBlank(valueField, "valueField", nilsException(CONFIG_ERROR)).trim();
    return this;
  }

  /**
   * Get the timeout in seconds after the cache should be cleared and translations are reloaded from
   * the database.
   *
   * <p>A value &lt; 0 disable the timeout.
   *
   * <p>The default is -1.
   *
   * @return The value in seconds.
   */
  public long getCacheTimeout() {
    return cacheTimeout;
  }

  /**
   * Set the timeout in seconds after the cache should be cleared and translations are reloaded from
   * the database.
   *
   * <p>A value &lt; 0 disable the timeout.
   *
   * <p>The default is -1.
   *
   * @param cacheTimeout The value in seconds.
   * @return This config object.
   * @see #getCacheTimeout()
   */
  public JdbcAdapterConfig cacheTimeout(long cacheTimeout) {
    this.cacheTimeout = cacheTimeout;
    return this;
  }

  /**
   * Get the flag, if a fallback to other locale is active.
   *
   * @return {@code true} if active, else {@code false}.
   */
  public boolean isFallbackActive() {
    return fallbackActive;
  }

  /**
   * Set the flag, if a fallback to other locale is active.
   *
   * @param fallback {@code true} if active, else {@code false}.
   * @see #isFallbackActive()
   * @return This config object.
   */
  public JdbcAdapterConfig fallbackActive(boolean fallback) {
    this.fallbackActive = fallback;
    return this;
  }

  /**
   * Get the root {@code Locale}.
   *
   * <p>The default value is a Locale without any language information ("").
   *
   * @return The root locale.
   */
  public Locale getRootLocale() {
    return rootLocale;
  }

  /**
   * Set the root {@code Locale}.
   *
   * @param rootLocale The root locale.
   * @return This config object.
   * @see #getRootLocale()
   */
  public JdbcAdapterConfig rootLocale(Locale rootLocale) {
    this.rootLocale = notNull(rootLocale, "rootLocale", nilsException(CONFIG_ERROR));
    return this;
  }

  @Override
  public Class<? extends AdapterFactory<?>> getFactoryClass() {
    return JdbcAdapterFactory.class;
  }

  /**
   * Create a <code>JdbcAdapterConfig</code>.
   *
   * @param url The connection URL
   * @param username The user name to access the database
   * @param password The password of the user
   * @return The created JdbcAdapterConfig.
   */
  public static JdbcAdapterConfig init(String url, String username, String password) {
    var config = new JdbcAdapterConfig();
    config.url(url).username(username);
    if (password != null) {
      config.password(password);
    }
    return config;
  }
}
