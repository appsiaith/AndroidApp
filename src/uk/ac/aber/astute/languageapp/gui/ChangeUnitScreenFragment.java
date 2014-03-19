package uk.ac.aber.astute.languageapp.gui;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


import uk.ac.aber.astute.languageapp.backend.NotifyOnUpdate;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.UnitAppMenuItem;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import uk.ac.aber.astute.languageapp.backend.db.Unit;
import uk.ac.aber.astute.languageapp.gui.utils.UnitSelectorMenuItemAdapter;

/**
 * This fragment is used to display the "grammar" list for the whole 
 * application.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class ChangeUnitScreenFragment extends Fragment implements NotifyOnUpdate {
	
	private Appearance appearance;
	private Context context;
	private LinearLayout layout;
	private ArrayList<UnitAppMenuItem> menuItems;
	
	/** Actually does nothing, just required by Fragments. */
	public ChangeUnitScreenFragment() { }
	
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
		this.layout.setBackgroundColor(Color.WHITE);	
		
		/* Set the screen title. */
		if (this.isAdded())
			this.getActivity().setTitle("Change unit to study");
		
		/* Get all the units. */
		ArrayList<Unit> units = Unit.getAllUnits(context);
		this.menuItems = new ArrayList<UnitAppMenuItem>();
		
		TextView title = new TextView(context);
		title.setText(Strings.fillString(Strings.SELECT_BETWEEN_STRING));
		
		title.setGravity(Gravity.CENTER);
		title.setPadding(20,40,20,20);
	
		TextView current = new TextView(context);
		current.setText(Strings.fillString(Strings.CURRENT_STRING));
		current.setGravity(Gravity.CENTER);
		current.setPadding(20, 20, 20, 20);
		
		final Switch northSouthSwitch = new Switch(context);
		northSouthSwitch.setTextOn("North");
		northSouthSwitch.setTextOff("South");
		northSouthSwitch.setText("");
		northSouthSwitch.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		layout.setGravity(Gravity.CENTER);
		northSouthSwitch.setGravity(Gravity.CENTER_HORIZONTAL);
		northSouthSwitch.setPadding(20,20,20,20);
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH) {
			northSouthSwitch.setChecked(true);
		} else {
			northSouthSwitch.setChecked(false);
		}
		northSouthSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (northSouthSwitch.isChecked())
					Tracker.getInstance().setNorthSouth(Tracker.NorthSouth.NORTH);
				else
					Tracker.getInstance().setNorthSouth(Tracker.NorthSouth.SOUTH);
				
				Log.e("LANG_APP", "Changed dialect to: " + arg1);
			}
		});
		
		
		layout.addView(title);
		layout.addView(northSouthSwitch);
		layout.addView(current);
		
		/* Build the menu items. */
		for (int i = 0; i < units.size(); i++) {
			
			boolean selected = ((i + 1) == Tracker.getInstance().getCurrentUnit());
			
			UnitAppMenuItem item = new UnitAppMenuItem(
					"Unit " + (i + 1),
					units.get(i).getEnglish() + "\n" +
					units.get(i).getLanguage(), selected);
			
			item.setId(units.get(i).getId());
			this.menuItems.add(item);
			
		}
				
		/* Build the menu. */
		ListView menu = new ListView(this.context);
		menu.setAdapter(new UnitSelectorMenuItemAdapter(this.context, 
											this.menuItems, this.appearance));
		
		menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, 
										View arg1, int arg2, long arg3) {
				
				Log.e("LANG_APP", "Item clicked...");
											
				UnitAppMenuItem tmp = menuItems.get(arg2);
				Tracker.getInstance().setCurrentUnit(tmp.getId());	
				
			}
			
		});
				
		menu.setPadding(20,20,20,20);
		//menu.setPadding(0, 20, 0, 20);
		this.layout.addView(menu);
				
	}
	
}