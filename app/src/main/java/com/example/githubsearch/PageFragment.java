package com.example.githubsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PageFragment extends Fragment {

    private List<User> users;
    public final static int COUNT_ELEMENTS = 30;

    public static PageFragment newInstance(int page, ArrayList<User> users) {
        PageFragment fragment = new PageFragment();
        int from = COUNT_ELEMENTS * page;
        int to = COUNT_ELEMENTS * page + COUNT_ELEMENTS;
        if (to > users.size()) {
            fragment.users = users.subList(from, users.size());
        } else {
            fragment.users = users.subList(from, to);
        }
        return fragment;
    }

    public PageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static String getTitle(Context context, int page) {
        return "Page №" + Integer.toString(page + 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, parent, false);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.githubList);
        DataAdapter.OnUserClickListener onUserClickListener = new DataAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent = new Intent(getContext(), UserInfo.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        };
        DataAdapter adapter = new DataAdapter(getActivity(), users, onUserClickListener);
        list.setAdapter(adapter);
        return view;
    }
}
