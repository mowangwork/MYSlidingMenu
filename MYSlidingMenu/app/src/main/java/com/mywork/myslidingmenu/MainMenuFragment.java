package com.mywork.myslidingmenu;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

/**
 * Created by 沫 on 2015/4/21.
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
        String title = pref.getTitle().toString();
        if(title.equals(this.getString(R.string.left_menu))) {
            Intent intent = new Intent(this.getActivity(), LeftMenuActivity.class);
            startActivity(intent);
        }
        return true;
    }
}