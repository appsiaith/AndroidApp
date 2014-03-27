package uk.ac.aber.astute.languageapp.backend.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

/**
 * This class is used to both load in and manage the configuration options for
 * the application. This includes information such as where images, sounds and
 * HTML files can be found, the name of the project, what the menu item names
 * should be, etc.
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.1
 */
@SuppressLint("NewApi")
public class Config {

	private String projectName;
	private String imageFolder;
	private String audioFolder;
	private String htmlFolder;
	private String aboutName;
	private String lessonName;
	private String dialogName;
	private String grammarName;
	private String questionName;
	private String patternName;
	private String vocabularyName;
	private String southName;
	private String northName;
	private String englishName;
	private String menuCurrentUnitName;
	private String menuCurrentUnitText;
	private String menuRevisionName;
	private String menuRevisionText;
	private String menuDictionaryName;
	private String menuDictionaryText;
	private String menuGrammarName;
	private String menuGrammarText;
	private String menuAboutName;
	private String menuAboutText;
	private String unitPatternName;
	private String unitPatternText;
	private String unitVocabName;
	private String unitVocabText;
	private String unitExercisesName;
	private String unitExercisesText;
	private String unitGrammarName;
	private String unitGrammarText;
	private String unitDialogName;
	private String unitDialogText;
	
	private static String config = "config";
	private static String menu = "menu";
	private static String text = "text";
	private static String subText = "subText";
	private static String defaultV = "defaultValue";
	private static String appV = "customValue";
	private static String args = "key=?";
	
	private static String [] PROJECT_NAME = new String[] { "PROJECT_NAME" };
	private static String [] IMAGE_FOLDER = new String[] { "IMAGE_FOLDER" };
	private static String [] AUDIO_FOLDER = new String[] { "AUDIO_FOLDER" };
	private static String [] HTML_FOLDER = new String[] { "HTML_FOLDER" };
	private static String [] ABOUT_NAME = new String[] { "ABOUT_NAME" };
	private static String [] LESSON_NAME = new String[] { "LESSON_NAME" };
	private static String [] DIALOG_NAME = new String[] { "DIALOG_NAME" };
	private static String [] GRAMMAR_NAME = new String[] { "GRAMMAR_NAME" };
	private static String [] QUESTION_NAME = new String[] { "QUESTION_NAME" };
	private static String [] PATTERN_NAME = new String[] { "PATTERN_NAME" };
	private static String [] VOCABULARY_NAME = new String[] {"VOCABULARY_NAME"};
	private static String [] SOUTH_NAME = new String[] { "SOUTH_NAME" };
	private static String [] NORTH_NAME = new String[] { "NORTH_NAME" };
	private static String [] ENGLISH_NAME = new String[] { "ENGLISH_NAME" };
	private static String [] MENU_CURRENTUNIT_NAME = new String[] { "MAIN_1" };
	private static String [] MENU_REVISION_NAME = new String[] { "MAIN_2" };
	private static String [] MENU_DICTIONARY_NAME = new String[] { "MAIN_3" };
	private static String [] MENU_GRAMMAR_NAME = new String[] { "MAIN_4" };
	private static String [] MENU_ABOUT_NAME = new String[] { "MAIN_5" };
	private static String [] UNIT_PATTERN_NAME = new String[] { "UNIT_1" };
	private static String [] UNIT_VOCAB_NAME = new String[] { "UNIT_2" };
	private static String [] UNIT_EXERCISES_NAME = new String[] { "UNIT_3" };
	private static String [] UNIT_GRAMMAR_NAME = new String[] { "UNIT_4" };
	private static String [] UNIT_DIALOG_NAME = new String[] { "UNIT_5" };
	
	private static Config instance;
	
