package be.ehb.myprojectexam.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import be.ehb.myprojectexam.models.entities.MyRestaurantImage;

public class MyRestaurantImageViewModel extends AndroidViewModel {

    private MyRestaurantImageRepository myRestaurantImageRepository;
    private LiveData<List<MyRestaurantImage>> myRestaurantImageList;

    /*private static MyRestaurantImageDatabase instance;
    public abstract MyRestaurantImageDAO myRestaurantImageDAO();*/

    // Application is een subclass van context
    // met executer abstract

    public MyRestaurantImageViewModel(@NonNull Application application) {
        super(application);

        myRestaurantImageRepository = new MyRestaurantImageRepository(application);
        myRestaurantImageList = myRestaurantImageRepository.getAllMyRestaurantImages();
    }

    public void insert(MyRestaurantImage myRestaurantImage) {
        /*MyRestaurantImageDatabase.databaseExecutor.execute(()->
        {instance.myRestaurantImageDAO().insert(myRestaurantImage);

        });*/

        myRestaurantImageRepository.insert(myRestaurantImage);
    }

    public void update(MyRestaurantImage myRestaurantImage) {
       /* MyRestaurantImageDatabase.databaseExecutor.execute(()->
        {instance.myRestaurantImageDAO().update(myRestaurantImage);

        });*/
        myRestaurantImageRepository.update(myRestaurantImage);
    }

    public void delete(MyRestaurantImage myRestaurantImage) {
        /*MyRestaurantImageDatabase.databaseExecutor.execute(()->
        {instance.myRestaurantImageDAO().delete(myRestaurantImage);

        });*/
        myRestaurantImageRepository.delete(myRestaurantImage);
    }

    public LiveData<List<MyRestaurantImage>> getAllMyRestaurantImages() {
        return myRestaurantImageList;
    }
}
