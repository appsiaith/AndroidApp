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
public class Dialog {

	private static LanguageDatabase ldb;
	private static SparseArray<Dialog> cache;
	
	private int id;
	private int groupId;
	private String south;
	private String north;
	private String audioSouth;
	private String audioNorth;
	private String speaker;
	private int ordering;
	
	private Context context;
	
	private static final String d_str = "dialog";
	
	
	public static Dialog getDialogById(Context context, int id) {
		
		Dialog instance = null;
		
		if (cache == null) cache = new SparseArray<Dialog>();
		if (cache.get(id) != null) return cache.get(id);
		
		ldb = LanguageDatabase.getInstance(context);
		
		String args[] = new String[] { ""+id };
		String id_eq = "id=?";
		
		try {
		
			instance = new Dialog();
			instance.setId(id);		
			instance.setContext(context);
			instance.setGroupId(
					ldb.getIntegerWithQuery(d_str, "groupId", id_eq, args));
			instance.setNorth(
				ldb.getStringWithQuery(d_str, "north", id_eq, args));
			instance.setSouth(
				ldb.getStringWithQuery(d_str, "south", id_eq, args));
			instance.setAudioNorth(
				ldb.getStringWithQuery(d_str, "audioNorth", id_eq, args));
			instance.setAudioSouth(
				ldb.getStringWithQuery(d_str, "audioSouth", id_eq, args));
			instance.setSpeaker(
				ldb.getStringWithQuery(d_str, "speaker", id_eq, args));
			instance.setOrdering(
					ldb.getIntegerWithQuery(d_str, "ordering", id_eq, args));

			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting dialog: " + dnfe.toString());
			instance = null;
		}
		
		if (instance != null) cache.put(id, instance);
		return instance;
		
	}
	
	public static ArrayList<Dialog> getDialogByGroup(Context context, int groupId) {
		
		ArrayList<Dialog> list = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);

		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(d_str, "id", "groupId=?",
					new String[] {""+groupId},"ordering");
			list = new ArrayList<Dialog>();
			
			for (int i = 0; i < ids.size(); i++)
				if (Dialog.getDialogById(context, ids.get(i)) != null)
					list.add(Dialog.getDialogById(context, ids.get(i)));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting dialog: " + dnfe.toString());
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
	
	public int getId() {
		return this.id;
	}

	private void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public int getGroupId() {
		return this.groupId;
	}
	
	private void setSouth(String south) {
		this.south = south;
	}
	
	private String getSouth() {
		return this.south;
	}
	
	private void setNorth(String north) {
		this.north = north;
	}
	
	private String getNorth() {
		
		if (this.north != null)
			if (!this.north.isEmpty())
				return this.north;
		
		return this.getSouth();
	}
	
	public String getLanguage() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getNorth();
		return this.getSouth();
	}
	
	private void setAudioSouth(String audioSouth) {
		this.audioSouth = 
				Config.getInstance(context).getAudioFolder() + 
				"/" + audioSouth;
	}
	
	private MediaPlayer getAudioSouth() {
		return Files.getAudioFile(this.getContext(), this.audioSouth);
	}
	
	private void setAudioNorth(String audioNorth) {
		if (audioNorth != null && !audioNorth.isEmpty())
			this.audioNorth = 
				Config.getInstance(context).getAudioFolder() + 
				"/" + audioNorth;
		else
			this.audioNorth = null;
	}
	
	private MediaPlayer getAudioNorth() {
		if (this.audioNorth != null)
			if (!this.audioNorth.isEmpty())
				return Files.getAudioFile(this.getContext(), this.audioNorth);
		
		return this.getAudioSouth();
		
	}
	
	private void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	
	public String getSpeaker() {
		return this.speaker;
	}
	
	public MediaPlayer getAudioLanguage() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getAudioNorth();
		return this.getAudioSouth();
	}

	private void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	
	public int getOrdering() {
		return this.ordering;
	}
	
	
	/** 
	 * Whilst we do not have single instances of Appearance objects, we do not
	 * wish to allow Appearance objects to be created that do not match the
	 * database settings. 
	 */
	private Dialog() { }
	
}
