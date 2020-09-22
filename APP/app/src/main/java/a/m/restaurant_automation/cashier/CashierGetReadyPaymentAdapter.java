package a.m.restaurant_automation.cashier;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.manager.MenuItemAdapter;
import a.m.restaurant_automation.requestModel.ChangePaymentStatusRequest;
import a.m.restaurant_automation.responseModel.ChangePaymentStatusResponse;
import a.m.restaurant_automation.responseModel.GetReadyForPaymentResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.UsersResponseModel;
import a.m.restaurant_automation.service.IDataService;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashierGetReadyPaymentAdapter extends RecyclerView.Adapter<CashierGetReadyPaymentAdapter.ViewHolder> {
    private ArrayList<GetReadyForPaymentResponseModel> getReadyForPaymentResponseModels;
    int size = 0;
    private Context context;
    public View.OnClickListener onItemListener;


    public CashierGetReadyPaymentAdapter(ArrayList<GetReadyForPaymentResponseModel> getReadyPayment,Context context) {
        this.getReadyForPaymentResponseModels=getReadyPayment;
        //this.context=context;
        this.size=getReadyPayment.size();

    }

    @NonNull
    @Override
    public CashierGetReadyPaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cashier_getreadypaymentlist, parent, false);
        this.context=parent.getContext();
        return new CashierGetReadyPaymentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CashierGetReadyPaymentAdapter.ViewHolder holder,final int position) {

        holder.billId.setText("Bill id: "+getReadyForPaymentResponseModels.get(position).getBillId());
        holder.billAmt.setText("Amount: "+getReadyForPaymentResponseModels.get(position).getBillingAmount()+ " $");

        if(getReadyForPaymentResponseModels.get(position).getTableID()==0)
        {
            holder.tableId.setText("Take Away");


        }
        else
        {
            holder.tableId.setText("Table Number: "+getReadyForPaymentResponseModels.get(position).getTableID());

        }

        holder.fName.setText("Name : "+getReadyForPaymentResponseModels.get(position).getFirstName());
        holder.lName.setText(" "+getReadyForPaymentResponseModels.get(position).getLastName());
        if(getReadyForPaymentResponseModels.get(position).getIcCardPayment()) {
            holder.paymentType.setText("Payment Type: "+"Card");
        }
        else
        {
            holder.paymentType.setText("Payment Type: "+"Cash");
        }
        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getReadyForPaymentResponseModels.get(position).getIsPaid()) {
                    showAlert("Change the payment status of the order.", getReadyForPaymentResponseModels.get(position).getBillId());
                }
                else
                {
                    Toast.makeText(context,"Paid",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView billId,billAmt,tableId,fName,lName,paymentType;
        Button btnPay;
        public ViewHolder(View itemView) {
            super(itemView);

            billId = itemView.findViewById(R.id.textView_billIdGRP);
            billAmt=itemView.findViewById(R.id.textView_billingAmtGRP);
            tableId=itemView.findViewById(R.id.textView_tableIDGRP);
            fName=itemView.findViewById(R.id.textView_FirstNameGRP);
            lName=itemView.findViewById(R.id.textView_LastNameGRP);
            paymentType=itemView.findViewById(R.id.textView_paymentTypeGRP);
            btnPay=itemView.findViewById(R.id.button_paymentGRP);
            //btnPay.setOnClickListener(onItemListener);
        }
    }
    public void changePaymentStatus(int billId) {
        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        ChangePaymentStatusRequest changePaymentStatusRequest =  new ChangePaymentStatusRequest();
        changePaymentStatusRequest.billId=billId;


        Call<ResponseModel<ChangePaymentStatusResponse>> call = dataService.changePaymentStatus(changePaymentStatusRequest);
        call.enqueue(new Callback<ResponseModel<ChangePaymentStatusResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<ChangePaymentStatusResponse>> call, Response<ResponseModel<ChangePaymentStatusResponse>> response) {
                ResponseModel<ChangePaymentStatusResponse> responseModel = response.body();
                if (responseModel != null) {
                    if (responseModel.getError() != null) {
                        Toast.makeText(context, responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context.getApplicationContext(),responseModel.getData().getStatusMessage(), Toast.LENGTH_LONG).show();

                        notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ChangePaymentStatusResponse>> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlert(String message, final int billId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Change Payment Status!!!");
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changePaymentStatus(billId);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
