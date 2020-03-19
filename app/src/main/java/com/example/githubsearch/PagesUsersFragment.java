package com.example.githubsearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PagesUsersFragment extends Fragment {

    private List<User> foundUsers;
    public final static int ELEMENTS_PER_PAGE = 30;

    public static PagesUsersFragment newInstance(int page, ArrayList<User> foundUsers) {
        PagesUsersFragment fragment = new PagesUsersFragment();
        int from = ELEMENTS_PER_PAGE * page;
        int to = ELEMENTS_PER_PAGE * page + ELEMENTS_PER_PAGE;
        if (to > foundUsers.size()) {
            fragment.foundUsers = foundUsers.subList(from, foundUsers.size());
        } else {
            fragment.foundUsers = foundUsers.subList(from, to);
        }
        return fragment;
    }

    public PagesUsersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static String getTitle(PagesUsersActivity pagesUsersActivity, int page) {
        int from = ELEMENTS_PER_PAGE * page;
        int to = ELEMENTS_PER_PAGE * page + ELEMENTS_PER_PAGE;
        if (to > pagesUsersActivity.getFoundUsers().size()) {
            to = pagesUsersActivity.getFoundUsers().size();
        }
        return from + "-" + to;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, parent, false);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.githubUsersList);
        FragmentDataAdapter.OnUserClickListener onUserClickListener = new FragmentDataAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        };
        FragmentDataAdapter adapter = new FragmentDataAdapter(getActivity(), foundUsers, onUserClickListener);
        list.setAdapter(adapter);
        return view;
    }
}
