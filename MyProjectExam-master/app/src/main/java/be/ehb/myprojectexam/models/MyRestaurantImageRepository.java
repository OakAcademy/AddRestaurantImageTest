package be.ehb.myprojectexam.models;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

import be.ehb.myprojectexam.models.entities.MyRestaurantImage;

public class MyRestaurantImageRepository {

    private MyRestaurantImageDAO myRestaurantImageDAO;

    private LiveData<List <MyRestaurantImage>> myRestaurantImageList;
    //private final Application mApplication;

    // repository is een subklasse van CONTEXT komt van de MyRestaurantImageDatabaseklasse
    // constructor
    //https://www.youtube.com/watch?v=HhmA9S53XV8&t=422s
    public MyRestaurantImageRepository (Application application ){
        // mApplication=application;
        MyRestaurantImageDatabase myRestaurantImageDatabase= MyRestaurantImageDatabase
            .getInstance(application);

       myRestaurantImageDAO= myRestaurantImageDatabase.myRestaurantImageDAO();
       myRestaurantImageList=myRestaurantImageDAO.getAllMyRestaurantImages();

    }

    public void insert (MyRestaurantImage myRestaurantImage){
    new InsertRestaurantImageAsyncTask(myRestaurantImageDAO).execute(myRestaurantImage);
    }

    public void update(MyRestaurantImage myRestaurantImage){
        new UpdateRestaurantImageAsyncTask(myRestaurantImageDAO).execute(myRestaurantImage);
    }


    public void delete (MyRestaurantImage myRestaurantImage){
        new DeleteRestaurantImageAsyncTask(myRestaurantImageDAO).execute(myRestaurantImage);
    }

    public LiveData<List<MyRestaurantImage>> getAllMyRestaurantImages() {
        return  myRestaurantImageList;
    }

// Room laat geen threads toe op de database vandaar async
    // MOET static zijn _ mag niet terugkeren naar repository- memoryleak
    // ASYNC https://www.youtube.com/watch?v=uKx0FuVriqA

   private static class InsertRestaurantImageAsyncTask extends AsyncTask<MyRestaurantImage,Void, Void> {

       private MyRestaurantImageDAO myRestaurantImageDAO;


       private InsertRestaurantImageAsyncTask(MyRestaurantImageDAO myRestaurantImageDAO) {
            this.myRestaurantImageDAO = myRestaurantImageDAO;
             }

        @Override
        protected Void doInBackground(@NonNull MyRestaurantImage... myRestaurantImages) {
            myRestaurantImageDAO.insert(myRestaurantImages [0]);

            return null;

        }


    }

    private static class UpdateRestaurantImageAsyncTask extends AsyncTask<MyRestaurantImage,Void, Void> {


        private MyRestaurantImageDAO myRestaurantImageDAO;

        private UpdateRestaurantImageAsyncTask(MyRestaurantImageDAO myRestaurantImageDAO) {
            this.myRestaurantImageDAO = myRestaurantImageDAO;
        }

        @Override
        protected Void doInBackground(MyRestaurantImage... myRestaurantImages) {
            myRestaurantImageDAO.update(myRestaurantImages [0]);

            return null;

        }
    }

    private static class DeleteRestaurantImageAsyncTask extends AsyncTask<MyRestaurantImage,Void, Void> {


       private MyRestaurantImageDAO myRestaurantImageDAO;

       private DeleteRestaurantImageAsyncTask(MyRestaurantImageDAO myRestaurantImageDAO) {
            this.myRestaurantImageDAO = myRestaurantImageDAO;
        }

        @Override
        protected Void doInBackground(MyRestaurantImage... myRestaurantImages) {
            myRestaurantImageDAO.delete(myRestaurantImages [0]);

            return null;

        }
    }

}

