package domain.entity;

import org.json.simple.JSONObject;

public class Location {
    private double x;
    private double y;

    public Location(JSONObject locationInfo){
        this.x = Double.parseDouble(locationInfo.get("x").toString());
        this.y = Double.parseDouble(locationInfo.get("y").toString()) ;
    }

    public Location(double x , double y){
        setX(x);
        setY(y);
    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }


    public String toString(){
        return "(" + Double.toString(this.x) + "," + Double.toString(this.y) +")";
    }


}
