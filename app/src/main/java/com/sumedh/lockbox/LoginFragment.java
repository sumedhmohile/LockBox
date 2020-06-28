package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private TextView registerText;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = view.findViewById(R.id.login_screen_username_text);
        passwordEditText = view.findViewById(R.id.login_screen_password_text);
        loginButton = view.findViewById(R.id.login_button);
        registerText = view.findViewById(R.id.login_screen_register_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator.validateNotEmpty(usernameEditText,  getContext()) && Validator.validateLength(usernameEditText, Constants.TEXT_FIELD_MIN_LENGTH, getContext()) && Validator.validateNoBlanks(usernameEditText, getContext()) && Validator.validateNotEmpty(passwordEditText, getContext())) {
                    ProgressBarManager.showProgressBar(getResources().getString(R.string.login_progress), getFragmentManager());
                    User user = new User(usernameEditText.getText().toString(), null);
                    AccountManager.loginUser(user, passwordEditText.getText().toString(), getActivity());
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = RegisterFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, registerFragment)
                        .commit();
            }
        });
        return view;
    }
}