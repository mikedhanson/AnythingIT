package com.mrhanson.anythingit.Models;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mrhanson.anythingit.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {
    private List<Ticket> ticketsList;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, details, date;

        public MyViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.lbl_title);
            details = view.findViewById(R.id.lbl_details);
            date = view.findViewById(R.id.lbl_date);
        }
    }

    public TicketAdapter(List<Ticket> noteList) {
        this.ticketsList = noteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_list_row, parent, false);
        return  new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ticket ticket = ticketsList.get(position);
        holder.title.setText(ticket.getTitle().toUpperCase());
        holder.details.setText(ticket.getDetails());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String reportDate = df.format(ticket.getDateSaved());
        holder.date.setText(reportDate);
    }

    @Override
    public int getItemCount(){
        return  ticketsList.size();
    }
}
