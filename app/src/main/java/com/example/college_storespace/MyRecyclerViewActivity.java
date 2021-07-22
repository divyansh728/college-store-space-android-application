package com.example.college_storespace;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyRecyclerViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_recycler_view );

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                String fileName = snapshot.getKey();
                String url = snapshot.getValue(String.class);

                ((MyAdapter)recyclerView.getAdapter()).update(fileName,url);

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        } );

         recyclerView = findViewById( R.id.recyclerView );
        recyclerView.setLayoutManager( new LinearLayoutManager( MyRecyclerViewActivity.this ) );
        MyAdapter myAdapter = new MyAdapter( recyclerView,MyRecyclerViewActivity.this,new ArrayList<String>(),new ArrayList<String>() );
        recyclerView.setAdapter( myAdapter );

    }
}
