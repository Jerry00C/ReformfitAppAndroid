package com.example.reformfitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryRecViewAdapter extends RecyclerView.Adapter<PaymentHistoryRecViewAdapter.ViewHolder> {




    private List<PaymentHistoryElement> payment_his_element_list = new ArrayList<>();



    public PaymentHistoryRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentHistoryElement oneElement = payment_his_element_list.get(position);
        TextView titleView = holder.title;
        TextView purchaseDateView = holder.purchase_date;
        TextView amount = holder.amount;

        titleView.setText(oneElement.getTitle());
        purchaseDateView.setText(oneElement.getPurchase_date());
        amount.setText(oneElement.getAmount());


    }

    @Override
    public int getItemCount() {
        return payment_his_element_list.size();
    }

    public void setPayment_his_element_list(List<PaymentHistoryElement> payment_his_element_list) {
        this.payment_his_element_list = payment_his_element_list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title, purchase_date, amount;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            purchase_date = itemView.findViewById(R.id.purchase_date);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
