package uk.ac.aber.astute.languageapp.backend.db;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

/**
 * This class is used to both load in and manage "About" objects from the
 * database, and also when used as an instance, contains specific information
 * about a specific "About" entry in the database. 
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
	
	/**
	 * About objects can contain links to external web sites, or they may also
	 * contain raw HTML, or indeed they may contain links to locally stored
	 * HTML files. This enum is used to define those possibilities.
	 * 
	 * The option LOCAL is used for local HTML files.
	 * The option URL is used for external web sites.
	 * The option DATA is if the database holds raw HTML.
	 * 
	 * @author Michael Clarke <mfc1@aber.ac.uk>
	 * @version 0.1
	 */
	public static enum LinkType { LOCAL, URL, DATA };
	
	private static final String a_str = "about";	
	
	/**
	 * Checks in the cache if we already have an instance of an About object
	 * that matches the given unique id. If we do not, we try and fetch one
	 * from the database. If we manage this, we cache it for later use.
	 * 
	 * @param context The context that the application is working within.
	 * 
	 * @param id The unique identifier for the About object in the 
	 *           cache/database.
	 *           
	 * @return A single instance About object that matches the data for 
	 *         the given id, otherwise null if one was not found.
	 */
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
	
	/**
	 * This method is used to get a list of all the About objects in the
	 * database. We first try and get all our data from the cache, as this is
	 * faster, but if we don't have all the objects we get them from the
	 * database first.
	 * 
	 * In all instances, once objects are retrieved from the database, they 
	 * will be cached for later use, hence the first time this method is called
	 * it may very well execute slower than subsequent calls.
	 * 
	 * @param context The current context that the About instances should 
	 *                exist in.
	 *                
	 * @return A list of all the About objects in the database.
	 */
	public static ArrayList<About> getAllAbout(Context context) {
		
		ArrayList<About> list = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);

		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(a_str, "id", 
														null, null,"ordering");
			
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
	
	/** 
	 * Whilst we do not have single instances of About objects, we do not
	 * wish to allow About objects to be created that do not match the
	 * database settings. 
	 */
	private About() { }
	
	/**
	 * Used to set the current context that the given instance of the About
	 * object is existing in.
	 * 
	 * @param context The current context.
	 */
	private void setContext(Context context) {
		
		this.context = context;
		
	}
	
	/**
	 * Used to get the current context that the given instance of the About
	 * object is existing in.
	 * 
	 * @return The current context.
	 */
	public Context getContext() {
		
		return this.context;
		
	}
	
	/**
	 * Used to set the unique id for the given instance of the About object.
	 * 
	 * @param id The unique id for the current instance.
	 */
	private void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Used to get the unique id for the given instance, it will match that
	 * stored in the database.
	 * 
	 * @return The unique id for the instance, matching that stored in the 
	 *         database.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Used to set the group id for the given instance of the About object.
	 * 
	 * @param groupId The group id for the current instance.
	 */
	private void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	/**
	 * Used to get the current group id for the given instance, it will match
	 * that stored in the database.
	 * 
	 * @return The group id for the instance, matching that stored in the 
	 *         database.
	 */
	public int getGroupId() {
		return this.groupId;
	}
	
	/**
	 * Used to store the title for the given instance.
	 * 
	 * @param title The current title for the given instance.
	 */
	private void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Used to get the current title for the given instance, it will match
	 * that stored in the database.
	 * 
	 * @return The current title for the instance, matching that stored in the
	 *         database.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Used to set the URL string for the given instance.
	 * 
	 * @param url The URL string for the given instance.
	 */
	private void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Returns the URL string for the given instance. It may not entirely match
	 * that in the database. For example, the URL string in the database, if
	 * for a local file rather than a external URL, etc., needs to know where
	 * HTML data is stored. As such the HTML folder location will be appended
	 * to the URL before it is returned.
	 * 
	 * In all instances, the URL returned can just be used, and it will be
	 * correct in one form or another, for the user to not care.
	 * 
	 * @return The URL in an appropriate form, based on the database entry.
	 */
	public String getUrl() {
		
		if (this.linkType == LinkType.LOCAL)
			return Config.getInstance(
					this.getContext()).getHtmlFolder() + "/" + this.url;
		 
		return this.url;
		
	}
	
	/**
	 * Used to set the current "info" for the given instance, it will match
	 * that stored in the database.
	 * 
	 * @param info The current information for the given instance.
	 */
	private void setInfo(String info) {
		
		this.info = info;
		
	}
	
	/**
	 * Used to get the current "info" for the given instance, it will match
	 * that stored in the database.
	 * 
	 * @return The current "info" for the given instance, matching the database
	 *         entry.
	 */
	public String getInfo() {
		
		return this.info;
		
	}
	
	/**
	 * Used to set the current link, or URL type. Strings recognised are "web" 
	 * which means an external URL, "local" which means a local HTML file, and
	 * anything else is treated like "data", i.e. the HTML is in a raw form in
	 * the database.
	 * 
	 * @param linkType The string representation of the link type.
	 */
	public void setLinkType(String linkType) {
		
		if (linkType.equalsIgnoreCase("web"))
			this.linkType = LinkType.URL;
		
		else if (linkType.equalsIgnoreCase("local"))
			this.linkType = LinkType.LOCAL;
		
		else
			this.linkType = LinkType.DATA;
		
	}
	
	/**
	 * Returns the link type for the given about object.
	 * 
	 * @return The link type for the given object, based on the link type in
	 *         the database.
	 *         
	 * @see LinkType
	 */
	public LinkType getLinkType() {
		
		return this.linkType;
		
	}
	
	/**
	 * Sets the ordering for this about object. Ordering is used by the
	 * application for displaying menu items in the desired order.
	 * 
	 * @param ordering The ordering information, directly from the database.
	 */
	private void setOrdering(int ordering) {
		
		this.ordering = ordering;
		
	}
	
	/**
	 * Gets the current ordering information for the given object. Ordering is
	 * used by the application for displaying menu items in the desired order.
	 * 
	 * @return The ordering information, as in the database entry.
	 */
	public int getOrdering() {
		
		return this.ordering;
		
	}
	
}
