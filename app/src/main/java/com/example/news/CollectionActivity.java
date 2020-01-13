package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.adapter.MyAdapter;
import com.example.news.adapter.NewsAdapter;
import com.example.news.model.NewsInfo;
import com.example.news.model.NewsVo;
import com.example.news.utils.GsonUtil;
import com.example.news.utils.MyDatabaseHelper;
import com.example.news.utils.ThemeUtil;

import java.util.ArrayList;
import java.util.List;
import org.xutils.x;

public class CollectionActivity  extends AppCompatActivity implements NewsAdapter.CallBack{
    private ListView collection;
    private Context context;
    private int type;

    private TextView tm;
    private TextView docid;
    private List<NewsInfo> newsList = new ArrayList<>();
    private ImageView image_drawer_home;
    private NewsAdapter adapter;
    private MyDatabaseHelper helper;
    private CollectionActivity mContext;
    private PopupWindow popupWindow;
    private RelativeLayout mEmptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        mContext = this;
        setContentView(R.layout.activity_collection);
        helper = new MyDatabaseHelper(this, "TaiDB.db", null, 1);


        mEmptyView =(RelativeLayout)findViewById(R.id.collection_empty_view);

//        tm =(TextView) findViewById(R.id.news_item_date);
        image_drawer_home =(ImageView) findViewById(R.id.image_drawer_home);
        image_drawer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        x.view().inject(this);
//        mNewsInfoDao = new NewsInfoDao();
        initView();
        initViews();
        collection.setAdapter(adapter);

        collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = newsList.get(i).getType();
                String docid = newsList.get(i).getId();
                String title = newsList.get(i).getTitle();
                String time = newsList.get(i).getTime();
                Intent intent = new Intent(CollectionActivity.this, ShowNewsActivity.class);
                intent.putExtra("share_url", url);
                intent.putExtra("share_docid", docid);
                intent.putExtra("share_title", title);
                intent.putExtra("share_time", time);
                startActivity(intent);
            }
        });

}
    private void initViews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from Collection_News order by id desc", null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            //遍历Cursor对象，取出数据并打印
                            String news_url = cursor.getString(cursor.getColumnIndex("news_url"));
                            String news_title = cursor.getString(cursor.getColumnIndex("news_title"));
                            String news_date = cursor.getString(cursor.getColumnIndex("news_date"));
                            String news_docid = cursor.getString(cursor.getColumnIndex("news_docid"));

                            NewsInfo news = new NewsInfo(news_title, news_url, news_date,news_docid);
                            newsList.add(news);

                        } while (cursor.moveToNext());
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "收藏夹为空！", Toast.LENGTH_SHORT).show();
                            mEmptyView.setVisibility(View.VISIBLE);
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                cursor.close();
                db.close();
            }
        }).start();



        //查找所有数据
//        List<NewsInfo> allList = mNewsInfoDao.findAllData(NewsInfo.class);
//        adapter = new NewsAdapter(this,R.layout.taiwan_item,allList, type);
//        if (allList != null) {
//            StringBuilder stringb = new StringBuilder();
//            for (int i = 0; i < allList.size(); i++) {
//                NewsInfo info = allList.get(i);
//                stringb.append(info.getTitle() + ",").append(info.getTime()).append("\n");
//
////                tv.setText(info.getTitle());
////                tm.setText(info.getTime());
//            }
//            allList.toString();
//            System.out.println("666666"+allList);
//tv.setText(stringb);

//        }
//        Toast.makeText(getApplicationContext(), "收藏夹为空！", Toast.LENGTH_SHORT).show();
    }
    private void initView() {
        collection = (ListView) findViewById(R.id.listview_collection);
        adapter = new NewsAdapter(this, R.layout.news_item, newsList, this);



    }

    @Override
    public void click(View view) {
//        int position = Integer.parseInt(view.getTag().toString());
//        System.out.println("666666"+position);
//        SQLiteDatabase db = helper.getReadableDatabase();
//        db.execSQL("delete from Collection_News where news_url=?",
//                new String[]{newsList.get(position).getType()});
//        db.close();
//        newsList.remove(position);
//        adapter.notifyDataSetChanged();
//        Toast.makeText(this, "该新闻已被移除收藏夹！", Toast.LENGTH_SHORT).show();
    }


}
