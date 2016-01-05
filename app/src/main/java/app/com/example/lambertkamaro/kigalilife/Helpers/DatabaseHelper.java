package app.com.example.lambertkamaro.kigalilife.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.Models.MyAdsModel;

/**
 * Created by Lambert.Kamaro on 1/5/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "kigaliLife";

    // Table Names
    private static final String TABLE_ADS = "ads";
    private static final String TABLE_MYADS = "my_ads";

    // Common column names
    private static final String KEY_ID = "id";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_BODY = "body";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_MESSAGE_ID = "message_id";
    public static final String KEY_MAIL_DATE = "mail_date";
    public static final String KEY_FILES = "files";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    // MY ads Table - column names
    private static final String KEY_IS_SENT = "is_sent";
    private static final String KEY_IS_REOCCURING = "is_reoccuring";

    // Table Create Statements
    // Ads table create statement
    private static final String CREATE_TABLE_ADS = "CREATE TABLE "
            + TABLE_ADS +
            "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_SUBJECT + " TEXT," + KEY_BODY + " TEXT,"
            + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT + " DATETIME"
            + KEY_OWNER + " VARCHAR(255)," + KEY_MESSAGE_ID + " VARCHAR(255)"
            + KEY_MAIL_DATE + " VARCHAR(255)," + KEY_FILES + " TEXT )";

    // My Ads table create statement
    private static final String CREATE_TABLE_MYADS = "CREATE TABLE "
            + TABLE_MYADS +
            "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_SUBJECT + " TEXT," + KEY_BODY + " TEXT,"
            + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT + " DATETIME"
            + KEY_OWNER + " VARCHAR(255)," + KEY_MESSAGE_ID + " VARCHAR(255)"
            + KEY_MAIL_DATE + " VARCHAR(255)," + KEY_FILES + " TEXT "
            + KEY_IS_SENT + " INT(1)," + KEY_IS_REOCCURING + " INT(1) )";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ADS);
        db.execSQL(CREATE_TABLE_MYADS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYADS);

        // create new tables
        onCreate(db);
    }

    /** SECTION FOR THE TABLE OPERATIONS **/

    /*
    * Creating a Ads
    */
    public long createToDo(AdModel ad, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT,ad.getSubject());
        values.put(KEY_BODY,ad.getBody());
        values.put(KEY_FILES,ad.getFiles());
        values.put(KEY_OWNER,ad.getOwner());
        values.put(KEY_MESSAGE_ID, ad.getMessage_id());
        values.put(KEY_MAIL_DATE, ad.getMail_date());


        // insert row
        long ad_id = db.insert(TABLE_ADS, null, values);

        return ad_id;
    }

    /*
   * Creating a My Ads
   */
    public long createToDo(MyAdsModel myAd, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT,myAd.getSubject());
        values.put(KEY_BODY,myAd.getBody());
        values.put(KEY_FILES,myAd.getFiles());
        values.put(KEY_OWNER,myAd.getOwner());
        values.put(KEY_MESSAGE_ID,myAd.getMessage_id());
        values.put(KEY_MAIL_DATE,myAd.getMail_date());
        values.put(KEY_IS_SENT,myAd.getIs_sent());
        values.put(KEY_IS_REOCCURING,myAd.getIs_reoccuring());


        // insert row
        long myad_id = db.insert(TABLE_MYADS, null, values);

        return myad_id;
    }
}