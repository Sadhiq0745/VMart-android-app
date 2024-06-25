package com.example.villagemart.viewholder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villagemart.R;
import com.example.villagemart.interfaces.ItemClickListner;
                                                                                    //Here we are extending the Recycler view and then geting to the INterface an


public class ViewProductsHolder extends RecyclerView.ViewHolder implements OnClickListener {
    public TextView addProductName, addProductPrice;
    private ItemClickListner itemClickListener;
    public ImageView addProductImg;


    public ViewProductsHolder(@NonNull View itemView) {
        super(itemView);

        addProductName=itemView.findViewById(R.id.prod_name);
        addProductPrice=itemView.findViewById(R.id.prod_price);
        addProductImg=itemView.findViewById(R.id.prod_img);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }
    public void setItemClickListener(ItemClickListner itemClickListener){
        this.itemClickListener=itemClickListener;
    }
}
