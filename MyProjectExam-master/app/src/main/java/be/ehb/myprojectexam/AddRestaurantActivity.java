package be.ehb.myprojectexam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.media.MediaCodec;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class AddRestaurantActivity extends AddRestaurantActivitySuper {

    private ImageView imageViewAddRestaurant;
    private EditText editTextViewAddRestaurantTitleName;
    private EditText editTextViewAddRestaurantDescription;
    private EditText editTextViewAddRestaurantAddress;
    private Button buttonSave;
    private Bitmap selectedRestaurantImage;
    // creating of objects
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Restaurant");
        setContentView(R.layout.activity_add_restaurant);

        imageViewAddRestaurant = findViewById(R.id.imageViewAddRestaurantImage);
        editTextViewAddRestaurantTitleName=findViewById(R.id.editTextAddRestaurantTitle);
        editTextViewAddRestaurantAddress=findViewById(R.id.editTextTextAddRestaurantAddress);
        editTextViewAddRestaurantDescription=findViewById(R.id.editTextTextAddRestaurantDescription);
        buttonSave=findViewById(R.id.buttonSave);

        /*imageViewAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });*/

        imageViewAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //voor de permission
                // if else
                if (ContextCompat.checkSelfPermission(AddRestaurantActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddRestaurantActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                    Toast.makeText(AddRestaurantActivity.this,
                        " Asking for user permission",
                        Toast.LENGTH_SHORT)
                        .show();

                } else {
                    //Intent to pick a picture
                    Intent imageIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent,
                        2);

                    Toast.makeText(AddRestaurantActivity.this,
                        "Allowed",
                        Toast.LENGTH_SHORT)
                        .show();
                }


            }


        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {



                if(selectedRestaurantImage == null){

                    Toast.makeText(AddRestaurantActivity.this,"Select the Restaurant Image",Toast.LENGTH_SHORT).show();

                }

                else{

                String title = editTextViewAddRestaurantTitleName.getText().toString();
                String description = editTextViewAddRestaurantDescription.getText().toString();
                String address = editTextViewAddRestaurantAddress.getText().toString();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                //scaledImage = makeSmall(selectedRestaurantImage,300);

                scaledImage = doSmall(selectedRestaurantImage,300);

                // theorie Outputstream

                scaledImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);

                byte[] image = outputStream.toByteArray();

                    Intent intent = new Intent();

                    intent.putExtra("title", title);


                    intent.putExtra("description",description);

                    intent.putExtra("address",address);

                    intent.putExtra("image", image);

                    setResult(RESULT_OK,intent);

                    finish();
                }

                //de foto's die wij willen opslaan moeten klein in grootte zijn anders crasht app
                // formaat , de kwaliteit tussen o en 100, OutstreamObject- kan nog een grote afmeting hebben
               // selectedRestaurantImage.compress(Bitmap.CompressFormat.PNG,50,);

            }
        });
    }

    // oude methode
    // doch geen override

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager
                .PERMISSION_GRANTED){

            Intent imageIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imageIntent,
                    2);

            Toast.makeText(AddRestaurantActivity.this,
                    "Allowed",
                    Toast.LENGTH_SHORT)
                    .show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            // dan kan alle data zonder meer door gestuurd worden naar de Mainactivity
            // door op bitmap te klikken krijgen wij een try and catch
            // API
            try {
                if(Build.VERSION.SDK_INT >= 28)
                {
                    ImageDecoder.Source source= ImageDecoder.
                            createSource(this.getContentResolver(),
                                    data.getData());
                    selectedRestaurantImage=ImageDecoder.decodeBitmap(source);
                }
                else
                {

                    selectedRestaurantImage=MediaStore.Images
                            .Media.getBitmap(this.getContentResolver(),
                                    data.getData()
                            );
                    // nu naar de MainActivity test van app fab
                }
                imageViewAddRestaurant.setImageBitmap(selectedRestaurantImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
             nieuwe methode moet blijkbaar via Launcher gebeuren- als ik nog tijd heb probeer ik dit uit
            https://www.youtube.com/watch?v=tUCfIK908i8 voor pictures en images
            https://www.youtube.com/watch?v=HxlAktedIhM voor bitmap
            https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
            */
    //@Override
    // users kiest een picture
    // OnActivity kwam er automatisch door op bitmap te klikken



    // crashing wegens te grote foto
    // foto van bitmaptype
    //https://www.tabnine.com/code/java/methods/Jama.Matrix/postScale

    /*
    public Bitmap makeSmall(Bitmap image, int maxSize) {



    }

     */

    public Bitmap doSmall(Bitmap image, int maxSize)
    {
        int width = image.getWidth();
        int heigth = image.getHeight();
        float ratio = (float) width / (float) heigth;


        if (ratio > 1) {

            width = maxSize;
            heigth = (int)(width/ratio);

        } else {
            heigth = maxSize;
            width = (int) ( heigth *ratio);

        }

        return Bitmap.createScaledBitmap(image, width, heigth,true);
    }


}
