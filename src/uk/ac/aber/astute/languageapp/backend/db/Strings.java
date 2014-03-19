package uk.ac.aber.astute.languageapp.backend.db;

import uk.ac.aber.astute.languageapp.backend.Tracker;

public class Strings {

	
	public static String OUT_OF_STRING = "Got %d out of %n right";
	public static String WERE_YOU_CORRECT = "Were you\ncorrect?";
	public static String START_PRACTICING = "Start practising...";
	public static String WHAT_IS_THE_WELSH_FOR = "What is the Welsh for...?";
	public static String SELECT_BETWEEN_STRING = "Select between North and South Welsh";
	public static String CURRENT_STRING = "You are currently on Unit U1. You can\nselect another unit to study below:";
	public static String GLOBAL_GRAMMAR_STYLE = "GlobalGrammar";
	public static String HOME_SCREEN_STYLE = "MainMenu";
	public static String GLOBAL_DICTIONARY_STYLE = "GlobalVocab";
	public static String QUESTION_STYLE = "QuestionHeader";
	public static String PATTERN_STYLE = "PatternHeader";
	public static String ABOUT_STYLE = "About";
	public static String VOCAB_STYLE = "UnitVocab";
	public static String PATTERNS_STYLE = "PatternHeader";
	public static String GRAMMARS_STYLE = "UnitGrammar";
	public static String QUESTIONS_STYLE = "QuestionHeader";
	public static String UNIT_SCREEN_STYLE = "UnitChoice";
	public static String DIALOG_STYLE = "DialogHeader";
	public static String DIALOG_BODY_STYLE = "DialogBody";
	public static String REVISION_STYLE = "";
	
//	public static String PATTERNS_MENU_ITEM_TITLE = "Pattern %n:";
	public static String GLOBAL_GRAMMAR_UNIT_TITLE = "Unit %n";
//	public static String QUESTIONS_MENU_ITEM_TITLE = "Exercise %n:";
	
	
//	public static String UNIT_SCREEN_PATTERN_TITLE = "Patterns";
//	public static String UNIT_SCREEN_PATTERN_DESCR = "Learn the patterns";
//	public static String UNIT_SCREEN_VOCAB_TITLE = "Vocabulary";
//	public static String UNIT_SCREEN_VOCAB_DESCR = "Practice the vocabulary";
//	public static String UNIT_SCREEN_EXERCISES_TITLE = "Exercises";
//	public static String UNIT_SCREEN_EXERCISES_DESCR = "Try the exercises";
//	public static String UNIT_SCREEN_GRAMMAR_TITLE = "Grammar";
//	public static String UNIT_SCREEN_GRAMMAR_DESCR = "Review the grammar";
//	public static String UNIT_SCREEN_DIALOGS_TITLE = "Dialogs";
//	public static String UNIT_SCREEN_DIALOGS_DESCR = "Practice with longer pieces";
	
	public static String fillString(String str) {
		
		Tracker t = Tracker.getInstance();
		
		String tmp = str.replaceAll("U1", "" + t.getCurrentUnit());
		tmp = tmp.replaceAll("%g", "" + t.getCurrentGroupId());
		
		return tmp;
		
	}
	
	public static String extraFillString(String str, int number) {
		return Strings.fillString(str.replaceAll("%n", ""+number));
	}
	
	public static String doScoreString(String str, int score, int total) {
		return str.replace("%d", ""+score).replace("%n", ""+total);
	}
	
}
