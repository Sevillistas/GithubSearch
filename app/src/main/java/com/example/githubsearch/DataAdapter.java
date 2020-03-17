package com.example.githubsearch;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<User> users;
    private OnUserClickListener onUserClickListener;

    public DataAdapter(Context context, List<User> users, OnUserClickListener listener) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.onUserClickListener = listener;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        Picasso.get().load(user.avatarUrl).into(holder.imageView);
        holder.imageView.setClipToOutline(true);
        holder.loginView.setText(user.login);
        holder.typeView.setText(user.type);
    }

    @Override
    public int getItemCount() {
        if(users.size()>PageFragment.COUNT_ELEMENTS) {
            return PageFragment.COUNT_ELEMENTS;
        }
        else {
            return users.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView imageView;
        final TextView loginView, typeView;

        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            loginView = (TextView) view.findViewById(R.id.login);
            typeView = (TextView) view.findViewById(R.id.type);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = users.get(getLayoutPosition());
                    onUserClickListener.onUserClick(user);
                }
            });
        }
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

}
