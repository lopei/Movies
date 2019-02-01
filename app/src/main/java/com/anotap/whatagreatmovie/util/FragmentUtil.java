package com.anotap.whatagreatmovie.util;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.anotap.whatagreatmovie.ui.MainActivity;
import com.anotap.whatagreatmovie.R;

/**
 * Created by alan on 11.05.2018.
 * The class that handles fragment change in {@link MainActivity}
 */


public class FragmentUtil {
    public static void replaceFragment(final FragmentManager manager, Fragment fragment) {
        replaceFragment(manager, fragment, true);
    }

    public static void replaceFragment(final FragmentManager manager, Fragment fragment,
                                       final boolean addToBackStack) {
        final FragmentTransaction fTrans;
        fTrans = manager.beginTransaction();
        fTrans.replace(R.id.container, fragment, fragment.getClass().getSimpleName());

        fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        /**
         *  this block of the code is used in Handler to avoid bug in native code when a fragment
         *  is overlaid by another fragment right after the replace call
         */

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                if (addToBackStack) {
                    fTrans.addToBackStack(null);
                } else {
                    try {
                        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
                fTrans.commitAllowingStateLoss();
            }
        });

    }
}
