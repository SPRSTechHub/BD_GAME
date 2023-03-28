package com.kidogame.android.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kidogame.android.R;
import com.kidogame.android.activity.SubGameActivity;
import com.kidogame.android.databinding.RowMainGameBinding;
import com.kidogame.android.model.game_category.GameCategory;
import java.util.List;

public class MainGameAdapter extends RecyclerView.Adapter<MainGameAdapter.ViewHolder> {
    public List<GameCategory> mList;
    String ApiUrl = "https://control.bdfatafat.live/";

    public MainGameAdapter(List<GameCategory> list) {
        this.mList = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((RowMainGameBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_main_game, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        GameCategory result = this.mList.get(position);
        holder.itemView.getRoot().setOnClickListener(view -> {
            Intent i = new Intent(holder.itemView.getRoot().getContext(), SubGameActivity.class);
            i.putExtra("id", result.getCatId());
            holder.itemView.getRoot().getContext().startActivity(i);
        });
        holder.itemView.tvGameName.setText(result.getCatTitle());
        holder.itemView.tvGameName2.setText(result.getCatTitle());

        Glide.with(holder.itemView.getRoot())
                .load(ApiUrl+"uploads/cat_img/" +result.getCat_iurl())
                .fitCenter()
                .into(holder.itemView.ivGame);
    }

    public int getItemCount() {
        List<GameCategory> list = this.mList;
        if (list != null) {
            return list.size();
        }
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public final RowMainGameBinding itemView;

        private ViewHolder(RowMainGameBinding itemView2) {
            super(itemView2.getRoot());
            this.itemView = itemView2;
        }
    }
}
