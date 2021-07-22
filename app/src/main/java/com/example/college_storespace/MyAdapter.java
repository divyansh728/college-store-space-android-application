package com.example.college_storespace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    public void update(String name,String url){
        items.add(name);
        urls.add(url);
        notifyDataSetChanged();
    }

    public MyAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items,ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.tms,parent,false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
      holder.nameofFile.setText( items.get( position ) );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameofFile;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super( itemView );
            nameofFile = itemView.findViewById( R.id.nameofFile );
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = recyclerView.getChildLayoutPosition( view );
                    Intent intent = new Intent();
                    intent.setType( intent.ACTION_VIEW );
                    intent.setData( Uri.parse( urls.get( position ) ) );
                    context.startActivity( intent );

                }
            } );

        }

    }
}

