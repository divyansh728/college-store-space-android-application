package com.example.college_storespace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class ImageUploadActivity extends AppCompatActivity {

    FirebaseDatabase mdatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imgbtn;
    EditText imgdata;
    Button imgupldbtn,showbtn;
    private static final int Gallery_code=1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_image_upload );

        imgbtn = findViewById( R.id.imggbtn );
        imgdata = findViewById( R.id.imgdata );
        imgupldbtn = findViewById( R.id.upldimgbtn );
        showbtn = findViewById( R.id.shwimgbtn );

        mdatabase = FirebaseDatabase.getInstance();
        mRef=mdatabase.getReference().child( "data and img" );
        mStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog( this );

        imgbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType( "image/*" );
                startActivityForResult( intent,Gallery_code );
            }
        } );

        showbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageUploadActivity.this,ImgandDataRecyclerViewActivity.class);
                startActivity( intent );
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode==Gallery_code && resultCode==RESULT_OK);
        {
            imageUrl = data.getData();
            imgbtn.setImageURI( imageUrl );
        }
        imgupldbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fn=imgdata.getText().toString().trim();

                if(!fn.isEmpty() && imageUrl!=null)
                {
                    progressDialog.setTitle( "Uploading....!!!" );
                    progressDialog.show();

                    StorageReference filepath = mStorage.getReference().child( "imagePost").child( imageUrl.getLastPathSegment());
                    filepath.putFile( imageUrl ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    String t= task.getResult().toString();

                                    DatabaseReference newPost=mRef.push();

                                    newPost.child( "imgdata" ).setValue( fn);
                                    newPost.child( "image" ).setValue( task.getResult().toString());
                                    progressDialog.dismiss();
                                    Toast.makeText( getApplicationContext()," Uploaded Successfully...",Toast.LENGTH_LONG ).show();
                                }
                            } );
                        }
                    } );
                }
            }
        } );
    }
}