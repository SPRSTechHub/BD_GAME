package com.kidogame.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kidogame.android.R;
import com.kidogame.android.databinding.RowWinHistoryBinding;
import com.kidogame.android.model.win_history.Datum;

import java.util.List;
import java.util.Map;

public class WinHistoryAdapter extends RecyclerView.Adapter<WinHistoryAdapter.ViewHolder> {
    public Map<String, List<Datum>> mList;

    public WinHistoryAdapter(Map<String, List<Datum>> data) {
        this.mList = data;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((RowWinHistoryBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_win_history, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        SubWinHistoryAdapter subWinHistoryAdapter = new SubWinHistoryAdapter((List<Datum>) mList.values().toArray()[position]);
        holder.itemView.rvWin.setLayoutManager(new LinearLayoutManager(holder.itemView.getRoot().getContext()));
        holder.itemView.rvWin.setAdapter(subWinHistoryAdapter);
        holder.itemView.tvDate.setText(String.valueOf(mList.keySet().toArray()[position]));
    }

    public int getItemCount() {
        Map<String, List<Datum>> list = this.mList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public final RowWinHistoryBinding itemView;

        private ViewHolder(RowWinHistoryBinding itemView2) {
            super(itemView2.getRoot());
            this.itemView = itemView2;
        }
    }
}
