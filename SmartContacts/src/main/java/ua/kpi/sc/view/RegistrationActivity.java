package ua.kpi.sc.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ua.kpi.sc.R;
import ua.kpi.sc.control.UserControl;
import ua.kpi.sc.model.User;

public class RegistrationActivity extends Activity {

    public static final int MIN_PASSWORD_LENGTH = 4;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText login;
    private EditText password;
    private EditText repPassword;
    private Button submit;
    private  UserControl userControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstName = (EditText)findViewById(R.id.reg_first_name);
        lastName = (EditText)findViewById(R.id.reg_last_name);
        email = (EditText)findViewById(R.id.reg_email);
        login = (EditText)findViewById(R.id.reg_login);
        password = (EditText)findViewById(R.id.reg_pass);
        repPassword = (EditText)findViewById(R.id.reg_rep_pass);

        submit = (Button)findViewById(R.id.reg_submit_button);

        final Activity activity = this;
        userControl = new UserControl(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputFields()) {
                    new UserControl(activity).addUser(getUserFromFields());
                    Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                    intent.putExtra("user_name", firstName.getText().toString() + " " + lastName.getText().toString());
                    startActivity(intent);
                }
            }
        });



    }

    private User getUserFromFields() {
        User user = new User();
        user.setFirstName(firstName.getText().toString().trim());
        user.setLastName(lastName.getText().toString().trim());
        user.setEmail(email.getText().toString().trim());
        user.setLogin(login.getText().toString().trim());
        user.setPassword(password.getText().toString());

        return user;
    }


    private View validateTextField(EditText view) {
        String value = view.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            view.setError(getString(R.string.error_field_required));
            return view;
        }
        return null;
    }

    private View validateLogin(EditText view) {
        View result = validateTextField(view);
        if (result == null) {
            if (userControl.isLoginExist(view.getText().toString())) {
                view.setError(getString(R.string.error_such_login_exist));
                return view;
            }
            return null;
        } else {
            return result;
        }
    }

    private View validateEmailField(EditText view) {
        String value = view.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            view.setError(getString(R.string.error_field_required));
            return view;
        } else if (!value.contains("@")) {
            view.setError(getString(R.string.error_invalid_email));
            return view;
        }
        if (userControl.isEmailExist(view.getText().toString())) {
            view.setError(getString(R.string.error_such_email_exist));
            return view;
        }
        return null;
    }

    private View validatePasswordField(EditText view) {
        String value = view.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            view.setError(getString(R.string.error_field_required));
            return view;
        }

        if (value.length() < MIN_PASSWORD_LENGTH) {
            view.setError(getString(R.string.error_invalid_password));
            return view;
        }

        return null;
    }

    private View validatePasswordField(EditText pass, EditText repPass) {
        View result = validatePasswordField(pass);

        if(result != null){
            return result;
        }
        result = validatePasswordField(repPass);
        if(result != null){
            return result;
        }

        String valuePass = pass.getText().toString();
        String valueRepPass = repPass.getText().toString();
        if (!valuePass.equals(valueRepPass)) {
            repPass.setError(getString(R.string.error_incorrect_password));
            return repPass;
        }
        return null;
    }


    private boolean validateInputFields() {
        View focusView = null;

        focusView = validatePasswordField(password, repPassword);
        if (focusView != null) {
            focusView.requestFocus();
            return false;
        }

        focusView = validateEmailField(email);
        if (focusView != null) {
            focusView.requestFocus();
            return false;
        }

        focusView = validateLogin(login);
        if (focusView != null) {
            focusView.requestFocus();
            return false;
        }

        focusView = validateTextField(lastName);
        if (focusView != null) {
            focusView.requestFocus();
            return false;
        }

        focusView = validateTextField(firstName);
        if (focusView != null) {
            focusView.requestFocus();
            return false;
        }

        return true;
    }



}
