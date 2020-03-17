package com.example.githubsearch;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class PagesUsers extends AppCompatActivity {

    private ArrayList<User> users;
    private PagerTabStrip tabStrip;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            users = (ArrayList<User>)arguments.get("users");
            setContentView(R.layout.users_pages);
            ViewPager pager = (ViewPager)findViewById(R.id.pager);
            pager.setAdapter(new PageAdapter(this, getSupportFragmentManager(), users));
            setStyleTabStrip();
        }
    }

    public void setStyleTabStrip(){
        tabStrip = (PagerTabStrip)findViewById(R.id.tabStrip);
        tabStrip.setDrawFullUnderline(true);
        tabStrip.setTabIndicatorColor(Color.parseColor("#E91E63"));
        for (int i = 0; i < tabStrip.getChildCount(); ++i) {
            View nextChild = tabStrip.getChildAt(i);
            if (nextChild instanceof TextView) {
                TextView textViewToConvert = (TextView)nextChild;
                textViewToConvert.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
            }
        }
    }
}
