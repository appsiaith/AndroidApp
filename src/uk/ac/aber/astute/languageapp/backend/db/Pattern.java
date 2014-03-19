package uk.ac.aber.astute.languageapp.backend.db;


import java.util.ArrayList;

import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.gui.utils.Files;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.SparseArray;

/**
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class Pattern {

	private static LanguageDatabase ldb;
	private static SparseArray<Pattern> cache;
	
	private int id;
	private int groupId;
	private String english;
	private String north;
	private String south;
	private String audioNorth;
	private String audioSouth;
	private int ordering;
	private int unit;
	private Context context;
	
	private static final String p_str = "Pattern";
	
	
	public static Pattern getPatternById(Context context, int id) {
		
		Pattern instance = null;
		
		if (cache == null) cache = new SparseArray<Pattern>();
		if (cache.get(id) != null) return cache.get(id);
		
		ldb = LanguageDatabase.getInstance(context);
		
		String args[] = new String[] { ""+id };
		String id_eq = "id=?";
		
		try {
		
			instance = new Pattern();
			instance.setId(id);
			instance.setGroupId(
					ldb.getIntegerWithQuery(p_str, "groupId", id_eq, args));
			GroupHeader gh = GroupHeader.getGroupHeaderById(context, 
					instance.getGroupId());
			if (gh != null) instance.setUnit(gh.getLessonId());
			instance.setEnglish(
					ldb.getStringWithQuery(p_str, "english", id_eq, args));
			instance.setSouth(
					ldb.getStringWithQuery(p_str, "south", id_eq, args));
			instance.setNorth(
					ldb.getStringWithQuery(p_str, "north", id_eq, args));
			instance.setAudioNorth(
					ldb.getStringWithQuery(p_str, "audioNorth", id_eq, args));
			instance.setAudioSouth(
					ldb.getStringWithQuery(p_str, "audioSouth", id_eq, args));
			instance.setOrdering(
					ldb.getIntegerWithQuery(p_str, "ordering", id_eq, args));
			instance.setContext(context);
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting pattern: " + dnfe.toString());
			instance = null;
		}
		
		if (instance != null) cache.put(id, instance);
		return instance;
		
	}
	
	public static ArrayList<Pattern> getPatternByGroup(Context context, int groupId) {
		
		ArrayList<Pattern> list = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);

		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(p_str, "id", "groupId=?",
					new String[] {""+groupId}, "ordering");
			list = new ArrayList<Pattern>();
			
			for (int i = 0; i < ids.size(); i++)
				if (Pattern.getPatternById(context, ids.get(i)) != null)
					list.add(Pattern.getPatternById(context, ids.get(i)));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting pattern: " + dnfe.toString());
			list = null;

		}
		
		return list;
		
	}
	
	private void setContext(Context context) {
		this.context = context;
	}
	
	private Context getContext() {
		return this.context;
	}
	
	private void setId(int id) {
		this.id = id;
	}
	
	private void setUnit(int unit) {
		this.unit = unit;
	}
	
	private int getUnit() {
		return this.unit;
	}
	
	public int getId() {
		return this.id;
	}

	private void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public int getGroupId() {
		return this.groupId;
	}
	
	private void setEnglish(String english) {
		this.english = english;
	}
	
	public String getEnglish() {
		return this.english;
	}
	
	private void setNorth(String north) {
		this.north = north;
	}
	
	private String getNorth() {
		if (this.north != null) {
			if (!this.north.isEmpty()) {
				return this.north;
			}
		}
		
		return this.getSouth();
	}
	
	private void setSouth(String south) {
		this.south = south;
	}
	
	private String getSouth() {
		return this.south;
	}
	
	public String getLanguage() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getNorth();
		return this.getSouth();
	}
	
	private void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	
	public int getOrdering() {
		return this.ordering;
	}
	
	private void setAudioNorth(String audioNorth) {
		if (audioNorth != null && !audioNorth.isEmpty())
			this.audioNorth = Strings.extraFillString(
				Config.getInstance(context).getAudioFolder() + "/", 
				this.getUnit()) + audioNorth;
		else
			this.audioNorth = null;
	}
	
	private void setAudioSouth(String audioSouth) {
		this.audioSouth = Strings.extraFillString(
				Config.getInstance(context).getAudioFolder() + "/", 
				this.getUnit()) + audioSouth;
	}
	
	private MediaPlayer getAudioSouth() {
		return Files.getAudioFile(this.getContext(), this.audioSouth);
	}
	
	private MediaPlayer getAudioNorth() {

		if (this.audioNorth != null)
			if (!this.audioNorth.isEmpty())
				return Files.getAudioFile(this.getContext(), this.audioNorth);
		
		return this.getAudioSouth();
		
	}
	
	public MediaPlayer getAudioLanguage() {
		
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getAudioNorth();
		
		return this.getAudioSouth();
		
	}
	
	/** 
	 * Whilst we do not have single instances of Appearance objects, we do not
	 * wish to allow Appearance objects to be created that do not match the
	 * database settings. 
	 */
	private Pattern() { }
	
}
