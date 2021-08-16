package be.ehb.myprojectexam.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.ehb.myprojectexam.models.entities.MyRestaurantImage;

@Database(version = 1, entities = {MyRestaurantImage.class})
//@TypeConverters({DateConverters.class})
public abstract class MyRestaurantImageDatabase extends RoomDatabase {

//via de Singleton versie - minstens een statische methode
    private static MyRestaurantImageDatabase instance;
    public abstract MyRestaurantImageDAO myRestaurantImageDAO();

    //nodig voor threads
    //static om dan op de instantie aan te spreken - final om eeuwig
    // Om meer dingen tegelijk uit te voeren

    //public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);


    // om een instantie op te vragen - via een getter
    public static synchronized MyRestaurantImageDatabase getInstance(Context context) {
        // indien null dan builden via een room builder
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext() ,
                MyRestaurantImageDatabase.class,
                "my_restaurant_database")
                .fallbackToDestructiveMigration()
                .build();
// het is de builder die alles opbouwt , abstract omdat interface dao abstract
        }
        return instance;
    }

    /*private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(instance);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Bitmap>{
        private MyRestaurantImageDAO myRestaurantImageDAO;

        public PopulateDbAsyncTask(MyRestaurantImageDatabase myRestaurantImageDatabase) {
            myRestaurantImageDAO = instance.myRestaurantImageDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myRestaurantImageDAO.insert(new MyRestaurantImage("Title  1","Description 1"));
            return null;
        }
    }
    Image moet via Bitmap  zie https://www.nigeapptuts.com/creating-asynctask-android/
    https://stackoverflow.com/questions/45530059/imageviews-picture-does-not-set-with-asynctask

    */



}
