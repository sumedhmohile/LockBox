package com.sumedh.lockbox;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import androidx.annotation.NonNull;

public class Validator {

    public static boolean validateNotEmpty(TextInputEditText inputEditText, Context context) {
        if (inputEditText != null && inputEditText.getText() != null && StringUtils.isNotBlank(inputEditText.getText().toString())) {
            return true;
        }
        inputEditText.setError(context.getResources().getString(R.string.empty_field_error));
        return false;
    }

    public static boolean validateLength(@NonNull TextInputEditText inputEditText, int length, Context context) {
        if (inputEditText.getText() != null) {
            if (inputEditText.getText().toString().length() >= length) {
                return true;
            }
            else {
                inputEditText.setError(context.getResources().getString(R.string.field_length_error));
            }
        }
        return false;
    }

    public static boolean validatePasswordMatch(TextInputEditText password, TextInputEditText confirmPassword, Context context) {
        if (Validator.validateNotEmpty(confirmPassword, context) && Validator.validateNotEmpty(password, context)) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                return true;
            } else {
                confirmPassword.setError(context.getResources().getString(R.string.password_match_error));
            }
        }
        return false;
    }

    public static boolean validateNoBlanks(TextInputEditText inputEditText, Context context) {
        if (Validator.validateNotEmpty(inputEditText, context)) {
            if (!inputEditText.getText().toString().contains(" ")) {
                return true;
            } else {
                inputEditText.setError(context.getResources().getString(R.string.blank_in_text_error));
            }
        }
        return false;
    }
}
