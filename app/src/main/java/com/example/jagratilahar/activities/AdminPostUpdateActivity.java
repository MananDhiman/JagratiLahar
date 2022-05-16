package com.example.jagratilahar.activities;
import com.example.jagratilahar.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jagratilahar.helper.Posts;
import com.example.jagratilahar.R;
import com.example.jagratilahar.helper.Variable;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminPostUpdateActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText update_et_title, update_et_text;
    private Button button;
    private int idItem, index;
    private Bitmap bitmap;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post_update);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        load();

    }

    private void load() {
        imageView = findViewById(R.id.update_iv_image);
        update_et_title = findViewById(R.id.update_et_title);
        update_et_text = findViewById(R.id.update_et_text);
        button = findViewById(R.id.update_but_update);

        Posts posts = getIntent().getParcelableExtra("posts");
        update_et_title.setText(posts.getTitle());
        update_et_text.setText(posts.getText());
        idItem = posts.getId();
        index = posts.getPosition();

        Glide.with(getApplicationContext())
                .load(Variable.base + "images/" + posts.getImage())
                .apply(new RequestOptions().centerCrop())
                .into(imageView);

        button.setOnClickListener(v -> {
            update();
        });

        imageView.setOnClickListener(v -> {
            image();
        });
    }

    private void image() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    private String bitmapToString(Bitmap bitPhoto){
        if (bitPhoto != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitPhoto.compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream);
            byte[] imageByte = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imageByte, Base64.DEFAULT);
        } else {
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                uri = result.getUri();
                if (uri != null){
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                imageView.setImageURI(uri);
            }
        }
    }

    private void update() {
        StringRequest request = new StringRequest(StringRequest.Method.POST, Variable.update_post, response -> {
            try {
                JSONObject object = new JSONObject(response);
                JSONObject jsonObject = object.getJSONObject("data");

                Posts posts = new Posts();
                posts.setId(jsonObject.getInt("id"));
                posts.setTitle(jsonObject.getString("title"));
                posts.setText(jsonObject.getString("text"));
                posts.setImage(jsonObject.getString("image"));

                AdminActivity.postsArrayList.set(index, posts);
                AdminActivity.recyclerView.getAdapter().notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Post Updated", Toast.LENGTH_LONG).show();
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {

        }){
            @Override
            protected Map<String, String> getParams(){
                HashMap<String,String> map = new HashMap<>();
                map.put("id", String.valueOf(idItem));
                map.put("title",update_et_title.getText().toString());
                map.put("text",update_et_text.getText().toString());
                map.put("image", bitmapToString(bitmap));
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}