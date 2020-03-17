package com.example.githubsearch;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {

    private ArrayList<User> users;
    private Context context;

    public PageAdapter(Context context, FragmentManager manager, ArrayList<User> users) {
        super(manager);
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        Double countUsers = Double.valueOf(users.size());
        Double countPages = Math.ceil(countUsers / PageFragment.COUNT_ELEMENTS);
        return countPages.intValue();
    }

    @Override
    public Fragment getItem(int page) {
        return (PageFragment.newInstance(page, users));
    }

    @Override
    public String getPageTitle(int page) {
        return (PageFragment.getTitle(context, page));
    }
}
