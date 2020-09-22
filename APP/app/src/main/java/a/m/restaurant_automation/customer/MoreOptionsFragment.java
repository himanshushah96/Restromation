package a.m.restaurant_automation.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import a.m.restaurant_automation.LoginActivity;
import a.m.restaurant_automation.R;
import a.m.restaurant_automation.repository.UserSession;


public class MoreOptionsFragment extends Fragment implements View.OnClickListener {

    Button logoutCustomer;
   // CardView editProfile,changePassword;
    AlertDialog.Builder alertDialog;
    OnChangePasswordPress onChangePasswordPress;
    TextView editProfile,nameTV, Feedback, changePassword;
    String username;
    UserSession session;
    EditText editText_changePassword;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.BottomnavigateMenuCustomer);
        bottomNavigationView.setVisibility(View.VISIBLE);
        logoutCustomer=view.findViewById(R.id.logoutCustomer);
        logoutCustomer.setOnClickListener(this);
        editProfile=view.findViewById(R.id.EditProfile);
        changePassword = view.findViewById(R.id.Changepassword);
        nameTV=view.findViewById(R.id.textviewName);
        username= session.getInstance().getFirstName();
        Feedback = view.findViewById(R.id.Feedback);
        Feedback.setOnClickListener(this);
        nameTV.setText(username);
        editProfile.setOnClickListener(this);
        changePassword.setOnClickListener(this);
            }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.logoutCustomer)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserSession.getInstance().clearSession();
                            getContext().stopService(new Intent(getContext(), CustomerNotificationService.class));
                            Intent _intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(_intent);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            alertDialog.show();
        }
        else if(id == R.id.EditProfile)
        {
            Navigation.findNavController(v).navigate(R.id.editProfileFragment);
        }

        else if (id == R.id.Changepassword) {
            alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Change Password")
                    .setMessage("Do you want to change your password?");
            editText_changePassword = new EditText(getActivity().getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            editText_changePassword.setLayoutParams(layoutParams);
            editText_changePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            alertDialog.setView(editText_changePassword)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!checkEmptyFields()){
                                if (!checkPassword()){
                                    String newPassword = editText_changePassword.getText().toString();
                                    UserSession session = UserSession.getInstance();
                                    int userId = Integer.parseInt(session.getUserId());
                                    onChangePasswordPress.OnChangePasswordPress(newPassword, userId);
                                }
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(R.drawable.ic_vpn_key_black_24dp)
                    .create();
            alertDialog.show();

        }

        else if(id == R.id.Feedback)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Feedback");
            builder.setMessage("Please write your feedback here");

            final EditText input = new EditText(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);



            // Set up the buttons
            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String feedback = input.getText().toString();
                    if (feedback.length() != 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "Thank you for your feedback :)", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter feedback!!!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    public boolean checkPassword (){
        if (editText_changePassword.getText().length() <6){
            editText_changePassword.setError("Password Cannot be less than 6 Characters.");
            editText_changePassword.requestFocus();
            return true;
        }
        return false;
    }

    public boolean checkEmptyFields() {
      if (TextUtils.isEmpty(editText_changePassword.getText().toString())){
            editText_changePassword.setError("Password Cannot be Empty!");
            editText_changePassword.requestFocus();
            return true;
        }
        return false;
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_options, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onChangePasswordPress= (MoreOptionsFragment.OnChangePasswordPress) context;
    }

    public interface OnChangePasswordPress {
        void  OnChangePasswordPress(String password, int userId);
    }


}

