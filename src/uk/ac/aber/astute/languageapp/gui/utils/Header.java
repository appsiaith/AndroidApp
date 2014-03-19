package uk.ac.aber.astute.languageapp.gui.utils;

import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class is used to build the header section of all screens within the
 * application. It deals with the positioning of a header image or text, 
 * as described by the passed in Appearance object.
 * 
 * @author Mike Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 * @see Appearance
 */
public class Header extends LinearLayout {

	private String title = null;
	private Appearance appearance = null;
	private Tracker tracker = null;
	private Context context;
	
	/**
	 * Creates a new instance of the Header object. For various reasons,
	 * this constructor does not immediately call setAppearance or setTitle,
	 * etc.
	 *
	 * @param context The context that we're working from. 
	 */
	public Header(Context context) {
		super(context);
		this.context = context;
		this.tracker = Tracker.getInstance();
	}
	
	/**
	 * Sets the appearance style to be used for this header. The appearance
	 * object describes images, titles, etc. Once the appearance is set it 
	 * called {@link #build()} to request the header is built according to the 
	 * latest title and appearance settings.
	 *
	 * @param appearance The Appearance instance that describes what the Header
	 *                   should look like.
	 * 
	 * @see Appearance
	 */
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
		this.build();
	}
	
	/**
	 * Sets the title string to be used for this header. Once the title
	 * is set it called {@link #build()} to request the header is built
	 * according to the latest title and appearance settings. 
	 * 
	 * @param title The title string to be used for this header.
	 */
	public void setTitle(String title) {
		this.title = title;
		this.build();
	}
	
	/**
	 * This method is called privately by {@link #setTitle(String)} and 
	 * {@link #setAppearance(Appearance)}. It requires both 
	 * {@link #setTitle(String)} and {@link #setAppearance(Appearance)}
	 * to have been called before it will do anything.
	 */
	private void build() {
		
		/* Check we have everything needed to build. */
		if (this.appearance == null) return;
		if (this.title == null) return;
		
		if (this.appearance.getHeaderImage() != null) {
			
			LinearLayout.LayoutParams params;
			Drawable d;
			ImageView iv;
			float scale;
			
			d = this.appearance.getHeaderImage();
			
			scale = ((float)(tracker.getWindowSize().x) 
								/ (float)(d.getIntrinsicWidth()));
			
			params = new LinearLayout.LayoutParams(
					(int)(d.getIntrinsicWidth() * scale),
								(int)(d.getIntrinsicHeight() * scale));
			
			iv = new ImageView(this.context);
			iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			iv.setImageDrawable(d);
			iv.setAdjustViewBounds(true);
			iv.setLayoutParams(params);
			
			this.addView(iv);
			
		} else {
			
			TextView tv = new TextView(this.context);
			tv.setBackgroundColor(this.appearance.getHeaderCellColour());
			tv.setText(this.title);
			tv.setTextColor(this.appearance.getHeaderFontColour());
			tv.setTextSize(this.appearance.getHeaderFontSize());
			tv.setPadding(10, 10, 10, 10);
			tv.setWidth(tracker.getWindowSize().x);
			tv.setMinimumHeight(this.appearance.getHeaderHeight());
			tv.setGravity(Gravity.CENTER);
			
			this.addView(tv);
			
		}
		
		this.setBackgroundColor(Color.argb(0,255,255,255));
		this.setPadding(0, 80, 0, 20);
		
	}

}