package dev.basjansen.scribble;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import dev.basjansen.scribble.models.Drawing;

public class DrawingsAdapter extends RecyclerView.Adapter<DrawingsAdapter.ViewHolder> {

    private final Context context;

    private Drawing[] drawings;

    public DrawingsAdapter(Context context, Drawing[] drawings) {
        this.drawings = drawings;
        this.context = context;
    }

    public DrawingsAdapter(Context context) {
        this(context, new Drawing[]{});
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.drawing_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(drawings[position].getName());
        holder.drawing = drawings[position];
    }

    @Override
    public int getItemCount() {
        return drawings.length;
    }

    public void setDrawings(Drawing[] drawings) {
        this.drawings = drawings;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        Drawing drawing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.drawing_name);
            itemView.setOnClickListener(this::onClick);
        }

        public void onClick(View v) {
            Intent intent = new Intent(context, ViewDrawingActivity.class);
            intent.putExtra("drawing", drawing);
            context.startActivity(intent);
        }
    }
}
