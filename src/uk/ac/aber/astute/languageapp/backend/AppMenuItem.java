package uk.ac.aber.astute.languageapp.backend;

/**
 * This class holds a model for application menu items.
 *  
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
public class AppMenuItem {

	private String mainText;
	private String subText;
	private int id;
	
	/**
	 * Creates a new menu item.
	 * 
	 * @param mainText The title text, i.e. the text that would normally
	 *                 be displayed at the top of a cell in any given menu.
	 *                 This should never be allowed to be null.
	 *                 
	 * @param subText Optional sub-text, i.e. the text that would normally
	 *                be displayed at the bottom of a cell in any given menu.
	 *                This can be null.
	 */
	public AppMenuItem(String mainText, String subText) {
		this.mainText = mainText;
		this.subText = subText;
	}
	
	/**
	 * Gets the title text, or the main text, i.e. the text that would normally
	 * be displayed at the top of a cell in any given menu.
	 * 
	 * @return The main, title text.
	 */
	public String getMainText() {
		return this.mainText;
	}
	
	/**
	 * Gets the sub-text, i.e. the text that would normally be displayed at the
	 * bottom of a cell in any given menu.
	 * 
	 * @return The sub-text.
	 */
	public String getSubText() {
		return this.subText;
	}
	
	/**
	 * Menu items generally need some form of identifier so that once items are
	 * "clicked" or "touched" they can respond. This ID should generally be 
	 * either a unique identifier for a specific menu item, i.e. when dealing
	 * with main menus in the app, etc., or alternatively it may be an 
	 * identifier that matches something that the menu item does - for example,
	 * a "select unit" menu item might hold the unit id the menu item selects.
	 * 
	 * @param id The unique identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the unique identifier for the given menu item.
	 * 
	 * @return A unique or meaningful identifier.
	 */
	public int getId() {
		return this.id;
	}
	
}
