package be.ehb.myprojectexam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.ehb.myprojectexam.fragments.FavoritesFragment;
import be.ehb.myprojectexam.fragments.HomeFragment;
import be.ehb.myprojectexam.fragments.MapsFragment;
import be.ehb.myprojectexam.fragments.SearchFragment;
import be.ehb.myprojectexam.models.MyRestaurantImageViewModel;
import be.ehb.myprojectexam.models.entities.MyRestaurantImage;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private MyRestaurantImageViewModel myRestaurantImageViewModel;
    private View rootView;
    private MyRestaurantImageAdapter myRestaurantImageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);




        @Deprecated
            /*setOnNavigation deprecated
            https://github.com/material-components/material-components-android/blob/95a769c373792d2fce70d7ee21f3857f65e9d74e/docs/components/NavigationRail.md
             */
        BottomNavigationView mBottomNavigationView=findViewById(R.id.bottom_nav_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mBottomNavigationListener);

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentsLayoutMain, new HomeFragment())
            .commit();

        /*
         Adapter
         to display de datagegevens in recyclerView
        View rootView = inflater.inflate(R.layout.fragment_home, false);
        */
        //final MyRestaurantImageAdapter

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        myRestaurantImageAdapter = new MyRestaurantImageAdapter();

        recyclerView.setAdapter(myRestaurantImageAdapter);

        /*
        final RecyclerView recyclerView= rootView.findViewById(R.id.fragment_home);
        myRestaurantImageAdapter=new MyRestaurantImageAdapter();
         recyclerView.setAdapter(myRestaurantImageAdapter);
        Viewmodel
        */

        myRestaurantImageViewModel= new ViewModelProvider
            .AndroidViewModelFactory(getApplication())
            .create(MyRestaurantImageViewModel.class);

        myRestaurantImageViewModel.getAllMyRestaurantImages() .observe(MainActivity.this,
                new Observer<List<MyRestaurantImage>>() {
            @Override
            public void onChanged(List<MyRestaurantImage> myRestaurantImages) {
                myRestaurantImageAdapter.setRestaurantImageList(myRestaurantImages);
                // en dan moet men terug naar Adapterklasse notifydatechange methode
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddRestaurantActivity.class);
                startActivityForResult(intent,3);
                // requestCode 3
            }


        });

        // delete onswipe- simple callback in wich direction
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            // drag and drop
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            // fingerswipe
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            /*
                 on the recyclerViewcomponent en zeggen wat als data moet gewist worden
                object van MyRestaurantImageDAO-daardoor een nieuw methode in de Adapterklass
                 return een object myRestaurantImages
                */
                myRestaurantImageViewModel.delete(myRestaurantImageAdapter.getPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        myRestaurantImageAdapter.setListener(new MyRestaurantImageAdapter.onRestaurantImageClickListener() {
            @Override
            public void onRestaurantImageClick(MyRestaurantImage myRestaurantImage) {
                //send

                Intent intent= new Intent(MainActivity.this,UpdateRestaurantActivity.class);
                // nu kan de Id van entity in database overschreven worden
                intent.putExtra("id",myRestaurantImage.getImage_id());
                intent.putExtra("title",myRestaurantImage.getImage_title());
                intent.putExtra("description",myRestaurantImage.getImage_description());
                intent.putExtra("address",myRestaurantImage.getImage_address());
                startActivityForResult(intent,4);
                //send the data naar UpdateActivityklasse en moet de data gelinkt worden met de componenten
                // home-fragment
            }
        });




    }



   // komt van de AddRestaurant


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3 && resultCode == RESULT_OK && data != null){
                /*
                 dan kan alle data zonder meer door gestuurd worden naar de Mainactivity
                 door op bitmap te klikken krijgen wij een try and catch
                */
            String title = data.getStringExtra("title");
            Log.d("title : ",title);
            String description = data.getStringExtra("description");
            String address = data.getStringExtra("address");
            byte[] image = data.getByteArrayExtra("image");

                    /*
                     dan wordt er Object Instance gemaakt van de MyRestaurantImagesKlasse, entity
                     met daarin de fields attributen als een constructor
                    */
            MyRestaurantImage myRestaurantImage = new MyRestaurantImage(title,description,address,image);
                    /*wordt de data opgeslaan in dataVIEWMODEL- My RestaurantViewImageViewModel
                     via de insert methode omschreven in die viewmodel
                */

            myRestaurantImageViewModel.insert(myRestaurantImage);
                    /*en om dit aan te tonen moet er nu code komen in de OnbindViewHolder in de
                     MyRestaurantImageViewHolder
                     */

        }

        if(requestCode == 4 && resultCode == RESULT_OK && data != null){
            /*
             dan kan alle data zonder meer door gestuurd worden naar de Mainactivity
             door op bitmap te klikken krijgen wij een try and catch
            */
            String title = data.getStringExtra("updateTitle");
            String description = data.getStringExtra("updateDescription");
            String address = data.getStringExtra("updateAddress");
            int id = data.getIntExtra("id",-1);
            byte[] image = data.getByteArrayExtra("image");

                /*
                 dan wordt er Object Instance gemaakt van de MyRestaurantImagesKlasse, entity
                 met daarin de fields attributen als een constructor
                */
            MyRestaurantImage myRestaurantImage= new MyRestaurantImage(title,description,address,image);
                /*wordt de data opgeslaan in dataVIEWMODEL- My RestaurantViewImageViewModel
                 via de insert methode omschreven in die viewmodel
            */
            myRestaurantImage.setImage_id(id);

            myRestaurantImageViewModel.update(myRestaurantImage);
                /*en om dit aan te tonen moet er nu code komen in de OnbindViewHolder in de
                 MyRestaurantImageViewHolder
                 */

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mBottomNavigationListener=
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment=null;

                switch(item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment=new HomeFragment();
                        break;
                    case R.id.nav_favorites:
                        selectedFragment=new FavoritesFragment();
                        break;

                    case R.id.nav_search_image:
                        selectedFragment=new SearchFragment();
                        break;
                    case R.id.nav_map:
                        selectedFragment=new MapsFragment();
                        break;


                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsLayoutMain,
                    selectedFragment).commit();
                return true;
            }
        };





}
