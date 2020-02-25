package io.reflectoring.solid.isp;

interface OrderService {
    void orderBurger(int quantity);
    void orderFries(int fries);
    void orderCombo(int quantity, int fries);
}