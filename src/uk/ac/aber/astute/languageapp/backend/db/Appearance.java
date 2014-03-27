 package uk.ac.aber.astute.languageapp.backend.db;

import java.util.HashMap;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.gui.utils.Files;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * This class is used to both load in and manage "Appearance" objects from the
 * database, and also when used as an instance, contains specific information
 * about a specific "Appearance" entry in the database.
 * 
 * Appearance instances contain information about how the application should
 * look, for example, font sizes, widths, colours, etc.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
public class Appearance {

	private static LanguageDatabase ldb;
	private static HashMap<String, Appearance> cache;
	
	private static int MODIFIER = 2;
	
	private String tableName;
	private String title;
	private String header;
	private boolean twoTexts;
	private int backColour;
	private int headerHeight;
	private String headerImage;
	private String backImage;
	private int headerFontSize;
	private int headerFontColour;
	private int headerCellColour;
	private int cellHeight;
	private int cellColour;
	private int mainFontSize;
	private int mainFontColour;
	private int subFontSize;
	private int subFontColour;
	
	private static Context context;
	
	/**
	 * Checks in the cache if we already have an instance of an Appearance 
	 * object that matches the given unique "table name". If we do not, we try 
	 * and fetch one from the database. If we manage this, we cache it for 
	 * later use.
	 * 
	 * @param context The context that the application is working within.
	 * 
	 * @param tableName The unique name that identifies the Appearance object.
	 *                  We call this "tableName" because the Appearance objects 
	 *                  generally reference a table in the app, i.e. where a 
	 *                  table is a "menu".
	 * 
	 * @return A single instance Appearance object that matches the data for 
	 *         the given tableName, otherwise null if one was not found.
	 */
	public static Appearance getApperanceFor(Context context, 
													String tableName) {
		
		Appearance apperanceInstance = null;
		Appearance.context = context;
		
		if (cache == null) cache = new HashMap<String, Appearance>();
		if (cache.get(tableName) != null) return cache.get(tableName);
		
		ldb = LanguageDatabase.getInstance(context);
		
		String    tn  = "appearance";
		String    ak = "tableName=?";
		String [] av = new String[] { tableName };
		
		apperanceInstance = new Appearance();
		
		/* Some things we might have, or might not have. For example, we
		 * might have a background image, but not a header image. This is
		 * okay, but we log the fact for debugging purposes.
		 */
		
		try {
			
			apperanceInstance.setBackImage(
					ldb.getStringWithQuery(tn, "backImage", ak, av));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.i("LANG_APP", "backImage not found in database: " 
												+ dnfe.toString());
			
		}
		
		try {
			
			apperanceInstance.setHeaderImage(
					ldb.getStringWithQuery(tn, "headerImage", ak, av));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.i("LANG_APP", "headerImage not found in database: "  
					+ dnfe.toString());
			
		}
		
		try {
			
			apperanceInstance.setHeader(
					ldb.getStringWithQuery(tn,"header",ak,av));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.i("LANG_APP", "header not found in database: " 
												+ dnfe.toString());
			
		}
		
		try {
			
			apperanceInstance.setTitle(
					ldb.getStringWithQuery(tn,"title",ak,av));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.i("LANG_APP", "title not found in database: " 
												+ dnfe.toString());
			
		}
		
		/* The rest of these values MUST be present. So, if we don't
		 * get one of these values, we log it is an error,rather than
		 * for info.
		 */
		
		try {
	
			apperanceInstance.setTableName(tableName);
			
			apperanceInstance.setTwoTexts(
				ldb.getStringWithQuery(tn, "twoTexts", ak, av));
			
			apperanceInstance.setBackColour(
				ldb.getIntegerWithQuery(tn, "backColorAlpha", ak, av),
				ldb.getIntegerWithQuery(tn, "backColorRed",   ak, av),
				ldb.getIntegerWithQuery(tn, "backColorGreen", ak, av),
				ldb.getIntegerWithQuery(tn, "backColorBlue",  ak, av));
			
			apperanceInstance.setCellColour(
				ldb.getIntegerWithQuery(tn, "cellColorAlpha", ak, av),
				ldb.getIntegerWithQuery(tn, "cellColorRed",   ak, av),
				ldb.getIntegerWithQuery(tn, "cellColorGreen", ak, av),
				ldb.getIntegerWithQuery(tn, "cellColorBlue",  ak, av));
			
			apperanceInstance.setMainFontColour(
				ldb.getIntegerWithQuery(tn, "mainFontColorAlpha", ak, av),
				ldb.getIntegerWithQuery(tn, "mainFontColorRed",   ak, av),
				ldb.getIntegerWithQuery(tn, "mainFontColorGreen", ak, av),
				ldb.getIntegerWithQuery(tn, "mainFontColorBlue",  ak, av));						
			
			apperanceInstance.setSubFontColour(
				ldb.getIntegerWithQuery(tn, "subFontColorAlpha", ak, av),
				ldb.getIntegerWithQuery(tn, "subFontColorRed",   ak, av),
				ldb.getIntegerWithQuery(tn, "subFontColorGreen", ak, av),
				ldb.getIntegerWithQuery(tn, "subFontColorBlue",  ak, av));
			
			apperanceInstance.setHeaderFontColour(
				ldb.getIntegerWithQuery(tn, "headerFontColorAlpha", ak, av),
				ldb.getIntegerWithQuery(tn, "headerFontColorRed",   ak, av),
				ldb.getIntegerWithQuery(tn, "headerFontColorGreen", ak, av),
				ldb.getIntegerWithQuery(tn, "headerFontColorBlue",  ak, av));
			
			apperanceInstance.setHeaderCellColour(
				ldb.getIntegerWithQuery(tn, "headerCellColorAlpha", ak, av),
				ldb.getIntegerWithQuery(tn, "headerCellColorRed",   ak, av),
				ldb.getIntegerWithQuery(tn, "headerCellColorGreen", ak, av),
				ldb.getIntegerWithQuery(tn, "headerCellColorBlue",  ak, av));
			
			apperanceInstance.setHeaderHeight(
				ldb.getIntegerWithQuery(tn, "headerHeight", ak, av));
			
			apperanceInstance.setHeaderFontSize(
				ldb.getIntegerWithQuery(tn, "headerFontSize", ak, av));
			
			apperanceInstance.setMainFontSize(
				ldb.getIntegerWithQuery(tn, "mainFontSize", ak, av));
			
			apperanceInstance.setSubFontSize(
				ldb.getIntegerWithQuery(tn, "subFontSize", ak, av));
			
			apperanceInstance.setCellHeight(
				ldb.getIntegerWithQuery(tn, "cellHeight", ak, av));
			
			
		} catch (DataNotFoundException dnfe) {
			
			apperanceInstance = null;
			Log.e("LANG_APP", "Unable to load settings for " +
											"screen: " + tableName);
			Log.e("LANG_APP", dnfe.toString());
			
		}
		
		if (apperanceInstance != null) cache.put(tableName, apperanceInstance);
		return apperanceInstance;
		
	}
	
