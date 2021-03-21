package dev.basjansen.scribble;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.basjansen.scribble.models.Drawing;

public class DrawingsAdapter extends RecyclerView.Adapter<DrawingsAdapter.ViewHolder> {

    private final Drawing[] drawings;

    public DrawingsAdapter(Drawing[] drawings) {
        this.drawings = drawings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return drawings.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
