package app.com.example.lambertkamaro.kigalilife.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT + " DATETIME,"
            + KEY_OWNER + " VARCHAR(255)," + KEY_MESSAGE_ID + " VARCHAR(255),"
            + KEY_MAIL_DATE + " VARCHAR(255)," + KEY_FILES + " TEXT )";

    // My Ads table create statement
    private static final String CREATE_TABLE_MYADS = "CREATE TABLE "
            + TABLE_MYADS +
            "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_SUBJECT + " TEXT," + KEY_BODY + " TEXT,"
            + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT + " DATETIME,"
            + KEY_OWNER + " VARCHAR(255)," + KEY_MESSAGE_ID + " VARCHAR(255),"
            + KEY_MAIL_DATE + " VARCHAR(255)," + KEY_FILES + " TEXT,"
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

    /** ------------------------ "Ads" table methods ----------------**/
    /*
    * Creating a Ads
    */
    public long createAd(AdModel ad) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT,ad.getSubject());
        values.put(KEY_BODY,ad.getBody());
        values.put(KEY_FILES,ad.getFiles());
        values.put(KEY_OWNER,ad.getOwner());
        values.put(KEY_MESSAGE_ID, ad.getMessage_id());
        values.put(KEY_MAIL_DATE, ad.getMail_date());
        values.put(KEY_CREATED_AT,getDateTime());
        values.put(KEY_UPDATED_AT,getDateTime());

        // insert row
        long ad_id = db.insert(TABLE_ADS, null, values);

        // If the ads are more than 50 and this was inserted then delete the ordest one
        if (this.getAdsCount() > 50){
            String toBeDeleteQuest = "SELECT "+KEY_ID+" FROM "+TABLE_ADS
                                     + " ORDER BY "+KEY_CREATED_AT +" ASC LIMIT 50";

            db = this.getReadableDatabase();

            Cursor results = db.rawQuery(toBeDeleteQuest,null);

            // looping through all rows and adding to list
            if (results.moveToFirst()) {
                do {
                    this.deleteAd(results.getInt(results.getColumnIndex(KEY_ID)));
                } while (results.moveToNext());
            }
        }

        return ad_id;
    }

    /** Find single AD **/
    public AdModel getAd(long ad_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_ADS +" WHERE "+KEY_ID+" = "+ad_id;

        Cursor results = db.rawQuery(query,null);

        // if we found something then shift cursor
        if (results!=null){
            results.moveToFirst();
        }
            // Add information to the model
        AdModel ad = new AdModel();
        ad.setId(results.getInt(results.getColumnIndex(KEY_ID)));
        ad.setSubject(results.getString(results.getColumnIndex(KEY_SUBJECT)));
        ad.setBody(results.getString(results.getColumnIndex(KEY_BODY)));
        ad.setMail_date(results.getString(results.getColumnIndex(KEY_MAIL_DATE)));
        ad.setMessage_id(results.getString(results.getColumnIndex(KEY_MESSAGE_ID)));
        ad.setOwner(results.getString(results.getColumnIndex(KEY_OWNER)));
        ad.setFiles(results.getString(results.getColumnIndex(KEY_FILES)));
        ad.setCreated_at(results.getString(results.getColumnIndex(KEY_CREATED_AT)));
        ad.setCreated_at(results.getString(results.getColumnIndex(KEY_UPDATED_AT)));

        return ad;
    }

    /** Get all ads **/
    public List<AdModel> getAllAds(){
        List<AdModel> ads = new ArrayList<AdModel>();
        String query = "SELECT * FROM "+TABLE_ADS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor results = db.rawQuery(query,null);

        // looping through all rows and adding to list
        if (results.moveToFirst()) {
            do {
                // Add information to the model
                AdModel ad = new AdModel();
                ad.setId(results.getInt(results.getColumnIndex(KEY_ID)));
                ad.setSubject(results.getString(results.getColumnIndex(KEY_SUBJECT)));
                ad.setBody(results.getString(results.getColumnIndex(KEY_BODY)));
                ad.setMail_date(results.getString(results.getColumnIndex(KEY_MAIL_DATE)));
                ad.setMessage_id(results.getString(results.getColumnIndex(KEY_MESSAGE_ID)));
                ad.setOwner(results.getString(results.getColumnIndex(KEY_OWNER)));
                ad.setFiles(results.getString(results.getColumnIndex(KEY_FILES)));
                ad.setCreated_at(results.getString(results.getColumnIndex(KEY_CREATED_AT)));
                ad.setCreated_at(results.getString(results.getColumnIndex(KEY_UPDATED_AT)));

                // adding to ads list
                ads.add(ad);
            } while (results.moveToNext());
        }

        return ads;
    }
    /** Get all ads **/
    public List<AdModel> searchAds(String keyword){
        List<AdModel> ads = new ArrayList<AdModel>();
        String query = "SELECT * FROM "+TABLE_ADS + " WHERE "+KEY_SUBJECT+" LIKE '%"+keyword
                        + "%' OR "+KEY_BODY +" LIKE '%"+keyword+ "%'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor results = db.rawQuery(query,null);

        // looping through all rows and adding to list
        if (results.moveToFirst()) {
            do {
                // Add information to the model
                AdModel ad = new AdModel();
                ad.setId(results.getInt(results.getColumnIndex(KEY_ID)));
                ad.setSubject(results.getString(results.getColumnIndex(KEY_SUBJECT)));
                ad.setBody(results.getString(results.getColumnIndex(KEY_BODY)));
                ad.setMail_date(results.getString(results.getColumnIndex(KEY_MAIL_DATE)));
                ad.setMessage_id(results.getString(results.getColumnIndex(KEY_MESSAGE_ID)));
                ad.setOwner(results.getString(results.getColumnIndex(KEY_OWNER)));
                ad.setFiles(results.getString(results.getColumnIndex(KEY_FILES)));
                ad.setCreated_at(results.getString(results.getColumnIndex(KEY_CREATED_AT)));
                ad.setCreated_at(results.getString(results.getColumnIndex(KEY_UPDATED_AT)));

                // adding to todo list
                ads.add(ad);
            } while (results.moveToNext());
        }

        return ads;
    }

    /** getting ads count */

    public  int getAdsCount(){
        String query = "SELECT COUNT(1) FROM "+TABLE_ADS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor results = db.rawQuery(query, null);

        int counts = results.getCount();
        results.close();

        return counts;
    }
    /**  Updating a Ads     */
    public int updateAds(AdModel ad) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT,ad.getSubject());
        values.put(KEY_BODY,ad.getBody());
        values.put(KEY_FILES,ad.getFiles());
        values.put(KEY_OWNER,ad.getOwner());
        values.put(KEY_MESSAGE_ID, ad.getMessage_id());
        values.put(KEY_MAIL_DATE, ad.getMail_date());
        values.put(KEY_CREATED_AT,getDateTime());
        values.put(KEY_UPDATED_AT,getDateTime());

        // updating row
        return db.update(TABLE_ADS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(ad.getId()) });
    }

    /**
     * Deleting a Ad
     */
    public void deleteAd(long ad_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADS, KEY_ID + " = ?",
                new String[]{String.valueOf(ad_id)});
    }


    /** ------------------------ "MyAds" table methods ----------------**/
    /*
     * Creating a My Ads
     */
    public long createMyAd(MyAdsModel myAd, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT, myAd.getSubject());
        values.put(KEY_BODY, myAd.getBody());
        values.put(KEY_FILES, myAd.getFiles());
        values.put(KEY_OWNER, myAd.getOwner());
        values.put(KEY_MESSAGE_ID, myAd.getMessage_id());
        values.put(KEY_MAIL_DATE, myAd.getMail_date());
        values.put(KEY_IS_SENT, myAd.getIs_sent());
        values.put(KEY_IS_REOCCURING, myAd.getIs_reoccuring());
        values.put(KEY_CREATED_AT,getDateTime());
        values.put(KEY_UPDATED_AT,getDateTime());


        // insert row
        long myad_id = db.insert(TABLE_MYADS, null, values);

        return myad_id;
    }

    /** Find single MyAds **/
    public MyAdsModel getMyAd(long ad_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_MYADS +" WHERE "+KEY_ID+" = "+ad_id;

        Cursor results = db.rawQuery(query, null);

        // if we found something then shift cursor
        if (results!=null){
            results.moveToFirst();
        }
        // Add information to the model
        MyAdsModel ad = new MyAdsModel();
        ad.setId(results.getInt(results.getColumnIndex(KEY_ID)));
        ad.setSubject(results.getString(results.getColumnIndex(KEY_SUBJECT)));
        ad.setBody(results.getString(results.getColumnIndex(KEY_BODY)));
        ad.setMail_date(results.getString(results.getColumnIndex(KEY_MAIL_DATE)));
        ad.setMessage_id(results.getString(results.getColumnIndex(KEY_MESSAGE_ID)));
        ad.setOwner(results.getString(results.getColumnIndex(KEY_OWNER)));
        ad.setFiles(results.getString(results.getColumnIndex(KEY_FILES)));
        ad.setIs_sent(results.getInt(results.getColumnIndex(KEY_IS_SENT)));
        ad.setIs_reoccuring(results.getInt(results.getColumnIndex(KEY_IS_REOCCURING)));
        ad.setCreated_at(results.getString(results.getColumnIndex(KEY_CREATED_AT)));
        ad.setCreated_at(results.getString(results.getColumnIndex(KEY_UPDATED_AT)));

        return ad;
    }

    /** Get all myads **/
    public List<MyAdsModel> getAllmyAds(){
        List<MyAdsModel> myads = new ArrayList<MyAdsModel>();
        String query = "SELECT * FROM "+TABLE_MYADS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor results = db.rawQuery(query,null);

        // looping through all rows and adding to list
        if (results.moveToFirst()) {
            do {
                // Add information to the model
                MyAdsModel ad = new MyAdsModel();
                ad.setId(results.getInt(results.getColumnIndex(KEY_ID)));
                ad.setSubject(results.getString(results.getColumnIndex(KEY_SUBJECT)));
                ad.setBody(results.getString(results.getColumnIndex(KEY_BODY)));
                ad.setMail_date(results.getString(results.getColumnIndex(KEY_MAIL_DATE)));
                ad.setMessage_id(results.getString(results.getColumnIndex(KEY_MESSAGE_ID)));
                ad.setOwner(results.getString(results.getColumnIndex(KEY_OWNER)));
                ad.setFiles(results.getString(results.getColumnIndex(KEY_FILES)));
                ad.setIs_sent(results.getInt(results.getColumnIndex(KEY_IS_SENT)));
                ad.setIs_reoccuring(results.getInt(results.getColumnIndex(KEY_IS_REOCCURING)));
                ad.setCreated_at(results.getString(results.getColumnIndex(KEY_CREATED_AT)));
                ad.setCreated_at(results.getString(results.getColumnIndex(KEY_UPDATED_AT)));

                // adding to todo list
                myads.add(ad);
            } while (results.moveToNext());
        }

        return myads;
    }

    /** getting myads count */

    public  int getMyAdsCount(){
        String query = "SELECT COUNT(1) FROM "+TABLE_MYADS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor results = db.rawQuery(query, null);

        int counts = results.getCount();
        results.close();

        return counts;
    }
    /**  Updating a myAds     */
    public int updateMyAds(MyAdsModel myAd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT, myAd.getSubject());
        values.put(KEY_BODY, myAd.getBody());
        values.put(KEY_FILES, myAd.getFiles());
        values.put(KEY_OWNER, myAd.getOwner());
        values.put(KEY_MESSAGE_ID, myAd.getMessage_id());
        values.put(KEY_MAIL_DATE, myAd.getMail_date());
        values.put(KEY_IS_SENT, myAd.getIs_sent());
        values.put(KEY_IS_REOCCURING, myAd.getIs_reoccuring());
        values.put(KEY_CREATED_AT,getDateTime());
        values.put(KEY_UPDATED_AT,getDateTime());

        // updating row
        return db.update(TABLE_MYADS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(myAd.getId()) });
    }

    /**
     * Deleting a MyAd
     */
    public void deleteMyAd(long ad_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MYADS, KEY_ID + " = ?",
                new String[]{String.valueOf(ad_id)});
    }


    /** closing database **/
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    /**     * get datetime     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}