	/**
	 * This is used by the application to get the modifier value. The modifier
	 * value is used for adjusting font sizes in the application so that they
	 * scale a bit better for different sized screens.
	 * 
	 * Currently, the MODIFIER value can be either 1 or 2. If it's 1, it means
	 * the screen size is smaller than 750 x 1150 pixels. In this instance,
	 * we're probably dealing with a phone, or smaller tablet so we keep things
	 * small and compact. If the screen size is *any* bigger than this, we have,
	 * in all probability a tablet (of size at least 10 inches), so we double
	 * the fonts, etc.
	 * 
	 * The screens all auto scale without the modifier, the modifier is really
	 * for fonts only.
	 * 
	 * TODO: There probably is a better way of doing this, but it works. 
	 * At some point, I'll think about making this a bit better.
	 * 
	 * @return The modifier value for the screen size.
	 */
	public static int getModifier() {
		
		if (Tracker.getInstance().getWindowSize().x > 750 &&
				Tracker.getInstance().getWindowSize().y > 1150 )
			MODIFIER = 2;
		
		else
			MODIFIER = 1;
		
		Log.i("LANG_APP", "Screen size: x: " + 
					Tracker.getInstance().getWindowSize().x + ", y: "
								 + Tracker.getInstance().getWindowSize().y);
		
		return MODIFIER;
		
	}
	
