package com.example.githubsearch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class PagesUsersActivity extends AppCompatActivity {

    private ArrayList<User> foundUsers;

    private ViewPager pager;
    private PagerTabStrip tabStrip;
    private BottomNavigationView navigationBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            setFoundUsers((ArrayList<User>) arguments.get("users"));
            setContentView(R.layout.users_pages);
            bindUI();
            pager.setAdapter(new PagesUsersAdapter(this, getSupportFragmentManager(), getFoundUsers()));
            setStyleTabStrip();
            navigationBar.getMenu().findItem(R.id.navigation_results).setChecked(true);
        }
        navigationBar.setOnNavigationItemSelectedListener(new NavigationOnItemSelectedListener());
    }

    private void bindUI() {
        pager = (ViewPager) findViewById(R.id.pager);
        tabStrip = (PagerTabStrip) findViewById(R.id.tabStrip);
        navigationBar = (BottomNavigationView) findViewById(R.id.navigationResults);
    }

    private void setStyleTabStrip() {
        tabStrip.setDrawFullUnderline(true);
        tabStrip.setTabIndicatorColor(Color.parseColor("#E91E63"));
        for (int i = 0; i < tabStrip.getChildCount(); ++i) {
            View nextChild = tabStrip.getChildAt(i);
            if (nextChild instanceof TextView) {
                TextView textViewToConvert = (TextView) nextChild;
                textViewToConvert.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
            }
        }
    }

    public ArrayList<User> getFoundUsers() {
        return foundUsers;
    }

    public void setFoundUsers(ArrayList<User> foundUsers) {
        this.foundUsers = foundUsers;
    }

    private class NavigationOnItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(PagesUsersActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                case R.id.navigation_results:
                    break;
            }
            return false;
        }
    }
}
