package domain.entity;


import org.json.simple.JSONObject;

public class Delivery {

    private String id;
    private Double velocity;
    private Location location;


    public Delivery(JSONObject deliveryInfo){
        this.id = deliveryInfo.get("id").toString();
        this.location = new Location((JSONObject)deliveryInfo.get("location"));
        this.velocity = Double.parseDouble(deliveryInfo.get("velocity").toString());
    }

    public String getId(){
        return this.id;
    }

    public Double getVelocity(){
        return this.velocity;
    }

    public Location getLocation(){
        return this.location;
    }

    public String toString(){
        return "Delivery: id=> " + id + " with velocity: " + Double.toString(velocity) +" at locaton: " + location;
    }


    public double distanceFromRestaurant(Restaurant restaurant){
        Location restaurantLocation = restaurant.getLocation();
        double x_restaurant = restaurantLocation.getX();
        double y_restaurant = restaurantLocation.getY();
        return Math.sqrt(((x_restaurant-this.location.getX())* (x_restaurant-this.location.getX()))
         + ((y_restaurant - this.location.getY()) * (y_restaurant - this.location.getY())));
    }

}





