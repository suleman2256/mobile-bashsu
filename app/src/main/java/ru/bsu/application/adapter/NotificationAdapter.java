package ru.bsu.application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.bsu.application.R;
import ru.bsu.application.dto.RecyclerItemNotification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<RecyclerItemNotification> itemList;
    private Context mContext;

    public NotificationAdapter(List<RecyclerItemNotification> itemList, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification,
                parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final RecyclerItemNotification item = itemList.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtDescription.setText(item.getDescription());
        holder.visible.setVisibility(item.getVisible());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;
        public ImageView visible;

        public ViewHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.textViewTitle);
            txtDescription = view.findViewById(R.id.textViewDescription);
            visible = view.findViewById(R.id.imageViewVisible);
        }
    }
}
