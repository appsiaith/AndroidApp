package uk.ac.aber.astute.languageapp.backend;

/**
 *  
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
public class AppMenuItem {

	private String mainText;
	private String subText;
	private int id;
	
	public AppMenuItem(String mainText, String subText) {
		this.mainText = mainText;
		this.subText = subText;
	}
	
	public String getMainText() {
		return this.mainText;
	}
	
	public String getSubText() {
		return this.subText;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
}
