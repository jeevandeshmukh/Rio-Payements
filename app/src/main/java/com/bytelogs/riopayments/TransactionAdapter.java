package com.bytelogs.riopayments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter  extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder>{

    List<TransactionModel> transactionModels;
    Context context;

    public TransactionAdapter(List<TransactionModel> transactionModels, Context context) {
        this.transactionModels = transactionModels;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_transaction,parent,false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        TransactionModel transactionModel = transactionModels.get(position);
        holder.status.setText(transactionModel.getTransationStatus());
        holder.amount.setText(transactionModel.getAmount());
        holder.mobile.setText(transactionModel.getMobile());
        holder.email.setText(transactionModel.getEmail());


    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }

    public  class  TransactionHolder extends RecyclerView.ViewHolder{

        TextView mobile,email,amount,status;

        public TransactionHolder(View itemView) {
            super(itemView);
            mobile = itemView.findViewById(R.id.mobile);
            email = itemView.findViewById(R.id.email);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
        }
    }
}
