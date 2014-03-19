package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Pattern;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This fragment is used to actually display individual patterns associated
 * with a lesson and a group. 
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class DoPatternScreenFragment extends Fragment 
									implements NotifyOnUpdate {

	
	private Appearance appearance;
	private Context context;
	private Tracker tracker;
	private LinearLayout layout;
	private int counter = 0;
	private ArrayList<Pattern> patterns;
	
	/** Actually does nothing, just required by Fragments. */
	public DoPatternScreenFragment() { }
	
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
								this.context, Strings.PATTERN_STYLE);
		
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
		//this.layout.setBackgroundColor(this.appearance.getBackColour());
		//if (this.appearance.getBackImage() != null)
		//	this.layout.setBackground(this.appearance.getBackImage());
		this.layout.setBackgroundColor(Color.WHITE);
		
		/* Set the screen title. */
		if (this.isAdded())
			this.getActivity().setTitle(
				Strings.fillString(this.appearance.getTitle()));
		
		/* We will be needing a space to store the "language" and English
		 * text for the pattern.
		 */
		TextView englishText = new TextView(this.context);
		TextView languageText = new TextView(this.context);
		
		/* Get a list of patterns for the current group. */
		this.patterns = Pattern.getPatternByGroup(this.context, 
												   tracker.getCurrentGroupId());
		
		if (this.counter >= this.patterns.size())
			this.counter = this.patterns.size()-1;
		else if (this.counter < 0)
			this.counter = 0;
		
		if (this.patterns != null && this.patterns.size() > 0) {
		
			/* Assuming we got some patterns, we now get the English and
			 * language representations of those languages to display.
			 */ 
			Log.e("LANG_APP", "COUNTER VALUE:::: " + this.counter + ", SIZE: " + this.patterns.size());
			englishText.setText(this.patterns.get(this.counter).getEnglish());
			languageText.setText(this.patterns.get(this.counter).getLanguage());
			
			
			englishText.setBackgroundColor(this.appearance.getCellColour());
			languageText.setBackgroundColor(this.appearance.getCellColour());
			
			englishText.setTextSize(this.appearance.getMainFontSize());
			languageText.setTextSize(this.appearance.getMainFontSize());
			
			englishText.setPadding(0, 40, 0, 40);
			languageText.setPadding(0, 40, 0, 80);
			
			englishText.setGravity(Gravity.CENTER);
			languageText.setGravity(Gravity.CENTER);
			
			if (this.isAdded()) {
				MediaPlayer mp = this.patterns.get(this.counter).getAudioLanguage();
				if (mp != null) mp.start();
			}
	
		} else {
			
			Log.e("LANG_APP", "No patterns available!");
			
		}

		
		/* We need a back and a forward button to scroll through 
		 * patterns in the group. 
		 */
		Button backButton = new Button(this.context);
		backButton.setText("Prev");
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if (counter > 0)
					counter--;
				
				updateDisplay();
				
			}
			
		});
		
		Button repeatButton = new Button(this.context);
		repeatButton.setText("Repeat");
		repeatButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MediaPlayer mp = patterns.get(counter).getAudioLanguage();
				if (mp != null) mp.start();
			}
		});
		
		Button nextButton = new Button(this.context);
		nextButton.setText("Next");
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if (counter < (patterns.size()-1))
					counter++;
				
				updateDisplay();
				
			}
		});
		
		
		if (counter > 0 && this.patterns.size() > 1) {
			backButton.setEnabled(true);
		} else {
			backButton.setEnabled(false);
		}
		
		if (counter < this.patterns.size()-1 && this.patterns.size() > 1) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
		
		this.layout.setGravity(Gravity.CENTER_VERTICAL);
		
		/* Display the exercises and control buttons. */
		this.layout.addView(englishText);
		this.layout.addView(languageText);
		
		LinearLayout controlButtons = new LinearLayout(this.context);
		controlButtons.setOrientation(LinearLayout.HORIZONTAL);
		controlButtons.setGravity(Gravity.CENTER);
		controlButtons.addView(backButton);
		controlButtons.addView(repeatButton);
		controlButtons.addView(nextButton);
		this.layout.addView(controlButtons);
		
		
	 }
}
