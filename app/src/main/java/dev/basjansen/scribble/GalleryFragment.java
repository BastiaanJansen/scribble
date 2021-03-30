package dev.basjansen.scribble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.DrawingService;

public class GalleryFragment extends Fragment {

    private DrawingService drawingService;
    private DrawingsAdapter drawingsAdapter;

    public GalleryFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Gallery");

        this.drawingService = new DrawingService();
        this.drawingsAdapter = new DrawingsAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        setupDrawingsList();

        drawingService.fetch((Drawing[] drawings) -> {
            drawingsAdapter.setDrawings(drawings);
            drawingsAdapter.notifyDataSetChanged();

            if (drawings.length == 0)
                getView().findViewById(R.id.no_drawings_available).setVisibility(View.VISIBLE);
        }, Exception::printStackTrace);
    }

    private void setupDrawingsList() {
        RecyclerView drawingsRecycleView = getView().findViewById(R.id.drawings_reclycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        drawingsRecycleView.setLayoutManager(linearLayoutManager);
        drawingsRecycleView.setAdapter(drawingsAdapter);
    }
}