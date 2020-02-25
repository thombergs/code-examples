class Main{
    public static void main(String[] args){
        ComboOrderService comboOrderService = new ComboOrderService();
        comboOrderService.orderCombo(4,5);
        OrderServiceObjectAdapter orderServObjectAdapter = new OrderServiceObjectAdapter(new ComboOrderService());
        orderServObjectAdapter.orderBurger(4);
    }
}