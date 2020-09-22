package a.m.restaurant_automation.manager;

import android.content.Context;
import android.os.Bundle;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RegisterFragment;
import a.m.restaurant_automation.model.AppStaticData;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AddEmployeeFragment extends Fragment {
    EditText editText_employeeFirstName, editText_employeeLastName, editText_employeeEmail, editText_password;
    Spinner spinner_employees;
    Button button_addEmployee;
    OnEmployeeRegisterPress onEmployeeRegisterPress;


    public AddEmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText_employeeFirstName = view.findViewById(R.id.editText_employeeFirstName);
        editText_employeeLastName = view.findViewById(R.id.editText_employeeLastName);
        editText_employeeEmail = view.findViewById(R.id.editText_employeeEmail);
        editText_password = view.findViewById(R.id.editText_createPassword);
        spinner_employees = view.findViewById(R.id.spinner_employees);
        button_addEmployee = view.findViewById(R.id.button_addEmployee);

        List<String> selectEmployee= new ArrayList<>();
        Set<String> set = AppStaticData.employeeType.keySet();
        selectEmployee.add(0,"Select Employee");
        selectEmployee.addAll(set);

        ArrayAdapter<String> employeeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,selectEmployee);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_employees.setAdapter(employeeAdapter);

        button_addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinnerItemId = spinner_employees.getSelectedItem().toString();
                int UserTypeid =0;

                if (!checkEmptyFields()) {
                    if (!checkPassword()) {
                        if (AppStaticData.employeeType.containsKey(spinnerItemId)){
                            UserTypeid = AppStaticData.employeeType.get(spinnerItemId);
                            String firstName = editText_employeeFirstName.getText().toString();
                            String lastName = editText_employeeLastName.getText().toString();
                            String email = editText_employeeEmail.getText().toString();
                            String password = editText_password.getText().toString();
                            onEmployeeRegisterPress.OnEmployeeRegisterPress(firstName, lastName, email, password,UserTypeid);
                            Navigation.findNavController(v).navigate(R.id.managerDashboardFragment);

                        }
                        else {
                                Snackbar.make(v,"Please select category", BaseTransientBottomBar.LENGTH_LONG).show();
                        }

                    }
                }
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_employee, container, false);
    }

    public boolean checkEmptyFields(){
        if (TextUtils.isEmpty(editText_employeeFirstName.getText().toString())){
            editText_employeeFirstName.setError("First Name Cannot be Empty!");
            editText_employeeFirstName.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(editText_employeeLastName.getText().toString())){
            editText_employeeLastName.setError("Last Name Cannot be Empty!");
            editText_employeeLastName.requestFocus();
            return true;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(editText_employeeEmail.getText().toString()).matches()){
            editText_employeeEmail.setError("Invalid Email Addresss!");
            editText_employeeEmail.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(editText_password.getText().toString())){
            editText_password.setError("Password Cannot be Empty!");
            editText_password.requestFocus();
            return true;
        }
        return false;
    }

    public boolean checkPassword (){
        if (editText_password.getText().length() <6){
            editText_password.setError("Password Cannot be less than 6 Characters.");
            editText_password.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onEmployeeRegisterPress = (AddEmployeeFragment.OnEmployeeRegisterPress) context;
    }



    public interface OnEmployeeRegisterPress {
        void OnEmployeeRegisterPress(String firstName,String lastName, String email, String password, int userType);
    }


}
