package com.mywork.myslidingmenu.effect;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mywork.myslidingmenu.R;
import com.mywork.myslidingmenu.SampleAdapter;
import com.mywork.myslidingmenulibrary.MYSlidingMenu;

public class LockInterfaceDynamicActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_interface_dynamic);

        MYSlidingMenu menu = new MYSlidingMenu(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menu.addView(R.layout.view_interface);
        int leftWidth = (int) getResources().getDimension(R.dimen.menuWidth);
        int rightWidth = (int) getResources().getDimension(R.dimen.menuWidth);
        menu.setLeftView(R.layout.view_left,leftWidth);
        menu.setRightView(R.layout.view_right,rightWidth);
        menu.setLockInterface(true);

        this.addContentView(menu, params);

        SampleAdapter adapter = new SampleAdapter(this);
        for (int i = 0; i < 20; i++) {
            adapter.add(new SampleAdapter.SampleItem("Sample List", android.R.drawable.ic_menu_search));
        }
        ListView leftlist = (ListView) this.findViewById(R.id.left_list);
        leftlist.setAdapter(adapter);
        ListView rightlist = (ListView) this.findViewById(R.id.right_list);
        rightlist.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lock_interface_dynamic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
