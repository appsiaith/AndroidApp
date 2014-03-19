package uk.ac.aber.astute.languageapp.gui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import uk.ac.aber.astute.APPLICATION_NAME_HERE.R;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Dialog;
import uk.ac.aber.astute.languageapp.backend.db.GroupHeader;
import uk.ac.aber.astute.languageapp.backend.db.Strings;

/**
 * This class is used to display HTML. It is used by a number of locations in
 * the application, such as the About and the Grammar screens.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class DoMonologueScreenFragment extends Fragment {
	
	private Appearance appearance;
	private Context context;
	private LinearLayout layout;
	private WebView webView;
	private Dialog d;
	private MediaPlayer player; 
	private Menu m;
	
	/** Actually does nothing, just required by Fragments. */
	public DoMonologueScreenFragment() {}
	
	/**
	 * Builds the display.
	 * 
	 * TODO: Stop using GLOBAL_GARRAM_STYLE and have the appearance info passed
	 * in, as we may be called from HELP, for example.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
												   Bundle savedInstanceState) {
		
		/* Acquire the context we're working in, and the layout settings. */
		this.context = container.getContext();
		this.appearance = Appearance.getApperanceFor(
								this.context, Strings.GLOBAL_GRAMMAR_STYLE);
		
		/* Build the fragment. */
		this.layout = new LinearLayout(this.context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		
		this.webView = new WebView(this.context);
		this.layout.setLayoutParams(new ViewGroup.LayoutParams(Tracker.getInstance().getWindowSize().x,
				Tracker.getInstance().getWindowSize().y));
		this.webView.setLayoutParams(new ViewGroup.LayoutParams(Tracker.getInstance().getWindowSize().x,
				Tracker.getInstance().getWindowSize().y));
		this.updateDisplay();
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setHasOptionsMenu(true);
		return this.layout;
		
	}
	
	/**
	 * This function is actually used to build the main display, and is also
	 * called by the tracker instance if ever the unit is changed in the 
	 * background, allowing us to update this unit and display the correct
	 * menu options.
	 */
	public void updateDisplay() {
		
		if (!this.isAdded()) return;
		
		this.layout.removeAllViews();
		
		/* Set the background parameters. */
		this.layout.setBackgroundColor(this.appearance.getBackColour());
		if(this.appearance.getBackImage() != null)
			this.layout.setBackground(this.appearance.getBackImage());
		

		d = Dialog.getDialogByGroup(this.context, 
				Tracker.getInstance().getCurrentGroupId()).get(0);
		this.webView.loadData(d.getLanguage(), "text/html", null);
		
		GroupHeader g = GroupHeader.getGroupHeaderById(this.context, 
				Tracker.getInstance().getCurrentGroupId());
	
		
		this.layout.addView(webView);
		this.getActivity().setTitle(g.getEnglish());
		this.player = d.getAudioLanguage();
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.play_menu, menu);
		m = menu;
		m.findItem(R.id.pause_item).setVisible(false);
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.play_item) {
			if (player != null) player.start();
			m.findItem(R.id.play_item).setVisible(false);
			m.findItem(R.id.pause_item).setVisible(true);
			return true;
		} else if (item.getItemId() == R.id.pause_item) {
			if (player != null) player.stop();
			player = d.getAudioLanguage();
			m.findItem(R.id.play_item).setVisible(true);
			m.findItem(R.id.pause_item).setVisible(false);
			return true;
		} 
		
		return false;
		
	}
	
}
