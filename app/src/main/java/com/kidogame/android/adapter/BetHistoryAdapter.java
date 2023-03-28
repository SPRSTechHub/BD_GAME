package com.kidogame.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kidogame.android.R;
import com.kidogame.android.databinding.RowBetHistoryBinding;
import java.util.List;

public class BetHistoryAdapter extends RecyclerView.Adapter<BetHistoryAdapter.ViewHolder> {
    public List<String> mList;

    public void setDataList(List<String> list) {
        this.mList = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((RowBetHistoryBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_bet_history, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        SubBetHistoryAdapter subBetHistoryAdapter = new SubBetHistoryAdapter();
        holder.itemView.rvBet.setLayoutManager(new LinearLayoutManager(holder.itemView.getRoot().getContext()));
        holder.itemView.rvBet.setAdapter(subBetHistoryAdapter);
    }

    public int getItemCount() {
        List<String> list = this.mList;
        if (list != null) {
            return list.size();
        }
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final RowBetHistoryBinding itemView;

        private ViewHolder(RowBetHistoryBinding itemView2) {
            super(itemView2.getRoot());
            this.itemView = itemView2;
        }
    }
}
