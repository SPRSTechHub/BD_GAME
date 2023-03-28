package com.kidogame.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.kidogame.android.R;
import com.kidogame.android.databinding.RowBetHistorySublistBinding;
import java.util.List;

public class SubBetHistoryAdapter extends RecyclerView.Adapter<SubBetHistoryAdapter.ViewHolder> {
    public List<String> mList;

    public void setDataList(List<String> list) {
        this.mList = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((RowBetHistorySublistBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_bet_history_sublist, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    public int getItemCount() {
        List<String> list = this.mList;
        if (list != null) {
            return list.size();
        }
        return 5;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final RowBetHistorySublistBinding itemView;

        private ViewHolder(RowBetHistorySublistBinding itemView2) {
            super(itemView2.getRoot());
            this.itemView = itemView2;
        }
    }
}
