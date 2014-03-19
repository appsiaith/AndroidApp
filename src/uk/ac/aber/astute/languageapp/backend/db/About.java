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
public class About {

	private static LanguageDatabase ldb;
	private static SparseArray<About> cache;
	
	private int id;
	private int groupId;
	private String title;
	private String info;
	private String url;
	private LinkType linkType;
	private int ordering;
	private Context context;
	
	public static enum LinkType { LOCAL, URL, DATA };
	
	private static final String a_str = "about";	
	
	public static About getAboutById(Context context, int id) {
		
		About instance = null;
		
		if (cache == null) cache = new SparseArray<About>();
		if (cache.get(id) != null) return cache.get(id);
		
		ldb = LanguageDatabase.getInstance(context);
		
		String args[] = new String[] { ""+id };
		String id_eq = "id=?";
		
		try {
		
			
			instance = new About();
			instance.setContext(context);
			instance.setId(id);
			instance.setGroupId(
					ldb.getIntegerWithQuery(a_str, "groupId", id_eq, args));
			instance.setTitle(
					ldb.getStringWithQuery(a_str, "title", id_eq, args));
			instance.setInfo(
					ldb.getStringWithQuery(a_str, "info", id_eq, args));
			instance.setUrl(
					ldb.getStringWithQuery(a_str, "url", id_eq, args));
			instance.setLinkType(
					ldb.getStringWithQuery(a_str, "linkType", id_eq, args));
			instance.setOrdering(
					ldb.getIntegerWithQuery(a_str, "ordering", id_eq, args));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting about screen: " + dnfe.toString());
			instance = null;
		}
		
		if (instance != null) cache.put(id, instance);
		return instance;
		
	}
	
	private void setContext(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return this.context;
	}
	
	public static ArrayList<About> getAllAbout(Context context) {
		
		ArrayList<About> list = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);

		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(a_str, "id", null, null,"ordering");
			list = new ArrayList<About>();
			
			for (int i = 0; i < ids.size(); i++)
				if (About.getAboutById(context, ids.get(i)) != null)
					list.add(About.getAboutById(context, ids.get(i)));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting about: " + dnfe.toString());
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

	private void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		if (this.linkType == LinkType.LOCAL)
			return Config.getInstance(this.getContext()).getHtmlFolder() + "/" + this.url;
		 
		return this.url;
	}
	
	private void setInfo(String info) {
		this.info = info;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	public void setLinkType(String linkType) {
		if (linkType.equalsIgnoreCase("web"))
			this.linkType = LinkType.URL;
		else if (linkType.equalsIgnoreCase("local"))
			this.linkType = LinkType.LOCAL;
		else
			this.linkType = LinkType.DATA;
	}
	
	public LinkType getLinkType() {
		return this.linkType;
	}
	
	private void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	
	public int getOrdering() {
		return this.ordering;
	}
		
	/** 
	 * Whilst we do not have single instances of About objects, we do not
	 * wish to allow About objects to be created that do not match the
	 * database settings. 
	 */
	private About() { }
	
}
