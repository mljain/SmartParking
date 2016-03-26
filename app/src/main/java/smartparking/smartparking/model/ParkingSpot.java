package smartparking.smartparking.model;

/**
 * Created by Parnit on 2/20/2016.
 */
public class ParkingSpot {
    private double latitude, longitude, price;
    private int quantiy;
    private String name;


    public  ParkingSpot(){

    }

    public void setLatitude(int latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLongitude(int longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return  longitude;
    }

    public int getQuantiy() {
        return quantiy;
    }

    public void setQuantiy(int quantiy) {
        this.quantiy = quantiy;
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
}
