package com.example.college_storespace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class AskDoubtActivity extends AppCompatActivity {
   Button btnsnd;
   EditText msget,senderet;
   ListView listView;
   ArrayAdapter msgAdapter;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_ask_doubt );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("doubtmessage");

        btnsnd = findViewById( R.id.btnsnd );
        senderet=findViewById( R.id.sender );
        msget=findViewById( R.id.msg );

        listView=findViewById( R.id.listviewdoubt );

        ArrayList msgList= new ArrayList<String>();
         msgAdapter= new ArrayAdapter<String>( this,R.layout.dbtlistitm,msgList );
        listView.setAdapter( msgAdapter );

        btnsnd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sender=senderet.getText().toString();
                String msg = msget.getText().toString();
                //msgAdapter.add(sender+">>  "+msg);
                myRef.push().setValue( sender+">>  "+msg );
                msget.setText( "" );
            }
        } );
        loadMsg();
    }
    private void loadMsg(){
        myRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                msgAdapter.add( snapshot.getValue().toString());
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