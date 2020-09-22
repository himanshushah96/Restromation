package a.m.restaurant_automation;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class LoginFragment extends Fragment implements View.OnClickListener {


    EditText emailId, password;
    Button toRegisterPage;
    ImageButton signIn;
    OnLoginPress onLoginPress;

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        emailId = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        signIn = view.findViewById(R.id.signin);
        toRegisterPage = view.findViewById(R.id.signupToRegister);
        signIn.setOnClickListener(this);
        toRegisterPage.setOnClickListener(this);



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onLoginPress = (OnLoginPress) context;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
            if (id == R.id.signin) {
                if (TextUtils.isEmpty(emailId.getText().toString())) {
                    emailId.setError(getString(R.string.validation_require_email));
                    emailId.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString()).matches()) {
                    emailId.setError(getString(R.string.validation_pattern_email));
                    emailId.requestFocus();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                password.setError(getString(R.string.validation_require_password));
                password.requestFocus();
            } else if (password.getText().toString().length() < 6) {
                password.setError(getString(R.string.validation_length_password));
                password.requestFocus();

            }

             else {

                String email = emailId.getText().toString();
                String pass = password.getText().toString();
                onLoginPress.OnEmailSet(email);
                onLoginPress.OnPasswordSet(pass);



            }
        } else if (id == R.id.signupToRegister) {
            NavController navController = Navigation.findNavController(getActivity(), R.id.hostFragment);
            navController.navigate(R.id.registerFragment);
        }

    }
}
    interface OnLoginPress {


        void OnEmailSet(String email);

        void OnPasswordSet(String password);
}

