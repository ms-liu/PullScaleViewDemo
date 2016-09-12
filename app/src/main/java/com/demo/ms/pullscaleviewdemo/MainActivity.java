package com.demo.ms.pullscaleviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        RecycleViewPager adPage = (RecycleViewPager) findViewById(R.id.ad_page);
        List<String> ads = new ArrayList<>();
        ads.add("http://c.hiphotos.baidu.com/image/pic/item/472309f7905298220099cbe5d2ca7bcb0a46d46a.jpg");
        ads.add("http://f.hiphotos.baidu.com/image/pic/item/b17eca8065380cd70c5150cba444ad345982814d.jpg");
        ads.add("http://f.hiphotos.baidu.com/image/pic/item/9c16fdfaaf51f3de719b4b4291eef01f3a2979ef.jpg");
        ads.add("http://f.hiphotos.baidu.com/image/pic/item/0824ab18972bd407208921db7e899e510fb3094d.jpg");
        adPage.startRecyle(this,ads);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
