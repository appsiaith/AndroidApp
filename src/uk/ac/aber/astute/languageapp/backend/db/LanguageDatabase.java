package uk.ac.aber.astute.languageapp.backend.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class is used for connecting to, and retrieving data from the 
 * language database. It is used as follows:
 * 
 * <code>
 * LanguageDatabase db;
 * db = LanguageDatabase.getInstance(context);
 * </code>
 * 
 * You should never, nor are you allowed to, create an instance of the 
 * LanguageDatabase. All requests for the database should go through a single 
 * instance, created via getInstance(context).
 * 
 * @author Michael Clarke <mfc1@aber.ac.uk>
 * @version 0.3
 */
@SuppressLint("SdCardPath")
public class LanguageDatabase extends SQLiteOpenHelper {
	
	private static LanguageDatabase instance = null;
	private static String DATABASE_PATH;
	private SQLiteDatabase db = null;
	private Context context;
	
	private static final String DBNAME = "langDB.sqlite3";
	private static final int DBVERSION = 1;
	
	/**
	 * Gets or creates an instance of the LanguageDatabase. 
	 * 	
	 * @param context The context we're working from, this is important as it 
	 * 				  is required to get access to various resource files that 
	 *                hold data such as the name and version of the SQLite
	 *                database.
	 *                
	 * @return The instance of the LanguageDatabase.
	 */
	public static LanguageDatabase getInstance(Context context) {
		
		if (instance == null) {
			instance = new LanguageDatabase(context);
		}
		
		return instance;
		
	}
	
	/**
	 * This method is responsible for checking if the database exists and, 
	 * if not, copying it from the assets folder, and then opening a connection 
	 * to the database.
	 * 
	 * @param context The context the instance is working from. 
	 */
	private LanguageDatabase(Context context) {
		
		super(context, DBNAME, null, DBVERSION); 
		
		/* This is irritating, but required otherwise 
		 * we're storing data in the wrong place! 
		 */
		if(android.os.Build.VERSION.SDK_INT >= 17)
			DATABASE_PATH = context.getApplicationInfo().dataDir 
														+ "/databases/";         
		else
			DATABASE_PATH = "/data/data/" + 
								context.getPackageName() + "/databases/";
		
		this.context = context;
		

			this.getReadableDatabase();
			this.close();
			try {
				this.copyDatabase();
				Log.i("LANG_APP", "Created database from asset folder.");
			} catch (Exception e) {
				Log.e("LANG_APP", "Error creating database from " +
										"asset folder. " + e.toString());
			}
		
		try {
		
			this.db = SQLiteDatabase.openDatabase(DATABASE_PATH + DBNAME, 
										null, SQLiteDatabase.OPEN_READONLY);
			
			Log.i("LANG_APP", "Database opened.");
			
		} catch (Exception e) {
			
			Log.e("DATABASE", "Error opening database. " + e.toString());
			
		}
		
		

	}
	
	public Context getContext() {
		return this.context;
	}
	
	/**
	 * Unfortunately we can't just open the database from the asset folder, 
	 * we have to copy it to the appropriate "database" location. Annoying? 
	 * Yes. So, if our database doesn't exist we need to copy it to the asset 
	 * folder. This method is used to do that.
	 * 
	 * @throws IOException If there are problems reading or writing from the 
	 * 					   database file in the asset folder or the new 
	 * 					   database file in the databases directory.
	 */
	private void copyDatabase() throws IOException {
		
		InputStream is = context.getAssets().open(DBNAME);
		
		OutputStream os = new FileOutputStream(DATABASE_PATH + DBNAME);
		
		byte[] buffer = new byte[1024];
		int length;
		
		while ((length = is.read(buffer))> 0 ) {
			os.write(buffer, 0, length);
		}
		
		os.flush();
		os.close();
		is.close();
		
	}
	
	/**
	 * This method is used to get a string from the SQLite database.
	 * 
	 * @param tableName The table in the database that holds the 
	 *                  string we're after.
	 * @param columnName The column name we're after.
	 * @param where Any WHERE clauses, i.e. "id=? AND name=?, etc."
	 * @param whereArgs The arguments that match the WHERE clause, 
	 *                  in a String array.
	 * 
	 * @return The found string, if it is in the database.
	 * 
	 * @throws DataNotFoundException if the data if not in the database, 
	 *                               or if an error occurs.
	 */
	public String getStringWithQuery(String tableName, String columnName, 
			  String where, String whereArgs[]) throws DataNotFoundException {
		
		String result = null;
		Cursor cursor = this.db.query(tableName, new String[] { columnName },
											where, whereArgs, null, null, null);
		
		if (cursor != null) {
			if (cursor.moveToNext()) {
				int index = cursor.getColumnIndexOrThrow(columnName);
				if (index != -1) {
					result = cursor.getString(index);
				}
			}
			cursor.close();
		}
		
		if (result == null) throw new DataNotFoundException(
				tableName, columnName, where, whereArgs);
		
		return result;
		
	}
	
