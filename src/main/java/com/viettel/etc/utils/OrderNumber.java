package com.viettel.etc.utils;

public enum OrderNumber{
    ORDER_BY_DEFAULT(0),
    ORDER_BY_NAME_DESC(1),
    ORDER_BY_NAME_ASC(2),
    ORDER_BY_PRICE_DESC(3),
    ORDER_BY_PRICE_ASC(4),
    ORDER_BY_TOTAL_BOOKING_ASC(5),
    ORDER_BY_TOTAL_BOOKING_DESC(6),
    ORDER_BY_TOTAL_HEALTHFACILITIES_ASC(7),
    ORDER_BY_TOTAL_HEALTHFACILITIES_DESC(8),
    ORDER_BY_SPECIALIST_NAME_ASC(9),
    ORDER_BY_SPECIALIST_NAME_DESC(10);

    private Integer orderNumber;

    private OrderNumber(Integer orderNumber){
        this.orderNumber = orderNumber;
    }

    public Integer val(){
        return this.orderNumber;
    }
}