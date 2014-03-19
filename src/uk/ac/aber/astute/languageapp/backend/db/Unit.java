package uk.ac.aber.astute.languageapp.backend.db;

import java.util.ArrayList;

import uk.ac.aber.astute.languageapp.backend.Tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseArray;


@SuppressLint("NewApi")
public class Unit {

	public static final String ENGLISH_UNIT = "ENGLISH_UNIT_KEY";
	public static final String NORTH_UNIT = "NORTH_UNIT_KEY";
	public static final String SOUTH_UNIT = "SOUTH_UNIT_KEY";
	
	private int id;
	
	private static LanguageDatabase ldb;
	private static SparseArray<Unit> cache;
	private String english;
	private String south;
	private String north;
	
	private static final String u_str = "lesson";
	
	/**
	 * Get the unit identified by id from the cache or database.
	 * 
	 * @param context The context we're working in.
	 * @param id The id of the unit we're trying to access.
	 * 
	 * @return The unit from the cache or database, or null if the unit 
	 *         was not found.
	 */
	public static Unit getUnitById(Context context, int id) {
		
		Unit unitInstance = null;
		
		if (cache == null) cache = new SparseArray<Unit>();
		if (cache.get(id) != null) return cache.get(id);
		
		ldb = LanguageDatabase.getInstance(context);
		
		try {
			
			String args[] = new String[] { ""+id };
			String id_eq = "id=?";
			
			unitInstance = new Unit();
			unitInstance.setId(id);
			unitInstance.setEnglish(
					ldb.getStringWithQuery(u_str, "english", id_eq, args));
			unitInstance.setNorth( 
					ldb.getStringWithQuery(u_str, "north", id_eq, args));
			unitInstance.setSouth( 
					ldb.getStringWithQuery(u_str, "south", id_eq, args));
	
		} catch (DataNotFoundException dnfe) {
			
			unitInstance = null;
			
		}
		
		if (unitInstance != null) cache.put(id, unitInstance);
		return unitInstance;
		
	}
	
	/**
	 * Gets all the available units from the database. Note, this also means 
	 * that all the units will be cached. 
	 * 
	 * @param context The context we're working in.
	 * 
	 * @return An array list of all the units found in the database, otherwise 
	 *         null if no units were found.
	 */
	public static ArrayList<Unit> getAllUnits(Context context) {
		
		ArrayList<Unit> units = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);

		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(u_str, "id", null, null,"ordering" );
			units = new ArrayList<Unit>();
			
			for (int i = 0; i < ids.size(); i++)
				if (Unit.getUnitById(context, ids.get(i)) != null)
					units.add(Unit.getUnitById(context, ids.get(i)));
			
			
		} catch (DataNotFoundException dnfe) {
			
			units = null;

		}
		
		return units;
	}
	
	/** No one else should ever be able to make an instance of this class. */
	private Unit() { }
	
	private void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}	

	
	private String getNorth() {
		if (this.north != null)
			if (!this.north.isEmpty())
				return this.north;
		return this.getSouth();
	}
	
	private String getSouth() {
		return this.south;
	}
	
	public String getLanguage() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getNorth();
		return this.getSouth();
	}
	
	public String getEnglish() {
		return this.english;
	}
	
	private void setNorth(String north) {
		this.north = north;
	}
	
	private void setSouth(String south) {
		this.south = south;
	}
	
	private void setEnglish(String english) {
		this.english = english;
	}
	
	
}