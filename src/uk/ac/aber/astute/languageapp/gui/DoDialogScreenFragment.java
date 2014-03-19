package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
import uk.ac.aber.astute.languageapp.backend.AppMenuItem;
import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Dialog;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import uk.ac.aber.astute.languageapp.gui.utils.Header;
import uk.ac.aber.astute.languageapp.gui.utils.MenuItemAdapter;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This fragment is used to actually display individual patterns associated
 * with a lesson and a group. 
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class DoDialogScreenFragment extends Fragment 
									implements NotifyOnUpdate {

	
	private Appearance appearance;
	private Context context;
	private Tracker tracker;
	private LinearLayout layout;
	private ArrayList<Dialog> dialog;
	
	/** Actually does nothing, just required by Fragments. */
	public DoDialogScreenFragment() { }
	
	/**
	 * Called when the fragment is created. Sets up the environment by getting
	 * appearance details for the view, defining the layout of the fragment,
	 * etc.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
							 					   Bundle savedInstanceState) {

		/* Acquire the context we're working in, and the layout settings. */
		this.context = container.getContext();
		this.appearance = Appearance.getApperanceFor(
								this.context, Strings.DIALOG_BODY_STYLE);
		
		this.tracker = Tracker.getInstance();
		
		/* Build the fragment. */
		this.layout = new LinearLayout(this.context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		
		/* Request the display is updated. */
		this.tracker.notifyOnUpdate(this);
		this.updateDisplay();
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		return this.layout;
		
	}
	
	/**
	 * This function is actually used to build the main display. It first
	 * clears all views in the layout, and updates the display according to
	 * the current information available.
	 * 
	 * The information that is important for patterns is the current group
	 * ID that brought us to this fragment, this is because patterns will
	 * belong to a specific group. We can get the current group ID from the
	 * tracker instance.
	 */
	public void updateDisplay() {

		if (!this.isAdded()) return;
		
		this.layout.removeAllViews();
		
		/* Set the background parameters. */
		this.layout.setBackgroundColor(this.appearance.getBackColour());
		if (this.appearance.getBackImage() != null)
			this.layout.setBackground(this.appearance.getBackImage());
		
		
		/* Build and display the header. */
		Header header = new Header(this.context);
		header.setTitle(Strings.fillString(this.appearance.getHeader()));
		header.setAppearance(this.appearance);
		this.layout.addView(header);
		
		/* Set the screen title. */
		if (this.isAdded())
			this.getActivity().setTitle(
				Strings.fillString(this.appearance.getTitle()));
		
		/* Get a list of patterns for the current group. */
		this.dialog = Dialog.getDialogByGroup(this.context, 
												   tracker.getCurrentGroupId());
		
		Log.e("LANG_APP", "group id: " + tracker.getCurrentGroupId());
		Log.e("LANG_APP", "size: " + this.dialog.size());
		
		final ArrayList<AppMenuItem> items = new ArrayList<AppMenuItem>();
		for (int i = 0; i < this.dialog.size(); i++) {
			
			AppMenuItem item = new AppMenuItem(
							this.dialog.get(i).getSpeaker() + " " +
							this.dialog.get(i).getLanguage(), "");
			item.setId(this.dialog.get(i).getId());
			items.add(item);
			
		}
		
		if (this.dialog.size() > 0) {
		
		ListView menu = new ListView(this.context);
		menu.setAdapter(new MenuItemAdapter(
							this.context, items, this.appearance));
		
		menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, 
										View arg1, int arg2, long arg3) {
											
				AppMenuItem tmp = items.get(arg2);
				Dialog d = Dialog.getDialogById(context, tmp.getId());
				MediaPlayer mp = d.getAudioLanguage();
				if (mp != null) mp.start();
			}
			
		});
		
		this.layout.addView(menu);
		
		}
		
	 }
}
