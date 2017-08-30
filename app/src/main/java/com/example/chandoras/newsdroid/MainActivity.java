package com.example.chandoras.newsdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final long RIPPLE_DURATION = 250;

    Toolbar toolbar;
    FrameLayout root;

    RequestQueue queue;

    ArrayList<NewsData> allDatas;
    RecyclerView recyclerView;
    NewsDataRecycler adapter;
    ImageView contentHamburger;

    GuillotineAnimation animation;

    NetworkInfo activeNetwork;
    ConnectivityManager conMgr;


    String[] general = {
            "the-times-of-india", "the-hindu", "abc-news-au", "time",
            "al-jazeera-english", "associated-press", "bbc-news", "bild", "cnn",
            "focus", "google-news", "independent", "metro", "mirror", "newsweek",
            "new-york-magazine", "reddit-r-all", "reuters", "spiegel-online",
            "the-guardian-au", "the-guardian-uk", "the-huffington-post", "the-new-york-times",
            "the-telegraph", "the-washington-post", "usa-today"};

    String[] technology = {
            "ars-technica", "engadget", "gruenderszene", "hacker-news",
            "t3n", "techcrunch", "techradar", "wired-de", "newsweek", "new-york-magazine",
            "recode", "reddit-r-all", "reuters", "spiegel-online"};

    String[] sports = {
            "bbc-sport", "espn", "espn-cric-info", "football-italia", "four-four-two",
            "fox-sports", "nfl-news", "talksport", "the-sport-bible"};

    String[] business = {
            "bloomberg", "business-insider", "business-insider-uk", "cnbc", "financial-times",
            "fortune", "the-economist", "the-wall-street-journal"};

    String[] entertainment = {
            "buzzfeed", "daily-mail", "entertainment-weekly", "mashable", "the-lad-bible"};

    String[] gaming = {
            "ign", "polygon"};

    String[] music = {
            "mtv-news", "mtv-news-uk"};

    String[] science = {
            "national-geographic", "new-scientist"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }


        root = (FrameLayout) findViewById(R.id.root);
        contentHamburger = (ImageView) findViewById(R.id.content_hamburger);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = conMgr.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {

        } else {
            connectionError();
        }

        adapter = new NewsDataRecycler(MainActivity.this, null);


        allDatas = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        queue = Volley.newRequestQueue(this);

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);


        animation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

        jsonRequest(general);


    }

    public void jsonRequest(final String[] types) {

        for (int i = 0; i < types.length; i++) {

            String type = types[i];

            String url = "https://newsapi.org/v1/articles?source=" + type + "&sortBy=top&apiKey=62969fb77822480ab4d79cf5a5b3dc8b";

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String title, description, source, url, urlToImage;
                        NewsData dataObj;

                        source = response.getString("source");

                        JSONArray articles = response.getJSONArray("articles");

                        for (int i = 0; i < articles.length(); i++) {

                            JSONObject jsonObject = articles.getJSONObject(i);
                            title = jsonObject.getString("title");
                            description = jsonObject.getString("description");
                            url = jsonObject.getString("url");
                            urlToImage = jsonObject.getString("urlToImage");

                            if (source.isEmpty()) {
                                source = "Not found!";
                            } else if (title.isEmpty()) {
                                title = "Not found!";
                            } else if (description.isEmpty()) {
                                description = "Not found!";
                            } else if (url.isEmpty()) {
                                url = null;
                            } else if (urlToImage.isEmpty()) {
                                urlToImage = "Not found!";
                            }


                            dataObj = new NewsData(source, title, description, url, urlToImage);
                            allDatas.add(dataObj);


                        }
                        if (!allDatas.isEmpty()) {

                            adapter = new NewsDataRecycler(MainActivity.this, allDatas);

                            recyclerView.setAdapter(adapter);

                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            adapter.notifyDataSetChanged();
                        } else {

                            jsonRequest(types);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            });

            queue.add(objectRequest);

        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false).setTitle("Exit").setMessage("Are you sure you want to exit!").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void fetchNews(View view) {
        allDatas.clear();
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = conMgr.getActiveNetworkInfo();


        if (activeNetwork != null && activeNetwork.isConnected()) {

            switch (view.getId()) {

                case R.id.general: {
                    jsonRequest(general);
                    animation.close();
                    break;
                }
                case R.id.technology: {

                    jsonRequest(technology);
                    animation.close();
                    break;
                }
                case R.id.sports: {

                    jsonRequest(sports);
                    animation.close();
                    break;
                }
                case R.id.business: {

                    jsonRequest(business);
                    animation.close();
                    break;
                }

                case R.id.entertainment: {

                    jsonRequest(entertainment);
                    animation.close();
                    break;
                }
                case R.id.gaming: {

                    jsonRequest(gaming);
                    animation.close();
                    break;
                }
                case R.id.music: {

                    jsonRequest(music);
                    animation.close();
                    break;
                }
                case R.id.science: {

                    jsonRequest(science);
                    animation.close();
                    break;
                }
                case R.id.about_us: {
                    animation.close();
                    aboutUsClick();
                    jsonRequest(general);
                    break;
                }
            }
        } else {
            connectionError();
        }


    }

    public void aboutUsClick() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("About us!").setCancelable(false).setMessage(R.string.about).setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void connectionError() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Error!").setCancelable(false).setMessage(R.string.error).setNegativeButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork = conMgr.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {
                    dialogInterface.dismiss();
                    jsonRequest(general);

                } else {
                    connectionError();


                }

            }
        }).setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }


}
