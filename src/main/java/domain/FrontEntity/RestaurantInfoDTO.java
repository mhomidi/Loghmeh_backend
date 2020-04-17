package domain.FrontEntity;


public class RestaurantInfoDTO {
    private String restaurantId;
    private String restaurantName;
    private String logoUrl;
    private String estimateDelivery;


    public RestaurantInfoDTO(String id, String name , String imageUrl) {
        this.restaurantId = id;
        this.restaurantName = name;
        this.logoUrl = imageUrl;
    }

    public String getId(){
        return  restaurantId;
    }
    public void setId(String id){
        this.restaurantId = id;
    }

    public String getName(){
        return restaurantName;
    }

    public void setName(String name){
        this.restaurantName = name;
    }


    public String getLogoUrl(){
        return logoUrl;
    }

    public void setLogoUrl(String imageUrl){
        this.logoUrl = imageUrl;
    }

    public String getEstimateDelivery() {
        return estimateDelivery;
    }

    public void setEstimateDelivery(String estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }

    @Override
    public String toString() {
        return "RestaurantInfoDTO{" +
                "restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", estimateDelivery='" + estimateDelivery + '\'' +
                '}';
    }
}

