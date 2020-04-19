package services;


import domain.entity.Delivery;
import domain.entity.DeliveryStatus;

import domain.entity.Order;
import domain.entity.User;
import domain.manager.DeliveryManager;
import domain.manager.UserManager;
import domain.repositories.Loghmeh;
import org.json.simple.JSONArray;

import java.time.LocalTime;
import java.util.ArrayList;

public class GetDeliveryList implements Runnable {

    @Override
    public void run() {
        Loghmeh instance = Loghmeh.getInstance();
        System.out.println("\n\n\nget delivery triggered by scheduler\n\n\n");
        DeliveryManager.getInstance().getDeliveryFromUrl();

//        System.out.println("\n\n\nLOOP DELIVERY FOR ALL USERS:");
//        for (User user : UserManager.getAllUsers()) {
//            System.out.print(user.getUsername());
//            if (user.getNotFoundDelivery().size() != 0) {
//                System.out.println("there is some order in user " + user.getUsername() + "" +
//                        " that did not match with any delivery!" + "handle here in run part 30 sec...");
//
//                if (DeliveryManager.getInstance().getAllDeliveries().size() != 0) {
//                    ArrayList<Order> orders = (ArrayList<Order>) user.getNotFoundDelivery().clone();
//                    for (Order order : orders) {
//                        System.out.println("try to find delivery with min time for order with id " +
//                                order.getId() + " that is in notFoundDelivery list!");
//                        Delivery findDelivery = DeliveryManager.getInstance().findDeliveryForOrder(order);
//                        if(findDelivery==null){
//                            System.out.println("find delivery is null???");
//                            continue;
//                        }
//                        else {
//                            System.out.println("delivery find is:==> " + findDelivery);
//                            order.orderGiveToDeliverySetTime(LocalTime.now(), findDelivery.getId());
//                            order.setStatus(DeliveryStatus.DELIVERING);
//                            user.removeOrderFromNotFoundDelivery(order);
//                        }
//                    }
//                }
//            }
//        }
    }
}