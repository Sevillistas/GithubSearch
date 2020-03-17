package com.example.githubsearch;

import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class UserInfo extends AppCompatActivity {

    private User user;
    private ImageView userInfoAvatar;
    private TextView userInfoLogin;
    private TextView userInfoId;
    private TextView userInfoAccountUrl;
    private TextView userInfoType;
    private TextView userInfoScore;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        if(args!=null){
            user=(User)args.get("user");
            setContentView(R.layout.user_info);
            bindUI();
            bindData();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bindUI(){
        userInfoAvatar=(ImageView)findViewById(R.id.userInfoAvatar);
        final ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), getResources().getDimension(R.dimen.borderRadius));
            }
        };
        userInfoAvatar.setOutlineProvider(viewOutlineProvider);
        userInfoAvatar.setClipToOutline(true);
        userInfoLogin=(TextView)findViewById(R.id.userInfoLogin);
        userInfoId=(TextView)findViewById(R.id.userInfoId);
        userInfoAccountUrl=(TextView)findViewById(R.id.userInfoAccountUrl);
        userInfoType=(TextView)findViewById(R.id.userInfoType);
        userInfoScore=(TextView)findViewById(R.id.userInfoScore);
    }

    public void bindData(){
        Picasso.get().load(user.avatarUrl).into(userInfoAvatar);
        userInfoLogin.setText(user.login);
        userInfoId.setText(String.format(Locale.getDefault(), "%d", user.id));
        userInfoAccountUrl.setText(user.url);
        userInfoType.setText(user.type);
        userInfoScore.setText(String.format(Locale.getDefault(), "%2.2f", user.score));
    }

}
