package com.example.jagratilahar.helper;
import com.example.jagratilahar.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jagratilahar.activities.MainActivity;
import com.example.jagratilahar.activities.PostViewActivity;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private View view;
    private ArrayList<Posts> postsArrayList;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Posts posts = postsArrayList.get(position);

        holder.item_tv_title.setText(posts.getTitle());
        holder.item_tv_text.setText(posts.getText());

        Glide.with(MainActivity.recyclerView)
                .load(Variable.base + "images/" + posts.getImage())
                .apply(new RequestOptions().centerCrop())
                .into(holder.item_iv_image);



        holder.item_tv_readMore.setOnClickListener(v -> {
            showDetail(posts, position);
        });
    }

    private void showDetail(Posts posts, int position) {
        Intent intentShowDetail = new Intent(context, PostViewActivity.class);
        posts.setPosition(position);
        intentShowDetail.putExtra("posts",posts);
        context.startActivity(intentShowDetail);
    }

    public PostsAdapter(Context context, ArrayList<Posts> postsArrayList){
        this.context = context;
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_tv_title, item_tv_text,item_tv_readMore;
        ImageView item_iv_image;
        LinearLayout itemLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            item_tv_title = itemView.findViewById(R.id.item_tv_title);
            item_tv_text = itemView.findViewById(R.id.item_tv_text);
            item_tv_readMore = itemView.findViewById(R.id.readMore);
            item_iv_image = itemView.findViewById(R.id.item_iv_image);
            itemLayout = itemView.findViewById(R.id.item_layout_post);
        }
    }
}