	/** 
	 * Whilst we do not have single instances of Appearance objects, we do not
	 * wish to allow Appearance objects to be created that do not match the
	 * database settings. 
	 */
	private Appearance() { }
	
	/**
	 * Used to set the title string for a given screen. This would be the
	 * text used in the title bar of the app.
	 * 
	 * @param title The title string for the given screen.
	 */
	private void setTitle(String title) {
		
		this.title = title;
		
	}
	
	/**
	 * The title string for the given appearance/screen. This would generally
	 * be used for the action bar title text.
	 * 
	 * @return The title string for the given appearance/screen as per the 
	 *         database entry.
	 */
	public String getTitle() {
		
		return this.title;
		
	}
	
	/**
	 * Used to set the header text for a given screen. This would be the text
	 * used at the top of a screen, and would only be displayed if there is no
	 * banner image file.
	 * 
	 * @param header The header string for the given screen.
	 */
	private void setHeader(String header) {
		
		this.header = header;
		
	}
	
	/**
	 * The header string for a given appearance/screen. This would be the text
	 * used at the top of a screen, and would only be displayed if there is no
	 * banner image file.
	 * 
	 * @return The header string for the given appearance/screen, as per the
	 *         database entry.
	 */
	public String getHeader() {
		
		return this.header;
		
	}
	
	/**
	 * This is used to set the background colour for the whole screen. 
	 * 
	 * @param alpha The alpha component of the ARGB background colour.
	 * @param red The red component of the ARGB background colour.
	 * @param green The green component of the ARGB background colour.
	 * @param blue The blue component of the ARGO background colour.
	 */
	private void setBackColour(int alpha, int red, int green, int blue) {
		
		this.backColour = Color.argb(alpha, red, green, blue);
		
	}

	/**
	 * This is used to get the background colour for the whole screen. If a
	 * background image is specified too, then that should be used instead.
	 * 
	 * @return The combined ARGB Color object for the background.
	 */
	public int getBackColour() {
		
		return this.backColour;
		
	}
	
	/**
	 * This is used to specify the minimum height for a cell in a menu.
	 * 
	 * @param cellHeight the minimum height for a cell in a menu.
	 */
	private void setCellHeight(int cellHeight) {
		
		this.cellHeight = (cellHeight * getModifier());
		
	}
	
	/**
	 * This is used to get the minimum height for a cell in any menu on the
	 * given screen.
	 * 
	 * @return The minimum height for cells in the menus.
	 */
	public int getCellHeight() {
		
		return this.cellHeight;
		
	}
	
	/**
	 * This is used to set the height of the header, either image or text area.
	 * 
	 * @param headerHeight The height of the header.
	 */
	private void setHeaderHeight(int headerHeight) {
		
		this.headerHeight = (headerHeight * getModifier());
		
	}
	
	/**
	 * This is used to get the height of the header, either image or text area.
	 *  
	 * @return The height of the header.
	 */
	public int getHeaderHeight() {
		
		return this.headerHeight;
	}
	
	/**
	 * This is used to set the font size for the header area.
	 * 
	 * @param headerFontSize Font size for the header area.
	 */
	private void setHeaderFontSize(int headerFontSize) {
		
		this.headerFontSize = (headerFontSize * getModifier());
		
	}
	