	/**
	 * We only ever wish to have once instance of the configuration. This 
	 * static method is used to load the configuration in from the database,
	 * if it has not already been loaded, and is then used to return the 
	 * current configuration instance.
	 * 
	 * @param context The context that the config exists in.
	 * 
	 * @return The current Config instance.
	 */
	public static Config getInstance(Context context) {
		
		if (instance != null)
			return instance;
		
		instance = new Config();
		
		LanguageDatabase ldb;
		
		ldb = LanguageDatabase.getInstance(context);
		
		try {
			
			instance = new Config();
			
			instance.setProjectName(
				ldb.getStringWithQuery(config,defaultV,args,PROJECT_NAME ),
				ldb.getStringWithQuery(config,appV,args,PROJECT_NAME ) );
			
			instance.setImageFolder(
				ldb.getStringWithQuery(config,defaultV,args,IMAGE_FOLDER ),
				ldb.getStringWithQuery(config,appV,args,IMAGE_FOLDER ) );
			
			instance.setHtmlFolder(
				ldb.getStringWithQuery(config,defaultV,args,HTML_FOLDER),
				ldb.getStringWithQuery(config,appV,args,HTML_FOLDER) );
			
			instance.setAudioFolder(
				ldb.getStringWithQuery(config,defaultV,args,AUDIO_FOLDER ),
				ldb.getStringWithQuery(config,appV,args,AUDIO_FOLDER ) );
			
			instance.setAboutName(
				ldb.getStringWithQuery(config,defaultV,args,ABOUT_NAME ),
				ldb.getStringWithQuery(config,appV,args,ABOUT_NAME ) );
			
			instance.setLessonName(
				ldb.getStringWithQuery(config,defaultV,args,LESSON_NAME ),
				ldb.getStringWithQuery(config,appV,args,LESSON_NAME ) );
			
			instance.setDialogName(
				ldb.getStringWithQuery(config,defaultV,args,DIALOG_NAME ),
				ldb.getStringWithQuery(config,appV,args,DIALOG_NAME ) );
			
			instance.setGrammarName(
				ldb.getStringWithQuery(config,defaultV,args,GRAMMAR_NAME ),
				ldb.getStringWithQuery(config,appV,args,GRAMMAR_NAME ) );
			
			instance.setQuestionName(
				ldb.getStringWithQuery(config,defaultV,args,QUESTION_NAME ),
				ldb.getStringWithQuery(config,appV,args,QUESTION_NAME ) );
			
			instance.setPatternName(
				ldb.getStringWithQuery(config,defaultV,args,PATTERN_NAME ),
				ldb.getStringWithQuery(config,appV,args,PATTERN_NAME ) );
			
			instance.setVocabularyName(
				ldb.getStringWithQuery(config,defaultV,args,VOCABULARY_NAME ),
				ldb.getStringWithQuery(config,appV,args,VOCABULARY_NAME ) );
			
			instance.setSouthName(
				ldb.getStringWithQuery(config,defaultV,args,SOUTH_NAME ),
				ldb.getStringWithQuery(config,appV,args,SOUTH_NAME ) );
			
			instance.setNorthName(
				ldb.getStringWithQuery(config,defaultV,args,NORTH_NAME ),
				ldb.getStringWithQuery(config,appV,args,NORTH_NAME ) );
			
			instance.setEnglishName(
				ldb.getStringWithQuery(config,defaultV,args,ENGLISH_NAME ),
				ldb.getStringWithQuery(config,appV,args,ENGLISH_NAME ) );
			
			instance.setMenuCurrentUnit(
				ldb.getStringWithQuery(menu,text,args,MENU_CURRENTUNIT_NAME ),
			  ldb.getStringWithQuery(menu,subText,args,MENU_CURRENTUNIT_NAME));
			
			instance.setMenuAbout(
				ldb.getStringWithQuery(menu,text,args,MENU_ABOUT_NAME ),
				ldb.getStringWithQuery(menu,subText,args,MENU_ABOUT_NAME ) );
			
			instance.setMenuDictionary(
				ldb.getStringWithQuery(menu,text,args,MENU_DICTIONARY_NAME ),
				ldb.getStringWithQuery(menu,subText,args,MENU_DICTIONARY_NAME));
			
			instance.setMenuRevision(
				ldb.getStringWithQuery(menu,text,args,MENU_REVISION_NAME ),
				ldb.getStringWithQuery(menu,subText,args,MENU_REVISION_NAME ) );
			
			instance.setMenuGrammar(
				ldb.getStringWithQuery(menu,text,args,MENU_GRAMMAR_NAME ),
				ldb.getStringWithQuery(menu,subText,args,MENU_GRAMMAR_NAME ) );
			
			instance.setUnitPattern(
				ldb.getStringWithQuery(menu,text,args,UNIT_PATTERN_NAME ),
				ldb.getStringWithQuery(menu,subText,args,UNIT_PATTERN_NAME ) );
			
			instance.setUnitVocab(
				ldb.getStringWithQuery(menu,text,args,UNIT_VOCAB_NAME ),
				ldb.getStringWithQuery(menu,subText,args,UNIT_VOCAB_NAME ) );
			
			instance.setUnitExercises(
				ldb.getStringWithQuery(menu,text,args,UNIT_EXERCISES_NAME ),
				ldb.getStringWithQuery(menu,subText,args,UNIT_EXERCISES_NAME ));
			
			instance.setUnitGrammar(
				ldb.getStringWithQuery(menu,text,args,UNIT_GRAMMAR_NAME ),
				ldb.getStringWithQuery(menu,subText,args,UNIT_GRAMMAR_NAME ) );
			
			instance.setUnitDialog(
				ldb.getStringWithQuery(menu,text,args,UNIT_DIALOG_NAME ),
				ldb.getStringWithQuery(menu,subText,args,UNIT_DIALOG_NAME ) );
			 
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", dnfe.toString());
			instance = null;
			
		}
		
		return instance;
	}
	
