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
	private int currentGrammarGroupId;
	private Point windowSize;
	private SharedPreferences sharedPreferences;
	
	public enum NorthSouth { NORTH, SOUTH };
	private NorthSouth northSouth = NorthSouth.SOUTH;
	
	private ArrayList<NotifyOnUpdate> updaterList;
	
	public static Tracker getInstance() {
		
		if (trackerInstance == null) {
			trackerInstance = new Tracker();
			
			/* Now we should read in what the current unit it, etc. so that
			 * every time we run the app we get the previous settings.
			 */
			
		}
		
		return trackerInstance;
		
	}
	
	private Tracker() {
		this.sharedPreferences = null;
		
	}

	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
		this.currentGroupId = sharedPreferences.getInt("currentGroup", 0);
		this.currentUnit = sharedPreferences.getInt("currentUnit", 0);
		if (sharedPreferences.getBoolean("northSouth", false)) {
			this.northSouth = NorthSouth.NORTH;
		} else {
			this.northSouth = NorthSouth.SOUTH;
		}
		Log.e("LANG_APP", "Shared preferences loaded.");
	}
	
	/**
	 * Adds a object to the notify on update request list. Objects in this list
	 * will get sent notifications when {@link #doNotifications()} is called.
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
	 * updateDisplay() method called.
	 * 
	 * @see #notifyOnUpdate(NotifyOnUpdate)
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
	      	Log.e("LANG_APP", "Shared perferences saved.");
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
		//this.doNotifications();
	}
	
	public void setCurrentGrammarGroupId(int currentGrammarGroupId) {
		this.currentGrammarGroupId = currentGrammarGroupId;
	}
	
	public int getCurrentGrammarGroupId() {
		return this.currentGrammarGroupId;
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
	 * Used to set the widnow size. 
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
	 * Used to get the size of the screen, in pixles.
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