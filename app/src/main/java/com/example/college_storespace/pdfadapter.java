package com.example.college_storespace;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class pdfadapter extends FirebaseRecyclerAdapter<FileinfoModel,pdfadapter.myviewholer> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public pdfadapter(@NonNull @NotNull FirebaseRecyclerOptions<FileinfoModel> options) {
        super( options );
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull myviewholer holder, int position, @NonNull @NotNull FileinfoModel model) {
        holder.headertxt.setText( model.getFilename());
        holder.img1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.img1.getContext(),displaypdf.class);
                intent.putExtra( "filename",model.getFilename());
                intent.putExtra( "fileurl",model.getFileurl());

                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                holder.img1.getContext().startActivity( intent );
            }
        } );
    }

    @NonNull
    @NotNull
    @Override
    public myviewholer onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from( parent.getContext()).inflate( R.layout.singlerowdesign,parent,false );
        return new myviewholer( view );
    }

    public class myviewholer extends RecyclerView.ViewHolder{
        ImageView img1;
        TextView headertxt;

        public myviewholer(@NonNull @NotNull View itemView) {
            super( itemView );

            img1 =itemView.findViewById( R.id.img1 );
            headertxt = itemView.findViewById( R.id.headertxt );

        }
    }
}
