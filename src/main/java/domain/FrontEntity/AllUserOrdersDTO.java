package domain.FrontEntity;


import domain.entity.User;

import java.util.ArrayList;

public class AllUserOrdersDTO {
    private String username;
    ArrayList<SingleUserOrderDTO> orders;

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
