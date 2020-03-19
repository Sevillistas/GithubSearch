package com.example.githubsearch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagesUsersAdapter extends FragmentPagerAdapter {

    private ArrayList<User> foundUsers;
    private PagesUsersActivity pagesUsersActivity;

    public PagesUsersAdapter(PagesUsersActivity pagesUsersActivity, FragmentManager manager, ArrayList<User> foundUsers) {
        super(manager);
        this.foundUsers = foundUsers;
        this.pagesUsersActivity = pagesUsersActivity;
    }

    @Override
    public int getCount() {
        Double totalUsersFound = Double.valueOf(foundUsers.size());
        Double countPages = Math.ceil(totalUsersFound / PagesUsersFragment.ELEMENTS_PER_PAGE);
        return countPages.intValue();
    }

    @Override
    public Fragment getItem(int page) {

        return (PagesUsersFragment.newInstance(page, foundUsers));
    }

    @Override
    public String getPageTitle(int page) {
        return (PagesUsersFragment.getTitle(pagesUsersActivity, page));
    }
}
