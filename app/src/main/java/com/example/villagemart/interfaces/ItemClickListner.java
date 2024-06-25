package com.example.villagemart.interfaces;

import android.view.View;                                               //This is linked to the Search Activity

public interface ItemClickListner {
    void onClick(View view,int position,boolean isLongClick);
}
