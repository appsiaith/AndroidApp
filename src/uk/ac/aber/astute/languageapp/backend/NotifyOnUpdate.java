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

	public void updateDisplay();
	 
}
