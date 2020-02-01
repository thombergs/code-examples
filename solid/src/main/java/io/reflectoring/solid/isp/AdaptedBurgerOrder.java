package io.reflectoring.solid.isp;

class AdaptedBurgerOrder implements IAdapterOrderForBurger {
    private final IOrder burgerOrder;
    public AdaptedBurgerOrder(IOrder burgerOrder){
        this.burgerOrder = burgerOrder;
    }
    @Override
    public void orderBurger(int quantity) {
        burgerOrder.orderBurger(quantity);
    }
}

