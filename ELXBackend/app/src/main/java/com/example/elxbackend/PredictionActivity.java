package com.example.elxbackend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PredictionActivity extends AppCompatActivity {
    private Context context;
    private ImageView imageView;
    private Bitmap photo;
    String destination;
    File input_photo;
    String filePath;
    int selectedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

//        imageFragment = new ImageFragment();
//        buttonFragment = new ButtonFragment();
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frameLayout_image, imageFragment)
//                .replace(R.id.frameLayout_button, buttonFragment)
//                .commit();

        imageView = findViewById(R.id.capturedImageView);
        Button selectImage = findViewById(R.id.uploadPicButton);
        Button predict = findViewById(R.id.predictButton);
        Button clickPicture = findViewById(R.id.clickPicButton);
        TextView predictionRes = findViewById(R.id.predictionTextView);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            selectedImage = intent.getIntExtra("avatar", 0);
        }


        // for making image clickable and popping prompts
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickableTV.setText("LEARN MORE");
//                //openLearnMoreActivity();
//            }
//        });


        // for image input button
//        selectImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startInputImage(v);
//            }
//        });

        // for predict button
        // On clicking this button, the ML model that is integrated starts its processing in the background
        // and presents the prediction result in a text view
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(photo, 224, 224, true);
                try {
                    Context context = getApplicationContext();
                    MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(context);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                    TensorImage tbuffer = TensorImage.fromBitmap(resizedBitmap);
                    ByteBuffer byteBuffer = tbuffer.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
                    @NonNull TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    float[] outputBuffer = outputFeature0.getFloatArray();
                    int maxIndex = getMaxValIndex(outputBuffer);
                    String fileData = LoadData("labels.txt");
                    String outputText[] = fileData.split("\n");
                    //String[] outputText = yourData.
                    //String outputText = String.valueOf(labelFileArray[maxIndex]);
                    predictionRes.setText("Let's learn more about the "+outputText[maxIndex]+" object!"); // final prediction result displayed

                    // Releases model resources if no longer used.
                    model.close();

                    // the recent picture clicked by the user for prediction is saved in a file and using Intent, sent to the next Activity
                    Intent intent = new Intent(PredictionActivity.this, LearnMoreActivity.class);
                    String fileName = System.currentTimeMillis() + ".jpg";  // file name for the image recently captured; can be used for pNG as well
                    filePath =  Environment.getExternalStorageDirectory().getAbsolutePath() + fileName;
                    //File destination = new File(filePath);
                    intent.putExtra("imagePath", filePath);
                    intent.putExtra("predictionResult", outputText);
                    intent.putExtra("avatar", selectedImage);
                    intent.putExtra("filename", fileName);
                    startActivity(intent);
                } catch (IOException e) {
                    // TODO Handle the exception
                }
                //startPrediction(v);
            }
        });

        // for camera access
        clickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_camera, 100);

            }
        });
    }

    public void startInputImage(View v){
        Intent input_image = new Intent(Intent.ACTION_PICK);
        input_image.setType("image/*");
        startActivityForResult(input_image, 200);
        //onActivityResult(100, );
    }

    //    public void startPrediction(View v){
//        Intent predict = new Intent(this, MainActivity2.class);
//        startActivity(predict);
//    }
    // for importing image from gallery or through camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photo = (Bitmap) data.getExtras().get("data");
        // gallery access
        if(resultCode == RESULT_OK && requestCode == 200){
            //to be completed
            imageView.setImageURI(data.getData());
            //imageView.setImageBitmap(photo);

        }
        else
            imageView.setImageBitmap(photo);

        this.destination = System.currentTimeMillis() + ".jpg";
        this.filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + destination;
        this.input_photo = new File(filePath);
        //intent_photo.putExtra("img")
        //System.out.println("destination of image: "+destination);


    }

    // returns the max value in all prediction values predicted by the ML model
    public int getMaxValIndex(float[] arr){
        int index = 0;
        float max = 0.0f;
        for(int i=0; i < arr.length; i++ ){
            if(arr[i] > max ){
                max = arr[i];
                index = i;
            }
        }
        return index;
    }

    // reading labels.txt (annotations)
    public String LoadData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);

        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }


}
