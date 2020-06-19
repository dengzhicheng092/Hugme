package com.qboxus.hugme.Code_Classes;

import java.util.ArrayList;

import com.qboxus.hugme.All_Model_Classes.SavedAddress;

public  interface SavedPlaceListener {
    public void onSavedPlaceClick(ArrayList<SavedAddress> mResultList, int position);
}
