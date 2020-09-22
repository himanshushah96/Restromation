package a.m.restaurant_automation.customer;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.requestModel.ReserveTableRequest;
import a.m.restaurant_automation.responseModel.CustomerReserveTableResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.StatusCheckResponse;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerTablesAdapter extends RecyclerView.Adapter<CustomerTablesAdapter.ViewHolder> {
    ArrayList<CustomerReserveTableResponse> tables;
    Context context;
    int numberOfPeople;
    UserSession session;
    public CustomerTablesAdapter(ArrayList<CustomerReserveTableResponse> tables, int numberOfPeople) {
        this.tables = tables;
        this.numberOfPeople = numberOfPeople;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reserviewtable, parent, false);
        context = parent.getContext();
        session = UserSession.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(tables.size() == 0){
            holder.tableIdTextView.setText("Currently No Table is Available, Please try again in Some Time!!! Sorry for the inconvenience caused");
            holder.bookButton.setVisibility(View.GONE);
            holder.capacityTextView.setVisibility(View.GONE);
        }
        else{
            holder.tableIdTextView.setText("Table Number: "+tables.get(position).tableId);
            holder.capacityTextView.setText("Capacity: "+tables.get(position).capacity);
        }
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Book Now")
                        .setMessage("Do you want to book Table "+tables.get(position).tableId+"?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Calendar calendar = Calendar.getInstance();
                                Date date= calendar.getTime();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                ReserveTableRequest reserveTableRequest = new ReserveTableRequest();
                                reserveTableRequest.tableId = tables.get(position).tableId;
                                reserveTableRequest.numberOfPeople = numberOfPeople;
                                reserveTableRequest.endTime = null;
                                reserveTableRequest.reservationDate = dateFormat.format(date);
                                reserveTableRequest.reservedBy =Integer.parseInt(session.getUserId());
                                reserveTableRequest.startTime = dateFormat.format(date);

                                IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
                                Call<ResponseModel<StatusCheckResponse>> call = dataService.reserveTable(reserveTableRequest);
                                call.enqueue(new Callback<ResponseModel<StatusCheckResponse>>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel<StatusCheckResponse>> call, Response<ResponseModel<StatusCheckResponse>> response) {
                                        ResponseModel<StatusCheckResponse> responseModel = response.body();
                                        if(responseModel != null){
                                            if(responseModel.getError() != null){
                                                Toast.makeText(context, "Error: "+responseModel.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                if(responseModel.getData().statusCode.equalsIgnoreCase("1")) {
                                                    Toast.makeText(context, "" + responseModel.getData().statusMessage, Toast.LENGTH_SHORT).show();
                                                    session.setIsTableReserved("Y");
                                                    Navigation.findNavController(view).navigate(R.id.customerMenuItemsFragment);
                                                }
                                                else{
                                                    Toast.makeText(context, "Error: " + responseModel.getData().statusMessage, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel<StatusCheckResponse>> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (tables.size() == 0)
            return 1;
        else
            return tables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tableIdTextView, capacityTextView;
        Button bookButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableIdTextView = itemView.findViewById(R.id.txtTableNumber);
            capacityTextView = itemView.findViewById(R.id.txtTablePeopleNumber);
            bookButton = itemView.findViewById(R.id.btnBookTable);
        }
    }
}
