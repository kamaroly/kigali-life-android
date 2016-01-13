package app.com.example.lambertkamaro.kigalilife.Models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Lambert.Kamaro on 1/5/2016.
 */
public class AdModel implements Parcelable {

    int id;
    String subject;
    String body;
    String owner;
    String message_id;
    String mail_date;
    String files;
    String created_at;
    String updated_at;
    private int mData;
    // Constructors
    public  AdModel(){

    }

    public AdModel(int id,String subject,String body,String owner,String message_id,String mail_date){
        this.id = id;
        this.subject = subject;
        this.body    = body;
        this.owner   = owner;
        this.message_id = message_id;
        this.mail_date  = mail_date;

        // Getting current time
        java.util.Date date= new java.util.Date();
        String time = (new java.sql.Timestamp(date.getTime())).toString();

        this.created_at = time;
        this.updated_at = time ;

    }

    public  AdModel(String subject,String body,String owner,String message_id,String mail_date){
        this.subject = subject;
        this.body    = body;
        this.owner   = owner;
        this.message_id = message_id;
        this.mail_date  = mail_date;

        // Getting current time
        java.util.Date date= new java.util.Date();
        String time = (new java.sql.Timestamp(date.getTime())).toString();

        this.created_at = time;
        this.updated_at = time ;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public void setMail_date(String mail_date) {
        this.mail_date = mail_date;
    }
    public void setFiles(String files) {
        this.files = files;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getOwner() {
        return owner;
    }

    public String getMessage_id() {
        return message_id;
    }

    public String getMail_date() {
        return mail_date;
    }

    public String getFiles() {
        return files;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }


    /** SECTION FOR THE Parcelable METHODS IMPLEMENATIONS **/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mData);
    }

    public static final Parcelable.Creator<AdModel> CREATOR
            = new Parcelable.Creator<AdModel>() {
        public AdModel createFromParcel(Parcel in) {
            return new AdModel(in);
        }

        public AdModel[] newArray(int size) {
            return new AdModel[size];
        }
    };

    /** recreate object from parcel */
    private AdModel(Parcel in) {
        mData = in.readInt();
    }

    /** END OF SECTION FOR THE Parcelable METHODS IMPLEMENATIONS **/
}
