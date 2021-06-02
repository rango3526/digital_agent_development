package com.example.elxbackend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.elxbackend.ml.MobilenetV110224Quant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class HomeActivity extends AppCompatActivity {
    private Context context;
    private ImageView imageView;
    private Bitmap photo;
    private TextView avatarTV;
    int selectedImage;

    ImageView avatarImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        avatarImageView = findViewById(R.id.avatarImageView);
        avatarTV = findViewById(R.id.avatarTV);
        Button next = findViewById(R.id.nextButton);

        avatarTV.setText("Let's learn more and have fun!");

        // Using Main Adapter class, we send the picture of the avatar that was chosen by the user
        // on the first screen (through Main Activity) using intent
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            selectedImage = intent.getIntExtra("avatar", 0);
            avatarImageView.setImageResource(selectedImage);
        }

        // Next button navigation logic using intent
        // Here we put the selected avatar image that user chose initially
        // and sends it to Prediction Activity so the application remembers which avatar was chosen
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PredictionActivity.class);
                intent.putExtra("avatar", selectedImage);
                startActivity(intent);
            }
        });


//        avatarFragment = new fragment_avatar();
//        buttonFragment = new ButtonFragment();

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frameLayout_avatar, avatarFragment)
//                .replace(R.id.frameLayout_button, buttonFragment)
//                .commit();


    }


}
