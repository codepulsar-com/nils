module com.codepulsar.nils.core {
  exports com.codepulsar.nils.api;
  exports com.codepulsar.nils.api.adapter;
  exports com.codepulsar.nils.core.adapter.config;
  exports com.codepulsar.nils.adapter.rb;
  exports com.codepulsar.nils.core.adapter.util;
  exports com.codepulsar.nils.core.config;
  exports com.codepulsar.nils.core.error;
  exports com.codepulsar.nils.core.handler;
  exports com.codepulsar.nils.core.util;
  requires java.base;
  requires org.slf4j;
}