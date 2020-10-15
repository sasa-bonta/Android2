package com.example.organizer.SQL;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizer.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Event> eventList;
    private Activity context;
    private AppDatabase database;

    public MainAdapter(Activity context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Event event = eventList.get(position);
        database = AppDatabase.getInstance(context);
        holder.dateView.setText(event.getDescription());
        String dateTime = event.getTime() + "\n" + event.getDate();
        holder.textView.setText(dateTime);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event1 = eventList.get(holder.getAdapterPosition());
                final int sId = event1.getId();
                String sDescription = event1.getDescription();

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.show();

                final EditText editText = dialog.findViewById(R.id.edit_text);
                Button btnUpdate = dialog.findViewById(R.id.bt_update);

                editText.setText(sDescription);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        dialog.dismiss();
                        String uDescription = editText.getText().toString().trim();

                        database.eventDao().update(sId, uDescription);
                        eventList.clear();
                        eventList.addAll(database.eventDao().getAll());
                        notifyDataSetChanged();
                    }
                });

            }
        });

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Event event2 = eventList.get(holder.getAdapterPosition());
                        database.eventDao().delete(event2);
                        int position = holder.getAdapterPosition();
                        eventList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, eventList.size());

                    }
                });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateView;
        TextView textView;
        ImageView btnEdit, btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateView = itemView.findViewById(R.id.date_view);
            textView = itemView.findViewById(R.id.text_view);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}