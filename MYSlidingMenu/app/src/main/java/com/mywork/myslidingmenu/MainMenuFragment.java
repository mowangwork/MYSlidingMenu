package com.mywork.myslidingmenu;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.mywork.myslidingmenu.dynamic.LeftAndRightDynamicActivity;
import com.mywork.myslidingmenu.dynamic.LeftDynamicMenuActivity;
import com.mywork.myslidingmenu.dynamic.RightDynamicMenuActivity;
import com.mywork.myslidingmenu.effect.LockInterfaceActivity;
import com.mywork.myslidingmenu.effect.LockInterfaceDynamicActivity;
import com.mywork.myslidingmenu.general.LeftAndRightActivity;
import com.mywork.myslidingmenu.general.LeftMenuActivity;
import com.mywork.myslidingmenu.general.RightMenuActivity;

/**
 * Created by ĭ on 2015/5/3.
 */
public class MainMenuFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference pref) {
        String dependency = pref.getDependency().toString();
        String title = pref.getTitle().toString();
        if(dependency.equals(getString(R.string.general))) {
            if (title.equals(getString(R.string.left_menu))) {
                Intent intent = new Intent(this.getActivity(), LeftMenuActivity.class);
                startActivity(intent);
            }
            if (title.equals(getString(R.string.right_menu))) {
                Intent intent = new Intent(this.getActivity(), RightMenuActivity.class);
                startActivity(intent);
            }
            if (title.equals(getString(R.string.left_right_menu))) {
                Intent intent = new Intent(this.getActivity(), LeftAndRightActivity.class);
                startActivity(intent);
            }
        } else if(dependency.equals(getString(R.string.dynamic))) {
            if (title.equals(getString(R.string.left_menu))) {
                Intent intent = new Intent(this.getActivity(), LeftDynamicMenuActivity.class);
                startActivity(intent);
            }
            if (title.equals(getString(R.string.right_menu))) {
                Intent intent = new Intent(this.getActivity(), RightDynamicMenuActivity.class);
                startActivity(intent);
            }
            if (title.equals(getString(R.string.left_right_menu))) {
                Intent intent = new Intent(this.getActivity(), LeftAndRightDynamicActivity.class);
                startActivity(intent);
            }
        } else if(dependency.equals(getString(R.string.effect))) {
            if (title.equals(getString(R.string.lock_interface))) {
                Intent intent = new Intent(this.getActivity(), LockInterfaceActivity.class);
                startActivity(intent);
            }
            if(title.equals(getString(R.string.lock_interface_dynamic))) {
                Intent intent = new Intent(this.getActivity(), LockInterfaceDynamicActivity.class);
                startActivity(intent);
            }
        }
        return true;
    }
}