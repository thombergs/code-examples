package io.reflectoring.solid.isp;

class BurgerOrder implements IOrder {
    @Override
    public void orderBurger(int quantity) {

    }

    @Override
    public void orderFries(int fries) {
        throw new UnsupportedOperationException("No fries in Burger only order");
    }
}
