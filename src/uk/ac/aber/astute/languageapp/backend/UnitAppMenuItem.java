package uk.ac.aber.astute.languageapp.backend;

/**
 * This class holds a model for application menu items, but it is slightly 
 * different to #{@link AppMenuItem} in that it also includes a "selected"
 * boolean. This is useful for displaying menus that have items that need to
 * be "selected", i.e. the current unit menu.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
public class UnitAppMenuItem extends AppMenuItem {

	private boolean selected;
	
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
	 * 
	 * @param selected Defines if this menu item is currently in a "selected"
	 *                 state. For example, when this model is used,  this might
	 *                 mean that the view will have a check box ticked, or the
	 *                 item may be a different colour, etc. 
	 */
	public UnitAppMenuItem(String mainText, String subText, boolean selected) {
		
		super(mainText, subText);
		this.selected = selected;
		
	}
	
	/**
	 * Used to define if this menu item is currently in a "selected" state. For 
	 * example, when this model is used, this might mean that the view will 
	 * have a check box ticked, or the item may be a different colour, etc.
	 *  
	 * @param selected True if the item is selected, otherwise false.
	 */
	public void setSelected(boolean selected) {
		
		this.selected = selected;
		
	}
	
	/**
	 * Used to get the current "selected" state of the menu item. For example, 
	 * when this model is used, this might mean that the view will have a check 
	 * box ticked, or the item may be a different colour, etc.
	 *  
	 * @return True if the item is selected, otherwise false.
	 */
	public boolean getSelected() {
		
		return this.selected;
		
	}
	
}
