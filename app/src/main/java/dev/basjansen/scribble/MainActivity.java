package dev.basjansen.scribble;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.DrawingService;
import dev.basjansen.scribble.services.FirebaseDrawingSaveStrategy;
import dev.basjansen.scribble.services.OnFetchSuccessListener;


public class MainActivity extends AppCompatActivity {

    private DrawingService drawingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Gallery");
        drawingService = new DrawingService();

        RecyclerView drawingsCollection = new RecyclerView(this);
        drawingsCollection.setAdapter(new DrawingsAdapter(new Drawing[]{ new Drawing("My drawing", "images/1616337682604.png"), new Drawing("My drawing", "images/1616337682604.png") }));

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, DrawingActivity.class);
            startActivity(intent);
        });

        fetchDrawings();
    }

    public void fetchDrawings() {
        drawingService.fetch(new OnFetchSuccessListener<Drawing[]>() {
            @Override
            public void onSuccess(Drawing[] data) {
                System.out.println(data);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}