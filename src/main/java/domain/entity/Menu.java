package domain.entity;

import org.json.simple.JSONObject;

public class Menu {
    private String name;
    private String description;
    private Double popularity;
    private Double price;
    private String urlImage;

    public Menu(JSONObject menuInfo){
        this.name = menuInfo.get("name").toString();
        this.description = menuInfo.get("description").toString();
        this.popularity = Double.parseDouble(menuInfo.get("popularity").toString());
        this.price = Double.parseDouble(menuInfo.get("price").toString());
        this.urlImage = menuInfo.get("image").toString();
    }

    public Menu(String foodName , String foodDescription , Double popularity , Double price , String urlImage){
        this.name = foodName;
        this.description = foodDescription;
        this.popularity = popularity;
        this.price = price;
        this.urlImage = urlImage;
    }

    public String getName(){
        return name;
    }

    public Double getPopularity(){
        return popularity;
    }

    public String getDescription() { return this.description; }

    public Double getPrice() { return this.price; }

    public String getUrlImage(){
        return this.urlImage;
    }

}
