package com.dali.reportingapp.activitys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dali.reportingapp.R;
import com.dali.reportingapp.helpers.InputValidation;
import com.dali.reportingapp.models.Response;
import com.dali.reportingapp.retrofit.ApiClient;
import com.dali.reportingapp.retrofit.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static com.dali.reportingapp.helpers.ConnectionChecher.checkNetwork;
import static com.dali.reportingapp.helpers.ConnectionChecher.isOnline;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private static final String host = "mongo-jar.000webhostapp.com";
    private static final String dbname = "id2463366_mydatabase";
    private static final String username = "id2463366_marweennp";
    private static final String password = "m07421603";

    @BindView(R.id.input_first_name_signup)
    TextInputEditText _firstNameSignupText;
    @BindView(R.id.input_last_name_signup)
    TextInputEditText _lastNameSignupText;
    @BindView(R.id.input_email_signup)
    TextInputEditText _emailSignupText;
    @BindView(R.id.input_password_signup)
    TextInputEditText _passwordSignupText;
    @BindView(R.id.input_confirm_password_signup)
    TextInputEditText _confirmPasswordSignupText;

    @BindView(R.id.button_signup)
    AppCompatButton _signupButton;

    @BindView(R.id.input_layout_first_name_signup)
    TextInputLayout _inputLayoutFirstNameSignup;
    @BindView(R.id.input_layout_last_name_signup)
    TextInputLayout _inputLayoutLastNameSignup;
    @BindView(R.id.input_layout_email_signup)
    TextInputLayout _inputLayoutEmailSignup;
    @BindView(R.id.input_layout_password_signup)
    TextInputLayout _inputLayoutPasswordSignup;
    @BindView(R.id.input_layout_confirm_password_signup)
    TextInputLayout _inputLayoutConfirmPasswordSignup;

    private InputValidation inputValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        inputValidation = new InputValidation(getApplicationContext());

        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validateInput()) {
                    signup();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork(findViewById(android.R.id.content), getApplicationContext());
    }

    private boolean validateInput() {

        if (!inputValidation.isInputEditTextFilled(_firstNameSignupText, _inputLayoutFirstNameSignup, getString(R.string.error_message_name))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(_lastNameSignupText, _inputLayoutLastNameSignup, getString(R.string.error_message_name))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(_emailSignupText, _inputLayoutEmailSignup, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextEmail(_emailSignupText, _inputLayoutEmailSignup, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(_passwordSignupText, _inputLayoutPasswordSignup, getString(R.string.error_message_password))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(_confirmPasswordSignupText, _inputLayoutConfirmPasswordSignup, getString(R.string.error_message_password))) {
            return false;
        }
        if (!inputValidation.isInputEditTextMatches(_passwordSignupText, _confirmPasswordSignupText, _inputLayoutConfirmPasswordSignup, getString(R.string.error_password_match))) {
            return false;
        }

        return true;

    }

    private void signup() {

        Log.d(TAG, "Login");

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppThemeDarkDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String user_first_name = _firstNameSignupText.getText().toString();
        String user_last_name = _lastNameSignupText.getText().toString();
        String user_email = _emailSignupText.getText().toString();
        String user_password = _passwordSignupText.getText().toString();

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Response> userCall = service.userSgineUp(host, dbname, username, password, user_email, user_password, user_first_name, user_last_name);

        if (!isOnline()) {
            checkNetwork(findViewById(android.R.id.content), getApplicationContext());
        } else {

            userCall.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                    progressDialog.dismiss();
                    _signupButton.setEnabled(true);

                    if (response.body().getSuccess() == 0) {
                        Toast.makeText(SignupActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    finish();

                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "" + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