	/**
	 * This class is a singleton, only the class itself should be able to 
	 * create an instance of the configuration object.
	 */
	private Config() { }
	
	/**
	 * Sets the project name, this is the name of the application. 
	 * 
	 * @param defaultS Default string from the database, used if no optional
	 *                 string is specified.
	 *                 
	 * @param appS Optional, application specific string.
	 */
	private void setProjectName(String defaultS, String appS) {
		
		if (appS != null && !appS.isEmpty()) {
			this.projectName = appS;
		} else {
			this.projectName = defaultS;
		}
		
	}
	
	/**
	 * Gets the name of the project.
	 * 
	 * @return The name of the project.
	 */
	public String getProjectName() {
		
		return this.projectName;
		
	}
	
	/**
	 * Sets the name of, and path of, the image folder.
	 * 
	 * @param defaultF Default string from the database, used if no optional
	 *                 string is specified. This is "/images".
	 *                 
	 * @param appF Optional, application specific string.
	 */
	private void setImageFolder(String defaultF, String appF) {
		
		if (appF != null && !appF.isEmpty()) {
			this.imageFolder = appF;
		} else {
			this.imageFolder = defaultF;
		}
		
	}
	
	/**
	 * Gets the location (path) of the image folder.
	 * 
	 * @return The location/path of the image folder.
	 */
	public String getImageFolder() {
		
		return this.imageFolder;
		
	}
	
	/**
	 * Sets the name of, and path of, the audio folder.
	 * 
	 * @param defaultF Default string from the database, used if not optional
	 *                 string is specified. This is "/audio".
	 *                 
	 * @param appF Optional, application specified string.
	 */
	private void setAudioFolder(String defaultF, String appF) {
		
		if (appF != null && ! appF.isEmpty()) {
			this.audioFolder = appF;
		} else {
			this.audioFolder = defaultF;
		}
		
	}
	
	/**
	 * Gets the location (path) of the audio folder.
	 * 
	 * @return The location/path of the audio folder.
	 */
	public String getAudioFolder() {
		
		return this.audioFolder;
		
	}
	
	/**
	 * Sets the name of, and path of, the HTML folder.
	 * 
	 * @param defaultF Default string from the database, used if no optional
	 *                 string is specified. This is "/html".
	 *                 
	 * @param appF Optional, application specified string.
	 */
	private void setHtmlFolder(String defaultF, String appF) {
		
		if (appF != null && ! appF.isEmpty()) {
			this.htmlFolder = appF;
		} else {
			this.htmlFolder = defaultF;
		}
		
	}
	
