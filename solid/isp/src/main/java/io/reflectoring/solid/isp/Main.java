package io.reflectoring.solid.isp;

class Main{
    public static void main(String[] args){
        OrderService comboOrderService = new ComboOrderService();
        NewBurgerOrderService burgerService =
            new OrderServiceObjectAdapter(new ComboOrderService());
        burgerService.orderBurger(4);
    }
}