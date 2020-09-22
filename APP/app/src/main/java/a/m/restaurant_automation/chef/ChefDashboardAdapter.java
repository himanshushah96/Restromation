package a.m.restaurant_automation.chef;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.requestModel.OrderStatusUpdateRequest;
import a.m.restaurant_automation.responseModel.GetOrderResponseModel;
import a.m.restaurant_automation.responseModel.OrderStatusUpdateResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChefDashboardAdapter extends RecyclerView.Adapter<ChefDashboardAdapter.ViewHolder> {
    ArrayList<GetOrderResponseModel> orders;
    Context context;

    public ChefDashboardAdapter(ArrayList<GetOrderResponseModel> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chef_dashboardorders, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(orders.get(position).tableId == null){
            holder.tableIdTV.setText("XX");
        }
        else
        holder.tableIdTV.setText("" + orders.get(position).tableId);
        holder.orderIdTV.setText("" + orders.get(position).orderId);
        holder.personNameTV.setText(orders.get(position).firstName + " " + orders.get(position).lastName);

        ChefDashBoardItemsAdapter itemsAdapter = new ChefDashBoardItemsAdapter(orders.get(position).menuItems);
        holder.recyclerViewItems.setAdapter(itemsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerViewItems.setLayoutManager(layoutManager);


        if (orders.get(position).orderStatusTitle.equalsIgnoreCase("Ordered")) {
            holder.changeStatusButton.setText("ACCEPT");
        } else if (orders.get(position).orderStatusTitle.equalsIgnoreCase("Inprogress")) {
            holder.changeStatusButton.setText("DONE");
        }


        holder.changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orders.get(position).orderStatusTitle.equalsIgnoreCase("Ordered")) {
                    showAlert("Do you want to accept this order?", orders.get(position).orderId, 2, "Order Accepted ", position);
                } else {
                    showAlert("Is Order prepared?", orders.get(position).orderId, 3, "Order prepared ", position);
                }
            }
        });

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
        TextView tableIdTV, orderIdTV, personNameTV;
        RecyclerView recyclerViewItems;
        Button expandCollapseBtn, changeStatusButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableIdTV = itemView.findViewById(R.id.textView_tableNumberChef);
            orderIdTV = itemView.findViewById(R.id.textView_orderNumberChef);
            personNameTV = itemView.findViewById(R.id.txtpersonNameChef);
            recyclerViewItems = itemView.findViewById(R.id.recyclerView_chefOrder);
            expandCollapseBtn = itemView.findViewById(R.id.btnArrowDown);
            changeStatusButton = itemView.findViewById(R.id.buttonStatusChange);
            cardView = itemView.findViewById(R.id.cardViewOrderChef);
        }
    }

    public void callUpdateRequest(int orderId, final int statusNext, final String statusMessage, final int position) {
        OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest();
        orderStatusUpdateRequest.orderId = orderId;
        orderStatusUpdateRequest.orderStatus = statusNext;
        IDataService iDataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        Call<ResponseModel<OrderStatusUpdateResponse>> call = iDataService.updateOrderStatus(orderStatusUpdateRequest);
        call.enqueue(new Callback<ResponseModel<OrderStatusUpdateResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<OrderStatusUpdateResponse>> call, Response<ResponseModel<OrderStatusUpdateResponse>> response) {
                ResponseModel<OrderStatusUpdateResponse> responseModel = response.body();
                if (responseModel.getError() != null) {
                    Toast.makeText(context, "" + responseModel.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (responseModel.getData().StatusCode.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + statusMessage, Toast.LENGTH_SHORT).show();
                        if(statusNext == 2){
                            orders.get(position).orderStatusTitle = "Inprogress";
                        }else if(statusNext == 3 || statusNext == 4){
                            orders.remove(position);
                        }
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "" + responseModel.getData().StatusMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<OrderStatusUpdateResponse>> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlert(String message, final int orderId, final int statusNext, final String statusMessage, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Change Order Status!!!");
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callUpdateRequest(orderId, statusNext, statusMessage, position);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
