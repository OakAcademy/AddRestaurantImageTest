package be.ehb.myprojectexam.models.entities;

import androidx.fragment.app.Fragment;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="my_restaurant_image")
public class  MyRestaurantImage {

    //kolommen opmaak van databese
    @PrimaryKey(autoGenerate = true)
    public int image_id;
    public String image_title;
    public String image_description;
    public String image_address;
    // binairy large object- array van binaire bytes
    public byte[] image;

    //POJO


    public MyRestaurantImage(String image_title, String image_description, String image_address, byte[] image) {
        this.image_title = image_title;
        this.image_description = image_description;
        this.image_address = image_address;
        this.image = image;
    }

    public int getImage_id() {
        return image_id;
    }

    public String getImage_title() {
        return image_title;
    }

    public String getImage_description() {
        return image_description;
    }

    public String getImage_address() {
        return image_address;
    }

    public byte[] getImage() {
        return image;
    }
// Bij update is het voldoende om id te kennen
    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
