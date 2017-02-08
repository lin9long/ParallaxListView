package com.linsaya.parallaxlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends AppCompatActivity {

    private ParallaxListView listView;
    private String[] indexArr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        listView = (ParallaxListView) findViewById(R.id.listview);
        View view = View.inflate(this, R.layout.layout_header, null);
        imageview = (ImageView) view.findViewById(R.id.image);
        listView.addHeaderView(view);
        //不再显示顶底滑到头的蓝色指示器
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listView.setImageView(imageview);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, indexArr));
    }
}
