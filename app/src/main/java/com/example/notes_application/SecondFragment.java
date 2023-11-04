package com.example.notes_application;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class SecondFragment extends AppCompatActivity {

    // Initialize the coloured Buttons
    private Button yellowButton;
    private Button orangeButton;
    private Button blueButton;
    private LinearLayout linearLayout; // Initialize the Layout
    byte[] imageBytes;
    private final int GALLERY_REQ_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;
    int id;

    EditText etNoteTitle, etNoteDescription;

    ImageView imageView;

    NotesDBHelper dataBaseHelper = new NotesDBHelper(SecondFragment.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_fragment);

        Intent intent = getIntent();

        // Get values passed from the main activity to correctly set the selected note
        id = intent.getIntExtra("note_id", -1);
        String title = intent.getStringExtra("note_title");
        String description = intent.getStringExtra("note_description");
        int color = intent.getIntExtra("note_color", Color.WHITE);
        byte[] image = intent.getByteArrayExtra("note_image");

        imageBytes = image;

        // Find Views
        etNoteTitle = findViewById(R.id.titleText);
        etNoteDescription = findViewById(R.id.descriptionText);
        imageView = findViewById(R.id.imageView); // Initialize the ImageView

        // component identification by ID
        linearLayout = findViewById(R.id.linearLayout);
        yellowButton = findViewById(R.id.yellowButton);
        orangeButton = findViewById(R.id.orangeButton);
        blueButton = findViewById(R.id.blueButton);

        // Set the note with the data passed over to show the selected note
        etNoteTitle.setText(title);
        etNoteDescription.setText(description);
        linearLayout.setBackgroundColor(color);

        // Set the image if it exists
        if (image != null) {
            Bitmap imageBitmap = convertBytesToImage(image);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
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

    // Function to convert a byte array to an image to later display it in note
    private Bitmap convertBytesToImage(byte[] imageBytes) {
        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }

    // Function to convert an bitmap to a byte array for DB storing
    private byte[] convertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void returnHome(View view) {
        Intent intent = new Intent(SecondFragment.this, MainActivity.class);
        startActivity(intent);
    }

    public void insertNote(View view) {
        Intent intent = new Intent(SecondFragment.this, MainActivity.class);

        Note note;

        String title = etNoteTitle.getText().toString();

        String description = etNoteDescription.getText().toString();

        int color = ((ColorDrawable) linearLayout.getBackground()).getColor();

        if (!title.isEmpty()) {
            try {
                note = new Note(id, title, description, color, imageBytes);

            } catch (Exception e) {
                Toast.makeText(SecondFragment.this, "Error creating note", Toast.LENGTH_SHORT).show();
                note = new Note(-1, "error", "error", 0, null);
            }
            dataBaseHelper.updateNote(note);

        } else {
            Toast.makeText(SecondFragment.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
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