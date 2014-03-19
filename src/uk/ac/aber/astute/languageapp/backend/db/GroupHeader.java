package uk.ac.aber.astute.languageapp.backend.db;

import java.util.ArrayList;
import java.util.HashMap;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

/**
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class GroupHeader {

	private static LanguageDatabase ldb;
	private static HashMap<String, GroupHeader> cache;
	
	private int id;
	private int lessonId;
	private String english;
	private String north;
	private String south;
	private int memberType;
	private String groupType;
	private int ordering;
	
	public static final String GRAMMAR_GROUP  = "grammar";
	public static final String EXERCISE_GROUP = "question";
	public static final String PATTERN_GROUP  = "pattern";
	public static final String VOCAB_GROUP    = "vocabulary";
	public static final String ABOUT_GROUP    = "about";
	public static final String DIALOG_GROUP   = "dialog";
	
	private static final String g_str = "group_header";
	
	
	public static GroupHeader getGroupHeaderById(Context context, int id) {
		
		GroupHeader instance = null;
		
		if (cache == null) cache = new HashMap<String, GroupHeader>();
		if (cache.get(""+id) != null) return cache.get(""+id);
		
		ldb = LanguageDatabase.getInstance(context);
		
		String args[] = new String[] { ""+id };
		String id_eq = "id=?";
		
		try {
		
			instance = new GroupHeader();
			instance.setId(id);
			instance.setLessonId(
					ldb.getIntegerWithQuery(g_str, "lessonId", id_eq, args));
			instance.setEnglish(
					ldb.getStringWithQuery(g_str, "english", id_eq, args));
			instance.setNorth(
					ldb.getStringWithQuery(g_str, "north", id_eq, args));
			instance.setSouth(
					ldb.getStringWithQuery(g_str, "south", id_eq, args));
			instance.setMemberType(
					ldb.getIntegerWithQuery(g_str, "memberType", id_eq, args));
			instance.setGroupType(
					ldb.getStringWithQuery(g_str, "groupType", id_eq, args));
			instance.setOrdering(
					ldb.getIntegerWithQuery(g_str, "ordering", id_eq, args));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting group headers: " + dnfe.toString());
			instance = null;
		}
		
		if (instance != null) cache.put(""+id, instance);
		return instance;
		
	}
	
	public static ArrayList<GroupHeader> getAllGroupHeadersByType(Context context, String groupType) {
		
		ArrayList<GroupHeader> groupHeaders = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);
		
		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(g_str, "id", "groupType=?", new String[] { ""+groupType},"ordering");
			groupHeaders = new ArrayList<GroupHeader>();
			
			for (int i = 0; i < ids.size(); i++)
				if (GroupHeader.getGroupHeaderById(context, ids.get(i)) != null)
					groupHeaders.add(GroupHeader.getGroupHeaderById(context, ids.get(i)));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting group headers: " + dnfe.toString());
			groupHeaders = null;
			
		}
		
		return groupHeaders;
		
	}
	
	public static ArrayList<GroupHeader> getGroupHeaders(Context context, int lessonId, String groupType) {
		
		ArrayList<GroupHeader> groupHeaders = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);

		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(g_str, "id", "lessonId=? AND groupType=?",
					new String[] {""+lessonId, groupType},"ordering");
			groupHeaders = new ArrayList<GroupHeader>();
			
			for (int i = 0; i < ids.size(); i++)
				if (GroupHeader.getGroupHeaderById(context, ids.get(i)) != null)
					groupHeaders.add(GroupHeader.getGroupHeaderById(context, ids.get(i)));
			
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting group headers: " + dnfe.toString());
			groupHeaders = null;

		}
		
		return groupHeaders;
		
	}
	
	private void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	private void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}
	
	public int getLessonId() {
		return this.lessonId;
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
		
		if (this.north != null)
			if (!this.north.isEmpty())
				return this.north;
		
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
	
	private void setMemberType(int memberType) {
		this.memberType = memberType;
	}
	
	public int getMemberType() {
		return this.memberType;
	}
	
	private void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	public String getGroupType() {
		return this.groupType;
	}
	
	private void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	
	public int getOrdering() {
		return this.ordering;
	}
	
	/**
	 * This function is used to check if there are any exercises associated
	 * with the lessonId.
	 * 
	 * @param context The context that we're working in.
	 * 
	 * @param lessonId The lession Id we're checking against, also know as 
	 *        the unit we're working with.
	 *        
	 * @return True if there are any exercises, otherwise in any other
	 *         circumstance (error, or failure to find results) a value of
	 *         false.
	 */
	public static boolean lessonHasExercises(Context context, int lessonId) {
		return GroupHeader.lessonHasX(context, lessonId, GroupHeader.EXERCISE_GROUP);
	}

	/**
	 * This function is used to check if there are any exercises associated
	 * with the lessonId.
	 * 
	 * @param context The context that we're working in.
	 * 
	 * @param lessonId The lesson Id we're checking against, also know as 
	 *        the unit we're working with.
	 *        
	 * @return True if there are any exercises, otherwise in any other
	 *         circumstance (error, or failure to find results) a value of
	 *         false.
	 */
	public static boolean lessonHasPatterns(Context context, int lessonId) {
		return GroupHeader.lessonHasX(context, lessonId, GroupHeader.PATTERN_GROUP);
	}
	
	/**
	 * This function is used to check if there are any exercises associated
	 * with the lessonId.
	 * 
	 * @param context The context that we're working in.
	 * 
	 * @param lessonId The lession Id we're checking against, also know as 
	 *        the unit we're working with.
	 *        
	 * @return True if there are any exercises, otherwise in any other
	 *         circumstance (error, or failure to find results) a value of
	 *         false.
	 */
	public static boolean lessonHasGrammar(Context context, int lessonId) {
		return GroupHeader.lessonHasX(context, lessonId, GroupHeader.GRAMMAR_GROUP);
	}
	
	/**
	 * This function is used to check if there are any exercises associated
	 * with the lessonId.
	 * 
	 * @param context The context that we're working in.
	 * 
	 * @param lessonId The lession Id we're checking against, also know as 
	 *        the unit we're working with.
	 *        
	 * @return True if there are any exercises, otherwise in any other
	 *         circumstance (error, or failure to find results) a value of
	 *         false.
	 */
	public static boolean lessonHasDialogs(Context context, int lessonId) {
		return GroupHeader.lessonHasX(context, lessonId, GroupHeader.DIALOG_GROUP);
	}
	
	/**
	 * This function is used to check if there are any exercises associated
	 * with the lessonId.
	 * 
	 * @param context The context that we're working in.
	 * 
	 * @param lessonId The lession Id we're checking against, also know as 
	 *        the unit we're working with.
	 *        
	 * @return True if there are any exercises, otherwise in any other
	 *         circumstance (error, or failure to find results) a value of
	 *         false.
	 */
	public static boolean lessonHasVocabulary(Context context, int lessonId) {
		return GroupHeader.lessonHasX(context, lessonId, GroupHeader.VOCAB_GROUP);
	}
	
	private static boolean lessonHasX(Context context, int lessonId, String x) {
		
		ldb = LanguageDatabase.getInstance(context);
		
		String    tn = "group_header";
		String    ak = "lessonId=? AND groupType=?";
		String [] av = new String[] { lessonId+"", x };
		
		int counter = 0;
		
		try {
			
			counter = ldb.getCountWithQuery(tn, "memberType", ak, av);
			
		} catch (DataNotFoundException dnfe) {
			
			return false;
			
		}
		
		return (counter > 0);
		
	}
	
	/** 
	 * Whilst we do not have single instances of Appearance objects, we do not
	 * wish to allow Appearance objects to be created that do not match the
	 * database settings. 
	 */
	private GroupHeader() { }

	

	
}
