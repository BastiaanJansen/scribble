package dev.basjansen.scribble;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.basjansen.scribble.models.Drawing;

public class DrawingsAdapter extends RecyclerView.Adapter<DrawingsAdapter.ViewHolder> {

    private Drawing[] drawings;
    private final LayoutInflater layoutInflater;

    public DrawingsAdapter(Context context, Drawing[] drawings) {
        this.drawings = drawings;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public DrawingsAdapter(Context context) {
        this(context, new Drawing[]{});
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.drawing_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(drawings[position].getName());
    }

    @Override
    public int getItemCount() {
        return drawings.length;
    }

    public void setDrawings(Drawing[] drawings) {
        this.drawings = drawings;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.drawing_name);
        }

        @Override
        public void onClick(View v) {
            Log.d("sd", "Clicked");
        }
    }
}
