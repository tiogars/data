package fr.tiogars.data.common.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextValidationUtilsTest {

    @Test
    void shouldTrimTextWhenValueIsPresent() {
        assertThat(TextValidationUtils.requireText("  valeur  ", "message")).isEqualTo("valeur");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenRequiredTextIsNull() {
        assertThatThrownBy(() -> TextValidationUtils.requireText(null, "Le texte est obligatoire"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Le texte est obligatoire");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenRequiredTextIsBlank() {
        assertThatThrownBy(() -> TextValidationUtils.requireText("   ", "Le texte est obligatoire"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Le texte est obligatoire");
    }

    @Test
    void shouldReturnNullWhenNullableTextIsNull() {
        assertThat(TextValidationUtils.normalizeNullableText(null)).isNull();
    }

    @Test
    void shouldReturnNullWhenNullableTextIsBlank() {
        assertThat(TextValidationUtils.normalizeNullableText("  ")).isNull();
    }

    @Test
    void shouldTrimTextWhenNullableValueIsPresent() {
        assertThat(TextValidationUtils.normalizeNullableText("  valeur  ")).isEqualTo("valeur");
    }
}