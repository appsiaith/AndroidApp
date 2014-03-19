package uk.ac.aber.astute.languageapp.backend.db;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

/**
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class Grammar {

	private static LanguageDatabase ldb;
	private static SparseArray<Grammar> cache;
	
	private int id;
	private int groupId;
	private String title;
	private String html;
	
	private static final String g_str = "grammar";
	
	
	public static Grammar getGrammarById(Context context, int id) {
		
		Grammar instance = null;
		
		if (cache == null) cache = new SparseArray<Grammar>();
		if (cache.get(id) != null) return cache.get(id);
		
		ldb = LanguageDatabase.getInstance(context);
		
		String args[] = new String[] { ""+id };
		String id_eq = "id=?";
		
		try {
		
			instance = new Grammar();
			instance.setId(id);		
			instance.setGroupId(
					ldb.getIntegerWithQuery(g_str, "groupId", id_eq, args));
			instance.setTitle(
					ldb.getStringWithQuery(g_str, "title", id_eq, args));
			instance.setHTML(
					ldb.getStringWithQuery(g_str, "html", id_eq, args));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting exercise: " + dnfe.toString());
			instance = null;
		}
		
		if (instance != null) cache.put(id, instance);
		return instance;
		
	}
	
	public static ArrayList<Grammar> getGrammarByGroup(Context context, int groupId) {
		
		ArrayList<Grammar> list = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);

		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(g_str, "id", "groupId=?",
					new String[] {""+groupId},"ordering");
			list = new ArrayList<Grammar>();
			
			for (int i = 0; i < ids.size(); i++)
				if (Grammar.getGrammarById(context, ids.get(i)) != null)
					list.add(Grammar.getGrammarById(context, ids.get(i)));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting exercise: " + dnfe.toString());
			list = null;

		}
		
		return list;
		
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
	
	private void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	private void setHTML(String html) {
		this.html = html;
	}
	
	public String getHTML() {
		return this.html;
	}
	
	
	/** 
	 * Whilst we do not have single instances of Appearance objects, we do not
	 * wish to allow Appearance objects to be created that do not match the
	 * database settings. 
	 */
	private Grammar() { }
	
}