	/**
	 * Used to get the font size for the header area.
	 * 
	 * @return The font size for the header area.
	 */
	public int getHeaderFontSize() {
		
		return this.headerFontSize;
		
	}
	
	/**
	 * Used to set the main font size for text in menus, etc.
	 * 
	 * @param mainFontSize The main font size for text in menus, etc.
	 */
	private void setMainFontSize(int mainFontSize) {
		
		this.mainFontSize = (mainFontSize * getModifier());
		
	}
	
	/**
	 * Used to get the main font size for text in menus, etc.
	 * 
	 * @return The main font size.
	 */
	public int getMainFontSize() {
		
		return this.mainFontSize;
		
	}
	
	/**
	 * Used to set the sub-font size for text in menus, etc.
	 * 
	 * @param subFontSize The sub-font size for text in menus, etc.
	 */
	private void setSubFontSize(int subFontSize) {
		
		this.subFontSize = (subFontSize * getModifier());
		
	}
	
	/**
	 * Used to get the sub-font size, for text in menus etc. For example, this
	 * font might be used for sub-text, or captions, etc.
	 * 
	 * @return The sun-font size.
	 */
	public int getSubFontSize() {
		
		return this.subFontSize;
		
	}
	
	/**
	 * This is used to set the background colour for the cells in the menus. 
	 * 
	 * @param alpha The alpha component of the ARGB background colour.
	 * @param red The red component of the ARGB background colour.
	 * @param green The green component of the ARGB background colour.
	 * @param blue The blue component of the ARGO background colour.
	 */
	private void setCellColour(int alpha, int red, int green, int blue) {
		
		this.cellColour = Color.argb(alpha, red, green, blue);
		
	}
	
	/**
	 * This is used to get the background colour for cells in the menus.
	 * 
	 * @return The combined ARGB Color object for the background.
	 */
	public int getCellColour() {
		
		return this.cellColour;
		
	}

	/**
	 * This is used to set the font colour for the header. 
	 * 
	 * @param alpha The alpha component of the ARGB header font colour.
	 * @param red The red component of the ARGB header font colour.
	 * @param green The green component of the ARGB header font colour.
	 * @param blue The blue component of the ARGO header font colour.
	 */
	private void setHeaderFontColour(int alpha, int red, int green, int blue) {
		
		this.headerFontColour = Color.argb(alpha, red, green, blue);
		
	}
	
	/**
	 * This is used to get the font colour for text in the header.
	 * 
	 * @return The combined ARGB Color object for the font colour.
	 */
	public int getHeaderFontColour() {
		
		return this.headerFontColour;
		
	}
	
	/**
	 * This is used to set the background colour for the header cell. 
	 * 
	 * @param alpha The alpha component of the ARGB background colour.
	 * @param red The red component of the ARGB background colour.
	 * @param green The green component of the ARGB background colour.
	 * @param blue The blue component of the ARGO background colour.
	 */
	private void setHeaderCellColour(int alpha, int red, int green, int blue) {
		
		this.headerCellColour = Color.argb(alpha, red, green, blue);
		
	}
	
	/**
	 * This is used to get the background colour for the header cell.
	 * 
	 * @return The combined ARGB Color object for the background.
	 */
	public int getHeaderCellColour() {
		
		return this.headerCellColour;
		
	}

	/**
	 * This is used to set the font colour for the main font. 
	 * 
	 * @param alpha The alpha component of the ARGB main font colour.
	 * @param red The red component of the ARGB main font colour.
	 * @param green The green component of the ARGB main font colour.
	 * @param blue The blue component of the ARGO main font colour.
	 */
	private void setMainFontColour(int alpha, int red, int green, int blue) {
		
		this.mainFontColour = Color.argb(alpha, red, green, blue);
		
	}
	
	/**
	 * This is used to get the font colour for the main text on the screen.
	 * 
	 * @return The combined ARGB Color object for the font colour.
	 */
	public int getMainFontColour() {
		
		return this.mainFontColour;
		
	}
	
