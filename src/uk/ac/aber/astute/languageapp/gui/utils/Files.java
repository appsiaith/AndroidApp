package uk.ac.aber.astute.languageapp.gui.utils;

import java.io.IOException;
import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.backend.db.Appearance;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;

/**
 * A useful class for dealing with files in the application. We should never
 * make an instance of this class.
 * 
 * @author Mike Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
public class Files {
	
	/**
	 * We should never need to make a instance of this class, it is purely 
	 * a utility class, hence our constructor is private.
	 */
	private Files() { }

	/**
	 * Given a audio file name, in the format of "folder/something.mp3",
	 * we produce a MediaPlayer and return it.
	 * 
	 * @param context The context we're working from, this should be gotten
	 *                from the tracker instance.
	 *                
	 * @param audioFile The filename of the audio file, it can include a folder,
	 *                  and it must include a file format extension, e.g. mp3.
	 *                  All files should be a sub-item of the asset folder;
	 *                  though the asset folder should not be specified in the 
	 *                  path. As such, a file in "/assets/audio/me.mp3" would
	 *                  be specified as "audio/me.mp3". 
	 * 
	 * @return A MediaPlayer instance for the given audio file, or NULL if the
	 *         file could not be found.
	 *         
	 * @see Tracker
	 * @see MediaPlayer
	 */
	public static MediaPlayer getAudioFile(Context context, String audioFile) {
		
		MediaPlayer mp = new MediaPlayer();
		AssetFileDescriptor afd = null;
		
		if (audioFile == null)
			return null;
		
		try {
		
			afd = context.getAssets().openFd(audioFile);
			Log.i("LANG_APP", "Loading audio file: " + audioFile);
			if (afd != null) {
			
				mp.setDataSource(afd.getFileDescriptor(),
									afd.getStartOffset(),afd.getLength());
				
				mp.prepare();
			}
		
		} catch (Exception e) {
			Log.e("LANG_APP", "Error loading audio file: " + audioFile);
		}
		
		return mp;
		
	}
	
	/**
	 * Given a image file name, in the format of "folder/image.jpg" or
	 * "folder/image", we produce a Drawable object and return it.
	 * 
	 * @param context The context we're working from, this should be gotten
	 *                from the tracker instance.
	 *                
	 * @param fname The filename of the image file, it can include a folder,
	 *              and it may include a file format extension, e.g. .jpg.
	 *              All files should be a sub-item of the asset folder;
	 *              though the asset folder should not be specified in the
	 *              path. As such, a file in "/assets/images/me.jpg" would
	 *              be specified as either "images/me.jpg" or "images/me". 
	 *              If a file name does not include a file extension, .jpg
	 *              is assumed.
	 *              
	 * @return A Drawable instance for the given image file, or NULL if the 
	 *         file could not be found.
	 *        
	 * @see Tracker
	 * @see Drawable       
	 */
	public static Drawable getImageFromString(Context context, String fname) {
		
		Drawable d = null;
		ScaleDrawable sd = null;
		
		try {
			
			Log.i("LANG_APP", "Loading image file: " + fname + ".jpg");
		/*	d = BitmapDrawable.createFromStream(
					context.getAssets().open(fname + ".jpg"), null); */
			Bitmap b = BitmapFactory.decodeStream(context.getAssets().open(fname + ".jpg"));
			if (Appearance.getModifierPractice() == 2) {
				if (b != null) {
					Bitmap n = Bitmap.createScaledBitmap(b, 120, 120, false);
					d = new BitmapDrawable(context.getResources(), b);
				}
			} else {
				d = BitmapDrawable.createFromStream(
						context.getAssets().open(fname + ".jpg"), null);
			}
			
		} catch (IOException ioe) {
			
			Log.e("LANG_APP", "Error loading image file: " + fname + ".jpg");
			d = null;
			
		}
		
		if (d == null) {

			try {
				
				Log.i("LANG_APP", "Loading audio file: " + fname);
				/*d = BitmapDrawable.createFromStream(
						context.getAssets().open(fname), null);*/
				Bitmap b = BitmapFactory.decodeStream(context.getAssets().open(fname));
				if (Appearance.getModifierPractice() == 2) {
					if (b != null) {
						Bitmap n = Bitmap.createScaledBitmap(b, 120, 120, false);
						d = new BitmapDrawable(context.getResources(), b);
					}
				} else {
					d = BitmapDrawable.createFromStream(
							context.getAssets().open(fname), null);
				}
				
			} catch (IOException ioee) {
				
				Log.e("LANG_APP", "Error loading image file: " + fname);
				d = null;
				
			}
			
		}
		
		//if (d != null)
		//	sd = new ScaleDrawable(d, Gravity.CENTER,2.0f, 2.0f);
		
		return d;
		
	}
	
}