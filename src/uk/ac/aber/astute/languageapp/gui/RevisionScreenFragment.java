package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import uk.ac.aber.astute.APPLICATION_NAME_HERE.R;
import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.GroupHeader;
import uk.ac.aber.astute.languageapp.backend.db.Pattern;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import uk.ac.aber.astute.languageapp.backend.db.Unit;
import uk.ac.aber.astute.languageapp.backend.db.Vocab;
import uk.ac.aber.astute.languageapp.gui.utils.Files;

/**
 * This fragment is used to display the exercise list for a given unit or
 * lesson. 
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class RevisionScreenFragment extends Fragment implements NotifyOnUpdate {
	
	private Context context;
	private LinearLayout layout;
	private Tracker tracker;
	private int counter = 0;
	private Random r;
	private boolean started = false;
	private int score = 0;
	private int total = 0;
	private Menu m;
	private boolean doingPhrases = false;
	
	/** Actually does nothing, just required by Fragments. */
	public RevisionScreenFragment() { }
	
	/**
	 * Called when the fragment is created. Sets up the environment by getting
	 * appearance details for the view, defining the layout of the fragment,
	 * etc.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
												   Bundle savedInstanceState) {
		
		/* Acquire the context we're working in, and the layout settings. */
		this.context = container.getContext();

		this.tracker = Tracker.getInstance();
		
		/* Build the layout style. */
		this.layout = new LinearLayout(this.context);
		this.layout.setOrientation(LinearLayout.VERTICAL);

		r = new Random();
		
		/* Inform the tracker that, if the currently selected unit changes,
		 * our display should be updated. Then request the display is 
		 * updated now. 
		 */
		this.tracker.notifyOnUpdate(this);	 
		this.updateDisplay();
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setHasOptionsMenu(true);
		
		return this.layout;
		
	}
	
	/**
	 * This function is actually used to build the main display. It first
	 * clears all views in the layout, and updates the display according to
	 * the current information available.
	 * 
	 * Information available will be the currently selected unit/lesson. This
	 * function could be called at any point by the tracker if there are 
	 * updates to the currently selected unit. As such, this function should
	 * be thread safe.
	 */
	public void updateDisplay() {
		
		if (!this.isAdded()) return;
		
		this.layout.removeAllViews();
		
		/* Set the background parameters. */
		this.layout.setBackgroundColor(Color.WHITE);
		TextView whatIs = new TextView(context);
		TextView question = new TextView(context);
		final TextView answer = new TextView(context);
		TextView correct = new TextView(context);
		Space space = new Space(context);
		space.setMinimumHeight(20);
	

		final ImageButton noButton = new ImageButton(context);
		noButton.setImageDrawable(Files.getImageFromString(context, "revision/revision_no_button.png"));
		noButton.setBackgroundColor(Color.argb(0, 0, 0, 0));
		
		final ImageButton yesButton = new ImageButton(context);
		yesButton.setImageDrawable(Files.getImageFromString(context, "revision/revision_yes_button.png"));
		yesButton.setBackgroundColor(Color.argb(0, 0, 0, 0));
		
		final ImageButton answerButton = new ImageButton(context);
		answerButton.setImageDrawable(Files.getImageFromString(context, "revision/revision_answer_button.png"));
		answerButton.setBackgroundColor(Color.argb(0, 0, 0, 0));
		correct.setText(Strings.WERE_YOU_CORRECT);
		
		this.layout.setBackgroundColor(Color.argb(255, 28,53,18));
		
		whatIs.setTextColor(Color.WHITE);
		question.setTextColor(Color.WHITE);
		correct.setTextColor(Color.WHITE);
		answer.setTextColor(Color.WHITE);
		
		whatIs.setGravity(Gravity.CENTER);
		question.setGravity(Gravity.CENTER);
		answer.setGravity(Gravity.CENTER);
		correct.setGravity(Gravity.CENTER);
		
		whatIs.setPadding(20, 30, 20, 30);
		question.setPadding(20,30,20,30);
		answer.setPadding(20,60,20,30);
		correct.setPadding(5,10,5,10);
		
		whatIs.setTextSize(18 * Appearance.getModifier());
		question.setTextSize(18 * Appearance.getModifier());
		correct.setTextSize(12 * Appearance.getModifier());
		answer.setTextSize(18 * Appearance.getModifier());
		
		this.layout.setPadding(40*Appearance.getModifier(), 40*Appearance.getModifier(), 40*Appearance.getModifier(), 40*Appearance.getModifier());

		final LinearLayout questionArea = new LinearLayout(context);
		questionArea.setOrientation(LinearLayout.VERTICAL);
		questionArea.setBackground(Files.getImageFromString(context, "revision/revision_box.png"));
		questionArea.addView(whatIs);
		questionArea.addView(question);
		questionArea.setPadding(30,30,30,30);
		questionArea.setMinimumHeight(518);
		
		final LinearLayout correctQuestion = new LinearLayout(context);
		correctQuestion.setOrientation(LinearLayout.HORIZONTAL);
		correctQuestion.setBackground(Files.getImageFromString(context, "revision/revision_click_area.png"));
		correctQuestion.addView(noButton);
		correctQuestion.addView(correct);
		correctQuestion.addView(yesButton);
		correctQuestion.setGravity(Gravity.CENTER);
		correctQuestion.setMinimumHeight(185);
		
		final LinearLayout answerButtonArea = new LinearLayout(context);
		answerButtonArea.setOrientation(LinearLayout.HORIZONTAL);
		answerButtonArea.setBackground(Files.getImageFromString(context, "revision/revision_click_area.png"));
		answerButtonArea.setGravity(Gravity.CENTER);
		answerButtonArea.setMinimumHeight(185);
		answerButtonArea.addView(answerButton);
		
		this.layout.setGravity(Gravity.CENTER_VERTICAL);
	
			
		/* Get all the phrases we've encountered up to the current unit. */
		
		ArrayList<Unit> units = Unit.getAllUnits(context);
		final ArrayList<Vocab> vocabFull = new ArrayList<Vocab>();
		final ArrayList<Pattern> patternFull = new ArrayList<Pattern>();	
	
			for (int i = 0; i < units.size(); i++) {
				
				if (units.get(i).getId() <= Tracker.getInstance().getCurrentUnit()) {
					
					ArrayList<GroupHeader> headers = GroupHeader.getGroupHeaders(context, 
											units.get(i).getId(), GroupHeader.VOCAB_GROUP);
					
					for (int j = 0; j < headers.size(); j++) {
						
						ArrayList<Vocab> currentVocab = Vocab.getVocabByGroupOrderedBy(context, headers.get(j).getId(),"english");
						
						for (int k = 0; k < currentVocab.size(); k++) {
							vocabFull.add(currentVocab.get(k));
						}
						
					}
					
				}
				
				if (units.get(i).getId() <= Tracker.getInstance().getCurrentUnit()) {
					
					ArrayList<GroupHeader> headers = GroupHeader.getGroupHeaders(context, 
											units.get(i).getId(), GroupHeader.PATTERN_GROUP);
					
					for (int j = 0; j < headers.size(); j++) {
						
						ArrayList<Pattern> currentPattern = Pattern.getPatternByGroup(context, headers.get(j).getId());
						
						for (int k = 0; k < currentPattern.size(); k++) {
							patternFull.add(currentPattern.get(k));
						}
						
					}
					
				}
				
			}			
		
		if (doingPhrases)
			counter = r.nextInt(vocabFull.size());
		else
			counter = r.nextInt(patternFull.size());
		
		answerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				started = true;
				layout.removeView(answerButtonArea);
				questionArea.addView(answer);
				layout.addView(correctQuestion);
			}
		});
		

		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if (doingPhrases)
					counter = r.nextInt(vocabFull.size());
				else
					counter = r.nextInt(patternFull.size());
				
				score++;
				total++;
				updateDisplay();
			}
		});
		
		

		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (doingPhrases)
					counter = r.nextInt(vocabFull.size());
				else
					counter = r.nextInt(patternFull.size());
				
				total++;
				updateDisplay();
			}
		});
		
		whatIs.setText(Strings.WHAT_IS_THE_WELSH_FOR);
		Log.e("LANG_APP", "Counter: " + counter);
		Log.e("LANG_APP", "Vocab Size: " + vocabFull.size());
		Log.e("LANG_APP", "Pattern Size: " + patternFull.size());
		if (doingPhrases) {
			Log.e("LANG_APP", "Doing phrases...");
		} else {
			Log.e("LANG_APP", "Doing words...");
		}
		
		if (doingPhrases) {
		
			if (counter >= vocabFull.size()) counter = vocabFull.size()-1;
			else if (counter < 0) counter = 0;
		
			question.setText(vocabFull.get(counter).getEnglish());
			answer.setText(vocabFull.get(counter).getLanguage());
			
		} else {
			
			if (counter >= patternFull.size()) counter = patternFull.size()-1;
			else if (counter < 0) counter = 0;
		
			question.setText(patternFull.get(counter).getEnglish());
			answer.setText(patternFull.get(counter).getLanguage());
			
		}
		
		/* Set the screen title. */
		if (this.isAdded()) 
			if (started)
				this.getActivity().setTitle(Strings.doScoreString(Strings.OUT_OF_STRING, score, total));
			else
				this.getActivity().setTitle(Strings.START_PRACTICING);
		

		this.layout.addView(questionArea);
		this.layout.addView(space);;
		this.layout.addView(answerButtonArea);
				
						
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.revision_menu, menu);
		m = menu;
		doingPhrases = false;
		m.findItem(R.id.words_item).setVisible(false);
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.phrases_item) {
			m.findItem(R.id.words_item).setVisible(true);
			m.findItem(R.id.phrases_item).setVisible(false);
			doingPhrases = true;
			return true;
		} else if (item.getItemId() == R.id.words_item) {
			m.findItem(R.id.words_item).setVisible(false);
			m.findItem(R.id.phrases_item).setVisible(true);
			doingPhrases = false;
			return true;
		} 
		
		return false;
		
	}
	
}
