package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import uk.ac.aber.astute.APPLICATION_NAME_HERE.R;
import uk.ac.aber.astute.languageapp.backend.AppMenuItem;
import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Config;
import uk.ac.aber.astute.languageapp.backend.db.GroupHeader;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import uk.ac.aber.astute.languageapp.backend.db.Unit;
import uk.ac.aber.astute.languageapp.gui.utils.Header;
import uk.ac.aber.astute.languageapp.gui.utils.MenuItemAdapter;

/**
 * This class holds the unit screen fragment. This fragment is used to build
 * and display the menu associated with a specific lesson/unit, i.e. with the
 * possible choices such as patterns, exercises, grammar, dialogs, etc.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.2
 */
@SuppressLint("NewApi")
public class UnitScreenFragment extends Fragment implements NotifyOnUpdate {
	
	private Appearance appearance;
	private Context context;
	private LinearLayout layout;
	private Unit currentUnit;
	private Tracker tracker;
	private ArrayList<AppMenuItem> menuItems;
	
	/** Actually does nothing, just required by Fragments. */
	public UnitScreenFragment() { }
	
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
								this.context, Strings.UNIT_SCREEN_STYLE);
		this.tracker = Tracker.getInstance();
		
		/* Build the fragment. */
		this.layout = new LinearLayout(this.context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		
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

		this.currentUnit = Unit.getUnitById(this.context,
									this.tracker.getCurrentUnit());
		
		/* Set the background parameters. */
		this.layout.setBackgroundColor(this.appearance.getBackColour());
		if(this.appearance.getBackImage() != null)
			this.layout.setBackground(this.appearance.getBackImage());
		
		/* Build and display the header. */
		Header header = new Header(this.context);
		header.setTitle(this.currentUnit.getEnglish());
		header.setAppearance(this.appearance);
		this.layout.addView(header);
		
		/* Set the screen title. */
		if (this.isAdded())
			this.getActivity().setTitle(
				Strings.fillString(this.appearance.getTitle()));
		
		/* Build and display the menu. */
		menuItems = new ArrayList<AppMenuItem>();	
		AppMenuItem tmp;
		
		if (GroupHeader.lessonHasPatterns(this.context,
										  this.tracker.getCurrentUnit())) {
			
			tmp = new AppMenuItem(
					Strings.fillString(Config.getInstance(context).getUnitPatternName()),
					Strings.fillString(Config.getInstance(context).getUnitPatternText()));
			tmp.setId(0);
			this.menuItems.add(tmp);
			
		}
		
		if (GroupHeader.lessonHasVocabulary(this.context,
											this.tracker.getCurrentUnit())) {
			
			tmp = new AppMenuItem(
					Strings.fillString(Config.getInstance(context).getUnitVocabName()),
					Strings.fillString(Config.getInstance(context).getUnitVocabText()));
			tmp.setId(1);
			this.menuItems.add(tmp);
			
		}
		
		if (GroupHeader.lessonHasExercises(this.context, 
										   this.tracker.getCurrentUnit())) {
			
			tmp = new AppMenuItem(

					Strings.fillString(Config.getInstance(context).getUnitExercisesName()),
					Strings.fillString(Config.getInstance(context).getUnitExercisesText()));
			tmp.setId(2);
			this.menuItems.add(tmp);
			
		}
		
		if (GroupHeader.lessonHasGrammar(this.context,
										 this.tracker.getCurrentUnit())) {
		
			tmp = new AppMenuItem(
					Strings.fillString(Config.getInstance(context).getUnitGrammarName()),
					Strings.fillString(Config.getInstance(context).getUnitGrammarText()));
			tmp.setId(3);
			this.menuItems.add(tmp);
		
			
		}

		if (GroupHeader.lessonHasDialogs(this.context,
										 this.tracker.getCurrentUnit())) {
	
			tmp = new AppMenuItem(
					Strings.fillString(Config.getInstance(context).getUnitDialogName()),
					Strings.fillString(Config.getInstance(context).getUnitDialogText()));

			tmp.setId(4);
			this.menuItems.add(tmp);
		
			
		}
		
		ListView menu = new ListView(this.context);
		menu.setAdapter(new MenuItemAdapter(
							this.context, this.menuItems, this.appearance));
				
		//menu.setPadding(20,20,20,20);
		menu.setPadding(0, 20, 0, 0);
		
		menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, 
											View arg1, int arg2, long arg3) {
				
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				
				
				ft.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out,
						android.R.animator.fade_in,
						android.R.animator.fade_out);
				
				AppMenuItem tmp = menuItems.get(arg2);
				
				if (tmp.getId() == 0)
					ft.replace(R.id.content_frame, 
									(Fragment)new PatternScreenFragment())
													.addToBackStack(null);
					
				
				if (tmp.getId() == 1)
					ft.replace(R.id.content_frame, 
									(Fragment)new VocabularyScreenFragment())
													.addToBackStack(null);
					
				
				if (tmp.getId() == 2)				
					ft.replace(R.id.content_frame, 
									(Fragment)new ExerciseScreenFragment())
													.addToBackStack(null);

				
				if (tmp.getId() == 3)
					ft.replace(R.id.content_frame, 
									(Fragment)new GrammarScreenFragment())
													.addToBackStack(null);
				
				if (tmp.getId() == 4)
					ft.replace(R.id.content_frame, 
									(Fragment)new DialogScreenFragment())
													.addToBackStack(null);
				
				ft.commit();
				
			}
			
		});
		
		this.layout.addView(menu);
				
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.change_unit_menu, menu);
	}
	
public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.change_unit) {
		
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			
			
			ft.setCustomAnimations(android.R.animator.fade_in,
					android.R.animator.fade_out,
					android.R.animator.fade_in,
					android.R.animator.fade_out);
			
			ft.replace(R.id.content_frame, 
					(Fragment)new ChangeUnitScreenFragment())
									.addToBackStack(null);

			ft.commit();
			
			return true;
		} 
		
		return false;
		
	}
	
}
