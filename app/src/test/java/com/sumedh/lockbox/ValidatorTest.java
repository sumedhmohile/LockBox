package com.sumedh.lockbox;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;

import com.google.android.material.textfield.TextInputEditText;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class ValidatorTest {


    @Test
    public void testValidateNotEmpty() {
        TextInputEditText textInputEditTextMock = PowerMockito.mock(TextInputEditText.class);
        Editable editableMock = PowerMockito.mock(Editable.class);
        Context context = PowerMockito.mock(Context.class);
        Resources resourcesMock = PowerMockito.mock(Resources.class);

        PowerMockito.when(textInputEditTextMock.getText()).thenReturn(editableMock);
        PowerMockito.when(context.getResources()).thenReturn(resourcesMock);
        PowerMockito.when(resourcesMock.getString(R.string.empty_field_error)).thenReturn("test error message");

        PowerMockito.when(editableMock.toString()).thenReturn("test string");
        Assert.assertTrue(Validator.validateNotEmpty(textInputEditTextMock, context));

        PowerMockito.when(editableMock.toString()).thenReturn(null);
        Assert.assertFalse(Validator.validateNotEmpty(textInputEditTextMock, context));

        PowerMockito.when(editableMock.toString()).thenReturn("");
        Assert.assertFalse(Validator.validateNotEmpty(textInputEditTextMock, context));

        PowerMockito.when(textInputEditTextMock.getText()).thenReturn(null);
        Assert.assertFalse(Validator.validateNotEmpty(textInputEditTextMock, context));

    }

    @Test
    public void testValidateLength() {
        TextInputEditText textInputEditTextMock = PowerMockito.mock(TextInputEditText.class);
        Editable editableMock = PowerMockito.mock(Editable.class);
        PowerMockito.when(textInputEditTextMock.getText()).thenReturn(editableMock);
        Context context = PowerMockito.mock(Context.class);
        Resources resourcesMock = PowerMockito.mock(Resources.class);

        PowerMockito.when(context.getResources()).thenReturn(resourcesMock);
        PowerMockito.when(resourcesMock.getString(R.string.field_length_error)).thenReturn("test error message");

        PowerMockito.when(editableMock.toString()).thenReturn("some long string");
        Assert.assertTrue(Validator.validateLength(textInputEditTextMock, 5, context));

        PowerMockito.when(editableMock.toString()).thenReturn("test");
        Assert.assertTrue(Validator.validateLength(textInputEditTextMock, 4, context));

        PowerMockito.when(editableMock.toString()).thenReturn("");
        Assert.assertFalse(Validator.validateLength(textInputEditTextMock, 3, context));

        PowerMockito.when(textInputEditTextMock.getText()).thenReturn(null);
        Assert.assertFalse(Validator.validateLength(textInputEditTextMock, 3, context));
    }

    @Test
    public void testValidatePasswordMatch() {
        TextInputEditText passwordTextInputEditTextMock = PowerMockito.mock(TextInputEditText.class);
        TextInputEditText confirmTextInputEditTextMock = PowerMockito.mock(TextInputEditText.class);
        Editable passwordEditableMock = PowerMockito.mock(Editable.class);
        Editable confirmEditableMock = PowerMockito.mock(Editable.class);
        Context context = PowerMockito.mock(Context.class);
        Resources resourcesMock = PowerMockito.mock(Resources.class);

        PowerMockito.when(passwordTextInputEditTextMock.getText()).thenReturn(passwordEditableMock);
        PowerMockito.when(confirmTextInputEditTextMock.getText()).thenReturn(confirmEditableMock);
        PowerMockito.when(passwordEditableMock.toString()).thenReturn("test");
        PowerMockito.when(confirmEditableMock.toString()).thenReturn("test");
        PowerMockito.when(context.getResources()).thenReturn(resourcesMock);
        PowerMockito.when(resourcesMock.getString(R.string.password_match_error)).thenReturn("test error message");

        Assert.assertTrue(Validator.validatePasswordMatch(passwordTextInputEditTextMock, confirmTextInputEditTextMock, context));

        PowerMockito.when(confirmEditableMock.toString()).thenReturn("other");
        Assert.assertFalse(Validator.validatePasswordMatch(passwordTextInputEditTextMock, confirmTextInputEditTextMock, context));
    }

    @Test
    public void testValidateNoBlanks() {
        TextInputEditText textInputEditTextMock = PowerMockito.mock(TextInputEditText.class);
        Editable editableMock = PowerMockito.mock(Editable.class);
        PowerMockito.when(textInputEditTextMock.getText()).thenReturn(editableMock);
        Context context = PowerMockito.mock(Context.class);
        Resources resourcesMock = PowerMockito.mock(Resources.class);

        PowerMockito.when(context.getResources()).thenReturn(resourcesMock);
        PowerMockito.when(resourcesMock.getString(R.string.blank_in_text_error)).thenReturn("test error message");

        PowerMockito.when(editableMock.toString()).thenReturn("some long string");
        Assert.assertFalse(Validator.validateNoBlanks(textInputEditTextMock, context));

        PowerMockito.when(editableMock.toString()).thenReturn("test");
        Assert.assertTrue(Validator.validateNoBlanks(textInputEditTextMock, context));
    }
}
