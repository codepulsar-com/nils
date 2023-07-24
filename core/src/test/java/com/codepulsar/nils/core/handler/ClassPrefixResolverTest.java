package com.codepulsar.nils.core.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.testdata.Dummy;

public class ClassPrefixResolverTest {

  @Test
  public void fqnClassnameResolver() {
    // Arrange
    ClassPrefixResolver underTest = ClassPrefixResolver.FQN_CLASSNAME;

    // Act
    String value = underTest.resolve(Dummy.class);

    // Assert
    assertThat(value).isEqualTo("com.codepulsar.nils.core.testdata.Dummy");
  }

  @Test
  public void simpleClassnameResolver() {
    // Arrange
    ClassPrefixResolver underTest = ClassPrefixResolver.SIMPLE_CLASSNAME;

    // Act
    String value = underTest.resolve(Dummy.class);

    // Assert
    assertThat(value).isEqualTo("Dummy");
  }
}
