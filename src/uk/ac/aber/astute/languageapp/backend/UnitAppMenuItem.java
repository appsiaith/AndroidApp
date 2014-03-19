package uk.ac.aber.astute.languageapp.backend;

/**
 *  
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
public class UnitAppMenuItem {

	private String mainText;
	private String subText;
	private int id;
	private boolean selected;
	
	public UnitAppMenuItem(String mainText, String subText, boolean selected) {
		this.mainText = mainText;
		this.subText = subText;
		this.selected = selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getMainText() {
		return this.mainText;
	}
	
	public String getSubText() {
		return this.subText;
	}
	
	public boolean getSelected() {
		return this.selected;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
}
