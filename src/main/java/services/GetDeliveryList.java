package services;


import domain.entity.Delivery;
import domain.entity.DeliveryStatus;

import domain.entity.Order;
import domain.entity.User;
import org.json.simple.JSONArray;

import java.time.LocalTime;
import java.util.ArrayList;

public class GetDeliveryList implements Runnable {

    @Override
    public void run() {
        System.out.println("get delivery triggered by scheduler");
        JSONArray jsonArray = DeliveryServices.getInstance().requestDeliveryApiGetList();
        DeliveryServices.getInstance().updateListOfDelivery(jsonArray);

        System.out.println("\n\n\nLOOP DELIVERY FOR ALL USERS:");
        for (User user : UserService.getAllUsers()) {
            System.out.print(user.getUsername());
            if (user.getNotFoundDelivery().size() != 0) {
                System.out.println("there is some order in user " + user.getUsername() + "" +
                        " that did not match with any delivery!" + "handle here in run part 30 sec...");

                if (DeliveryServices.getInstance().getAllDeliveries().size() != 0) {
                    ArrayList<Order> orders = (ArrayList<Order>) user.getNotFoundDelivery().clone();
                    for (Order order : orders) {
                        System.out.println("try to find delivery with min time for order with id " +
                                order.getId() + " that is in notFoundDelivery list!");
                        Delivery findDelivery = DeliveryServices.getInstance().findDeliveryForOrder(order);
                        if(findDelivery==null){
                            System.out.println("find delivery is null???");
                            continue;
                        }
                        else {
                            System.out.println("delivery find is:==> " + findDelivery);
                            order.orderGiveToDeliverySetTime(LocalTime.now(), findDelivery.getId());
                            order.setStatus(DeliveryStatus.DELIVERING);
                            user.removeOrderFromNotFoundDelivery(order);
                        }
                    }
                }
            }
        }
    }
}