package com.kidogame.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.kidogame.android.R;
import com.kidogame.android.databinding.RowWinHistorySublistBinding;
import com.kidogame.android.model.win_history.Datum;

import java.util.List;

public class SubWinHistoryAdapter extends RecyclerView.Adapter<SubWinHistoryAdapter.ViewHolder> {
    public List<Datum> mList;

    public SubWinHistoryAdapter(List<Datum> data) {
        this.mList = data;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((RowWinHistorySublistBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_win_history_sublist, parent, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Datum datum = mList.get(position);
        holder.itemView.tvAmount.setText(datum.getAmount());
        holder.itemView.tvDigit.setText(datum.getDigit());
        holder.itemView.tvGame.setText(datum.getGame());
        holder.itemView.tvPatti.setText(datum.getPatti());
        holder.itemView.tvTime.setText(datum.getTime());
    }

    public int getItemCount() {
        List<Datum> list = this.mList;
        if (list != null) {
            return list.size();
        }
        return 5;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final RowWinHistorySublistBinding itemView;

        private ViewHolder(RowWinHistorySublistBinding itemView2) {
            super(itemView2.getRoot());
            this.itemView = itemView2;
        }
    }
}
