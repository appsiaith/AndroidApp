 package uk.ac.aber.astute.languageapp.backend.db;

import java.util.HashMap;

import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.gui.utils.Files;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
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
	
	public static Appearance getApperanceFor(Context context, String tableName) {
		
		Appearance apperanceInstance = null;
		Appearance.context = context;
		
		if (cache == null) cache = new HashMap<String, Appearance>();
		if (cache.get(tableName) != null) return cache.get(tableName);
		
		ldb = LanguageDatabase.getInstance(context);
		
		String    tn  = "appearance";
		String    ak = "tableName=?";
		String [] av = new String[] { tableName };
		
		apperanceInstance = new Appearance();
		
		try {
			apperanceInstance.setBackImage(
					ldb.getStringWithQuery(tn, "backImage", ak, av));
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "backImage not found in database: " 
												+ dnfe.toString());
			
		}
		
		try {
			apperanceInstance.setHeaderImage(
					ldb.getStringWithQuery(tn, "headerImage", ak, av));
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "headerImage not found in database: "  
					+ dnfe.toString());
			
		}
		
		try { 
			apperanceInstance.setHeader(
					ldb.getStringWithQuery(tn,"header",ak,av));
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "header not found in database: " 
												+ dnfe.toString());
			
		}
		
		try { 
			apperanceInstance.setTitle(
					ldb.getStringWithQuery(tn,"title",ak,av));
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "title not found in database: " 
												+ dnfe.toString());
			
		}
		
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
	 * Whilst we do not have single instances of Appearance objects, we do not
	 * wish to allow Appearance objects to be created that do not match the
	 * database settings. 
	 */
	private Appearance() { }
	
	private void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	private void setHeader(String header) {
		this.header = header;
	}
	
	public String getHeader() {
		return this.header;
	}
	
	private void setBackColour(int alpha, int red, int green, int blue) {
		this.backColour = Color.argb(alpha, red, green, blue);
	}

	public int getBackColour() {
		return this.backColour;
	}
	
	private void setCellHeight(int cellHeight) {
		this.cellHeight = (cellHeight * getModifier());
	}
	
	public int getCellHeight() {
		return this.cellHeight;
	}
	
	private void setHeaderHeight(int headerHeight) {
		this.headerHeight = (headerHeight * getModifier());
	}
	
	public int getHeaderHeight() {
		return this.headerHeight;
	}
	
	private void setHeaderFontSize(int headerFontSize) {
		this.headerFontSize = (headerFontSize * getModifier());
	}
	
	public int getHeaderFontSize() {
		return this.headerFontSize;
	}
	
	private void setMainFontSize(int mainFontSize) {
		this.mainFontSize = (mainFontSize * getModifier());
	}
	
	public int getMainFontSize() {
		return this.mainFontSize;
	}
	
	private void setSubFontSize(int subFontSize) {
		this.subFontSize = (subFontSize * getModifier());
	}
	
	public int getSubFontSize() {
		return this.subFontSize;
	}
	
	private void setCellColour(int alpha, int red, int green, int blue) {
		this.cellColour = Color.argb(alpha, red, green, blue);
	}
	
	public int getCellColour() {
		return this.cellColour;
	}

	
	private void setHeaderFontColour(int alpha, int red, int green, int blue) {
		this.headerFontColour = Color.argb(alpha, red, green, blue);
	}
	
	public int getHeaderFontColour() {
		return this.headerFontColour;
	}
	
	private void setHeaderCellColour(int alpha, int red, int green, int blue) {
		this.headerCellColour = Color.argb(alpha, red, green, blue);
	}
	
	public int getHeaderCellColour() {
		return this.headerCellColour;
	}

	private void setMainFontColour(int alpha, int red, int green, int blue) {
		this.mainFontColour = Color.argb(alpha, red, green, blue);
	}
	
	public int getMainFontColour() {
		return this.mainFontColour;
	}
	
	private void setSubFontColour(int alpha, int red, int green, int blue) {
		this.subFontColour = Color.argb(alpha, red, green, blue);
	}
	
	public int getSubFontColour() {
		return this.subFontColour;
	}
	
	private void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	private void setTwoTexts(String twoTexts) {
		this.twoTexts = twoTexts.equalsIgnoreCase("YES");
	}
	
	public boolean getTwoTexts() {
		return this.twoTexts;
	}
	
	private void setBackImage(String img) {		
		this.backImage = img;
	}
	
	public Drawable getBackImage() {
		
		Drawable img;
		
		img = Files.getImageFromString(Appearance.context,
				Config.getInstance(Appearance.context).getImageFolder() + "/" + Strings.fillString(this.backImage));
				
		return img;
	}
	
	private void setHeaderImage(String img) {
		this.headerImage = img;
	}
	
	public Drawable getHeaderImage() {
		Drawable img;
		
		img = Files.getImageFromString(Appearance.context,
				Config.getInstance(Appearance.context).getImageFolder() + "/" + Strings.fillString(this.headerImage));
		
		return img;
	}
	
	public static int getModifier() {
		
		if (Tracker.getInstance().getWindowSize().x > 2 &&
				Tracker.getInstance().getWindowSize().y > 2 )
			MODIFIER = 2;
		else
			MODIFIER = 1;
		
		Log.e("LANG_APP", "Screen size: x: " + Tracker.getInstance().getWindowSize().x + ", y: " 
											 + Tracker.getInstance().getWindowSize().y);
		
		return MODIFIER;
	}
	

}
