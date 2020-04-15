package domain.entity;

import org.json.simple.JSONObject;


public class MenuParty {
    private String name;
    private String description;
    private Double popularity;
    private Double oldPrice;
    private String urlImage;
    private Double newPrice;
    private int count;




    public MenuParty(JSONObject menuInfo){
        this.name = menuInfo.get("name").toString();
        this.description = menuInfo.get("description").toString();
        this.popularity = Double.parseDouble(menuInfo.get("popularity").toString());
        this.oldPrice = Double.parseDouble(menuInfo.get("oldPrice").toString());
        this.urlImage = menuInfo.get("image").toString();
        this.newPrice = Double.parseDouble(menuInfo.get("price").toString());
        this.count = Integer.parseInt(menuInfo.get("count").toString());
    }


    public String getName(){
        return name;
    }

    public Double getPopularity(){
        return popularity;
    }

    public String getDescription() { return this.description; }

    public Double getOldPrice() { return this.oldPrice; }

    public Double getNewPrice(){
        return this.newPrice;
    }

    public String getUrlImage(){
        return this.urlImage;
    }

    public int getCount(){
        return this.count;
    }

    public void decreaseCount(int num){
        this.count-=num;
    }

    public boolean canChooseThisMenu(int count){
        return this.count>=count;
    }

}
