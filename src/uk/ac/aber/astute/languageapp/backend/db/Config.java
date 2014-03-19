package uk.ac.aber.astute.languageapp.backend.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

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
	private static String [] VOCABULARY_NAME = new String[] { "VOCABULARY_NAME" };
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
					ldb.getStringWithQuery(menu,subText,args,MENU_CURRENTUNIT_NAME ) );
			instance.setMenuAbout(
					ldb.getStringWithQuery(menu,text,args,MENU_ABOUT_NAME ),
					ldb.getStringWithQuery(menu,subText,args,MENU_ABOUT_NAME ) );
			instance.setMenuDictionary(
					ldb.getStringWithQuery(menu,text,args,MENU_DICTIONARY_NAME ),
					ldb.getStringWithQuery(menu,subText,args,MENU_DICTIONARY_NAME ) );
			instance.setMenuRevision(
					ldb.getStringWithQuery(menu,text,args,MENU_REVISION_NAME ),
					ldb.getStringWithQuery(menu,subText,args,MENU_REVISION_NAME ) );
			instance.setMenuGrammar(
					ldb.getStringWithQuery(menu,text,args,MENU_GRAMMAR_NAME ),
					ldb.getStringWithQuery(menu,subText,args,MENU_GRAMMAR_NAME ) );
			instance.setUnitPattern(ldb.getStringWithQuery(menu,text,args,UNIT_PATTERN_NAME ),
					ldb.getStringWithQuery(menu,subText,args,UNIT_PATTERN_NAME ) );
			instance.setUnitVocab(ldb.getStringWithQuery(menu,text,args,UNIT_VOCAB_NAME ),
					ldb.getStringWithQuery(menu,subText,args,UNIT_VOCAB_NAME ) );
			instance.setUnitExercises(ldb.getStringWithQuery(menu,text,args,UNIT_EXERCISES_NAME ),
					ldb.getStringWithQuery(menu,subText,args,UNIT_EXERCISES_NAME ) );
			instance.setUnitGrammar(ldb.getStringWithQuery(menu,text,args,UNIT_GRAMMAR_NAME ),
					ldb.getStringWithQuery(menu,subText,args,UNIT_GRAMMAR_NAME ) );
			instance.setUnitDialog(ldb.getStringWithQuery(menu,text,args,UNIT_DIALOG_NAME ),
					ldb.getStringWithQuery(menu,subText,args,UNIT_DIALOG_NAME ) );
			 
		} catch (DataNotFoundException dnfe) {
			
			Log.e("LANG_APP", dnfe.toString());
			instance = null;
			
		}
		
		return instance;
	}
	
	private Config() { }
	
	private void setProjectName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.projectName = appS;
		} else {
			this.projectName = defaultS;
		}
	}
	
	public String getProjectName() {
		return this.projectName;
	}
	
	private void setImageFolder(String defaultF, String appF) {
		if (appF != null && !appF.isEmpty()) {
			this.imageFolder = appF;
		} else {
			this.imageFolder = defaultF;
		}
	}
	
	public String getImageFolder() {
		return this.imageFolder;
	}
	
	private void setAudioFolder(String defaultF, String appF) {
		if (appF != null && ! appF.isEmpty()) {
			this.audioFolder = appF;
		} else {
			this.audioFolder = defaultF;
		}
	}
	
	public String getAudioFolder() {
		return this.audioFolder;
	}
	
	private void setHtmlFolder(String defaultF, String appF) {
		if (appF != null && ! appF.isEmpty()) {
			this.htmlFolder = appF;
		} else {
			this.htmlFolder = defaultF;
		}
	}
	
	public String getHtmlFolder() {
		return this.htmlFolder;
	}
	
	private void setAboutName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.aboutName = appS;
		} else {
			this.aboutName = defaultS;
		}
	}
	
	public String getAboutName() {
		return this.aboutName;
	}
	
	private void setLessonName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.lessonName = appS;
		} else {
			this.lessonName = defaultS;
		}
	}
	
	public String getLessonName() {
		return this.lessonName;
	}
	
	private void setDialogName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.dialogName = appS;
		} else {
			this.dialogName = defaultS;
		}
	}
	
	public String getDialogName() {
		return this.dialogName;
	}
	
	private void setGrammarName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.grammarName = appS;
		} else {
			this.grammarName = defaultS;
		}
	}
	
	public String getGrammarName() {
		return this.grammarName;
	}
	
	private void setQuestionName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.questionName = appS;
		} else {
			this.questionName = defaultS;
		}
	}
	
	public String getQuestionName() {
		return this.questionName;
	}
	
	private void setPatternName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.patternName = appS;
		} else {
			this.patternName = defaultS;
		}
	}
	
	public String getPatternName() {
		return this.patternName;
	}
	
	private void setVocabularyName(String defaultS, String appS) {
		if (appS != null && !appS.isEmpty()) {
			this.vocabularyName = appS;
		} else {
			this.vocabularyName = defaultS;
		}
	}
	
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
