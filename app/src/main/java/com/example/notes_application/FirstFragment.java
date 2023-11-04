package com.example.notes_application;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirstFragment extends AppCompatActivity {

    // Initialize the coloured Buttons
    private Button yellowButton;
    private Button orangeButton;
    private Button blueButton;
    private LinearLayout linearLayout; // Initialize the Layout
    private final int GALLERY_REQ_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;
    byte[] imageBytes;
    EditText etNoteTitle, etNoteDescription;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_fragment);

        // component identification by ID
        linearLayout = findViewById(R.id.linearLayout);
        yellowButton = findViewById(R.id.yellowButton);
        orangeButton = findViewById(R.id.orangeButton);
        blueButton = findViewById(R.id.blueButton);

        etNoteTitle = findViewById(R.id.titleText);
        etNoteDescription = findViewById(R.id.descriptionText);
        imageView = findViewById(R.id.imageView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // Capturing picture
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null) {

                    Bundle extras = data.getExtras();
                    if (extras != null) {

                        Bitmap capturedImage = (Bitmap) extras.get("data");

                        // Convert the Bitmap image to a byte array
                        imageBytes = convertBitmapToBytes(capturedImage);

                        // Display image
                        imageView.setImageBitmap(capturedImage);
                    }
                }
            }

            // Uploading picture
            if (requestCode == GALLERY_REQ_CODE) {
                if (data != null) {
                    Uri imageUri = data.getData();

                    // Convert the selected image to bytes
                    imageBytes = convertImageToBytes(imageUri);

                    // Display image
                    imageView.setImageURI(imageUri);
                }
            }
        }
    }

    // Function to convert an image to a byte array for DB storing
    private byte[] convertImageToBytes(Uri imageUri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    // Function to convert an bitmap to a byte array for DB storing
    private byte[] convertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void returnHome(View view) {
        Intent intent = new Intent(FirstFragment.this, MainActivity.class);
        startActivity(intent);
    }

    public void insertNote(View view) {
        Intent intent = new Intent(FirstFragment.this, MainActivity.class);

        String title = etNoteTitle.getText().toString().trim();
        String description = etNoteDescription.getText().toString().trim();
        int color = ((ColorDrawable) linearLayout.getBackground()).getColor();

        if (!title.isEmpty()) {
            Note note = new Note(-1, title, description, color, imageBytes);
            NotesDBHelper dataBaseHelper = new NotesDBHelper(FirstFragment.this);
            dataBaseHelper.addOne(note);
        } else {
            Toast.makeText(FirstFragment.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
    }

    public void changeBackgroundColor(View view) {
        int newColor = 0;

        if (view == yellowButton) {
            newColor = Color.parseColor("#FFFF00");
        } else if (view == orangeButton) {
            newColor = Color.parseColor("#FFA600");
        } else if (view == blueButton) {
            newColor = Color.parseColor("#00DDDD");
        }

        view.setBackgroundColor(newColor);
        linearLayout.setBackgroundColor(newColor);
    }

    public void captureImage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void insertImage(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQ_CODE);
    }
}