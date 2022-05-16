package com.example.jagratilahar.activities;
import com.example.jagratilahar.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
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

public class AdminPostAddActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    private EditText add_et_title, add_et_text;
    private Button add_but_add;
    private ProgressDialog progressDialog;
    private Bitmap bitmap = null;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post_add);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        load();
    }

    private void load() {
        ivPhoto = findViewById(R.id.add_iv_image);
        add_et_text = findViewById(R.id.add_et_text);
        add_et_title = findViewById(R.id.add_et_title);
        add_but_add = findViewById(R.id.add_but_add);

        add_but_add.setOnClickListener(v -> {
            AddPost();
        });

        ivPhoto.setOnClickListener(v -> {
            pickImage();
        });


    }

    private void test() {
        Toast.makeText(this, "Toast", Toast.LENGTH_LONG).show();

        progressDialog.setMessage("Adding Post... ");
        progressDialog.show();



        StringRequest request = new StringRequest(StringRequest.Method.POST, Variable.create_post, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject object = jsonObject.getJSONObject("data");
                Posts posts = new Posts();
                posts.setId(object.getInt("id"));
                posts.setTitle(object.getString("title"));
                posts.setText(object.getString("text"));

                AdminActivity.postsArrayList.add(posts);
                AdminActivity.recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Post Added", Toast.LENGTH_SHORT).show();
                finish();


            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            progressDialog.dismiss();
        },error -> {
            error.printStackTrace();
            progressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams(){
                HashMap<String,String> map = new HashMap<>();
                map.put("title", add_et_title.getText().toString().trim());
                map.put("text", add_et_text.getText().toString().trim());
                map.put("image", bitmapToString(bitmap));
                return map;
            }

        };
        Volley.newRequestQueue(getApplicationContext()).add(request);



    }

    private void pickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    private void AddPost() {

       // progressDialog.setMessage("Adding Post... ");
       // progressDialog.show();

        StringRequest request = new StringRequest(StringRequest.Method.POST, Variable.create_post, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONObject object = jsonObject.getJSONObject("data");
                Posts posts = new Posts();
                posts.setId(object.getInt("id"));
                posts.setTitle(object.getString("title"));
                posts.setText(object.getString("text"));

                AdminActivity.postsArrayList.add(posts);
                AdminActivity.recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Post Added", Toast.LENGTH_SHORT).show();

                finish();


            } catch (JSONException e) {
                e.printStackTrace();
               //  progressDialog.dismiss();
            }
              //progressDialog.dismiss();
        },error -> {
                error.printStackTrace();
                //progressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams(){
                HashMap<String,String> map = new HashMap<>();
                map.put("title", add_et_title.getText().toString().trim());
                map.put("text", add_et_text.getText().toString().trim());
                map.put("image", bitmapToString(bitmap));
                return map;
            }

        };
        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null){
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream);
            byte[] imageByte = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imageByte, Base64.DEFAULT);
        }
        else {
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                uri = result.getUri();
                if (uri != null){
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ivPhoto.setImageURI(uri);
            }
        }
    }
}
