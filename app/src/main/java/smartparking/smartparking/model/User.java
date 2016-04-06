package smartparking.smartparking.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Parnit on 2/20/2016.
 */
public class User  implements Serializable{
    private String id;



    private String parkingID;
    private String firstName, lastName, email;
    private boolean hasParking;
    private ParkingSpot parkingSpot;
    private Date reservationDate;

    public User(){

    }

    public User(String id, String firstName, String lastName, String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        hasParking = false;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setFirstName(String firstname){
        this.firstName = firstname;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public boolean hasParking(){ return hasParking; }

    public void setParkingSpot(ParkingSpot spot){
        parkingSpot = spot;
        hasParking = true;
        reservationDate = new Date();
    }

    public void setHasParking(boolean status){
        hasParking = status;
    }

    public ParkingSpot getParkingSpot() {
       return  parkingSpot ;
    }

    public void releaseParking(){
        parkingSpot = null;
        hasParking = false;
    }

    public String getParkingID() {
        return parkingID;
    }

    public void setParkingID(String parkingID) {
        this.parkingID = parkingID;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

}
