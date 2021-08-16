package be.ehb.myprojectexam.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import be.ehb.myprojectexam.models.entities.MyRestaurantImage;



@Dao
public interface MyRestaurantImageDAO {

@Insert
    void insert(MyRestaurantImage myRestaurantImage);

@Update
    void update(MyRestaurantImage myRestaurantImage);

@Delete
    void delete (MyRestaurantImage myRestaurantImage);

@Query("SELECT * FROM my_restaurant_image ORDER BY image_id ASC")
LiveData<List<MyRestaurantImage>> getAllMyRestaurantImages();

}


