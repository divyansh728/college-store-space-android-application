package com.example.college_storespace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VideoUploadActivity extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;
    VideoView videoView;
    TextView textView;
    Button button;
    ProgressBar progressBar;
    EditText editText;
    private Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Member member;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_video_upload );

        member = new Member();
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("video");

        videoView = findViewById( R.id.videoview );
        button = findViewById( R.id.button_upload );
       progressBar= findViewById( R.id.progressBar );
        editText = findViewById( R.id.et_video_title );
        mediaController = new MediaController( this );
        videoView.setMediaController( mediaController );
        videoView.start();

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadvideo();
            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == PICK_VIDEO || resultCode == RESULT_OK ||
                data != null || data.getData() != null) {
            videoUri = data.getData();

            videoView.setVideoURI( videoUri );
        }
    }

    public void chooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType( "video/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( intent,PICK_VIDEO );
    }

     private String getExt(Uri uri){
        ContentResolver contentResolver =getContentResolver();
         MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
         return mimeTypeMap.getExtensionFromMimeType( contentResolver.getType( uri ) );
     }
    public void showVideo(View view) {

        Intent intent = new Intent(VideoUploadActivity.this, Showvideo.class);
        startActivity( intent );
    }
    private void uploadvideo() {
        String videoName = editText.getText().toString();
        String search = editText.getText().toString().toLowerCase();
        if (videoUri != null || !TextUtils.isEmpty( videoName )) {
            progressBar.setVisibility( View.VISIBLE );
            final StorageReference reference = storageReference.child( System.currentTimeMillis() + "." + getExt( videoUri ) );
            uploadTask = reference.putFile( videoUri );

            Task<Uri> urltask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            } )
                    .addOnCompleteListener( new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                progressBar.setVisibility( View.INVISIBLE );
                                Toast.makeText( VideoUploadActivity.this, "Data Uploaded", Toast.LENGTH_LONG ).show();

                                member.setName( videoName );
                                member.setVideourl( downloadUrl.toString() );
                                member.setSearch( search );
                                String i = databaseReference.push().getKey();
                                databaseReference.child(i).setValue( member );

                            } else {
                                Toast.makeText( VideoUploadActivity.this, "Process Failed", Toast.LENGTH_LONG ).show();

                            }
                        }
                    } );
        }else{
            Toast.makeText(this ,"All Fields Are Required",Toast.LENGTH_LONG ).show();

        }
    }
    }
