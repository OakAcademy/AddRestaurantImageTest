package be.ehb.myprojectexam;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateRestaurantActivity extends AppCompatActivity {

    private ImageView imageViewUpdateRestaurant;
    private EditText editTextViewUpdateRestaurantTitleName;
    private EditText editTextViewUpdateRestaurantDescription;
    private EditText editTextViewUpdateRestaurantAddress;
    private Button buttonUpdate;
    // Variabelen

    private String title;
    private String description;
    private String address;
    private byte[] image;
    private int id;

    private Bitmap selectedRestaurantImage;

    // creating of objects

    private Bitmap scaledImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update Restaurant");
        setContentView(R.layout.activity_update_restaurant);

        imageViewUpdateRestaurant = findViewById(R.id.imageViewUpdateRestaurantImage);
        editTextViewUpdateRestaurantTitleName = findViewById(R.id.editTextUpdateRestaurantTitle);
        editTextViewUpdateRestaurantAddress = findViewById(R.id.editTextTextUpdateRestaurantAddress);
        editTextViewUpdateRestaurantDescription = findViewById(R.id.editTextTextUpdateRestaurantDescription);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        address = getIntent().getStringExtra("address");
        image= getIntent().getByteArrayExtra("image");

        editTextViewUpdateRestaurantTitleName.setText(title);
        editTextViewUpdateRestaurantAddress.setText(description);
        editTextViewUpdateRestaurantDescription.setText(address);
        imageViewUpdateRestaurant.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        imageViewUpdateRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to pick a picture
                Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageIntent,
                    5);
            }

        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();

            }
        });

    }

    public void updateData() {

        // id check


        if(id == -1){

            Toast.makeText(UpdateRestaurantActivity.this,
                "There is a problem ",
                Toast.LENGTH_SHORT).show();
        }

        else{

            String updateTitle = editTextViewUpdateRestaurantTitleName.getText().toString();
            String updateDescription = editTextViewUpdateRestaurantDescription.getText().toString();
            String updateAddress = editTextViewUpdateRestaurantAddress.getText().toString();

            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("updateTitle", updateTitle);
            intent.putExtra("updateDescription", updateDescription);
            intent.putExtra("updateAddress", updateAddress);

            // terugzenden naar MainActivity
            if (selectedRestaurantImage == null) {
                intent.putExtra("image",image);

            } else {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                scaledImage = makeSmall(selectedRestaurantImage, 300);
                scaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);

                byte[] image = outputStream.toByteArray();

                intent.putExtra("updateImage", image);
           }

            setResult(RESULT_OK, intent);

            finish();

        }



    }


    /*
     users kiest een picture
     OnActivity kwam er automatisch door op bitmap te klikken
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK && data != null) {
            // dan kan alle data zonder meer door gestuurd worden naar de Mainactivity
            // door op bitmap te klikken krijgen wij een try and catch
            // API
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.
                            createSource(this.getContentResolver(),
                                    data.getData());
                    selectedRestaurantImage = ImageDecoder.decodeBitmap(source);
                } else {

                    selectedRestaurantImage = MediaStore.Images
                            .Media.getBitmap(this.getContentResolver(),
                                    data.getData());
                    // nu naar de MainActivity test van app fab
                }

                imageViewUpdateRestaurant.setImageBitmap(selectedRestaurantImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // crashing wegens te grote foto
    // foto van bitmaptype
    //https://www.tabnine.com/code/java/methods/Jama.Matrix/postScale

    public Bitmap makeSmall(Bitmap image, int maxSize) {

        int width = image.getWidth();
        int heigth = image.getHeight();
        float ratio = (float) width / (float) heigth;


        if (ratio > 1) {

            width = maxSize;
            heigth = (int) (width / ratio);

        } else {
            heigth = maxSize;
            width = (int) (heigth * ratio);

        }
        return Bitmap.createScaledBitmap(image, width, heigth, true);

    }
}
