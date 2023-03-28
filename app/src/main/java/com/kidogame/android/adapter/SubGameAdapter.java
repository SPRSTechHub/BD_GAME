package com.kidogame.android.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kidogame.android.R;
import com.kidogame.android.activity.BetSelectionActivity;
import com.kidogame.android.databinding.RowSubGameBinding;
import com.kidogame.android.model.game_list.GameList;
import java.util.List;

public class SubGameAdapter extends RecyclerView.Adapter<SubGameAdapter.ViewHolder> {
    public List<GameList> mList;

    public SubGameAdapter(List<GameList> results) {
        this.mList = results;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((RowSubGameBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_sub_game, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        GameList result = this.mList.get(position);
        holder.itemView.getRoot().setOnClickListener(view -> {
            if (position == this.mList.size() - 1) {
                Intent i = new Intent(holder.itemView.getRoot().getContext(), BetSelectionActivity.class);
                i.putExtra("id", result.getMatchId());
                i.putExtra("id2", "");
                i.putExtra("time",result.getMatchTime());
                holder.itemView.getRoot().getContext().startActivity(i);
                return;
            }
            Intent i2 = new Intent(holder.itemView.getRoot().getContext(), BetSelectionActivity.class);
            i2.putExtra("id", result.getMatchId());
            i2.putExtra("id2", this.mList.get(position + 1).getMatchId());
            i2.putExtra("time",result.getMatchTime());
            holder.itemView.getRoot().getContext().startActivity(i2);
        });

        if (result.getLive().equals("yes")) {
            holder.itemView.getRoot().setAlpha(1);
            holder.itemView.getRoot().setEnabled(true);
        }else{
            holder.itemView.getRoot().setAlpha(0.5f);
            holder.itemView.getRoot().setEnabled(false);
        }

        Glide.with(holder.itemView.getRoot())
                .load(result.getMatch_icon())
                .fitCenter()
                .into(holder.itemView.ivGame);
        holder.itemView.tvGameName.setText(result.getGameTitle());
        holder.itemView.tvGameTime.setText(result.getMatchTime());
    }


    public int getItemCount() {
        List<GameList> list = this.mList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public final RowSubGameBinding itemView;

        private ViewHolder(RowSubGameBinding itemView2) {
            super(itemView2.getRoot());
            this.itemView = itemView2;
        }
    }
}
