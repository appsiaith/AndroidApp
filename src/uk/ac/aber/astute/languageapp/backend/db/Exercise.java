package uk.ac.aber.astute.languageapp.backend.db;


import java.util.ArrayList;


import uk.ac.aber.astute.languageapp.backend.Tracker;
import uk.ac.aber.astute.languageapp.gui.utils.Files;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.SparseArray;

/**
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class Exercise {

	private static LanguageDatabase ldb;
	private static SparseArray<Exercise> cache;
	
	private int id;
	private int groupId;
	private String questionNorth;
	private String questionSouth;
	private String questionAudioSouth;
	private String questionAudioNorth;
	private String answerNorth;
	private String answerSouth;
	private String answerAudioSouth;
	private String answerAudioNorth;
	private String title;
	private Drawable picture;
	private int ordering;
	private Context context;
	
	private static final String p_str = "question";
	
	
	public static Exercise getExerciseById(Context context, int id) {
		
		Exercise instance = null;
		
		if (cache == null) cache = new SparseArray<Exercise>();
		if (cache.get(id) != null) return cache.get(id);
		
		ldb = LanguageDatabase.getInstance(context);
		
		String args[] = new String[] { ""+id };
		String id_eq = "id=?";
		
		try {
		
			instance = new Exercise();
			instance.setId(id);		
			instance.setContext(context);
			instance.setGroupId(
					ldb.getIntegerWithQuery(p_str, "groupId", id_eq, args));
			instance.setQuestionSouth(
				ldb.getStringWithQuery(p_str, "questionSouth", id_eq, args));
			instance.setQuestionNorth(
				ldb.getStringWithQuery(p_str, "questionNorth", id_eq, args));
			instance.setQuestionAudioSouth(
				ldb.getStringWithQuery(p_str, "questionAudioSouth", id_eq, args));
			instance.setQuestionAudioNorth(
				ldb.getStringWithQuery(p_str, "questionAudioNorth", id_eq, args));
			instance.setAnswerSouth(
				ldb.getStringWithQuery(p_str, "answerSouth", id_eq, args));
			instance.setAnswerNorth(
				ldb.getStringWithQuery(p_str, "answerNorth", id_eq, args));
			instance.setAnswerAudioSouth(
				ldb.getStringWithQuery(p_str, "answerAudioSouth", id_eq, args));
			instance.setAnswerAudioNorth(
				ldb.getStringWithQuery(p_str, "answerAudioNorth", id_eq, args));
			instance.setTitle(
					ldb.getStringWithQuery(p_str, "title", id_eq,args));
			instance.setPicture(
					ldb.getStringWithQuery(p_str, "picture", id_eq, args));
			instance.setOrdering(
					ldb.getIntegerWithQuery(p_str, "ordering", id_eq, args));

			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting exercise: " + dnfe.toString());
			instance = null;
		}
		
		if (instance != null) cache.put(id, instance);
		return instance;
		
	}
	
	public static ArrayList<Exercise> getExerciseByGroup(Context context, int groupId) {
		
		ArrayList<Exercise> list = null;
		ArrayList<Integer> ids = null;
		
		ldb = LanguageDatabase.getInstance(context);

		try {
			
			ids = ldb.getIntegerArrayWithQueryOrdered(p_str, "id", "groupId=?",
					new String[] {""+groupId},"ordering");
			list = new ArrayList<Exercise>();
			
			for (int i = 0; i < ids.size(); i++)
				if (Exercise.getExerciseById(context, ids.get(i)) != null)
					list.add(Exercise.getExerciseById(context, ids.get(i)));
			
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", "Error getting exercise: " + dnfe.toString());
			list = null;

		}
		
		return list;
		
	}
	
	private void setContext(Context context) {
		this.context = context;
	}
	
	private Context getContext() {
		return this.context;
	}
	
	private void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}

	private void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public int getGroupId() {
		return this.groupId;
	}
	
	private void setQuestionSouth(String questionSouth) {
		Log.e("LANG_APP", "Setting question sotuh:" + questionSouth);
		this.questionSouth = questionSouth;
	}
	
	private String getQuestionSouth() {
		return this.questionSouth;
	}
	
	private void setQuestionNorth(String questionNorth) {
		this.questionNorth = questionNorth;
	}
	
	private String getQuestionNorth() {
		
		if (this.questionNorth != null)
			if (!this.questionNorth.isEmpty())
				return this.questionNorth;
		
		return this.getQuestionSouth();
	}
	
	public String getQuestionLanguage() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getQuestionNorth();
		return this.getQuestionSouth();
	}
	
	private void setQuestionAudioSouth(String questionAudioSouth) {
		if (questionAudioSouth != null && !questionAudioSouth.isEmpty())
			this.questionAudioSouth = 
				Config.getInstance(context).getAudioFolder() + 
				"/" + questionAudioSouth;
		else
			this.questionAudioSouth = null;
	}
	
	private MediaPlayer getQuestionAudioSouth() {
		return Files.getAudioFile(this.getContext(), this.questionAudioSouth);
	}
	
	private void setQuestionAudioNorth(String questionAudioNorth) {
		if (questionAudioNorth != null && !questionAudioNorth.isEmpty())
			this.questionAudioNorth = 
				Config.getInstance(context).getAudioFolder() + 
				"/" + questionAudioNorth;
		else
			this.questionAudioNorth = null;
	}
	
	private MediaPlayer getQuestionAudioNorth() {
		if (this.questionAudioNorth != null)
			if (!this.questionAudioNorth.isEmpty())
				return Files.getAudioFile(this.getContext(), this.questionAudioNorth);
		
		return this.getQuestionAudioSouth();
		
	}
	
	public MediaPlayer getQuestionAudioLanguage() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getQuestionAudioNorth();
		return this.getQuestionAudioSouth();
	}

	private void setAnswerSouth(String answerSouth) {
		this.answerSouth = answerSouth;
	}
	
	private String getAnswerSouth() {
		return this.answerSouth;
	}
	
	private void setAnswerNorth(String answerNorth) {
		this.answerNorth = answerNorth;
	}
	
	private String getAnswerNorth() {
		
		if (this.answerNorth != null)
			if (!this.answerNorth.isEmpty())
				return this.answerNorth;
		
		return this.answerSouth;
		
	}
	
	public String getAnswerLanguage() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getAnswerNorth();
		return this.getAnswerSouth();
	}
	
	private void setAnswerAudioSouth(String answerAudioSouth) {
		this.answerAudioSouth = 
				Config.getInstance(context).getAudioFolder() +
				"/" + answerAudioSouth;
	}
	
	private MediaPlayer getAnswerAudioSouth() {
		return Files.getAudioFile(this.getContext(), this.answerAudioSouth);
	}
	
	private void setAnswerAudioNorth(String answerAudioNorth) {
		if (answerAudioNorth != null && !answerAudioNorth.isEmpty())
			this.answerAudioNorth = 
				Config.getInstance(context).getAudioFolder() +
				"/" + answerAudioNorth;
		else
			this.answerAudioNorth = null;
	}
	
	private MediaPlayer getAnswerAudioNorth() {
		
		if (this.answerAudioNorth != null) 
			if (!this.answerAudioNorth.isEmpty())
				return Files.getAudioFile(this.getContext(), this.answerAudioNorth);
		
		return this.getAnswerAudioSouth();
	}
	
	public MediaPlayer getAnswerAudioLanguage() {
		if (Tracker.getInstance().getNorthSouth() == Tracker.NorthSouth.NORTH)
			return this.getAnswerAudioNorth();
		return this.getAnswerAudioSouth();
	}
	
	private void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	private void setPicture(String picture) {
		this.picture = Files.getImageFromString(context,
				Config.getInstance(context).getImageFolder() + "/" + picture);
	}
	
	public Drawable getPicture() {
		return this.picture;
	}
	
	private void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	
	public int getOrdering() {
		return this.ordering;
	}
	
	
	/** 
	 * Whilst we do not have single instances of Appearance objects, we do not
	 * wish to allow Appearance objects to be created that do not match the
	 * database settings. 
	 */
	private Exercise() { }
	
}
