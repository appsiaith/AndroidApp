package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
import uk.ac.aber.astute.APPLICATION_NAME_HERE.R;
import uk.ac.aber.astute.languageapp.backend.AppMenuItem;
import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Config;
import uk.ac.aber.astute.languageapp.backend.db.GroupHeader;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import uk.ac.aber.astute.languageapp.backend.db.Vocab;
import uk.ac.aber.astute.languageapp.gui.utils.Header;
import uk.ac.aber.astute.languageapp.gui.utils.MenuItemAdapter;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * This class holds the home screen fragment. This fragment is used to display
 * the main menu with the unit choices, etc. 
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.3
 */
@SuppressLint("NewApi")
public class HomeScreenFragment extends Fragment implements NotifyOnUpdate {
	
	private Appearance appearance;
	private Context context;
	private ArrayList<AppMenuItem> menuItems;
	private Tracker tracker;
	private LinearLayout layout;
	
	/** Actually does nothing, just required by Fragments. */
	public HomeScreenFragment() { }
	
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
								this.context, Strings.HOME_SCREEN_STYLE);
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
		
		/* We need to cache some stuff to make some screens faster! */
		new Thread(new Runnable() {
			public void run() {
				GroupHeader.getAllGroupHeadersByType(context, 
										GroupHeader.GRAMMAR_GROUP);
				
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				Vocab.getAllVocabOrderedEnglish(context);
			}
		}).start();
		
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
		this.layout.setBackgroundColor(this.appearance.getBackColour());
		if (this.appearance.getBackImage() != null)
			this.layout.setBackground(this.appearance.getBackImage());
	
		/* Build and display the header. */
		Header header = new Header(this.context);
		header.setTitle(Strings.fillString(
				Config.getInstance(context).getProjectName()));
		header.setAppearance(this.appearance);
		this.layout.addView(header);
		
		/* Set the screen title. */
		if (this.isAdded())
			this.getActivity().setTitle(
				Strings.fillString(
						Config.getInstance(context).getProjectName()));
		
		/* Build and display the menu. */
		menuItems = new ArrayList<AppMenuItem>();
		AppMenuItem tmp;
		
		tmp = new AppMenuItem(
				Strings.fillString(
						Config.getInstance(context).getMenuCurrentUnitName()),
				Strings.fillString(
						Config.getInstance(context).getMenuCurrentUnitText()));
		tmp.setId(0);
		menuItems.add(tmp);
		
		tmp = new AppMenuItem(
				Strings.fillString(
						Config.getInstance(context).getMenuRevisionName()),
				Strings.fillString(
						Config.getInstance(context).getMenuRevisionText()));
		tmp.setId(1);
		menuItems.add(tmp);
		
		tmp = new AppMenuItem(
				Strings.fillString(
						Config.getInstance(context).getMenuDictionaryName()),
				Strings.fillString(
						Config.getInstance(context).getMenuDictionaryText()));
		tmp.setId(2);
		menuItems.add(tmp);
		
		tmp = new AppMenuItem(
				Strings.fillString(
						Config.getInstance(context).getMenuGrammarName()),
				Strings.fillString(
						Config.getInstance(context).getMenuGrammarText()));
		tmp.setId(3);
		menuItems.add(tmp);
				
		tmp = new AppMenuItem(
				Strings.fillString(
						Config.getInstance(context).getMenuAboutName()),
				Strings.fillString(
						Config.getInstance(context).getMenuAboutText()));
		tmp.setId(4);
		menuItems.add(tmp);
				
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
				
				if (tmp.getId() == 0 )					
					ft.replace(R.id.content_frame, 
									(Fragment)new UnitScreenFragment())
													.addToBackStack(null);
				
				if (tmp.getId() == 1)
					ft.replace(R.id.content_frame,
									(Fragment)new RevisionScreenFragment())
													.addToBackStack(null);
								
				if (tmp.getId() == 2)
					ft.replace(R.id.content_frame,
									(Fragment)new DictionaryScreenFragment())
													.addToBackStack(null);
				
				if (tmp.getId() == 3)
					ft.replace(R.id.content_frame,
									(Fragment)new AllGrammarScreenFragment())
													.addToBackStack(null);
				if (tmp.getId() == 4)
					ft.replace(R.id.content_frame,
									(Fragment)new AboutScreenFragment())
													.addToBackStack(null);
		
				
				ft.commit();
				
			}
		});
		

		this.layout.addView(menu);
		
	}
	
}
	

