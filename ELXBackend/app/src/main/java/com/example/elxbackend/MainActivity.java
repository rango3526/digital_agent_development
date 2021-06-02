package com.example.elxbackend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elxbackend.ml.MobilenetV110224Quant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    // initializing both arrays for the avatars to be later used using Main Adapter class
    GridView gridView_AvatarSelection;
    private String [] avatarDescriptions = {"Meet Derek. His passion for Mechanics developed at the age of 10 years, while satisfying his curiosity of knowing how exactly a lever works. He pursued this passion by just observing the incidents and things around him and learning about its cause and operation. He graduated from Stanford University with a Doctorate in Mechanical Engineering and started working at GE Motors and is now leading a team at Hyundai. He also has his own Youtube channel wherein he explains how a device operates every week and aspires to inspire the next generation to cherish their curiosity and build on it.",
            "Meet Emma. She was always interested in knowing how a particular object or electronic device works. Her favorite hobby used to be disintegrating electronic devices and learning about their internal configuration. She cherished her innate love for mechanics and electronics by pursuing a degree in Electronics and Communications Engineering from Massachusetts Institute of Technology. She completed her Masters from MIT as well by which she had found her focus: Self-driving cars. Currently, she works as a Senior Communications Engineer at Tesla and hopes to pass on her passion for electronics to the youth.",
            "Meet William. He is a friend of Elon Musk since their college days. While at the University of Pennsylvania, William and Elon Musk studied together where they bonded on their love for Physics while working on numerous projects together. William has a PhD in Physics and is passionate about educating young people about Physics in everyday life.",
            "Meet Jessica. She got interested in Physics and Mathematics during her school years after reading about Newton’s laws. She is an ardent believer that one can gather a lot of knowledge by observing things present in one’s surroundings with curiosity, inspired by the great scientist Isaac Newton. Jessica further pursued their education at the University of Cambridge where she was taught by Stephen Hawking and currently works as an educator and entrepreneur helping others pique their interest in everyday objects."};
    private int[] avatars = {R.drawable.derek, R.drawable.emma, R.drawable.william, R.drawable.jessica};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView_AvatarSelection = (GridView) findViewById(R.id.gridViewHome);
        MainAdapter adapter = new MainAdapter(MainActivity.this, avatarDescriptions, avatars);
        gridView_AvatarSelection.setAdapter(adapter);

        // avatar selection after an avatar is clicked on
        gridView_AvatarSelection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("avatarDescription", avatarDescriptions[position]);
                intent.putExtra("avatar", avatars[position]);
                startActivity(intent);
            }
        });
    }
}