	/**
	 * Gets the location (path) of the HTML folder.
	 * 
	 * @return The location/path of the HTML folder.
	 */
	public String getHtmlFolder() {
		
		return this.htmlFolder;
		
	}
	
	/**
	 * Sets the name of the style for the About table.
	 * 
	 * @param defaultS Default string from the database, used if no optional
	 *                 string is specified.
	 *                 
	 * @param appS Optional, application specific string.
	 */
	private void setAboutName(String defaultS, String appS) {
		
		if (appS != null && !appS.isEmpty()) {
			this.aboutName = appS;
		} else {
			this.aboutName = defaultS;
		}
		
	}
	
	/**
	 * Gets the name of the style for the About screen.
	 *
	 * @return Name of the style for the About screen.
	 */
	public String getAboutName() {
		
		return this.aboutName;
		
	}
	
	/**
	 * Sets the name of the style for the lessons table.
	 * 
	 * @param defaultS Default string from the database, used if no optional
	 *                 string is specified.
	 *                 
	 * @param appS Optional, application specific string.
	 */
	private void setLessonName(String defaultS, String appS) {
		
		if (appS != null && !appS.isEmpty()) {
			this.lessonName = appS;
		} else {
			this.lessonName = defaultS;
		}
		
	}
	
	/**
	 * Gets the name of the style for the lessons screen.
	 * 
	 * @return Name of the style for the Lessons screen.
	 */
	public String getLessonName() {
		
		return this.lessonName;
		
	}
	
	/**
	 * Sets the name of the style for the dialog table.
	 * 
	 * @param defaultS Default string from the database, used if no optional
	 *                 string is specified.
	 *                 
	 * @param appS Optional, application specific string.
	 */
	private void setDialogName(String defaultS, String appS) {
		
		if (appS != null && !appS.isEmpty()) {
			this.dialogName = appS;
		} else {
			this.dialogName = defaultS;
		}
		
	}
	
	/**
	 * Gets the name of the style for the dialog screen.
	 * 
	 * @return Name of the style for the dialog screen.
	 */
	public String getDialogName() {
		
		return this.dialogName;
		
	}
	
	/**
	 * Sets the name of the style for the grammar table.
	 * 
	 * @param defaultS Default string from the database, used if no optional
	 *                 string is specified.
	 *                 
	 * @param appS Optional, application specific string.
	 */
	private void setGrammarName(String defaultS, String appS) {
		
		if (appS != null && !appS.isEmpty()) {
			this.grammarName = appS;
		} else {
			this.grammarName = defaultS;
		}
		
	}
	
	/**
	 * Gets the name of the style for the grammar screen.
	 * 
	 * @return Name of the style for the grammar screen.
	 */
	public String getGrammarName() {
		
		return this.grammarName;
		
	}
	
	/**
	 * Sets the name of the style for the questions table.
	 * 
	 * @param defaultS Default string from the database, used if no optional
	 *                 string is specified.
	 *                 
	 * @param appS Optional, application specific string.
	 */
	private void setQuestionName(String defaultS, String appS) {
		
		if (appS != null && !appS.isEmpty()) {
			this.questionName = appS;
		} else {
			this.questionName = defaultS;
		}
		
	}
	
	/**
	 * Gets the name of the style for the questions screen.
	 * 
	 * @return Name of the style for the questions screen.
	 */
	public String getQuestionName() {
		
		return this.questionName;
		
	}
	
	/**
	 * Sets the name of the style for the pattern table.
	 * 
	 * @param defaultS Default string from the database, used if no optional
	 *                 string is specified.
	 *                 
	 * @param appS Optional, application specific string.
	 */
	private void setPatternName(String defaultS, String appS) {
		
		if (appS != null && !appS.isEmpty()) {
			this.patternName = appS;
		} else {
			this.patternName = defaultS;
		}
		
	}
	
	/**
	 * Gets the name of the style for the patterns screen.
	 * 
	 * @return Name of the style for the patterns screen.
	 */
	public String getPatternName() {
		
		return this.patternName;
		
	}
	
