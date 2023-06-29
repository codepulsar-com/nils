package com.codepulsar.nils.core.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.rb.ResourceBundleAdapter;
import com.codepulsar.nils.core.adapter.rb.ResourceBundleAdapterConfig;
import com.codepulsar.nils.core.config.SuppressableErrorTypes;
import com.codepulsar.nils.core.error.NilsException;

public class IncludeHandlerTest {

  private ResourceBundleAdapter adapter;
  private NLSImpl nls;
  private IncludeHandler underTest;
  private AdapterConfig adapterConfig;

  @BeforeEach
  public void arrange() {
    // Arrange
    adapterConfig = ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/includes");
    adapter = new ResourceBundleAdapter(adapterConfig, Locale.ENGLISH);
    NilsConfig nilsConfig =
        NilsConfig.init(adapterConfig).suppressErrors(SuppressableErrorTypes.NONE);
    nls = new NLSImpl(adapter, nilsConfig, Locale.ENGLISH);
    underTest = new IncludeHandler(nilsConfig, adapter::getTranslation);
  }

  @Test
  public void requestKeyHasTranslationItself() {
    // Actual
    assertThat(adapter.getTranslation("com.xy.model.Customer.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("com.xy.model.Customer.name").isPresent()).isTrue();
    assertThat(adapter.getTranslation("com.xy.model.Parent.name").isPresent()).isTrue();

    // Act
    Optional<String> resolved = underTest.findKey("com.xy.model.Customer.name");
    String translation = nls.get("com.xy.model.Customer.name");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(resolved.get()).isEqualTo("Name (Parent)");
    assertThat(translation).isEqualTo("Name (Customer)");
  }

  @Test
  public void getTranslationFrom1stElementInIncludeListButBothProvideAValue() {
    // Actual
    assertThat(adapter.getTranslation("com.xy.model.Customer.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("com.xy.model.Customer.id").isEmpty()).isTrue();
    assertThat(adapter.getTranslation("com.xy.model.Parent.id").isPresent()).isTrue();
    assertThat(adapter.getTranslation("BaseData.id").isPresent()).isTrue();

    // Act
    Optional<String> resolved = underTest.findKey("com.xy.model.Customer.id");
    String translation = nls.get("com.xy.model.Customer.id");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(translation).isEqualTo("Id (Parent)");
  }

  @Test
  public void getTranslationFrom1stElementInIncludeList() {
    // Actual
    assertThat(adapter.getTranslation("com.xy.model.Customer.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("com.xy.model.Customer.createDate").isPresent()).isFalse();
    assertThat(adapter.getTranslation("com.xy.model.Parent.createDate").isPresent()).isTrue();
    assertThat(adapter.getTranslation("BaseData.createDate").isPresent()).isFalse();

    // Act
    Optional<String> resolved = underTest.findKey("com.xy.model.Customer.createDate");
    String translation = nls.get("com.xy.model.Customer.createDate");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(translation).isEqualTo("Create date (Parent)");
  }

  @Test
  public void requestKeyHasTranslationItselfBut2ndIncludeProvideAlsoATranslation() {
    // Actual
    assertThat(adapter.getTranslation("com.xy.model.Customer.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("com.xy.model.Customer.street").isPresent()).isTrue();
    assertThat(adapter.getTranslation("com.xy.model.Parent.street").isPresent()).isFalse();
    assertThat(adapter.getTranslation("BaseData.street").isPresent()).isTrue();

    // Act
    Optional<String> resolved = underTest.findKey("com.xy.model.Customer.street");
    String translation = nls.get("com.xy.model.Customer.street");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(resolved.get()).isEqualTo("Street (Base)");
    assertThat(translation).isEqualTo("Street (Customer)");
  }

  @Test
  public void getTranslationFrom2stElementInIncludeList() {
    // Actual
    assertThat(adapter.getTranslation("com.xy.model.Customer.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("com.xy.model.Customer.city").isPresent()).isFalse();
    assertThat(adapter.getTranslation("com.xy.model.Parent.city").isPresent()).isFalse();
    assertThat(adapter.getTranslation("BaseData.city").isPresent()).isTrue();

    // Act
    Optional<String> resolved = underTest.findKey("com.xy.model.Customer.city");
    String translation = nls.get("com.xy.model.Customer.city");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(translation).isEqualTo("City (Base)");
  }

  @Test
  public void resolveKeyFromHigherlevelInclude() {
    // Actual
    assertThat(adapter.getTranslation("data.Message.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("data.Message.buttons.ok").isPresent()).isFalse();
    assertThat(adapter.getTranslation("data.Message.buttons").isPresent()).isFalse();
    assertThat(adapter.getTranslation("data.BaseMessage.buttons.ok").isPresent()).isTrue();

    // Act
    Optional<String> resolved = underTest.findKey("data.Message.buttons.ok");
    String translation = nls.get("data.Message.buttons.ok");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(translation).isEqualTo("OK (BaseMessage)");
  }

  @Test
  public void resolveKeyDirectlyButKeyIsAlsoInHigherLevelIncludeAvailable() {
    // Actual
    assertThat(adapter.getTranslation("data.Message.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("data.Message.buttons.cancel").isPresent()).isTrue();
    assertThat(adapter.getTranslation("data.Message.buttons").isPresent()).isFalse();
    assertThat(adapter.getTranslation("data.BaseMessage.buttons.cancel").isPresent()).isTrue();

    // Act
    Optional<String> resolved = underTest.findKey("data.Message.buttons.cancel");
    String translation = nls.get("data.Message.buttons.cancel");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(resolved.get()).isEqualTo("Cancel (BaseMessage)");
    assertThat(translation).isEqualTo("Cancel (Message)");
  }

  @Test
  public void resolveValueWithMoreLevels() {
    // Actual
    assertThat(adapter.getTranslation("Level2.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Level1.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Level1.value").isPresent()).isFalse();
    assertThat(adapter.getTranslation("Level0.value").isPresent()).isTrue();

    // Act
    Optional<String> resolved = underTest.findKey("Level2.value");
    String translation = nls.get("Level2.value");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(resolved.get()).isEqualTo("Value (Level0)");
    assertThat(translation).isEqualTo("Value (Level0)");
  }

  @Test
  public void circularInclude() {
    // Actual
    assertThat(adapter.getTranslation("Cycle1.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Cycle2.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Cycle1.value").isPresent()).isFalse();
    assertThat(adapter.getTranslation("Cycle2.value").isPresent()).isFalse();

    // Act / Assert
    assertThatThrownBy(() -> underTest.findKey("Cycle1.value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-002: Found circular include on 'Cycle2'.");

    // Act / Assert
    assertThatThrownBy(() -> nls.get("Cycle1.value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-002: Found circular include on 'Cycle2'.");
  }

  @Test
  public void circularInclude_suppressException() {
    // Arrange
    var nilsConfig =
        NilsConfig.init(adapterConfig).suppressErrors(SuppressableErrorTypes.INCLUDE_LOOP_DETECTED);
    var nls_ = new NLSImpl(adapter, nilsConfig, Locale.ENGLISH);
    var underTest_ = new IncludeHandler(nilsConfig, adapter::getTranslation);

    // Actual
    assertThat(adapter.getTranslation("Cycle1.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Cycle2.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Cycle1.value").isPresent()).isFalse();
    assertThat(adapter.getTranslation("Cycle2.value").isPresent()).isFalse();

    // Act / Assert
    assertThatThrownBy(() -> underTest_.findKey("Cycle1.value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-002: Found circular include on 'Cycle2'.");

    // Act / Assert
    assertThatThrownBy(() -> nls_.get("Cycle1.value"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'Cycle1.value' and locale 'en'.");
  }

  @Test
  public void circularInclude_suppressAll() {
    // Arrange
    var nilsConfig = NilsConfig.init(adapterConfig).suppressErrors(SuppressableErrorTypes.ALL);
    var nls_ = new NLSImpl(adapter, nilsConfig, Locale.ENGLISH);
    var underTest_ = new IncludeHandler(nilsConfig, adapter::getTranslation);

    // Actual
    assertThat(adapter.getTranslation("Cycle1.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Cycle2.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("Cycle1.value").isPresent()).isFalse();
    assertThat(adapter.getTranslation("Cycle2.value").isPresent()).isFalse();

    // Act / Assert
    assertThatThrownBy(() -> underTest_.findKey("Cycle1.value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-002: Found circular include on 'Cycle2'.");

    // Act
    String result = nls_.get("Cycle1.value");

    // Assert
    assertThat(result).isEqualTo("[Cycle1.value]");
  }

  @Test
  public void circularIncludeLarger() {
    // Actual
    assertThat(adapter.getTranslation("LargerCycle1.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("LargerCycle2.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("LargerCycle3.@include").isPresent()).isTrue();
    assertThat(adapter.getTranslation("LargerCycle1.value").isPresent()).isFalse();
    assertThat(adapter.getTranslation("LargerCycle2.value").isPresent()).isFalse();
    assertThat(adapter.getTranslation("LargerCycle3.value").isPresent()).isFalse();

    // Act / Assert
    assertThatThrownBy(() -> underTest.findKey("LargerCycle1.value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-002: Found circular include on 'LargerCycle3'.");

    // Act / Assert
    assertThatThrownBy(() -> nls.get("LargerCycle1.value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-002: Found circular include on 'LargerCycle3'.");
  }

  @Test
  public void resolveKeyForOtherIncludeTag() {
    // Arrange
    var _adapterConfig =
        ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/includes");
    var _adapter = new ResourceBundleAdapter(_adapterConfig, Locale.ENGLISH);
    var _nilsConfig = NilsConfig.init(_adapterConfig).includeTag("[include]");
    var _nls = new NLSImpl(adapter, _nilsConfig, Locale.ENGLISH);
    var _underTest = new IncludeHandler(_nilsConfig, adapter::getTranslation);

    // Actual
    assertThat(_adapter.getTranslation("data.Message.[include]").isPresent()).isTrue();
    assertThat(_adapter.getTranslation("data.Message.buttons.ok").isPresent()).isFalse();
    assertThat(_adapter.getTranslation("data.AlternativeMessage.buttons.ok").isPresent()).isTrue();

    // Act
    Optional<String> resolved = _underTest.findKey("data.Message.buttons.ok");
    String translation = _nls.get("data.Message.buttons.ok");

    // Assert
    assertThat(resolved).isNotEmpty();
    assertThat(translation).isEqualTo("Ok (AlternativeMessage)");
  }
}
