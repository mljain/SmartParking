package smartparking.smartparking.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Parnit on 2/20/2016.
 */
public class ParkingSpot implements Serializable {
    private double latitude, longitude, price;
    private int quantity;
    private String name, imageUrl, priceDesc, id;
    private boolean isBooked;

    private Date reserveStartTime;


    public  ParkingSpot(){

    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return  longitude;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceDesc() {
        return priceDesc;
    }

    public void setPriceDesc(String priceDesc) {
        this.priceDesc = priceDesc;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setBooked(){
        isBooked = true;
        reserveStartTime = new Date();
    }

    public boolean isBooked(){
        return isBooked;
    }

    public void releaseBooking(){
        isBooked = false;
        reserveStartTime = null;
    }

    public Date getReserveStartTime(){
        return reserveStartTime;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
