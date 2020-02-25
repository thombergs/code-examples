package io.reflectoring.solid.isp;

class OrderServiceObjectAdapter implements NewBurgerOrderService {
    private OrderService adaptee;
    public OrderServiceObjectAdapter(OrderService adaptee) {
        super();
        this.adaptee = adaptee;
    }

    @Override
    public void orderBurger(int quantity) {
        adaptee.orderBurger(quantity);
    }
}