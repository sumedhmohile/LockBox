package com.sumedh.lockbox;


import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Button registerButton;

    public RegisterFragment() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailEditText = view.findViewById(R.id.register_screen_email_text);
        passwordEditText = view.findViewById(R.id.register_screen_password_text);
        confirmPasswordEditText = view.findViewById(R.id.register_screen_password_confirm_text);
        registerButton = view.findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getAndValidateTextField(emailEditText);
                String password = getAndValidateTextField(passwordEditText);

                if (Validator.validatePasswordMatch(passwordEditText, confirmPasswordEditText, getContext())) {
                    AccountManager.registerUser(email, password, getActivity());
                }
            }
        });


        return view;
    }

    private String getAndValidateTextField(TextInputEditText textInput) {
        if (Validator.validateNotEmpty(textInput, getContext()) && Validator.validateLength(textInput, Constants.TEXT_FIELD_MIN_LENGTH, getContext())) {
            return textInput.getText().toString();
        }
        return "";
    }
}
