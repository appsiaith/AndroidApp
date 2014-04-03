package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import uk.ac.aber.astute.languageapp.backend.db.Grammar;
import uk.ac.aber.astute.languageapp.backend.db.GroupHeader;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import uk.ac.aber.astute.languageapp.gui.utils.Header;
import uk.ac.aber.astute.languageapp.gui.utils.MenuItemAdapter;

/**
 * This fragment is used to display the "grammar" list for the whole 
 * application.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class AllGrammarScreenFragment extends Fragment implements NotifyOnUpdate {
	
	private Appearance appearance;
	private Context context;
	private LinearLayout layout;
	private ArrayList<AppMenuItem> menuItems;
	
	/** Actually does nothing, just required by Fragments. */
	public AllGrammarScreenFragment() { }
	
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
								this.context, Strings.GLOBAL_GRAMMAR_STYLE);
		
		/* Build the layout style. */
		this.layout = new LinearLayout(this.context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		
		/* Request the display is updated. */
		this.updateDisplay();
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		Tracker.getInstance().notifyOnUpdate(this);
		
		this.setHasOptionsMenu(true);
		return this.layout;
		
	}
	
	/**
	 * This function is actually used to build the main display. It first
	 * clears all views in the layout, and updates the display according to
	 * the current information available. Information available is usually
	 * things such as the current unit, group, etc. However, because this 
	 * fragment is used for the home screen we don't need that info - we really
	 * do want to display *every* grammar item available in the database.
	 */
	public void updateDisplay() {
		
		if (!this.isAdded()) return;
		
		this.layout.removeAllViews();
		
		/* Set the background parameters. */
		this.layout.setBackgroundColor(this.appearance.getBackColour());
		if(this.appearance.getBackImage() != null)
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
		
		
		/* Get the items to put in the menu. */
		ArrayList<GroupHeader> groupHeaders = 
				GroupHeader.getAllGroupHeadersByTypeOrderBy(this.context, 
											GroupHeader.GRAMMAR_GROUP, "lessonId");
				
		this.menuItems = new ArrayList<AppMenuItem>();
		
		for (int i = 0; i < groupHeaders.size(); i++) {
			
			AppMenuItem item = new AppMenuItem(
					groupHeaders.get(i).getEnglish(),
				Strings.extraFillString(Strings.GLOBAL_GRAMMAR_UNIT_TITLE,
										groupHeaders.get(i).getLessonId()));
			
			item.setId(groupHeaders.get(i).getId());
			this.menuItems.add(item);
			
		}
		
		/* Build the menu. */
		ListView menu = new ListView(this.context);
		menu.setAdapter(new MenuItemAdapter(this.context, this.menuItems, 
															this.appearance));
		
		menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, 
										View arg1, int arg2, long arg3) {
											
				AppMenuItem tmp = menuItems.get(arg2);
				
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				
				ArrayList<Grammar> g = 
						Grammar.getGrammarByGroup(context, tmp.getId());
		
				if (g.size() == 1) {
					
					HTMLScreenFragment fragment = new HTMLScreenFragment();	
					
					fragment.setDisplay(g.get(0).getTitle(), 
												g.get(0).getHTML(), null);
					
					ft.setCustomAnimations(android.R.animator.fade_in,
							android.R.animator.fade_out,
							android.R.animator.fade_in,
							android.R.animator.fade_out);
					ft.replace(R.id.content_frame,
							((Fragment)fragment)).addToBackStack(null);
					ft.commit();
					
				}
				
			}
			
		});
				
		//menu.setPadding(20,20,20,20);
		menu.setPadding(0, 20, 0, 0);
		this.layout.addView(menu);
				
	}
	
}