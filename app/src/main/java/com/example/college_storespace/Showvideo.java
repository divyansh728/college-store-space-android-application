package com.example.college_storespace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Showvideo extends AppCompatActivity {
   private static final int PERMISSION_STORAGE_CODE = 1000;
    DatabaseReference databaseReference,likesreference ;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    String url,downloadurl;
    ImageButton downloadbtn;
    Boolean likechecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_showvideo );


        recyclerView = findViewById( R.id.recyclerview_ShowVideo );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("video");
        likesreference = database.getReference("likes");
    }

    private void firebaseSearch(String searchtext) {
        String query  = searchtext.toLowerCase();
        Query firebaseQuery  = databaseReference.orderByChild("serch").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<Member> options = new FirebaseRecyclerOptions.Builder<Member>()
                .setQuery( firebaseQuery,Member.class )
                .build();
        FirebaseRecyclerAdapter<Member,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Member, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Member model) {

                        holder.setExoplayer( getApplication(), model.getName(), model.getVideourl() );
                    }

                    @NonNull
                    @NotNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from( parent.getContext() )
                                .inflate( R.layout.tms, parent, false );
                        return new ViewHolder( view );


                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter( firebaseRecyclerAdapter );

    }

                    @Override
                    protected void onStart() {
                        super.onStart();

                        FirebaseRecyclerOptions<Member> options = new FirebaseRecyclerOptions.Builder<Member>()
                                .setQuery( databaseReference, Member.class )
                                .build();
                        FirebaseRecyclerAdapter<Member, ViewHolder> firebaseRecyclerAdapter =
                                new FirebaseRecyclerAdapter<Member, ViewHolder>( options ) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Member model) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String currentUserId = user.getUid();
                                        final String postkey = getRef( position ).getKey();

                                        holder.setExoplayer( getApplication(), model.getName(), model.getVideourl() );

                                        url = getItem( position ).getVideourl();
                                        downloadbtn = findViewById( R.id.download_button_viewholder );
                                        holder.downloadbtn.setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                    if (checkCallingOrSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE )==
                                                            PackageManager.PERMISSION_DENIED){
                                                        String permission = (Manifest.permission.WRITE_EXTERNAL_STORAGE);

                                                        requestPermissions( new String[]{permission},PERMISSION_STORAGE_CODE );
                                                    }else {
                                                        downloadurl = getItem( position).getVideourl();
                                                        startDownloading(downloadurl);
                                                    }
                                                }else {
                                                    downloadurl = getItem( position).getVideourl();
                                                    startDownloading(downloadurl);
                                                }
                                            }
                                        } );
                                        holder.setLikesbuttonStatus(postkey);
                                        holder.likebutton.setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                likechecker = true;
                                                likesreference .addValueEventListener( new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                        if(likechecker.equals( true )){
                                                            if(snapshot.child( postkey ).hasChild( currentUserId ) ){
                                                                likesreference.child( postkey ).child( currentUserId ).removeValue();
                                                                likechecker = false;
                                                            }else{
                                                                likesreference.child( postkey ).child( currentUserId ).setValue(true);
                                                                likechecker = false;
                                                            }
                                                        }
                                                                        }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                } );
                                            }
                                        } );
                                    }

                                    @NonNull
                                    @NotNull
                                    @Override
                                    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from( parent.getContext() )
                                                .inflate( R.layout.tms, parent, false );
                                        return new ViewHolder( view );

                                    }
                                };

                        firebaseRecyclerAdapter.startListening();
                        recyclerView.setAdapter( firebaseRecyclerAdapter );

                    }

    private void startDownloading(String downloadurl) {
        DownloadManager.Request request = new DownloadManager.Request( Uri.parse(downloadurl) );
        request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI  |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle( "Download" );
        request.setDescription( "Downloadin file..." );
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility( DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED );
        request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis() );

        DownloadManager manager = (DownloadManager) getSystemService( Context.DOWNLOAD_SERVICE );
        manager.enqueue( request );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.search_menu,menu);
        MenuItem item = menu.findItem( R.id.search_firebase );
        SearchView searchView = (SearchView) MenuItemCompat.getActionView( item );
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch( query );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch( newText );
                return false;
            }
        } );
        return super.onCreateOptionsMenu( menu );
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_STORAGE_CODE:{
            if (grantResults.length > 0 && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED) {
                startDownloading( downloadurl );
            }
             else {
                Toast.makeText( this, "permission denied", Toast.LENGTH_SHORT ).show();
            }
            }
        }
    }
}