package uk.ac.aber.astute.languageapp.gui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import uk.ac.aber.astute.languageapp.backend.db.Strings;

/**
 * This class is used to display HTML. It is used by a number of locations in
 * the application, such as the About and the Grammar screens.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class HTMLScreenFragment extends Fragment {
	
	private Appearance appearance;
	private Context context;
	private LinearLayout layout;
	private WebView webView;
	private String html;
	private String title;
	private String url;
	
	/** Actually does nothing, just required by Fragments. */
	public HTMLScreenFragment() {}
	
	/**
	 * Builds the display.
	 * 
	 * TODO: Stop using GLOBAL_GARRAM_STYLE and have the appearance info passed
	 * in, as we may be called from HELP, for example.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
												   Bundle savedInstanceState) {
		
		/* Acquire the context we're working in, and the layout settings. */
		this.context = container.getContext();
		this.appearance = Appearance.getApperanceFor(
								this.context, Strings.GLOBAL_GRAMMAR_STYLE);
		
		/* Build the fragment. */
		this.layout = new LinearLayout(this.context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		
		this.webView = new WebView(this.context);
		this.layout.setLayoutParams(new ViewGroup.LayoutParams(Tracker.getInstance().getWindowSize().x,
				Tracker.getInstance().getWindowSize().y));
		this.webView.setLayoutParams(new ViewGroup.LayoutParams(Tracker.getInstance().getWindowSize().x,
				Tracker.getInstance().getWindowSize().y));
		this.updateDisplay();
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		return this.layout;
		
	}
	
	/**
	 * This method is used to set the title and HTML content of the screen.
	 * 
	 * @param title The title for the window.
	 * 
	 * @param html The HTML content to be displayed. This should be as HTML,
	 *             not a filename.
	 */
	public void setDisplay(String title, String html, String url) {
		
		this.title = title;
		this.html = html;
		this.url = url;
		
	}
	
	/**
	 * This function is actually used to build the main display, and is also
	 * called by the tracker instance if ever the unit is changed in the 
	 * background, allowing us to update this unit and display the correct
	 * menu options.
	 */
	public void updateDisplay() {
		
		if (!this.isAdded()) return;
		
		this.layout.removeAllViews();
		
		/* Set the background parameters. */
		this.layout.setBackgroundColor(this.appearance.getBackColour());
		if(this.appearance.getBackImage() != null)
			this.layout.setBackground(this.appearance.getBackImage());
		
		if (url != null) {
			this.webView.loadUrl(url);
		} else {
			this.webView.loadData(html, "text/html", null);
		}		
		
		this.layout.addView(webView);
		this.getActivity().setTitle(title);
				
	}
	
}
