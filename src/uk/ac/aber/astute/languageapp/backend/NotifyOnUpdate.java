package uk.ac.aber.astute.languageapp.backend;

/**
 * This interface defines methods that must be present for objects to be 
 * notified about updates in the application. Such updates might include
 * a change of unit or group.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
public interface NotifyOnUpdate {

	/**
	 * This method is called whenever updates occur. It is the responsibility
	 * of the class that contains this method to decide if the update is 
	 * relevant to it.
	 */
	public void updateDisplay();
	 
}
