package smartparking.smartparking.model;

import java.io.Serializable;

/**
 * Created by Parnit on 2/20/2016.
 */
public class User  implements Serializable{
    private int id;
    private String firstName, lastName, email;
    private boolean hasParking;
    private ParkingSpot parkingSpot;

    public User(){

    }

    public User(int id, String firstName, String lastName, String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        hasParking = false;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
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

    private boolean hasParking(){ return hasParking; }

    private void setParkingSpot(ParkingSpot spot){
        parkingSpot = spot;
        hasParking = true;
    }

    public ParkingSpot getParkingSpot() {
       return  parkingSpot ;
    }

    public void releaseParking(){
        parkingSpot = null;
        hasParking = false;
    }

}
