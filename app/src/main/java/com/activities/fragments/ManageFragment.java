package com.activities.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ManageFragment {


    public static void moveToFragment(FragmentActivity activity,Fragment fragment,int containerViewId) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .setReorderingAllowed(true);
        fragmentTransaction.replace (containerViewId, fragment);
        fragmentTransaction.commit();
    }


}
