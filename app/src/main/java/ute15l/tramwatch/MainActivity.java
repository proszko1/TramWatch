package ute15l.tramwatch;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private FindPublicTransportFragment findPublicTransportFragment;
    private AboutFragment aboutFragment;
    private AddNotificationsFragment addNotificationsFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Menu menu;

    private boolean alreadyAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        Log.e("sd", "onCreate");

    }

    @Override
    public void onNavigationDrawerItemSelected(int selectedPositionInDrawer) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.e("sd", "try");
    if(!alreadyAdded){
        alreadyAdded = true;
        fragmentManager.beginTransaction()
                .add(R.id.container, findPublicTransportFragment = new FindPublicTransportFragment())
                .add(R.id.container, addNotificationsFragment = new AddNotificationsFragment())
                .add(R.id.container, aboutFragment = new AboutFragment())
                .commit();
    }
        fragmentManager.beginTransaction()
                .hide(findPublicTransportFragment)
                .hide(addNotificationsFragment)
                .hide(aboutFragment)
                .show(getProperFragment(selectedPositionInDrawer))
                .commit();
        Log.e("sd", "trys");
    }

    private Fragment getProperFragment(int selectedPositionInDrawer){
        if(selectedPositionInDrawer == 0){
            if (findPublicTransportFragment == null){
                Log.e("sd", "publicnull");
                findPublicTransportFragment = new FindPublicTransportFragment();
            }
            Log.e("sd", "wybrano");
            return findPublicTransportFragment;
        } else if (selectedPositionInDrawer == 1){
            if (addNotificationsFragment == null){
                addNotificationsFragment = new AddNotificationsFragment();
            }
            return addNotificationsFragment;
        } else {
            if(aboutFragment == null){
                aboutFragment = new AboutFragment();
            }
            return  aboutFragment;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (!isDrawerOpen()) {
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean isDrawerOpen(){
        return mNavigationDrawerFragment.isDrawerOpen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
