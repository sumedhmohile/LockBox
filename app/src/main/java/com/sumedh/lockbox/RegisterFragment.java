package com.sumedh.lockbox;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class RegisterFragment extends Fragment {

    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Button registerButton;
    private TextView loginText;

    public RegisterFragment() {}

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        usernameEditText = view.findViewById(R.id.register_screen_username_text);
        emailEditText = view.findViewById(R.id.register_screen_email_text);
        passwordEditText = view.findViewById(R.id.register_screen_password_text);
        confirmPasswordEditText = view.findViewById(R.id.register_screen_password_confirm_text);
        registerButton = view.findViewById(R.id.register_button);
        loginText = view.findViewById(R.id.register_screen_login_text);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validatePasswordMatch(passwordEditText, confirmPasswordEditText, getContext()) && Validator.validateNoBlanks(usernameEditText, getContext())) {
                    ProgressBarManager.showProgressBar(getResources().getString(R.string.register_progress_message), getFragmentManager());
                    String username = getAndValidateTextField(usernameEditText);
                    String email = getAndValidateTextField(emailEditText);
                    String password = getAndValidateTextField(passwordEditText);
                    User user = new User(username, email);
                    AccountManager.registerUser(user, password, getActivity());
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = LoginFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, loginFragment)
                        .commit();
            }
        });

        return view;
    }

    private String getAndValidateTextField(TextInputEditText textInput) {
        if (Validator.validateNotEmpty(textInput, getContext()) && Validator.validateLength(textInput, Constants.TEXT_FIELD_MIN_LENGTH, getContext())) {
            return textInput.getText().toString();
        }
        ProgressBarManager.dismissProgressBar();
        return "";
    }
}
