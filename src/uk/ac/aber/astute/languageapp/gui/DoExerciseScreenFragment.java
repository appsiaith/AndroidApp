package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Exercise;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This fragment is used to actually display individual exercises associated
 * with a lesson and a group. 
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class DoExerciseScreenFragment extends Fragment implements NotifyOnUpdate {

	private Appearance appearance;
	private Context context;
	private Tracker tracker;
	private LinearLayout layout;
	private int counter = 0;
	private boolean displayAnswer;
	private ImageView iv;
	private ArrayList<Exercise> exercise;
	
	/** Actually does nothing, just required by Fragments. */
	public DoExerciseScreenFragment() { }

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
								this.context, Strings.QUESTION_STYLE);
		
		this.tracker = Tracker.getInstance();
		
		/* Build the layout style. */
		this.layout = new LinearLayout(this.context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		
		this.displayAnswer = false;
		
		/* Request the display is updated. */
		this.updateDisplay();
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		this.tracker.notifyOnUpdate(this);
		
		return this.layout;
		
	}
		
	/**
	 * This function is actually used to build the main display. It first
	 * clears all views in the layout, and updates the display according to
	 * the current information available.
	 * 
	 * The information that is important for exercises is the current group
	 * ID that brought us to this fragment, this is because exercises will
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
		 * text for the exercise.
		 */
		TextView englishText = new TextView(this.context);
		TextView languageText = new TextView(this.context);
		
		TextView answerText = new TextView(this.context);
		
		/* Get a list of exercises for the current group. We also need to
		 * get the group header as for exercises we display the group header
		 * for the English text, not the exercise English text!
		 */
		//GroupHeader gh = GroupHeader.getGroupHeaderById(
			//			this.context, tracker.getCurrentGroupId());
		
		this.exercise = Exercise.getExerciseByGroup(
							this.context, this.tracker.getCurrentGroupId());
		
		if (this.counter >= this.exercise.size())
			this.counter = this.exercise.size()-1;
		if (this.counter <= 0) 
			this.counter = 0;
		
		if (this.exercise != null && this.exercise.size() > 0) {
		
			/* Assuming we got some exercises, we now get the English and
			 * language representations of those languages to display.
			 */
			englishText.setText(
					this.exercise.get(this.counter).getTitle());
			languageText.setText(
					this.exercise.get(this.counter).getQuestionLanguage());
			
			englishText.setTextSize(this.appearance.getMainFontSize() * Appearance.getModifier());
			languageText.setTextSize(this.appearance.getMainFontSize() * Appearance.getModifier());
			answerText.setTextSize(this.appearance.getMainFontSize() * Appearance.getModifier());
			
			
			if (this.exercise.get(this.counter).getPicture() != null) {
				iv = new ImageView(this.context);
				iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				iv.setImageDrawable(this.exercise.get(
											this.counter).getPicture());
				
				languageText.setPadding(0, 15*Appearance.getModifier(), 0, 
						30*Appearance.getModifier());
				answerText.setPadding(0,15*Appearance.getModifier(),0,15*Appearance.getModifier());
			} else {
				iv = null;
				languageText.setPadding(0,110*Appearance.getModifier(), 0,
												110*Appearance.getModifier());
				answerText.setPadding(0,45*Appearance.getModifier(),0,45*Appearance.getModifier());
			}
			
			englishText.setPadding(0, 15*Appearance.getModifier(), 0, 
												15*Appearance.getModifier());
			
			
			
			
			englishText.setGravity(Gravity.CENTER);
			languageText.setGravity(Gravity.CENTER);
			answerText.setGravity(Gravity.CENTER);
			
			
			if (this.displayAnswer) {
				answerText.setText(
						this.exercise.get(this.counter).getAnswerLanguage());
				if (this.isAdded()) {
					MediaPlayer mp = exercise.get(counter).getAnswerAudioLanguage();
					if (mp != null) mp.start();
				}
			} else {
				answerText.setText("");
				if (this.isAdded()) {
					MediaPlayer mp = exercise.get(counter).getQuestionAudioLanguage();
					if (mp != null) mp.start();
				}
			}
			
		} else {
			
			Log.e("LANG_APP", "No exercises available!");
			
		}
		
		/* We need a back and a forward button to scroll through 
		 * exercises in the group. 
		 */
		Button backButton = new Button(this.context);
		backButton.setText("Last");
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if (counter > 0)
					counter--;
				
				displayAnswer = false;
				updateDisplay();
				
			}
			
		});

		Button nextButton = new Button(this.context);
		nextButton.setText("Next");
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if (counter < (exercise.size()-1))
					counter++;
				
				displayAnswer = false;
				updateDisplay();
				
			}
		});
		
		Button showAnswerButton = new Button(this.context);
		showAnswerButton.setText("Answer");
		showAnswerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				
				displayAnswer = true;
				updateDisplay();
				
			}
		});
		
		Button repeatButton = new Button(this.context);
		repeatButton.setText("Repeat");
		repeatButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				MediaPlayer mp = exercise.get(counter).getQuestionAudioLanguage();
				if (mp != null) mp.start();
				
			}
		});
		 
		
		if (counter > 0 && this.exercise.size() > 1) {
			backButton.setEnabled(true);
		} else {
			backButton.setEnabled(false);
		}
		
		if (counter < this.exercise.size()-1 && this.exercise.size() > 1) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
		
		if (this.exercise.get(counter).getQuestionAudioLanguage() == null) {
			repeatButton.setEnabled(false);
		} else {
			repeatButton.setEnabled(true);
		}
		
		/* Display the exercises and control buttons. */
		LinearLayout questionArea = new LinearLayout(this.context);
		questionArea.setOrientation(LinearLayout.VERTICAL);
		questionArea.setGravity(Gravity.CENTER_HORIZONTAL);
		questionArea.addView(englishText);
		questionArea.addView(languageText);
		Log.e("LANG_APP", languageText.getText().toString());
		if (iv != null) {
			this.layout.removeView(iv);
			questionArea.addView(iv);
		}
		this.layout.addView(questionArea);
		
		LinearLayout answerArea = new LinearLayout(this.context);
		answerArea.setOrientation(LinearLayout.HORIZONTAL);
		answerArea.setGravity(Gravity.CENTER_HORIZONTAL);
		answerArea.addView(answerText);
		this.layout.addView(answerArea);
		
		LinearLayout controlButtons = new LinearLayout(this.context);
		controlButtons.setOrientation(LinearLayout.HORIZONTAL);
		controlButtons.setGravity(Gravity.CENTER_HORIZONTAL);
		controlButtons.addView(backButton);
		controlButtons.addView(repeatButton);
		controlButtons.addView(showAnswerButton);
		controlButtons.addView(nextButton);
		this.layout.addView(controlButtons);
				
		
	 }
}
