package io.reflectoring.solid.isp;

class AdaptedFriesOrder implements IAdapterOrderForFries {
    private final IOrder friesOrder;
    public AdaptedFriesOrder(IOrder friesOrder){
        this.friesOrder = friesOrder;
    }

    @Override
    public void orderFries(int quantity) {
        friesOrder.orderFries(quantity);
    }
}