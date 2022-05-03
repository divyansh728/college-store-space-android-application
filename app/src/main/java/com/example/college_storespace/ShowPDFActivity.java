package com.example.college_storespace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ShowPDFActivity extends AppCompatActivity {
    RecyclerView recview;
    pdfadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_pdfactivity );

        recview = findViewById( R.id.recvw );
        recview.setLayoutManager( new LinearLayoutManager( this ) );

        FirebaseRecyclerOptions<FileinfoModel> options =
                new FirebaseRecyclerOptions.Builder<FileinfoModel>()
                .setQuery( FirebaseDatabase.getInstance().getReference().child( "myPDFdocuments" ),FileinfoModel.class )
                .build();

        adapter = new pdfadapter( options );
        recview.setAdapter( adapter );

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}