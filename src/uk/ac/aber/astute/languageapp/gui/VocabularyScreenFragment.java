package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import uk.ac.aber.astute.APPLICATION_NAME_HERE.R;
import uk.ac.aber.astute.languageapp.backend.AppMenuItem;
import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.GroupHeader;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import uk.ac.aber.astute.languageapp.backend.db.Vocab;
import uk.ac.aber.astute.languageapp.gui.utils.Header;
import uk.ac.aber.astute.languageapp.gui.utils.MenuItemAdapter;

/**
 * This fragment is used to display the vocabulary list for a given unit or
 * lesson. 
 * 
 * @author Mike Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class VocabularyScreenFragment extends Fragment 
										implements NotifyOnUpdate {
	
	private Appearance appearance;
	private Context context;
	private LinearLayout layout;
	private Tracker tracker;
	private ArrayList<AppMenuItem> menuItems;
	private Menu m;
	private boolean orderWelsh = false;
	
	/** Actually does nothing, just required by Fragments. */
	public VocabularyScreenFragment() { }
	
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
									this.context, Strings.VOCAB_STYLE);
		
		this.tracker = Tracker.getInstance();
		
		/* Build the layout style. */
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
		
		/* We need to get the group headers for the current unit. */
		ArrayList<GroupHeader> groups = GroupHeader.getGroupHeaders(
				this.context, tracker.getCurrentUnit(), 
									GroupHeader.VOCAB_GROUP);
		
		/* Now we can build the list of vocab. */
		this.menuItems = new ArrayList<AppMenuItem>();
		
		if (groups != null) {
			for (int i = 0; i < groups.size(); i++) {
				
				/* For each group, get all the vocab. */
				ArrayList<Vocab> vocab = Vocab.getVocabByGroupOrderedBy(
								this.context, groups.get(i).getId(),"english");
				if (orderWelsh)
					vocab = Vocab.getVocabByGroupOrderedBy(
							this.context, groups.get(i).getId(),"south");
				
				for (int j = 0; j < vocab.size(); j++) {
					
					AppMenuItem tmp = new AppMenuItem(vocab.get(j).getEnglish(),
												vocab.get(j).getLanguage());
					tmp.setId(vocab.get(j).getId());
					this.menuItems.add(tmp);					
				}

			}
		}
		
		/* Put together the list into a menu. */
		ListView menu = new ListView(this.context);
		
		if (this.menuItems.size() > 0) {
		
			menu.setAdapter(new MenuItemAdapter(
					this.context, this.menuItems, this.appearance));
		
			menu.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, 
										View arg1, int arg2, long arg3) {
								
					AppMenuItem tmp = menuItems.get(arg2);
				
					MediaPlayer mp = Vocab.getVocabById(context, 
											tmp.getId()).getAudioLanguage();
				
					if (mp != null) mp.start();

				}
			
			});
			
			//menu.setPadding(20,20,20,20);
			menu.setPadding(0, 20, 0, 0);
			this.layout.addView(menu);
			
		}
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.dictionary_menu, menu);
		m = menu;
		m.findItem(R.id.order_english).setVisible(false);
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.order_english) {
			m.findItem(R.id.order_welsh).setVisible(true);
			m.findItem(R.id.order_english).setVisible(false);
			orderWelsh = false;
			this.updateDisplay();
			return true;
		} else if (item.getItemId() == R.id.order_welsh) {
			m.findItem(R.id.order_welsh).setVisible(false);
			m.findItem(R.id.order_english).setVisible(true);
			orderWelsh = true;
			this.updateDisplay();
			return true;
		} 
		
		
		
		return false;
		
	}
	
}