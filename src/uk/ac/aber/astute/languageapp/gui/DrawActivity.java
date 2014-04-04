package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
import uk.ac.aber.astute.APPLICATION_NAME_HERE.R;
import uk.ac.aber.astute.languageapp.backend.AppMenuItem;
import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Unit;
import uk.ac.aber.astute.languageapp.gui.utils.DrawerItemAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.app.FragmentManager;

/**
 * This is the main, and actually the only, activity for the language 
 * application. It is responsible for loading the HomeScreenFragment 
 * and for dealing with requests from the drawer.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.3
 */
@SuppressLint("NewApi")
public class DrawActivity extends Activity implements NotifyOnUpdate {

	private String[] drawerTitles;
	private ListView drawerList;
	private ArrayList<Unit> units;
	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout dv;
	private Switch northSouthSwitch;
	
	/**
	 * This method is used to load the application, build the drawer and
	 * load the first HomeScreenFragment.
	 * 
	 * @param savedInstanceState The saved instance state of the application,
	 *        we use it for getting the last saved state of the application,
	 *        i.e. for checking what unit, group and dialect were selected or
	 *        in use when the application was last closed.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		
		Tracker t = Tracker.getInstance();
		t.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
		
		dv = (DrawerLayout) findViewById(R.id.activity_draw);
		
		this.units = Unit.getAllUnits(this);
		this.drawerTitles = new String[units.size()];
		ArrayList<AppMenuItem> drawerItems = new ArrayList<AppMenuItem>();
		for (int i = 0; i < units.size(); i++) {
			drawerTitles[i] = "Unit " + (i+1);
			String title = units.get(i).getEnglish();
			if (title.length() > 30) {
				title = title.substring(0,30) + "...";
			}
			
			AppMenuItem item = new AppMenuItem(
					"Unit " + (i + 1), title);
			drawerItems.add(item);
		}
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.left_drawer);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER_HORIZONTAL);
		northSouthSwitch = new Switch(this);
		northSouthSwitch.setTextOn("North");
		northSouthSwitch.setTextOff("South");
		northSouthSwitch.setText("Dialect:");
		northSouthSwitch.setTextColor(Color.WHITE);
		northSouthSwitch.setBackgroundColor(Color.argb(0,255,255,255));
		northSouthSwitch.setPadding(7,15,7,15);
		northSouthSwitch.setTextSize(20);
		
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH) {
			northSouthSwitch.setChecked(true);
		} else {
			northSouthSwitch.setChecked(false);
		}
		
		northSouthSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (northSouthSwitch.isChecked())
					Tracker.getInstance().setNorthSouth(Tracker.NorthSouth.NORTH);
				else
					Tracker.getInstance().setNorthSouth(Tracker.NorthSouth.SOUTH);
				
				Log.e("LANG_APP", "Changed dialect to: " + arg1);
			}
		});
		
		
		this.drawerList = new ListView(this);
		this.drawerList.setBackgroundColor(Color.argb(0,255,255,255));
		this.drawerList.setAdapter(new DrawerItemAdapter(this, drawerItems));
		this.drawerList.setOnItemClickListener(
				
				new ListView.OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, 
														int arg2, long arg3) {
						
						Tracker.getInstance().setCurrentUnit(arg2+1);
						dv.closeDrawer(GravityCompat.START);
				
					}
					
				}
		);
		
		layout.setBackgroundColor(Color.argb(180,0,0,0));
		layout.addView(northSouthSwitch);
		layout.addView(drawerList);
		
		Tracker tracker = Tracker.getInstance();
		tracker.setWindowSize(this.getWindowManager().getDefaultDisplay());
		tracker.setCurrentUnit(1);
		tracker.setNorthSouth(Tracker.NorthSouth.SOUTH);
		
		Fragment fragment = new HomeScreenFragment();
		FragmentManager fragmentManager = this.getFragmentManager();
		fragmentManager.beginTransaction().replace(
				R.id.content_frame, fragment).commit();
		
	       drawerToggle = new ActionBarDrawerToggle(
	                this,                 
	                dv,         
	                R.drawable.ic_navigation_drawer,  
	                R.string.drawer_open,  
	                R.string.drawer_close  
	                ) {

	            /** Called when a drawer has settled in a completely closed state. */
	            public void onDrawerClosed(View view) {
	            	   invalidateOptionsMenu(); 
	            }

	            /** Called when a drawer has settled in a completely open state. */
	            public void onDrawerOpened(View drawerView) {
	            	   invalidateOptionsMenu(); 
	            }
	        };
	        
	        dv.setDrawerListener(drawerToggle);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Tracker.getInstance().notifyOnUpdate(this);

		
	}
	
	/**
	 * Causes the drawer toggle object to sync with the window. This is
	 * required so we can have a notification icon in the top left of the app.
	 */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
	
	/**
	 * This method is called when buttons are selected in the menu for the
	 * application. Options are generally settings (for changing the unit)
	 * or the "Home/Back" button in the top right corner.
	 * 
	 * In all instances, this effectively just toggles the slide menu open
	 * and closed.
	 * 
	 * @param item Where the request or callback came from.
	 * 
	 * @return True if we were able to do as requested, otherwise False if 
	 *         the requested option wasn't available or found.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
	        if (dv.isDrawerVisible(GravityCompat.START)) {
	            dv.closeDrawer(GravityCompat.START);
	        } else {
	            dv.openDrawer(GravityCompat.START);
	        }
	        return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void updateDisplay() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH) {
			northSouthSwitch.setChecked(true);
		} else {
			northSouthSwitch.setChecked(false);
		}
		
	}
	

}
