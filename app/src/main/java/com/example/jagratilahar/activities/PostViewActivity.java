package com.example.jagratilahar.activities;
import com.example.jagratilahar.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.jagratilahar.helper.Posts;
import com.example.jagratilahar.R;
import com.example.jagratilahar.helper.Variable;

public class PostViewActivity extends AppCompatActivity {

    private ImageView imageView;
    private int postId, index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        viewPost();

    }

    private void viewPost() {

        TextView detail_tv_text = findViewById(R.id.detail_tv_text);
        TextView detail_tv_title = findViewById(R.id.detail_tv_title);
        TextView detail_tv_time = findViewById(R.id.detail_tv_time);

        imageView = findViewById(R.id.detail_iv_image);

        Posts posts = getIntent().getParcelableExtra("posts");

        detail_tv_title.setText(posts.getTitle());
        detail_tv_text.setText(posts.getText());
        detail_tv_time.setText("This Post was Created On: "+posts.getTime());

        postId = posts.getId();
        index = posts.getPosition();

        loadImage(posts);

    }

    private void loadImage(Posts posts) {
        Glide.with(getApplicationContext())
                .load(Variable.base + "images/" + posts.getImage())
                .apply(new RequestOptions().centerCrop())
                .into(imageView);

    }

}