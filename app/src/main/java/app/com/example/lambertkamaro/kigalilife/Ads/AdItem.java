package app.com.example.lambertkamaro.kigalilife.Ads;

/**
 * Created by Lambert.Kamaro on 12/27/2015.
 */

public class AdItem {
    private String member_name;
    private int profile_pic_id;
    private String status;
    private String contactType;


    /**
     * Constructor for our class
     * @param member_name
     * @param profile_pic_id
     * @param status
     * @param contactType
     */
    public AdItem(String member_name,int profile_pic_id,String status,String contactType){
        this.member_name = member_name;
        this.profile_pic_id = profile_pic_id;
        this.status = status;
        this.contactType = contactType;
    }

    /**
     * Set member name
     * @param memberName
     */
    public  void setMemberName(String memberName){
        this.member_name = memberName;
    }

    /**
     * Get member name
     * @return string
     */
    public  String getMemberName(){
        return member_name;
    }

    /**
     * Set profile picture
     * @param profilePicId
     */
    public  void setProfilePicId(int profilePicId){
        this.profile_pic_id = profilePicId;
    }
    /**
     * Get profile picture ID
     * @return
     */
    public int getProfilePicId(){
        return profile_pic_id;
    }

    /**
     * Set status
     * @param status
     */
    public  void setStatus(String status){
        this.status = status;
    }

    /**
     * Get current status
     * @return
     */
    public  String getStatus(){
        return this.status;
    }

    /**
     * Set contact Type
     * @param contactType
     */
    public void setContactType(String contactType){
        this.contactType = contactType;
    }

    /**
     * Getting contact type
     * @return
     */
    public  String getContactType(){
        return this.contactType;
    }
}
