package io.reflectoring.solid.lsp.paymentexample;

import io.reflectoring.solid.lsp.paymentexample.common.OrderDetails;
import io.reflectoring.solid.lsp.paymentexample.common.instruments.CreditCard;
import io.reflectoring.solid.lsp.paymentexample.common.instruments.DebitCard;
import io.reflectoring.solid.lsp.paymentexample.forcefitandconditional.PaymentProcessor;
import io.reflectoring.solid.lsp.paymentexample.forcefitandconditional.instruments.RewardsCard;
import java.util.Date;
import java.util.Scanner;

public class ForceFitAndConditionalCodeMainApp {
    public static void main(String[] args) {
        System.out.println("This program simulates the different flows through the payment system.");
        System.out.println("This version shows how force-fitting the new class RewardsCard "
            + "into the current class hierarchy and adding conditional checks fixes LSP violation but results in "
            + " code that is difficult to maintain.");
        System.out.println();
        System.out.println("Available flows: ");

        System.out.println("1. Successful credit card payment flow");
        System.out.println("2. Unsuccessful credit card payment - fraud detected flow");
        System.out.println("3. Unsuccessful credit card payment - payment gateway error");

        System.out.println("4. Successful debit card payment flow");
        System.out.println("5. Unsuccessful debit card payment - fraud detected flow");
        System.out.println("6. Unsuccessful debit card payment - payment gateway error");

        System.out.println("7. Successful rewards card payment flow");
        System.out.print("Select the flow you want to simulate, no other inputs required: ");

        PaymentProcessor processor = new PaymentProcessor();
        Scanner input = new Scanner(System.in);
        int flow = input.nextInt();
        System.out.println("----------------------------------------------------------------------");
        String name = "John Smith";
        String code = "123";
        String orderNum = "default-order-123";
        OrderDetails order = new OrderDetails();
        order.setOrderNumber(orderNum);

        switch (flow) {
            case 1:
                String cardNum = "4567876523455432";
                CreditCard card = new CreditCard(name, cardNum, code, new Date());

                System.out.println("Starting payment processing for customer " + name + " with credit card number " + cardNum);
                processor.process(order, card);
                System.out.println("Credit card payment completed successfully!");

                break;
            case 2:
                String cardNum2 = "0567876523455432";
                CreditCard card2 = new CreditCard(name, cardNum2, code, new Date());

                System.out.println("Starting payment processing for customer " + name + " with credit card number " + cardNum2);
                processor.process(order, card2);
                System.out.println("Credit card payment failed!");

                break;
            case 3:
                String cardNum3 = "1567876523455432";
                CreditCard card3 = new CreditCard(name, cardNum3, code, new Date());

                System.out.println("Starting payment processing for customer " + name + " with credit card number " + cardNum3);
                processor.process(order, card3);
                System.out.println("Credit card payment failed!");

                break;
            case 4:
                String cardNum4 = "6567876523455432";
                DebitCard card4 = new DebitCard(name, cardNum4, code, new Date());

                System.out.println("Starting payment processing for customer " + name + " with debit card number " + cardNum4);
                processor.process(order, card4);
                System.out.println("Debit card payment completed successfully!");

                break;
            case 5:
                String cardNum5 = "0567876523455432";
                DebitCard card5 = new DebitCard(name, cardNum5, code, new Date());

                System.out.println("Starting payment processing for customer " + name + " with debit card number " + cardNum5);
                processor.process(order, card5);
                System.out.println("Debit card payment failed!");

                break;
            case 6:
                String cardNum6 = "1567876523455432";
                DebitCard card6 = new DebitCard(name, cardNum6, code, new Date());

                System.out.println("Starting payment processing for customer " + name + " with debit card number " + cardNum6);
                processor.process(order, card6);
                System.out.println("Debit card payment failed!");

                break;
            case 7:
                String cardNum7 = "25678765";
                RewardsCard card7 = new RewardsCard(name, cardNum7, code, new Date());

                System.out.println("Starting payment processing for customer " + name + " with rewards card number " + cardNum7);
                processor.process(order, card7);
                System.out.println("Rewards card payment successful!");

                break;

            default:
                System.out.println("Invalid flow selected");
                break;
        }
    }
}