package a.m.restaurant_automation.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import a.m.restaurant_automation.CustomerActivity;
import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.requestModel.EditProfileRequestModel;
import a.m.restaurant_automation.responseModel.EditProfileReponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.UsersResponseModel;
import a.m.restaurant_automation.service.IDataService;
import a.m.restaurant_automation.service.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment implements View.OnClickListener {
    TextView userTV, emailTV, firstNameTV, lastNameTV, phoneTV, genderTV;
    UserSession session;
    String username, userEmail, userFirstName, userLastName, userPhone, userGender;
    Button updateButton;
    UsersResponseModel usersResponseModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.BottomnavigateMenuCustomer);
        bottomNavigationView.setVisibility(View.GONE);
        super.onViewCreated(view, savedInstanceState);
        session = UserSession.getInstance();
        phoneTV = view.findViewById(R.id.phoneNumberTV);
        genderTV = view.findViewById(R.id.genderUserTV);
        userTV = view.findViewById(R.id.textview_user);
        updateButton = view.findViewById(R.id.buttonUpdateProfile);
        updateButton.setVisibility(View.GONE);
        emailTV = view.findViewById(R.id.emailIdUser);
        firstNameTV = view.findViewById(R.id.fisrtnameuser);
        lastNameTV = view.findViewById(R.id.lastnameuser);

        userFirstName = session.getFirstName();
        userLastName = session.getLastName();
        userEmail = session.getEmail();
        username = userFirstName + " " + userLastName;
        userPhone = session.getPhoneNumber();
        userGender = session.getGender();

        userTV.setText(username);
        emailTV.setText(userEmail);
        firstNameTV.setText(userFirstName);
        lastNameTV.setText(userLastName);
        phoneTV.setText(userPhone);
        genderTV.setText(userGender);

        firstNameTV.setOnClickListener(this);
        lastNameTV.setOnClickListener(this);
        emailTV.setOnClickListener(this);
        phoneTV.setOnClickListener(this);
        genderTV.setOnClickListener(this);
        updateButton.setOnClickListener(this);
    }

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phoneNumberTV:
                showAlert("Please enter new phone number.", "Phone Number Update", 1, "5148523654");
                break;
            case R.id.genderUserTV:
                showAlert("Please select your gender.", "Gender Update", 2, "M or F");
                break;
            case R.id.emailIdUser:
                showAlert("Please enter new email.", "Email Update", 3, "xyz@gmail.com");
                break;
            case R.id.fisrtnameuser:
                showAlert("Please enter first name.", "First Name Update", 4, "John");
                break;
            case R.id.lastnameuser:
                showAlert("Please enter last name.", "Last Name Update", 5, "Smith");
                break;
            case R.id.buttonUpdateProfile: IUserService iUserService = RetrofitClient.getRetrofitInstance().create(IUserService.class);
                EditProfileRequestModel requestModel = new EditProfileRequestModel();
                requestModel.email = emailTV.getText().toString();
                requestModel.firstName = firstNameTV.getText().toString();
                requestModel.lastName = lastNameTV.getText().toString();
                requestModel.gender = genderTV.getText().toString();
                requestModel.phone = phoneTV.getText().toString();
                requestModel.userId = Integer.parseInt(session.getUserId());
            Call<ResponseModel<EditProfileReponseModel>> call = iUserService.updateProfile(requestModel);
            call.enqueue(new Callback<ResponseModel<EditProfileReponseModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<EditProfileReponseModel>> call, Response<ResponseModel<EditProfileReponseModel>> response) {
                    ResponseModel<EditProfileReponseModel> reponseModel = response.body();
                    if(reponseModel.getError() != null){
                        Toast.makeText(getActivity().getApplicationContext(), ""+reponseModel.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        session.setGender(reponseModel.getData().gender);
                        session.setPhoneNumber(reponseModel.getData().phone);
                        session.setFirstName(reponseModel.getData().firstName);
                        session.setLastName(reponseModel.getData().lastName);
                        session.setEmail(reponseModel.getData().email);
                        userTV.setText(reponseModel.getData().firstName+" "+reponseModel.getData().lastName);
                        Toast.makeText(getActivity().getApplicationContext(), "Succesully Updated", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<EditProfileReponseModel>> call, Throwable t) {

                }
            });
                break;
        }
    }

    @SuppressLint("ResourceType")
    public void showAlert(String message, String title, final int id, String hint) {
        final EditText editText = new EditText(getActivity());
        editText.setHint(hint);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        if (id == 1) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(editText);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(editText);
        }

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (id == 1) {
                    String number = editText.getText().toString();
                    if (number.length() == 10) {
                        phoneTV.setText(number);
                        updateButton.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Number must be 10 digit long!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                if (id == 2) {
                    String gender = editText.getText().toString();
                    if(gender.length() == 1) {
                        if (gender.equalsIgnoreCase("M")) {
                            gender = "Male";
                            genderTV.setText(gender);
                            updateButton.setVisibility(View.VISIBLE);

                        } else if (gender.equalsIgnoreCase("F")) {
                            gender = "Female";
                            genderTV.setText(gender);
                            updateButton.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Invalid Gender!!!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "Enter valid Gender!!", Toast.LENGTH_SHORT).show();
                    }
                }
                String text = editText.getText().toString();

                if (id == 3) {
                    if (Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                        emailTV.setText(text);
                        updateButton.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid email!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                if (id == 4) {
                    String fName = editText.getText().toString();
                    if (fName.length() != 0) {
                        firstNameTV.setText(fName);
                        updateButton.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter first name!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                if (id == 5) {
                    String lname = editText.getText().toString();
                    if (lname.length() != 0) {
                        lastNameTV.setText(lname);
                        updateButton.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter last name!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

}
