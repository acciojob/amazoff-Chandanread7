package com.driver.repository;

import java.util.*;

import com.driver.model.DeliveryPartner;
import com.driver.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        partnerMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            partnerToOrderMap.putIfAbsent(partnerId, new HashSet<>());
            partnerToOrderMap.get(partnerId).add(orderId);
            orderToPartnerMap.put(orderId, partnerId);
            partnerMap.get(partnerId).setNumberOfOrders(partnerToOrderMap.get(partnerId).size());
        }
    }

    public Order findOrderById(String orderId){
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here

        return partnerToOrderMap.getOrDefault(partnerId, new HashSet<>()).size();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here

        return new ArrayList<>(partnerToOrderMap.getOrDefault(partnerId, new HashSet<>()));


    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders

        return new ArrayList<>(orderMap.keySet());

    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        if(partnerMap.containsKey(partnerId)){
            HashSet<String> orders = partnerToOrderMap.remove(partnerId);
            if (orders != null) {
                for (String orderId : orders) {
                    orderToPartnerMap.remove(orderId);
                }
            }
            partnerMap.remove(partnerId);
        }

    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        if (orderMap.containsKey(orderId)){
            if (orderToPartnerMap.containsKey(orderId)){
                String partnerId=orderToPartnerMap.get(orderId);
                partnerToOrderMap.get(partnerId).remove(orderId);
                partnerMap.get(partnerId).setNumberOfOrders(partnerToOrderMap.get(partnerId).size());
                orderToPartnerMap.remove(orderId);
            }
            orderMap.remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        return orderMap.size()-orderToPartnerMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        if (!partnerToOrderMap.containsKey(partnerId)) return 0;
        String[] dTime=timeString.split(":");
        int hrs=Integer.parseInt(dTime[0]);
        int minutes=Integer.parseInt(dTime[1]);
        int timeLimit = (hrs*60)+minutes;
        int count = 0;

        for (String orderId : partnerToOrderMap.get(partnerId)) {
            if (orderMap.get(orderId).getDeliveryTime() > timeLimit) {
                count++;
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        if (!partnerToOrderMap.containsKey(partnerId)) return null;

        int maxTime = 0;

        for (String orderId : partnerToOrderMap.get(partnerId)) {
            maxTime = Math.max(maxTime, orderMap.get(orderId).getDeliveryTime());
        }
        int hours = maxTime / 60;
        int minutes = maxTime % 60;
        return String.format("%02d:%02d", hours, minutes);


    }
}