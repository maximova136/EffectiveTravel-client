package com.et.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.et.R;
import com.et.api.Auth;
import com.et.exception.SignupFailed;

public class SignupActivity extends AppCompatActivity {

    private SignUpTask signUpTask;

    private EditText loginInput;
    private EditText passwordInput;
    private View     mProgressView;
    private View     mSignUpFormView;

    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loginInput = (EditText) findViewById(R.id.new_login);
        passwordInput = (EditText) findViewById(R.id.new_password);

        mSignUpFormView = (View) findViewById(R.id.signup_view);
        mProgressView = (View) findViewById(R.id.signup_progress);

        Button signUpButton = (Button) findViewById(R.id.commit_signup);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        auth = new Auth();

    }

    private void attemptSignUp() {
        if(signUpTask != null) {
            return;
        }

        loginInput.setError(null);
        passwordInput.setError(null);

        // Store values at the time of the login attempt.
        String login = loginInput.getText().toString();
        String password = passwordInput.getText().toString();
        login.trim();
        password.trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordInput.setError(getString(R.string.error_invalid_password));
            focusView = passwordInput;
            cancel = true;
        }

        // Check for a valid login.
        if (TextUtils.isEmpty(login)) {
            loginInput.setError(getString(R.string.error_field_required));
            focusView = loginInput;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            signUpTask = new SignupActivity.SignUpTask(login, password);
            signUpTask.execute((Void) null);
        }

    }


    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private class SignUpTask extends AsyncTask<Void, Void, Boolean> {

        private final String login;
        private final String password;

        public SignUpTask(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.i("SignupActivity", "Trying to sign up...");
            return auth.signup(this.login, this.password);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            signUpTask = null;
            showProgress(false);

            if(success) {
                startActivity(new Intent(SignupActivity.this, MenuActivity.class));
            }
            else {
                loginInput.setError("Failed to sign up");
                loginInput.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            signUpTask = null;
            showProgress(false);
        }
    }


}
