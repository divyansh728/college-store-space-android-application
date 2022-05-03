package com.example.college_storespace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImgandDataRecyclerViewActivity extends AppCompatActivity {

    FirebaseDatabase mdatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    RecyclerView recyclerView;
    TextView tvimgdata;
    ImagedataAdapter imagedataAdapter;
    List<ImageModel> imageModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_imgand_data_recycler_view );
        mdatabase = FirebaseDatabase.getInstance();
        tvimgdata = findViewById( R.id.imgdata );
        mRef=mdatabase.getReference().child( "data and img" );
        mStorage = FirebaseStorage.getInstance();
        recyclerView=findViewById( R.id.recyclerviewimg );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        imageModelList = new ArrayList<ImageModel>();
        imagedataAdapter= new ImagedataAdapter( ImgandDataRecyclerViewActivity.this, imageModelList );
        recyclerView.setAdapter( imagedataAdapter );
        
        mRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                ImageModel imageModel = snapshot.getValue(ImageModel.class);
                imageModelList.add( imageModel );
                imagedataAdapter.notifyDataSetChanged();
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
    }
}