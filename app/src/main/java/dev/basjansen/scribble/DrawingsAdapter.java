package dev.basjansen.scribble;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.DrawingService;

public class DrawingsAdapter extends RecyclerView.Adapter<DrawingsAdapter.ViewHolder> {

    private final Context context;

    private Drawing[] drawings;
    private final DrawingService drawingService;

    public DrawingsAdapter(Context context, Drawing[] drawings) {
        this.drawings = drawings;
        this.context = context;
        this.drawingService = new DrawingService();
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
        Drawing drawing = drawings[position];
        holder.drawingNameTextView.setText(drawing.getName());
        holder.userNameTextView.setText(drawing.getUser().getDisplayName());
        holder.drawing = drawing;

        drawingService.downloadBitmap(drawing.getPath(), holder.drawingImageView::setImageBitmap, Exception::printStackTrace);
    }

    @Override
    public int getItemCount() {
        return drawings.length;
    }

    public void setDrawings(Drawing[] drawings) {
        this.drawings = drawings;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView drawingNameTextView;
        TextView userNameTextView;
        ImageView drawingImageView;
        Drawing drawing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.drawingNameTextView = itemView.findViewById(R.id.drawing_name);
            this.userNameTextView = itemView.findViewById(R.id.row_drawing_user_name);
            this.drawingImageView = itemView.findViewById(R.id.row_drawing_view);
            itemView.setOnClickListener(this::onClick);
        }

        public void onClick(View v) {
            Intent intent = new Intent(context, ViewDrawingActivity.class);
            intent.putExtra("drawing", drawing);
            context.startActivity(intent);
        }
    }
}
