package uk.ac.aber.astute.languageapp.backend;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

/**
 * This tracker instance is used to keep track of important variables in the
 * application such as  the current unit, group, etc. It is also used to update
 * and notify objects that need to know when these values change.
 * 
 * Further, at load time, this is responsible for restoring the state of the
 * application, i.e. ensuring the user is presented with the correct unit, and
 * the correct dialect.
 * 
 * It is also responsible for saving such details, when updates happen, i.e.
 * when settings change.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.2
 */
public class Tracker {

	private static Tracker trackerInstance = null;
	private int currentUnit;
	private int currentGroupId;
	private Point windowSize;
	private SharedPreferences sharedPreferences;
	
	public enum NorthSouth { NORTH, SOUTH };
	private NorthSouth northSouth = NorthSouth.SOUTH;
	
	private ArrayList<NotifyOnUpdate> updaterList;
	
	/**
	 * This static method is used to get hold of the current singleton
	 * tracker interface. If a tracker does not yet exist, it creates one.
	 * 
	 * @return The current tracker instance.
	 */
	public static Tracker getInstance() {
		
		if (trackerInstance == null) {
			trackerInstance = new Tracker();
		}
		
		return trackerInstance;
		
	}
	
	/**
	 * This class is a singleton, only the class itself should be able to 
	 * create an instance of the tracker object.
	 */
	private Tracker() {
		this.sharedPreferences = null;
	}

	/**
	 * Shared preferences are used by the Android platform to save the sate of
	 * applications between executions, or when updates occur such as screen
	 * rotations. As our tracker instance keeps track of updates across the 
	 * application, it needs to have access to the shared preferences in order
	 * to save and load in the state of the application.
	 * 
	 * When setting shared preferences, it is important to note that the first
	 * thing that happens is the tracker uses the shared preferences object to
	 * load in the state of the application. As such, setting a "new" shared
	 * preferences file may have unexpected results such as causing the
	 * application to change the current unit to a different one.
	 * 
	 * @param sharedPreferences The shared preferences associated with the 
	 *                          running application.
	 */
	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		
		this.sharedPreferences = sharedPreferences;
		
		this.currentGroupId = sharedPreferences.getInt("currentGroup", 0);
		this.currentUnit = sharedPreferences.getInt("currentUnit", 0);
		
		if (sharedPreferences.getBoolean("northSouth", false)) {
			
			this.northSouth = NorthSouth.NORTH;
			
		} else {
			
			this.northSouth = NorthSouth.SOUTH;
			
		}
		
		Log.i("LANG_APP", "Shared preferences loaded.");
		
	}
	
	/**
	 * Adds an object to the notify on update request list. Objects in this 
	 * list will get sent notifications when {@link #doNotifications()} is 
	 * called.
	 * 
	 * @param nou The instance of the object requesting updates.
	 */
	public void notifyOnUpdate(NotifyOnUpdate nou) {
		
		if (this.updaterList == null)
			this.updaterList = new ArrayList<NotifyOnUpdate>();
		
		if (!this.updaterList.contains(nou))
			this.updaterList.add(nou);
		
	}
	
	/**
	 * Causes all objects that have requested updates to have their 
	 * updateDisplay() method called. In addition, because we are
	 * doing notifications, it implys something has changed, thus we
	 * also save the state of the application in the shared preferences.
	 * 
	 * @see #notifyOnUpdate(NotifyOnUpdate)
	 * @see #setSharedPreferences(SharedPreferences)
	 */
	private void doNotifications() {
		
		if (this.sharedPreferences != null) {
			
	      	SharedPreferences.Editor editor = sharedPreferences.edit();
	      	editor.putInt("currentUnit", this.getCurrentUnit());
	      	editor.putInt("currentGroup", this.getCurrentGroupId());
	      	
	      	if (this.getNorthSouth() == NorthSouth.SOUTH) {
	      		
	      		editor.putBoolean("northSouth", false);
	      		
	      	} else {
	      		
	      		editor.putBoolean("northSouth", true);
	      		
	      	}
	      	
	      	editor.commit();
	      	Log.i("LANG_APP", "Shared perferences saved.");
	      	
		}
	      
		if (this.updaterList != null) 
			for (int i = 0; i < this.updaterList.size(); i++)
				this.updaterList.get(i).updateDisplay();
		
	}
	
	/**
	 * Used to set the current unit id that is in use. Some parts of the
	 * application need this, others do not. It should be updated whenever 
	 * any part of the application starts to use other units.
	 * 
	 * It will cause {@link #doNotifications()} to be called.
	 * 
	 * @param currentUnit The current unit id.
	 */
	public void setCurrentUnit(int currentUnit) {
		
		this.currentUnit = currentUnit;
		this.doNotifications();
		
	}
	
	/**
	 * Used to get the current unit id that is in use. Some parts of the
	 * application need this, others do not.
	 * 
	 * @return The current unit id that is being used.
	 */
	public int getCurrentUnit() {
		
		return this.currentUnit;
		
	}
	
	/**
	 * Used to set the current group header that is in use. Some parts of the
	 * application need this, others do not. It should be updated whenever
	 * any part of the application starts to use other group headers.
	 * 
	 * It will cause {@link #doNotifications()} to be called.
	 * 
	 * @param currentGroupId The current group header id.
	 */
	public void setCurrentGroupId(int currentGroupId) {
		
		this.currentGroupId = currentGroupId;
		
	}
	
	/**
	 * Used to get the current group header that is in use. Some parts of the
	 * application need this, others do not.
	 * 
	 * @return The current group id that is being used.
	 */
	public int getCurrentGroupId() {
		
		return this.currentGroupId;
		
	}
	
	/**
	 * Used to set the window size. 
	 * 
	 * @param display We pass in the display, and the window size is calculated
	 *                from that information.
	 */
	@SuppressLint("NewApi")
	public void setWindowSize(Display display) {
		
		this.windowSize = new Point();
		display.getSize(this.windowSize);
		
	}
	
	/**
	 * Used to get the size of the screen, in pixels.
	 * 
	 * @return The size of the screen display.
	 */
	public Point getWindowSize() {
		
		return this.windowSize;
		
	}
	
	/**
	 * Used to set if we're currently in the North or South dialect
	 * version of the application.
	 * 
	 * @param northSouth NORTH or SOUTH, as appropriate.
	 */
	public void setNorthSouth(NorthSouth northSouth) {
		
		this.northSouth = northSouth;
		this.doNotifications();
		
	}
	
	/**
	 * Used to check if we're currently in the North or South dialect
	 * version of the application.
	 * 
	 * @return true if north, otherwise false.
	 */
	public NorthSouth getNorthSouth() {
		
		return this.northSouth;
		
	}
	
}