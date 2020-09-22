package a.m.restaurant_automation.manager;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.model.AppStaticData;
import a.m.restaurant_automation.requestModel.AddMenuItemRequestModel;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IUserService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddMenuItem extends AppCompatActivity implements View.OnClickListener {
    EditText editText_itemName, editText_itemPrice, editText_itemDescription, editText_addQuantity, editText_itemImage;
    CircleImageView circleImageView;
    Spinner spinner_category;
    Button button_addItem;
    private int mUserType = AppStaticData.USERTYPE_MANAGER;
    String imgDecodableString;
    boolean imageUploaded = false;
    ConstraintLayout progressWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        editText_itemName = findViewById(R.id.editText_itemName);
        editText_itemPrice = findViewById(R.id.editText_itemPrice);
        editText_itemDescription = findViewById(R.id.editText_itemDescription);
        editText_addQuantity = findViewById(R.id.editText_addQuantity);
        editText_itemImage = findViewById(R.id.editText_itemImage);
        spinner_category = findViewById(R.id.spinner_category);
        button_addItem = findViewById(R.id.button_addItem);
        progressWindow = findViewById(R.id.loadingLayout);

        List<String> selectCategory = new ArrayList<>();
        Set<String> set = AppStaticData.categories.keySet();
        selectCategory.add(0, "Select Category");
        selectCategory.addAll(set);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(AddMenuItem.this, android.R.layout.simple_spinner_item, selectCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(categoryAdapter);
        // spinner_category.setOnItemClickListener(this);
        button_addItem.setOnClickListener(this);

        editText_itemImage.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_addItem:
                String spinnerItemId = spinner_category.getSelectedItem().toString();
                int id = 0;
                if (!checkEmptyFields()) {
                    if (AppStaticData.categories.containsKey(spinnerItemId)) {
                        id = AppStaticData.categories.get(spinnerItemId);
                        String itemName = editText_itemName.getText().toString();
                        double itemPrice = Double.parseDouble(editText_itemPrice.getText().toString());
                        String itemDescription = editText_itemDescription.getText().toString();
                        int itemQuantity = Integer.parseInt(editText_addQuantity.getText().toString());
                        progressWindow.setVisibility(View.VISIBLE);
                        uploadImage(imgDecodableString, itemName, itemPrice, itemDescription, itemQuantity, id);
                    }
                } else {
                    Snackbar.make(v, "Please select category", BaseTransientBottomBar.LENGTH_LONG).show();
                }
                break;
            case R.id.editText_itemImage:
                if (isReadStoragePermissionGranted()) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    // Sets the type as image/*. This ensures only components of type image are selected
                    intent.setType("image/*");
                    //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    // Launching the Intent
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(this, "Please give Storage permission to upload image!!!", Toast.LENGTH_SHORT).show();
                }

        }

    }

    public boolean checkEmptyFields() {
        if (TextUtils.isEmpty(editText_itemName.getText().toString())) {
            editText_itemName.setError("Item Name Cannot be Empty!");
            editText_itemName.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(editText_itemPrice.getText().toString())) {
            editText_itemPrice.setError("Item Price Cannot be Empty!");
            editText_itemPrice.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(editText_itemDescription.getText().toString())) {
            editText_itemDescription.setError("Item Description cannot be Empty!");
            editText_itemDescription.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(editText_addQuantity.getText().toString())) {
            editText_addQuantity.setError("Item Quantity Cannot be Empty!");
            editText_addQuantity.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(editText_itemImage.getText().toString())) {
            editText_itemImage.setError("Please Select a Image!");
            editText_itemImage.requestFocus();
            return true;
        }
        return false;
    }

    public void AddMenuItem(String menuItemName, double menuItemPrice, String menuItemDescription, int menuItemQuantity, int categoryId, int createdBy) {
        IUserService userService = RetrofitClient.getRetrofitInstance().create(IUserService.class);
        AddMenuItemRequestModel addMenuItemRequestModel = new AddMenuItemRequestModel();
        addMenuItemRequestModel.menuItemName = menuItemName;
        addMenuItemRequestModel.price = menuItemPrice;
        addMenuItemRequestModel.menuItemDescription = menuItemDescription;
        addMenuItemRequestModel.availablequantity = menuItemQuantity;
        addMenuItemRequestModel.categoryId = categoryId;
        addMenuItemRequestModel.createdBy = createdBy;
        addMenuItemRequestModel.itemImage = "https://elasticbeanstalk-us-west-2-458214277381.s3-us-west-2.amazonaws.com/Menu+Items/"+menuItemName+".jpg";
        Call<ResponseModel<MenuItemResponse>> call = userService.addMenuItem(addMenuItemRequestModel);

        call.enqueue(new Callback<ResponseModel<MenuItemResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<MenuItemResponse>> call, Response<ResponseModel<MenuItemResponse>> response) {
                ResponseModel<MenuItemResponse> responseModel = response.body();
                progressWindow.setVisibility(View.GONE);
                if (responseModel != null && responseModel.getError() != null && !responseModel.getError().equals("")) {
                    Toast.makeText(getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getApplicationContext(),  responseModel.getData().getMenuItemId().toString(),Toast.LENGTH_LONG).show();
                    Intent intentToMain = new Intent(AddMenuItem.this, EmployeeMenuItemActivity.class);
                    startActivity(intentToMain);
                    finish();

                }

            }

            @Override
            public void onFailure(Call<ResponseModel<MenuItemResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong not able to add item" + t.getMessage(), Toast.LENGTH_LONG).show();
                progressWindow.setVisibility(View.GONE);
            }
        });


    }


    public void uploadImage(final String image, final String fileName, final double itemPrice, final String itemDescription, final int itemQuantity, final int spinId) {
        imageUploaded = false;
        File file = new File(image);
        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAWVL5TAECZHQXLUM2", "ZF9UDWpkk50MwUsa9MviOT0xJiNvm0J+h0U9AyWw");
        AmazonS3 s3 = new AmazonS3Client(awsCredentials);
        TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());
        final TransferObserver observer = transferUtility.upload(
                "elasticbeanstalk-us-west-2-458214277381",  //this is the bucket name on S3
                "Menu Items/"+fileName+".jpg", //this is the path and name
                file, //path to the file locally
                CannedAccessControlList.PublicRead //to make the file public
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.equals(TransferState.COMPLETED)) {
                    imageUploaded = true;
                    AddMenuItem(fileName, itemPrice, itemDescription, itemQuantity, spinId, mUserType);
                    Toast.makeText(AddMenuItem.this, "Image uploaded succesfully", Toast.LENGTH_SHORT).show();
                } else if (state.equals(TransferState.FAILED)) {
                    Toast.makeText(AddMenuItem.this, "Image Upload Failed!!!", Toast.LENGTH_SHORT).show();
                    progressWindow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(AddMenuItem.this, "Failed: " + ex.getMessage()+id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 1:
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    String[] fileExtension = {MediaStore.Images.Media.MIME_TYPE};
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    //imageView.setImageURI(selectedImage);
                    editText_itemImage.setText(imgDecodableString);
            }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted1");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted1");
            return true;
        }
    }

}