	/**
	 * Sets the name of the style for the vocabulary table.
	 * 
	 * @param defaultS Default string from the database, used if no optional
	 *                 string is specified.
	 *                 
	 * @param appS Optional, application specific string.
	 */
	private void setVocabularyName(String defaultS, String appS) {
		
		if (appS != null && !appS.isEmpty()) {
			this.vocabularyName = appS;
		} else {
			this.vocabularyName = defaultS;
		}
		
	}
	
	/**
	 * Gets the name of the style for the vocabulary screen.
	 * 
	 * @return Name of the style for the vocabulary screen.
	 */
	public String getVocabularyName() {
		
		return this.vocabularyName;
		
	}
	
	
	
	private void setSouthName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.southName = appS;
		} else {
			this.southName = defaultS;
		}
	}
	
	public String getSouthName() {
		return this.southName;
	}
	
	private void setNorthName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.northName = appS;
		} else {
			this.northName = defaultS;
		}
	}
	
	public String getNorthName() {
		return this.northName;
	}
	
	private void setEnglishName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.englishName = appS;
		} else {
			this.englishName = defaultS;
		}
	}
	
	public String getEnglishName() {
		return this.englishName;
	}
	
	private void setMenuCurrentUnit(String title, String subtext) {
		this.menuCurrentUnitName = title;
		this.menuCurrentUnitText = subtext;
	}
	
	public String getMenuCurrentUnitName() {
		return this.menuCurrentUnitName;
	}
	
	public String getMenuCurrentUnitText() {
		return this.menuCurrentUnitText;
	}
	
	private void setMenuRevision(String title, String subtext) {
		this.menuRevisionName = title;
		this.menuRevisionText = subtext;
	}
	
	public String getMenuRevisionName() {
		return this.menuRevisionName;
	}
	
	public String getMenuRevisionText() {
		return this.menuRevisionText;
	}
	
	private void setMenuDictionary(String title, String subtext) {
		this.menuDictionaryName = title;
		this.menuDictionaryText = subtext;
	}
	
	public String getMenuDictionaryName() {
		return this.menuDictionaryName;
	}
	
	public String getMenuDictionaryText() {
		return this.menuDictionaryText;
	}
	
	private void setMenuGrammar(String title, String subtext) {
		this.menuGrammarName = title;
		this.menuGrammarText = subtext;
	}
	
	public String getMenuGrammarName() {
		return this.menuGrammarName;
	}
	
	public String getMenuGrammarText() {
		return this.menuGrammarText;
	}
	
	private void setMenuAbout(String title, String subtext) {
		this.menuAboutName = title;
		this.menuAboutText = subtext;
	}
	
	public String getMenuAboutName() {
		return this.menuAboutName;
	}
	
	public String getMenuAboutText() {
		return this.menuAboutText;
	}
	
	private void setUnitPattern(String title, String subtext) {
		this.unitPatternName = title;
		this.unitPatternText = subtext;
	}
	
	public String getUnitPatternName() {
		return this.unitPatternName;
	}
	
	public String getUnitPatternText() {
		return this.unitPatternText;
	}
	
	private void setUnitVocab(String title, String subtext) {
		this.unitVocabName = title;
		this.unitVocabText = subtext;
	}
	
	public String getUnitVocabName() {
		return this.unitVocabName;
	}
	
	public String getUnitVocabText() {
		return this.unitVocabText;
	}
	
	private void setUnitExercises(String title, String subtext) {
		this.unitExercisesName = title;
		this.unitExercisesText = subtext;
	}
	
	public String getUnitExercisesName() {
		return this.unitExercisesName;
	}
	
	public String getUnitExercisesText() {
		return this.unitExercisesText;
	}
	
	private void setUnitGrammar(String title, String subtext) {
		this.unitGrammarName = title;
		this.unitGrammarText = subtext;
	}
	
	public String getUnitGrammarName() {
		return this.unitGrammarName;
	}
	
	public String getUnitGrammarText() {
		return this.unitGrammarText;
	}
	
	private void setUnitDialog(String title, String subtext) {
		this.unitDialogName = title;
		this.unitDialogText = subtext;
	}
	
	public String getUnitDialogName() {
		return this.unitDialogName;
	}
	
	public String getUnitDialogText() {
		return this.unitDialogText;
	}
	
}
