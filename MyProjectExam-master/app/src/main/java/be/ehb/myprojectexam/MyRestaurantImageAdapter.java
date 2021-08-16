package be.ehb.myprojectexam;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import be.ehb.myprojectexam.models.entities.MyRestaurantImage;

public class MyRestaurantImageAdapter extends RecyclerView.
    Adapter<MyRestaurantImageAdapter.MyRestaurantImageHolder> implements Filterable {

    // Filter
    private List<MyRestaurantImage> restaurantImageList= new ArrayList<>();
    private List<MyRestaurantImage> restaurantImageListFull= new ArrayList<>();

    private onRestaurantImageClickListener listener;

    @SuppressLint("NotifyDataSetChanged")
    public void setRestaurantImageList(List<MyRestaurantImage> restaurantImageList) {
        this.restaurantImageList = restaurantImageList;
        notifyDataSetChanged();
    }

    public MyRestaurantImageAdapter(List<MyRestaurantImage> restaurantImageList) {
        this.restaurantImageList = restaurantImageList;
        restaurantImageListFull = new ArrayList<>(restaurantImageList);

    }

    public MyRestaurantImageAdapter() {

    }




    /*
     settermethode van ONCLICK_ dit moet gelinkt worden met
     design in fragment home - dus in de holder hieronder in de constructur MyRestaurantImageHolder
    */

    public void setListener(onRestaurantImageClickListener listener) {
        this.listener = listener;
    }


    //https://www.youtube.com/watch?v=reSPN7mgshI
    /*
    public void setRestaurantImageList(List<MyRestaurantImage> restaurantImageList) {
        this.restaurantImageList = restaurantImageList;

        // om de wijzigende data aan te tonen die komen uit de database dat
        // weergeven wordt via Recyclerview
        notifyDataSetChanged();
        // en dit met - recyclerview in mainactivity

        // Nu de weergave in database
    }

     */
        // for updating and creating from onRestaurantImageClickListener no body
    public interface onRestaurantImageClickListener{

        void onRestaurantImageClick(MyRestaurantImage myRestaurantImage);
        }

    @NonNull
    @Override
    public MyRestaurantImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
            // homefragment item example
            .inflate(R.layout.fragment_home,parent,false);

        /*
        MyRestaurantImageHolder myRestaurantImageHolder= new MyRestaurantImageHolder();
        return myRestaurantImageHolder*/
        return new MyRestaurantImageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyRestaurantImageHolder holder, int position) {
    // door een ENTITY te maken van MyRestaurantImageklasse  en dan printen in holder_ fragment home layout
        MyRestaurantImage myRestaurantImage= restaurantImageList.get(position);
        holder.tv_title_home.setText(myRestaurantImage.getImage_title());
        //Log.d("title",myRestaurantImage.getImage_title());
        holder.tv_description_home.setText(myRestaurantImage.getImage_description());
        holder.tv_address_home.setText(myRestaurantImage.getImage_address());
        // nood aan decoder
        holder.image_home_restaurant.setImageBitmap(BitmapFactory.
            decodeByteArray(myRestaurantImage.getImage()
            ,0,myRestaurantImage.getImage().length));



    }
    // DELETE en deze methode moet geschreven worden in Onswip main
    public MyRestaurantImage getPosition(int position){
        return restaurantImageList.get(position);
    }

    @Override
    public int getItemCount() {
        // aantal rijen in recyclerview als content
        return restaurantImageList.size();
    }

    @Override
    public Filter getFilter() {
        return restaurantFilter;

    }

    //https://www.youtube.com/watch?v=sJ-Z9G0SDhc filters search op recyclerView
    private Filter restaurantFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<MyRestaurantImage> filteredRestuarantList= new ArrayList<>();
           if (constraint==null||constraint.length()==0){
            // alle resultaten tonen
               filteredRestuarantList.addAll(restaurantImageListFull);
        }
           else {

            String filterPattern= constraint.toString().toLowerCase().trim();
            for(MyRestaurantImage restaurant:restaurantImageListFull){
                if(restaurant.getImage_title().toLowerCase().contains(filterPattern)){
                    filteredRestuarantList.add(restaurant);
                }

            }


           }
            FilterResults restaurantFilterResults= new FilterResults();
           restaurantFilterResults.values=filteredRestuarantList;

           return restaurantFilterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            restaurantImageList.clear();
            // kijk naar de parameters van de methode- en casten in een lijst
        restaurantImageList.addAll((List)(results.values));
        notifyDataSetChanged();
        }
    };


        public class MyRestaurantImageHolder extends RecyclerView.ViewHolder{
        // volgens de componenten van CardDesign homefragment
        private ImageView image_home_restaurant;
        private TextView tv_title_home,tv_description_home,tv_address_home;

        public MyRestaurantImageHolder(@NonNull View itemView) {

            super(itemView);
            // printen van data op de componenten van home-fragment-layout
            image_home_restaurant=itemView.findViewById(R.id.image_home_restaurant);
            tv_title_home = itemView.findViewById(R.id.tv_title_home);
            tv_description_home = itemView.findViewById(R.id.tv_description_home);
            tv_address_home = itemView.findViewById(R.id.tv_address_home);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //maken van een position variable en overdracht naar de adapter
                    int position=getAdapterPosition();
                    // in de recyclerviewcomponent
                    if (listener !=null && position !=RecyclerView.NO_POSITION){
                        listener.onRestaurantImageClick(restaurantImageList.get(position));
                    }

                    // en dit moet dan methode komen in de Main myRestaurantImageAdapter.setListener
                }
            });

        }
    }
}
