package controller.user.responses;


import domain.entity.User;

import java.util.ArrayList;

public class UserOrdersResponse {
    private String username;
    ArrayList<SingleOrderResponese> orders;


    public UserOrdersResponse(User user){
        orders = new ArrayList<SingleOrderResponese>();
        System.out.println("user has order: num=>" );
        System.out.println(user.getAllOrders().size());
        this.username = user.getUsername();
        for(int i=0;i<user.getAllOrders().size();i++){
            orders.add(new SingleOrderResponese(user.getAllOrders().get(i)));
        }
    }

    public void setOrders(ArrayList<SingleOrderResponese> orders) {
        this.orders = orders;
    }

    public ArrayList<SingleOrderResponese> getOrders() {
        return orders;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
