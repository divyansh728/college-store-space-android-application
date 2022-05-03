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

public class Uploadpdf extends AppCompatActivity {

    ImageView brwsimage;
    EditText filetitle;
    Button uploadbtn,showpdfbtn;
    Uri filepath;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_uploadpdf);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("myPDFdocuments");

        filetitle = findViewById( R.id.flttl );
        brwsimage = findViewById( R.id.dcximgbrws );
        uploadbtn = findViewById( R.id.upldbtn );
        showpdfbtn = findViewById( R.id.shwdocxbtn );
        brwsimage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext( getApplicationContext() )
                        .withPermission( Manifest.permission.READ_EXTERNAL_STORAGE )
                        .withListener( new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType( "application/pdf" );
                                intent.setAction( Intent.ACTION_GET_CONTENT );
                                startActivityForResult( Intent.createChooser( intent,"PDF File Selected" ),101 );
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
                   uploadbtn.setOnClickListener( new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           processupload(filepath);
                       }
                   } );
                   showpdfbtn.setOnClickListener( new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent = new Intent(Uploadpdf.this,ShowPDFActivity.class);
                           startActivity( intent );
                       }
                   } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode==101 && resultCode== RESULT_OK);
        {
            filepath = data.getData();
        }
    }
    public void processupload(Uri filepath){
        final ProgressDialog pd = new ProgressDialog( this );
        pd.setTitle( "File Uploading.....!!!" );
        pd.show();
        StorageReference reference = storageReference.child( "PDFuploads/"+System.currentTimeMillis()+".pdf" );
        reference.putFile( filepath )
                .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                            FileinfoModel obj = new FileinfoModel( filetitle.getText().toString(),uri.toString() );
                            databaseReference.child( databaseReference.push().getKey()).setValue( obj );

                            pd.dismiss();
                                Toast.makeText( getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG ).show();
                            filetitle.setText( "" );
                            }
                        } );
                    }
                } )
                .addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                     float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                     pd.setMessage( "Uploaded :" + (int)percent+"%");
                    }
                } );
    }
}