	/**
	 * This method is used to get a single character from the SQLite database. 
	 * It can also be used to get just the first character of a string entry in 
	 * the database.
	 * 
	 * @param tableName The table in the database that holds the character 
	 *                  we're after.
	 * @param columnName The column name we're after.
	 * @param where Any WHERE clauses, i.e. "id=? AND name=?, etc."
	 * @param whereArgs The arguments that match the WHERE clause, 
	 *                  in a String array.
	 * 
	 * @return The found character, if it is in the database.
	 * 
	 * @throws DataNotFoundException if the data if not in the database,
	 *                               or if an error occurs.
	 */
	public char getCharWithQuery(String tableName, String columnName,
			 String where, String whereArgs[]) throws DataNotFoundException {

		return this.getStringWithQuery(tableName, columnName, 
										where, whereArgs).toCharArray()[0];

	}
	
	/**
	 * This method is used to get an integer from the SQLite database.
	 * 
	 * @param tableName The table in the database that holds the integer 
	 *                  value we're after.
	 * @param columnName The column name we're after.
	 * @param where Any WHERE clauses, i.e. "id=? AND name=?, etc."
	 * @param whereArgs The arguments that match the WHERE clause, in a 
	 * 					String array.
	 * 
	 * @return The found integer, if it is in the database.
	 * 
	 * @throws DataNotFoundException if the data if not in the database, 
	 *                               or if an error occurs.
	 */
	public int getIntegerWithQuery(String tableName, String columnName,
								   			String where, String whereArgs[]) 
								   			  	 throws DataNotFoundException {
		
		Log.e("Info:", "tableName: " + tableName + ", columnName: " + columnName + ", where: " + where);
		for (int i = 0; i < whereArgs.length; i++) {
			Log.e("Info:", whereArgs[i]);
		}
		
		Integer result = null;

		Cursor cursor = this.db.query(tableName,  new String[] {columnName}, 
											where, whereArgs, null, null, null);
		if (cursor != null) {
			
			
			if (cursor.moveToNext()) {
				int index = cursor.getColumnIndexOrThrow(columnName);
				if (index != -1) {
					result = Integer.valueOf(cursor.getInt(index));
				}
			}
			
			cursor.close();
		}
		

				
		if (result == null) throw new DataNotFoundException(
				tableName, columnName, where, whereArgs);
		
		return (int)result;
		
	}
	
	/**
	 * This method is used to the a count of the number of items matching the
	 * requested query string. 
	 * 
	 * @param tableName The table name in the database to query.
	 * @param columnName The column name in the table that holds the data 
	 *                   we want.
	 * @param where Any specific constraints, i.e. "id=?", otherwise null. 
	 * @param whereArgs The argument strings that match the "where" param,
	 *                  otherwise null.
	 *                  
	 * @return An integer with the number of items that matched the search
	 *         criteria. 
	 * 
	 * @throws DataNotFoundException If no data is found, we throw this.
	 */
	public int getCountWithQuery(String tableName, String columnName,
										String where, String whereArgs[]) 
												throws DataNotFoundException {
		
		Cursor cursor = this.db.query(tableName,  new String[] {columnName}, 
											where, whereArgs, null, null, null);

		
		if (cursor == null) throw new DataNotFoundException(
				tableName, columnName, where, whereArgs);
		
		int count = cursor.getCount();
		cursor.close();
		
		return count;
		
	}
	
	/**
	 * This method is used to get a whole array of integer values from the 
	 * database. It is generally used to get IDs for other queries.
	 *  
	 * @param tableName The table name in the database to query.
	 * @param columnName The column name in the table that holds the data 
	 *                   we want.
	 * @param where Any specific constraints, i.e. "id=?", otherwise null.
	 * @param whereArgs The argument strings that match the "where" param, 
	 *                  otherwise null.
	 * 
	 * @return An array list of integers that match the requested criteria. 
	 * 
	 * @throws DataNotFoundException If no data is found, we throw this.
	 */
	public ArrayList<Integer> getIntegerArrayWithQuery(String tableName, 
						String columnName, String where, String whereArgs[]) 
												throws DataNotFoundException {
		
		ArrayList<Integer> result = null;
		
		Cursor cursor = this.db.query(tableName, new String[] {columnName}, 
											where, whereArgs, null, null, null);
		
		if (cursor != null) {
			
			result = new ArrayList<Integer>();
			
			while (cursor.moveToNext()) {
				int index = cursor.getColumnIndexOrThrow(columnName);
				if (index != -1) {
					result.add(Integer.valueOf(cursor.getInt(index)));
				}
			}
			
			cursor.close();
			
		}
	
		if (result == null) throw new DataNotFoundException(
									tableName, columnName, where, whereArgs);
		
		return result;
		
	}
	
	public ArrayList<Integer> getIntegerArrayWithQueryOrdered(String tableName,
					String columnName, String where, String whereArgs[],
						String orderBy) throws DataNotFoundException {
		
		ArrayList<Integer> result = null;
		
		Cursor cursor = this.db.query(tableName, new String[] {columnName}, 
						where, whereArgs, null, null, "" +orderBy+"");
			
		if (cursor != null) {
			
			result = new ArrayList<Integer>();
			while (cursor.moveToNext()) {
				int index = cursor.getColumnIndexOrThrow(columnName);
				if (index != -1) {
					result.add(Integer.valueOf(cursor.getInt(index)));
				}
			}
			
			cursor.close();
			
		}

		return result;
		
	}
	
	/**
	 * We never wish to do this. This is only here because it has to be. 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	 /**
	  * We never wish to do this. This is only here because it has to be. 
	  */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
}