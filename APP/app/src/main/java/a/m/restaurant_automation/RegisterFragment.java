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

import a.m.restaurant_automation.model.AppStaticData;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    EditText firstNameForRegister, emailForRegister, passwordForRegister,lastNameForRegister;
    ImageButton signUp;
    Button toLoginPage;
    OnRegisterPress onRegisterPress;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstNameForRegister = view.findViewById(R.id.firstName);
        lastNameForRegister= view.findViewById(R.id.lastName);
        emailForRegister = view.findViewById(R.id.emailRegister);
        passwordForRegister = view.findViewById(R.id.passwordRegister);
        signUp = view.findViewById(R.id.signup);
        toLoginPage = view.findViewById(R.id.signinToLoginFragment);


        toLoginPage.setOnClickListener(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEmptyFields()){
                            if (!checkPassword()){
                                String firstName = firstNameForRegister.getText().toString();
                                String lastName = lastNameForRegister.getText().toString();
                                String email = emailForRegister.getText().toString();
                                String password = passwordForRegister.getText().toString();
                                onRegisterPress.OnRegisterPress(firstName,lastName,email,password, AppStaticData.USERTYPE_CUSTOMER);
                                NavController navController = Navigation.findNavController(getActivity(), R.id.hostFragment);
                                navController.navigate(R.id.loginFragment);

                    }
                }

            }
        });



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public boolean checkEmptyFields(){
        if (TextUtils.isEmpty(firstNameForRegister.getText().toString())){
            firstNameForRegister.setError("First Name Cannot be Empty!");
            firstNameForRegister.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(lastNameForRegister.getText().toString())){
            lastNameForRegister.setError("Last Name Cannot be Empty!");
            lastNameForRegister.requestFocus();
            return true;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailForRegister.getText().toString()).matches()){
            emailForRegister.setError("Invalid Email Addresss!");
            emailForRegister.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(passwordForRegister.getText().toString())){
            passwordForRegister.setError("Password Cannot be Empty!");
            passwordForRegister.requestFocus();
            return true;
        }
        return false;
    }

    public boolean checkPassword (){
        if (passwordForRegister.getText().length() <6){
            passwordForRegister.setError("Password Cannot be less than 6 Characters.");
            passwordForRegister.requestFocus();
            return true;
        }
        return false;
    }


    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.signinToLoginFragment)
        {
            NavController navController = Navigation.findNavController(getActivity(), R.id.hostFragment);
            navController.navigate(R.id.loginFragment);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onRegisterPress = (OnRegisterPress) context;
    }

    interface OnRegisterPress {
        void OnRegisterPress(String firstName,String lastName, String email, String password, int userType);
    }
}
