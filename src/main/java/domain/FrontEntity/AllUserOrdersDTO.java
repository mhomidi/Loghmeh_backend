package domain.FrontEntity;


import domain.entity.User;

import java.util.ArrayList;

public class AllUserOrdersDTO {
    private String username;
    ArrayList<SingleUserOrderDTO> orders;

    public AllUserOrdersDTO(String username){
        this.username = username;
        this.orders = new ArrayList<SingleUserOrderDTO>();
    }


    public AllUserOrdersDTO(String username, ArrayList<SingleUserOrderDTO> orders){
        this.username = username;
        this.orders = new ArrayList<SingleUserOrderDTO>();
        this.orders = orders;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<SingleUserOrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<SingleUserOrderDTO> orders) {
        this.orders = orders;
    }
}
