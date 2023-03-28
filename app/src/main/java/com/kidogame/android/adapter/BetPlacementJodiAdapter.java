package com.kidogame.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.kidogame.android.R;
import com.kidogame.android.databinding.RowBetPlacementJodiBinding;
import com.kidogame.android.model.betPlacement.BetPlacementJodiRequest;
import java.util.List;

public class BetPlacementJodiAdapter extends RecyclerView.Adapter<BetPlacementJodiAdapter.ViewHolder> {
    public List<BetPlacementJodiRequest> mList;

    public BetPlacementJodiAdapter(List<BetPlacementJodiRequest> list) {
        this.mList = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((RowBetPlacementJodiBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_bet_placement_jodi, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        BetPlacementJodiRequest betPlacementRequest = this.mList.get(position);
        holder.itemView.tvDigit.setText(betPlacementRequest.getDigit1());
        holder.itemView.tvDigit2.setText(betPlacementRequest.getDigit2());
        holder.itemView.tvAmount.setText(betPlacementRequest.getAmount());
        holder.itemView.btnDelete.setOnClickListener(view -> {
            this.mList.remove(position);
            notifyItemRemoved(position);
        });
    }

    public int getItemCount() {
        List<BetPlacementJodiRequest> list = this.mList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public final RowBetPlacementJodiBinding itemView;

        private ViewHolder(RowBetPlacementJodiBinding itemView2) {
            super(itemView2.getRoot());
            this.itemView = itemView2;
        }
    }
}
