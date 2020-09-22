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

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.requestModel.ChangePaymentStatusRequest;
import a.m.restaurant_automation.responseModel.ChangePaymentStatusResponse;
import a.m.restaurant_automation.responseModel.GetOrderResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.StatusCheckResponse;
import a.m.restaurant_automation.service.IDataService;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashierDashboardAdapter extends RecyclerView.Adapter<CashierDashboardAdapter.ViewHolder>{
    public ArrayList<GetOrderResponseModel> orders;
    Context context;

    public CashierDashboardAdapter(ArrayList<GetOrderResponseModel> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public CashierDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cashier_dashboardorder, parent, false);
        this.context = parent.getContext();
        return new CashierDashboardAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CashierDashboardAdapter.ViewHolder holder, final int position) {
        if(orders.get(position).tableId==null) {
            holder.tableIdTV.setText("TakeAway");
        }
        else{
            holder.tableIdTV.setText("Table : " + orders.get(position).tableId);
        }
        holder.orderIdTV.setText("" + orders.get(position).billId);
        holder.personNameTV.setText(orders.get(position).firstName + " " + orders.get(position).lastName);
        holder.priceTV.setText("Amount Paid : "+orders.get(position).billingAmount+ "$");


        CashierDashBoardItemsAdapter itemsAdapter = new CashierDashBoardItemsAdapter(orders.get(position).menuItems);
        holder.recyclerViewItems.setAdapter(itemsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerViewItems.setLayoutManager(layoutManager);




      /*  if (!orders.get(position).isPaid) {

            holder.paymentButton.setText("PAYMENT");
        }


        holder.paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!orders.get(position).isPaid) {
                    showAlert("Change the payment status of the order.", orders.get(position).billId);
                }
            }
        });*/

        holder.expandCollapseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams layoutParams = holder.recyclerViewItems.getLayoutParams();
                if (layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    holder.recyclerViewItems.setLayoutParams(layoutParams);
                    holder.expandCollapseBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    layoutParams.height = 90;
                    holder.recyclerViewItems.setLayoutParams(layoutParams);
                    holder.expandCollapseBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tableIdTV, orderIdTV, personNameTV,priceTV;
        RecyclerView recyclerViewItems;
        Button expandCollapseBtn , paymentButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableIdTV = itemView.findViewById(R.id.txtTableNumberCashier);
            orderIdTV = itemView.findViewById(R.id.txtOrderNumberCashier);
            personNameTV = itemView.findViewById(R.id.txtpersonNameCashier);
            recyclerViewItems = itemView.findViewById(R.id.recyclerViewCashierScreenOrder);
            expandCollapseBtn = itemView.findViewById(R.id.btnArrowDownCashier);
            //paymentButton = itemView.findViewById(R.id.btnPay);
            priceTV=itemView.findViewById(R.id.textview_pricecashier);
            cardView = itemView.findViewById(R.id.cardview_cashiercard);
        }
    }

   /* public void changePaymentStatus(int billId) {
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
    }*/

 /*   public void showAlert(String message, final int billId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Change Order Status!!!");
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changePaymentStatus(billId);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }*/
}
