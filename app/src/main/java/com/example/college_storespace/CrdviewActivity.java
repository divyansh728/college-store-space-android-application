package com.example.college_storespace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CrdviewActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView crd1,crd2,crd3,crd4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_crdview );

        crd1 = (CardView) findViewById( R.id.crdvideo );
        crd2 = (CardView) findViewById( R.id.crdaudio );
        crd3 = (CardView) findViewById( R.id.crdpdf );
        crd4 = (CardView) findViewById( R.id.crddocx );

        crd1.setOnClickListener( this );
        crd3.setOnClickListener( this );
        crd4.setOnClickListener( this );
        crd2.setOnClickListener( this );
    }

    @Override

    public void onClick(View view) {

        Intent i;

        switch (view.getId()){
            case R.id.crdvideo:
                i=new Intent(this,VideoUploadActivity.class);
                startActivity( i );
                break;

            case R.id.crdpdf :
                i=new Intent(this,Uploadpdf.class);
                startActivity( i );
                break;

            case R.id.crddocx:
                i=new Intent(this,UploaddocxActivity.class);
                startActivity( i );
                break;

            case R.id.crdaudio:
                i=new Intent(this,ImageUploadActivity.class);
                startActivity( i );
                break;
        }
    }

}