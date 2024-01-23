module com.codepulsar.nils.adapter.jackson {
  exports com.codepulsar.nils.adapter.jackson;
  requires transitive com.codepulsar.nils.core;
  requires com.fasterxml.jackson.databind;
  requires static com.fasterxml.jackson.dataformat.yaml;
  requires java.base;
  requires org.slf4j;
}