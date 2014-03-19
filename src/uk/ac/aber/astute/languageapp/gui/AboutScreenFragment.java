package uk.ac.aber.astute.languageapp.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import uk.ac.aber.astute.APPLICATION_NAME_HERE.R;
import uk.ac.aber.astute.languageapp.backend.AppMenuItem;
import uk.ac.aber.astute.languageapp.backend.db.About;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Strings;
import uk.ac.aber.astute.languageapp.gui.utils.Header;
import uk.ac.aber.astute.languageapp.gui.utils.MenuItemAdapter;

/**
 * This fragment is used to display the grammar list for a given unit or
 * lesson. 
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class AboutScreenFragment extends Fragment {
	
	private Appearance appearance;
	private Context context;
	private LinearLayout layout;
	private ArrayList<AppMenuItem> menuItems;
	
	/** Actually does nothing, just required by Fragments. */
	public AboutScreenFragment() { }
	
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
								this.context, Strings.ABOUT_STYLE);
			
		/* Build the layout style. */
		this.layout = new LinearLayout(this.context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		
		/* Inform the tracker that, if the currently selected unit changes,
		 * our display should be updated. Then request the display is 
		 * updated now. 
		 */
		this.updateDisplay();
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
		ArrayList<About> aboutList = About.getAllAbout(this.context);
				
		/* Now we can build the list of grammar. */
		this.menuItems = new ArrayList<AppMenuItem>();
		
		for (int i = 0; i < aboutList.size(); i++) {
			
			AppMenuItem item = new AppMenuItem(aboutList.get(i).getTitle(),
										aboutList.get(i).getInfo());
			
			item.setId(aboutList.get(i).getId());
			this.menuItems.add(item);
			
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
					About aboutItem = About.getAboutById(context,
																tmp.getId());
					
					if (aboutItem.getLinkType() == About.LinkType.LOCAL) {
					
						InputStream is;
						String fileName = aboutItem.getUrl();
						boolean worked = true;
						try {
							
							is = context.getAssets().open(aboutItem.getUrl());
						} catch (IOException e) {
							worked = false;
						}
						if (!worked) fileName = aboutItem.getUrl() + ".html";
						
						try {
							
							is = context.getAssets().open(fileName);
							
							byte[] buffer = new byte[is.available()];
							is.read(buffer);
							is.close();
												
							FragmentManager fragmentManager = getFragmentManager();
							FragmentTransaction ft = fragmentManager.beginTransaction();
							
							HTMLScreenFragment fragment = new HTMLScreenFragment();
							Log.e("LANG_APP:", "HTML: " + new String(buffer));
							fragment.setDisplay(aboutItem.getTitle(), 
														new String(buffer), null);
							
							ft.setCustomAnimations(android.R.animator.fade_in,
									android.R.animator.fade_out,
									android.R.animator.fade_in,
									android.R.animator.fade_out);
							ft.replace(R.id.content_frame,
										((Fragment)fragment)).addToBackStack(null);
							ft.commit();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

								
					} else {
						Uri uri = Uri.parse(aboutItem.getUrl());
						Intent i = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(i);
						
					}
					
			
				}
			
			});
				
			//menu.setPadding(20,20,20,20);
			menu.setPadding(0, 20, 0, 0);
			this.layout.addView(menu);
			
		}
				
	}
	
}
