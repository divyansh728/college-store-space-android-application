package com.example.college_storespace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

public class UploaddocxActivity extends AppCompatActivity {

    ImageView docximgbrws;
    Button upldbtn,shwdocxbtn;
    EditText flttl;
    Uri flpath;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_uploaddocx );

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("myDOCXdocuments");
        flttl = findViewById( R.id.flttl );
        upldbtn=findViewById( R.id.upldbtn );
        docximgbrws = findViewById( R.id.dcximgbrws );

        docximgbrws.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext( getApplicationContext() )
                        .withPermission( Manifest.permission.READ_EXTERNAL_STORAGE )
                        .withListener( new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType( "application/docx" );
                                intent.setAction( Intent.ACTION_GET_CONTENT );
                                startActivityForResult( Intent.createChooser( intent,"DOCX File Selected" ),110 );
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                           permissionToken.continuePermissionRequest();
                            }
                        } ).check();
            }
        } );

        upldbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prcsupload(flpath);
            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode==110 && resultCode == RESULT_OK)
        {
            flpath = data.getData();
            Toast.makeText(getApplicationContext(),"DOCX File Selected",Toast.LENGTH_LONG).show();

        }
    }
    public void prcsupload(Uri flpath){
        final ProgressDialog pdc=new ProgressDialog( this );
        pdc.setTitle( "File Uploading....!!" );
        pdc.show();
        final StorageReference reference = storageReference.child( "DOCXuploads/" +System.currentTimeMillis()+".docx");
        reference.putFile( flpath )
                .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      reference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                          Docxmodel oj=new Docxmodel( flttl.getText().toString(),uri.toString());

                          databaseReference.child( databaseReference.push().getKey()).setValue( oj );
                          pdc.dismiss();
                          Toast.makeText( getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG ).show();
                          flttl.setText( "" );
                          }
                      } );
                    }
                } )
                .addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                       float prcnt = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                       pdc.setMessage( "Uploaded :"+(int)prcnt+"%");
                    }
                } );
    }
}