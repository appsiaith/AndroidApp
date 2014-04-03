package uk.ac.aber.astute.languageapp.gui.utils;

import java.util.ArrayList;
import uk.ac.aber.astute.APPLICATION_NAME_HERE.R;
import uk.ac.aber.astute.languageapp.backend.AppMenuItem;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.UnitAppMenuItem;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * This adapter is used to build menu items in the application, according to 
 * the look and feel described in a Appearance object.
 * 
 * @author Mike Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 * @see Appearance
 */
public class UnitSelectorMenuItemAdapter implements ListAdapter { 

	private final Context context;
	private ArrayList<UnitAppMenuItem> items;
	private Appearance appearance;
	
	/**
	 * Constructor for the adapter. Contains a list of items to display, 
	 * and the appearance instance defining how the menu items should look.
	 * 
	 * @param context The context we're working in.
	 * 
	 * @param items The list of items to be displayed in the menu. This
	 *              should be an {@link ArrayList} of {@link AppMenuItem} 
	 *              objects.
	 *          
	 * @param appearance The {@link Appearance} object defining how the menu
	 *                   items should look. 
	 */
	public UnitSelectorMenuItemAdapter(Context context, ArrayList<UnitAppMenuItem> items, 
														Appearance appearance) {
		this.context = context;
		this.items = items;
		this.appearance = appearance;
		
	}
	
	/**
	 * Builds the view for a given menu item.
	 * 
	 * @param position The position in the item list that we're trying to get
	 *                 the view for.
	 * 
	 * @param convertView Not sure what this is actually used for, we don't use
	 *                    it in this application. Simply required by the 
	 *                    interface.
	 * 
	 * @param parent Defines the parent, required by the {@link LayoutInflater}.
	 * 
	 * @return A View representing the given menu item.
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = 
				(LayoutInflater)context.getSystemService(
									Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.unit_menu, parent, false);
		
		TextView title = (TextView) rowView.findViewById(R.id.title);
		TextView subtext = (TextView) rowView.findViewById(R.id.subtext);
		CheckBox selected = (CheckBox) rowView.findViewById(R.id.selected);
		
		selected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				Tracker.getInstance().setCurrentUnit(items.get(position).getId());
				
			}

		});
		
		title.setTextSize(this.appearance.getMainFontSize());
		subtext.setTextSize(this.appearance.getSubFontSize());
		
		title.setTextColor(Color.BLUE);
		subtext.setTextColor(Color.GRAY);
		
		title.setText(items.get(position).getMainText());
		subtext.setText(items.get(position).getSubText());
				
		rowView.setBackgroundColor(Color.WHITE);
		rowView.setMinimumHeight(this.appearance.getCellHeight());
		
		selected.setChecked(items.get(position).getSelected());
		//selected.setEnabled(false);
		
		if (!this.appearance.getTwoTexts() || 
				items.get(position).getSubText().isEmpty())
			subtext.setTextSize(0);
		
		
		rowView.setPadding(0, 10, 0, 10);	
		rowView.setClickable(true);
		return rowView;
		
	}
	
	/**
	 * Gets the number of menu items that this adapter has.
	 * 
	 * @return The number of menu items that the adapter has in its list.
	 */
	@Override
	public int getCount() {
		return items.size();
	}

	/**
	 * Gets a specific menu item by its index, casted to a standard Object.
	 * 
	 * @return The Object version of the menu item at the specified index.
	 */
	@Override
	public Object getItem(int index) {
		return items.get(index);
	}

	/**
	 * Used to see if the adapter has any menu items.
	 * 
	 * @return true if there are no items, otherwise false.
	 */
	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}
	/**
	 * Gets the number of menu items that this adapter has.
	 * 
	 * @return The number of menu items that the adapter has in its list.
	 */
	@Override
	public int getViewTypeCount() {
		return items.size();
	}
	
	/** Unused in our implementation.
	 * @return Always returns zero. 
	 */
	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	/** Unused in our implementation. 
	 * @return Always returns zero.
	 */
	@Override
	public int getItemViewType(int arg0) {
		return 0;
	}

	/** Unused in our implementation. 
	 * @return Always false.
	 */
	@Override
	public boolean hasStableIds() {
		return false;
	}

	/** Unused in our implementation. */
	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		
	}

	/** Unused in our implementation. */
	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		
	}

	/** Unused in our implementation.
	 * @return Always true.
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	/** Unused in our implementation. 
	 * @return Always true.
	 */
	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}

}

