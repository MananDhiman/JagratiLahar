package com.example.jagratilahar.activities;
import com.example.jagratilahar.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jagratilahar.helper.AdminPostsAdapter;
import com.example.jagratilahar.helper.Posts;
import com.example.jagratilahar.helper.Variable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    public static RecyclerView recyclerView;
    public static ArrayList<Posts> postsArrayList;
    private AdminPostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Button admin_but_add_post = findViewById(R.id.admin_but_add_post);

        admin_but_add_post.setOnClickListener(v -> {
            Intent intentLogin = new Intent(AdminActivity.this, AdminPostAddActivity.class);
            this.startActivity(intentLogin);
        });

        loadPosts();
    }

    private void loadPosts() {

        postsArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.admin_rv_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        StringRequest request = new StringRequest(StringRequest.Method.POST, Variable.show_posts, response -> {
            try {
                JSONArray array = new JSONArray(response);

                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    Posts posts = new Posts();
                    posts.setId(object.getInt("id"));
                    posts.setTitle(object.getString("title"));
                    posts.setText(object.getString("text"));
                    posts.setImage(object.getString("image"));

                    posts.setTime(object.getString("time"));

                    postsArrayList.add(posts);
                }
                postsAdapter = new AdminPostsAdapter(AdminActivity.this,postsArrayList);
                recyclerView.setAdapter(postsAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "There seems to be some technical difficulty, Please Try Again", Toast.LENGTH_SHORT).show();
            }
        },error -> {
            error.printStackTrace();
            Toast.makeText(this, "Please Make Sure Your Internet Is Working", Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response){
                if (response.statusCode == 200){
                    JSONArray array = null;
                    try {
                        array = new JSONArray(response);

                        for (int i = 0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);

                            Posts posts = new Posts();
                            posts.setId(object.getInt("id"));
                            posts.setTitle(object.getString("title"));
                            posts.setText(object.getString("text"));
                            posts.setImage(object.getString("image"));

                            posts.setTime(object.getString("time"));

                            postsArrayList.add(posts);
                        }
                        postsAdapter = new AdminPostsAdapter(getApplicationContext(), postsArrayList);
                        recyclerView.setAdapter(postsAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return  super.parseNetworkResponse(response);
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.app_menu_login:
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
