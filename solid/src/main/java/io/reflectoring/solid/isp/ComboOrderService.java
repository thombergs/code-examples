package io.reflectoring.solid.isp;

class ComboOrderService implements OrderService{
    @Override
    public void orderBurger(int quantity) {
        System.out.println("Received order of "+quantity+" burgers");
    }

    @Override
    public void orderFries(int fries) {
        System.out.println("Received order of "+fries+ " fries");
    }

    @Override
    public void orderCombo(int quantity, int fries) {
        System.out.println("Received order of "+quantity+" burgers and "+ fries+" fries");
    }
}