	/**
	 * This is used to set the font colour for the sub font. 
	 * 
	 * @param alpha The alpha component of the ARGB sub font colour.
	 * @param red The red component of the ARGB sub font colour.
	 * @param green The green component of the ARGB sub font colour.
	 * @param blue The blue component of the ARGO sub font colour.
	 */
	private void setSubFontColour(int alpha, int red, int green, int blue) {
		
		this.subFontColour = Color.argb(alpha, red, green, blue);
		
	}
	
	/**
	 * This is used to get the font colour for any sub-text on the screen.
	 * 
	 * @return The combined ARGB Color object for the font colour.
	 */
	public int getSubFontColour() {
		
		return this.subFontColour;
		
	}
	
	/**
	 * Used to set the unique identifier for this appearance item, i.e. the 
	 * name of the table/screen that the appearance info relates to. This is
	 * the same as the unique key in the database.
	 * 
	 * @param tableName The unique identifier for this appearance item.
	 */
	private void setTableName(String tableName) {
		
		this.tableName = tableName;
		
	}
	
	/**
	 * Used to get the unique identifier for this appearance item, i.e. the 
	 * name of the table/screen that the appearance info relates to. This is
	 * the same as the unique key in the database.
	 * 
	 * @return The name of this appearance.
	 */
	public String getTableName() {
		
		return this.tableName;
		
	}
	
	/**
	 * Used to define if cells in menus, etc. should display sub-text as well 
	 * as main text.
	 * 
	 * @param twoTexts "YES" (case is unimportant) if two text should be used,
	 *                 otherwise, anything else will result in false.
	 */
	private void setTwoTexts(String twoTexts) {
		
		this.twoTexts = twoTexts.equalsIgnoreCase("YES");
		
	}
	
	/**
	 * Used to check if we should display sub-text in menus, etc. as well as 
	 * main text.
	 * 
	 * @return True if sub-text should be displayed, otherwise false.
	 */
	public boolean getTwoTexts() {
		
		return this.twoTexts;
		
	}
	
	/**
	 * Used to set the background image that should be used for the screen. If
	 * this is not set, the application will use the background colour instead.
	 * 
	 * @param img File name of the background image, i.e. "background.png". You
	 *            can also use "backgroundU1.png" and U1 will be replaced with 
	 *            the active unit to have different backgrounds per unit, etc.
	 */
	private void setBackImage(String img) {
		
		this.backImage = img;
		
	}
	
	/**
	 * Returns a drawable object for the background image for the given screen,
	 * based on the image filename in the database.
	 * 
	 * @return The background image to draw, otherwise null if no background
	 *         image was set.
	 */
	public Drawable getBackImage() {
		
		Drawable img;
		
		img = Files.getImageFromString(Appearance.context,
				Config.getInstance(Appearance.context).getImageFolder() + "/" 
										+ Strings.fillString(this.backImage));
				
		return img;
	}
	
	/**
	 * Used to set the header image that should be used for the screen. If
	 * this is not set, the application will use the header text instead.
	 * 
	 * @param img File name of the header image, i.e. "header.png". You can 
	 *            also use "headerU1.png" and U1 will be replaced with the 
	 *            active unit to have different headers per unit, etc.
	 */
	private void setHeaderImage(String img) {
		
		this.headerImage = img;
		
	}
	
	/**
	 * Returns a drawable object for the header image for the given screen,
	 * based on the image filename in the database.
	 * 
	 * @return The header image to draw, otherwise null if no header image 
	 *         was set.
	 */
	public Drawable getHeaderImage() {
		
		Drawable img;
		
		img = Files.getImageFromString(Appearance.context,
				Config.getInstance(Appearance.context).getImageFolder() + "/" 
										+ Strings.fillString(this.headerImage));
		
		return img;
		
	}
	
}
