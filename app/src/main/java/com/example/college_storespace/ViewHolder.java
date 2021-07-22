package com.example.college_storespace;

import android.app.Application;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ViewHolder extends RecyclerView.ViewHolder {

    SimpleExoPlayer exoPlayer;
    PlayerView playerView;
    ImageButton likebutton,downloadbtn;
    TextView likesdisplay;
    int likescount;
    DatabaseReference likesref;

    public ViewHolder(@NonNull @NotNull View itemView) {
        super( itemView );

    }

    public void setLikesbuttonStatus(final String postkey){
        likebutton = itemView.findViewById( R.id.like_btn );
        likesdisplay = itemView.findViewById( R.id.likes_textview );
        downloadbtn = itemView.findViewById( R.id.download_button_viewholder );
        likesref = FirebaseDatabase.getInstance().getReference("likes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String likes = "likes";

        likesref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child( postkey ).hasChild( userId )){
                    likescount = (int)snapshot.child( postkey ).getChildrenCount();
                    likebutton.setImageResource( R.drawable.ic_baseline_favorite_like );
                 likesdisplay.setText( Integer.toString( likescount ) + likes );
                }
                else {
                    likescount = (int)snapshot.child( postkey ).getChildrenCount();
                    likebutton.setImageResource( R.drawable.ic_baseline_favorite_dislike );
                    likesdisplay.setText( Integer.toString( likescount ) + likes );
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        } );
    }

    public void setExoplayer(Application application, String name, String Videourl ){

        TextView textView = itemView.findViewById( R.id.tv_tms );
        playerView = itemView.findViewById( R.id.exoplayer_tms );
        textView.setText( name );

        try {

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance( application );
                Uri video = Uri.parse( Videourl );
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory( "video" );
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource( video,dataSourceFactory,extractorsFactory, null, null );
                playerView.setPlayer( exoPlayer );
                exoPlayer.prepare( mediaSource );
                exoPlayer.setPlayWhenReady( false );
                
                
                

        }catch (Exception e){
            Log.e("ViewHolder", "exoplayer error"+e.toString());

        }
    }
}
