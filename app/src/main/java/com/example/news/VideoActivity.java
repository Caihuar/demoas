package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class VideoActivity extends AppCompatActivity {
    private WebView show_news;
    private Intent data;
    private String share_url;
    //    private Toolbar toolbar;
    private ImageView image_drawer_home;

    private String pageDescription = "";

    private Handler handler=new Handler(){
        //从子线程拿到的消息去主线程发消息
        @Override
        public void handleMessage(@NonNull Message msg) {

            Bundle bundle = msg.getData();
            String video = bundle.getString("videourl");
            String title = bundle.getString("titles");
            String author = bundle.getString("authors");
            String publishdate = bundle.getString("publishdates");
            Intent intent = new Intent(VideoActivity.this, vitamio.class);
            intent.putExtra("videourl",video);
            intent.putExtra("titles",title);
            intent.putExtra("authors",author);
            intent.putExtra("publishdates",publishdate);

            startActivity(intent);
            finish();
        }


    };
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String share_url = intent.getStringExtra("share_url");
        initDate(share_url);

    }

    //   @Override
//    protected void onCreate(Bundle savedInstanceState) {
////
//       super.onCreate(savedInstanceState);
//////     setContentView(R.layout.activity_show_news);
////
//      data = getIntent();
//      share_url = data.getStringExtra("share_url");
////
//image_drawer_home = (ImageView) findViewById(R.id.image_drawer_home);
////////        toolbar = (Toolbar) findViewById(R.id.contentToolbar);
////////        toolbar.setTitle("融视频 - 文章内容");
//        image_drawer_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//      initDate(share_url);
////
////
//   }
    public void initDate(final String url) {

        //只有主线程才能更新UI
        new Thread()
        {
            public void run()
            {
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).timeout(10000).get();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String video = doc.select("video").attr("src");
                String title = doc.select("h1#title").text();
                String author = doc.select("p.editor").text();
                String publishdate = doc.select("meta[name=publishdate]").attr("content");

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("titles", title);
                bundle.putString("authors", author);
                bundle.putString("publishdates", publishdate);
                bundle.putString("videourl", video);
                message.setData(bundle);
                handler.sendMessage(message);

            }
        }.start();

    }


/*
    Intent intent = new Intent(VideoActivity.this, vitamio.class);
                intent.putExtra("videourl", video);
    startActivity(intent);*/


}