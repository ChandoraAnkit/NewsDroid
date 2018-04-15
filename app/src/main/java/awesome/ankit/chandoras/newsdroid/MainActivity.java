package awesome.ankit.chandoras.newsdroid;


import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;

public class MainActivity extends AppCompatActivity {
    private static final long RIPPLE_DURATION = 250;


    Toolbar toolbar;
    FrameLayout root;
    RequestQueue queue;

    ArrayList<NewsData> allDatas = new ArrayList<>();
    RecyclerView recyclerView;
    NewsDataRecycler adapter;
    ImageView contentHamburger;

    GuillotineAnimation animation;

    String[] general = {
            "the-times-of-india", "the-hindu", "abc-news-au", "time",
            "al-jazeera-english", "associated-press", "bbc-news", "cnn", "google-news", "independent", "metro", "mirror", "newsweek",
            "new-york-magazine", "reddit-r-all", "reuters",
            "the-guardian-au", "the-guardian-uk", "the-huffington-post", "the-new-york-times",
            "the-telegraph", "the-washington-post", "usa-today"};

    String[] technology = {
            "ars-technica", "engadget", "gruenderszene", "hacker-news",
            "t3n", "techcrunch", "techradar", "wired-de", "newsweek", "new-york-magazine",
            "recode", "reddit-r-all", "reuters"};

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


    public static String code = "DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }


        root =  findViewById(R.id.root);

        contentHamburger = findViewById(R.id.content_hamburger);

        adapter = new NewsDataRecycler(MainActivity.this, null);




        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        queue = Volley.newRequestQueue(this);

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);


        animation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

    if (savedInstanceState != null){

        allDatas = savedInstanceState.getParcelableArrayList("DATA");
        Log.i("MAIN", "onCreate: saved "+allDatas.size());
        adapter = new NewsDataRecycler(MainActivity.this, allDatas);
        recyclerView.setAdapter(adapter);


    }else {

        jsonRequest(general);
    }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("MAIN", "onSaveInstanceState: ");
        outState.putParcelableArrayList(code,allDatas);
        Log.i("MAIN", "onSaveInstanceState: size "+ allDatas.size());
    }



    public void jsonRequest(final String[] types) {


        for (int j = 0; j < types.length; j++) {

            String type = types[j];

            String url = "https://newsapi.org/v1/articles?source=" + type + "&sortBy=top&apiKey=62969fb77822480ab4d79cf5a5b3dc8b";

            final JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray articles = response.getJSONArray("articles");
                        for (int i = 0; i < articles.length(); i++) {

                            String title = "Not available";
                            String description = "Not available";
                            String source = "Not available";
                            String url = "Not available";
                            String urlToImage = "Not available";


                            JSONObject jsonObject = articles.getJSONObject(i);

                            if (jsonObject.has("source") && !jsonObject.isNull("source")) {
                                source = response.getString("source");
                            }

                            if (jsonObject.has("title") && !jsonObject.isNull("title")) {
                                title = jsonObject.getString("title");
                            }


                            if (jsonObject.has("description") && !jsonObject.isNull("description")) {
                                description = jsonObject.getString("description");
                            }

                            if (jsonObject.has("url") && !jsonObject.isNull("url")) {
                                url = jsonObject.getString("url");
                            }

                            if (jsonObject.has("urlToImage") && !jsonObject.isNull("urlToImage")) {
                                urlToImage = jsonObject.getString("urlToImage");
                            }

                            NewsData dataObj = new NewsData(source, title, description, url, urlToImage);
                            allDatas.add(dataObj);
                            Log.i("First","1");



                        }
                        adapter = new NewsDataRecycler(MainActivity.this, allDatas);


                        recyclerView.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.i("EXCE", e.toString());
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("ERROR", error.toString());

                }
            });


            queue.add(objectRequest);
        }

    }

    public void fetchNews(View view) {

        switch (view.getId()) {


            case R.id.general: {
                allDatas.clear();
                jsonRequest(general);
                animation.close();
                break;
            }
            case R.id.technology: {


                allDatas.clear();
                jsonRequest(technology);
                animation.close();
                break;
            }
            case R.id.sports: {
                allDatas.clear();
                jsonRequest(sports);
                animation.close();
                break;
            }
            case R.id.business: {
                allDatas.clear();
                jsonRequest(business);
                animation.close();
                break;
            }

            case R.id.entertainment: {

                allDatas.clear();
                jsonRequest(entertainment);
                animation.close();
                break;
            }
            case R.id.gaming: {

                allDatas.clear();
                jsonRequest(gaming);
                animation.close();
                break;
            }
            case R.id.music: {


                allDatas.clear();
                jsonRequest(music);
                animation.close();
                break;
            }
            case R.id.science: {

                allDatas.clear();
                jsonRequest(science);
                animation.close();
                break;
            }
            case R.id.about_us: {
                animation.close();
                aboutUsClick();
                allDatas.clear();
                jsonRequest(general);
                break;
            }

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
}
