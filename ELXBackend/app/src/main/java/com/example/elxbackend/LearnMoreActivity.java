package com.example.elxbackend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.UUID;

public class LearnMoreActivity extends AppCompatActivity {
    Fragment imageFragment;
    Fragment avatarFragment;
    private ImageView imageView;
    private TextView avatarTextView;
    private Bitmap photo;
    String imageFilePath, filePath;
    String filename;
    String predictedResult;
    int selectedImage;
    ImageView avatarImageView;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        setContentView(R.layout.activity_learn_more);

        avatarImageView = findViewById(R.id.avatarImageView);
        Button backButton = (Button)  findViewById(R.id.backButton);
        Button homeButton = (Button) findViewById(R.id.homeButton);
        Button learnMoreButton = (Button) findViewById(R.id.learnMoreButton);
        TextView predictionRes = findViewById(R.id.predictionTextView);
        avatarTextView = findViewById(R.id.avatarTV);
        imageView = findViewById(R.id.capturedImageView);

        // using Intent, we get all the information of the previous activities
        Intent intent = this.getIntent();
        if(intent.getExtras() != null){
            selectedImage = intent.getIntExtra("avatar", 0);
            predictedResult = intent.getStringExtra("predictionResult");
            imageFilePath = intent.getStringExtra("imagePath");
            filename = intent.getStringExtra("filename");


            avatarImageView.setImageResource(selectedImage);
            // OLD IMAGE STUFF
//            filePath =  Environment.getExternalStorageDirectory().getAbsolutePath() + filename;
//            File imgFile = new File(filePath);
//            if(imgFile.exists()){
//                photo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                imageView = (ImageView) findViewById(R.id.capturedImageView);
//                imageView.setImageBitmap(photo);
//            }
            // NEW IMAGE STUFF
//            byte[] byteArray = intent.getByteArrayExtra("imgBitmap");
//            photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//            imageView.setImageBitmap(photo);
            Uri imageUri = Uri.parse(intent.getStringExtra("imgUri"));
            imageView.setImageURI(imageUri);
            uploadImageToFirebase(imageUri);

            predictionRes.setText("Let's learn more about the " + predictedResult + "!"); // not working
//            Log.e("Past predictRes", "Prediction text: " + predictedResult);
        }

        // for backward navigation
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnMoreActivity.this, PredictionActivity.class);
                intent.putExtra("avatar", selectedImage);
                startActivity(intent);
            }
        });

        // for navigation to Home (HomeActivity): not working
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnMoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // for prompt to pop up
        learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLearnMoreActivity();
            } // using this function, we start the intent for Pop up Activity
        });
//        imageFragment = new ImageFragment();
//        avatarFragment = new ButtonFragment();
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frameLayout_image, imageFragment)
//                .replace(R.id.frameLayout_avatar, avatarFragment)
//                .commit();
    }

    // starts pop up activity for displaying prompts
    public void openLearnMoreActivity(){
        Intent intent = new Intent(LearnMoreActivity.this, PopUp.class);
        intent.putExtra("avatar", selectedImage);
        //intent.putExtra("capturedImage", 0);
        startActivity(intent);
    }

    private void uploadImageToFirebase(Uri filePath)
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + predictedResult +"-" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getApplicationContext(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getApplicationContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
}
