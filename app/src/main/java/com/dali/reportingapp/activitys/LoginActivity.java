package com.dali.reportingapp.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dali.reportingapp.R;
import com.dali.reportingapp.helpers.InputValidation;
import com.dali.reportingapp.models.User;
import com.dali.reportingapp.retrofit.ApiClient;
import com.dali.reportingapp.retrofit.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dali.reportingapp.helpers.ConnectionChecher.checkNetwork;
import static com.dali.reportingapp.helpers.ConnectionChecher.isOnline;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final String host = "mongo-jar.000webhostapp.com";
    private static final String dbname = "id2463366_mydatabase";
    private static final String username = "id2463366_marweennp";
    private static final String password = "m07421603";

    @BindView(R.id.input_email)
    TextInputEditText _emailText;
    @BindView(R.id.input_password)
    TextInputEditText _passwordText;
    @BindView(R.id.button_login)
    AppCompatButton _loginButton;
    @BindView(R.id.text_signup)
    AppCompatTextView _signupLink;
    @BindView(R.id.input_layout_email)
    TextInputLayout _inputLayoutEmail;
    @BindView(R.id.input_layout_password)
    TextInputLayout _inputLayoutPassword;

    private InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        inputValidation = new InputValidation(getApplicationContext());

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validateInput()) {
                    login();
                }

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork(findViewById(android.R.id.content), getApplicationContext());
    }

    private boolean validateInput() {

        Log.d(TAG, "Input Validation");

        if (!inputValidation.isInputEditTextFilled(_emailText, _inputLayoutEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextEmail(_emailText, _inputLayoutEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(_passwordText, _inputLayoutPassword, getString(R.string.error_message_password))) {
            return false;
        }

        return true;

    }

    private void login() {

        Log.d(TAG, "Login");

        _loginButton.setEnabled(false);

        String user_email = _emailText.getText().toString();
        String user_password = _passwordText.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppThemeDarkDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<User> userCall = service.userLogIn(host, dbname, username, password, user_email, user_password);

        if (!isOnline()) {
            checkNetwork(findViewById(android.R.id.content), getApplicationContext());
        } else {

            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    progressDialog.dismiss();
                    _loginButton.setEnabled(true);

                    if (response.body().getSuccess() == 0) {
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent;
                    if (response.body().getUserRole().equals("admin")) {
                        intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                    }
                    intent.putExtra("user_id", response.body().getUserId());
                    intent.putExtra("user_email", response.body().getUserEmail());
                    intent.putExtra("user_first_name", response.body().getUserFirstName());
                    intent.putExtra("user_last_name", response.body().getUserLastName());
                    startActivity(intent);
                    finish();

                }


                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "" + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }

}
