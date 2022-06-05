package ru.bsu.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.bsu.application.R;
import ru.bsu.application.dto.RecyclerItemPoster;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder>{

    private List<RecyclerItemPoster> itemList;
    private Context mContext;

    public PosterAdapter(List<RecyclerItemPoster> itemList, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_poster,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final RecyclerItemPoster item = itemList.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtTime.setText(item.getTime());
        holder.txtCost.setText(item.getCost());
        holder.txtLocation.setText(item.getLocation());
        holder.txtReason.setText(item.getReason());
        holder.txtDate.setText(String.valueOf(item.getDate()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtTime;
        public TextView txtCost;
        public TextView txtLocation;
        public TextView txtReason;
        public TextView txtDate;

        public ViewHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.textViewNamePoster);
            txtTime = view.findViewById(R.id.textViewTime);
            txtCost = view.findViewById(R.id.textViewCost);
            txtLocation = view.findViewById(R.id.textViewLocation);
            txtReason = view.findViewById(R.id.textViewReason);
            txtDate = view.findViewById(R.id.textViewDate);
        }
    }
}
