package com.example.college_storespace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImagedataAdapter extends RecyclerView.Adapter<ImagedataAdapter.ViewHolder> {
    Context context;
    List<ImageModel> imageModelList;

    public ImagedataAdapter(Context context, List<ImageModel> imageModelList) {
        this.context = context;
        this.imageModelList = imageModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from( parent.getContext()).inflate( R.layout.image_design_row ,parent,false);
        return new ViewHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        ImageModel imageModel = imageModelList.get( position );
        holder.tvimgdata.setText( "Data of Image:-"+imageModel.getImgdata() );
        String imageUri = null;
        imageUri = imageModel.getImage();
        Picasso.get().load( imageUri ).into( holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvimgdata;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super( itemView );

            imageView = itemView.findViewById( R.id.imggbtn );
            tvimgdata = itemView.findViewById( R.id.imgdata );
        }
    }
}
