package com.example.chandoras.newsdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class DetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    TextView title_tv;
    TextView decp_tv;

    TextView source_tv;
    String url;

   private AdView mAdview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        MobileAds.initialize(DetailActivity.this,"ca-app-pub-8203960224001766~2630990594");

        mAdview= (AdView)findViewById(R.id.adView) ;
        AdRequest adRequest =  new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        imageView = (ImageView)findViewById(R.id.image_view);
        title_tv = (TextView)findViewById(R.id.tv_title);
       decp_tv= (TextView)findViewById(R.id.tv_description);

       source_tv = (TextView)findViewById(R.id.tv_source);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent intent = getIntent();

        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESC");
        String urlToImage = intent.getStringExtra("URL_IMAGE");
         url = intent.getStringExtra("URL");
        String source = intent.getStringExtra("SOURCE");

        Glide.with(DetailActivity.this).load(urlToImage).into(imageView);
        title_tv.setText(title);
       decp_tv.setText(description);

        source_tv.setText("Source:- "+ source);







    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void moreDetails(View view) {

  Intent intent = new Intent(DetailActivity.this,WebviewActivity.class);
      intent.putExtra("URL",url);
        startActivity(intent);
    }
}
