package io.reflectoring.solid.isp;

class FriesOrderService implements OrderService {
    @Override
    public void orderBurger(int quantity) {
        throw new UnsupportedOperationException("No burger in fries only order");
    }

    @Override
    public void orderFries(int fries) {
        System.out.println("Received order of "+fries+ " fries");
    }

    @Override
    public void orderCombo(int quantity, int fries) {
        throw new UnsupportedOperationException("No combo in fries only order");
    }
}

