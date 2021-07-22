package com.example.college_storespace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.ktx.FirebaseStorageKtxRegistrar;

import org.jetbrains.annotations.NotNull;

public class LoadActivity extends AppCompatActivity {
    Button selectFile,upload;
    TextView notification;
    Uri pdfUri;
    FirebaseStorage storage;
    FirebaseDatabase database;
    StorageReference storageReference;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_load );
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        selectFile = findViewById( R.id.selectFile );
        upload = findViewById( R.id.upload );
        notification = findViewById( R.id.notification );

        selectFile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission( LoadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                }
                else {
                    ActivityCompat.requestPermissions( LoadActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9 );
                }
            }
        } );

        upload.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(pdfUri!=null) {
                 uploadFile( pdfUri );
             }
             else{
                 Toast.makeText( LoadActivity.this,"Select a File",Toast.LENGTH_LONG ).show();
             }

            }
        } );
    }

    private void uploadFile(Uri pdfUri) {
        progressDialog=new ProgressDialog( this );
        progressDialog.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL );
        progressDialog.setProgress( 0 );
        progressDialog.setTitle( "Uploading File" );
        progressDialog.show();

       final String fileName=System.currentTimeMillis()+".pdf";
         final String fileName1=System.currentTimeMillis()+"";
        StorageReference storageReference=storage.getReference();
        storageReference.child( "Uploads" ).child( fileName ).getDownloadUrl()
       .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png' in uri
                System.out.println( uri.toString() );
                DatabaseReference reference = database.getReference();
                reference.child( fileName1 ).setValue( uri ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( LoadActivity.this, "File Successfully Uploaded:)", Toast.LENGTH_LONG ).show();
                        } else {
                            Toast.makeText( LoadActivity.this, "File is not Uploaded:)", Toast.LENGTH_LONG ).show();
                        }
                    }
                } );
            }
            }).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

                Toast.makeText( LoadActivity.this,"File is not Uploaded:)",Toast.LENGTH_LONG ).show();
            }
        } );
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if(requestCode ==9 &&  grantResults[0]== PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }
        else{
            Toast.makeText( LoadActivity.this,"Please Provide Permissions...",Toast.LENGTH_LONG ).show();
        }

        }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType( "application/pdf" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( intent,86 );

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

       if (requestCode == 86 && resultCode==RESULT_OK && data!=null){
           pdfUri = data.getData();
           notification.setText( "A file is selected :"+ data.getData().getLastPathSegment());
       }
       else{
           Toast.makeText( LoadActivity.this, "Please select a file", Toast.LENGTH_LONG ).show();
       }
    }
}