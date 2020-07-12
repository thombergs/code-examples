package io.reflectoring.solid.lsp.paymentexample;

import io.reflectoring.solid.lsp.paymentexample.common.OrderDetails;
import io.reflectoring.solid.lsp.paymentexample.redesigned.PaymentProcessor;
import io.reflectoring.solid.lsp.paymentexample.redesigned.fraud.IFraudChecker;
import io.reflectoring.solid.lsp.paymentexample.redesigned.fraud.ThirdPartyFraudChecker;
import io.reflectoring.solid.lsp.paymentexample.redesigned.instruments.CreditCard;
import io.reflectoring.solid.lsp.paymentexample.redesigned.instruments.DebitCard;
import io.reflectoring.solid.lsp.paymentexample.redesigned.instruments.RewardsCard;
import io.reflectoring.solid.lsp.paymentexample.redesigned.paymentgateway.IPaymentGatewayHandler;
import io.reflectoring.solid.lsp.paymentexample.redesigned.paymentgateway.PaymentGatewayHandler;
import io.reflectoring.solid.lsp.paymentexample.redesigned.validators.BankCardBasicValidator;
import io.reflectoring.solid.lsp.paymentexample.redesigned.validators.IPaymentInstrumentValidator;
import java.util.Date;
import java.util.Scanner;

public class RedesignedMainApp {
    public static void main(String[] args) {
        System.out.println("This program simulates the different flows through the payment system.");
        System.out.println("This version shows how fixing the class hierarchy issues fixes LSP violation and "
            + "also makes the solution more flexible.");
        System.out.println();
        System.out.println("Available flows: ");

        System.out.println("1. Successful credit card payment flow");
        System.out.println("2. Unsuccessful credit card payment - fraud detected flow");
        System.out.println("3. Unsuccessful credit card payment - payment gateway error");

        System.out.println("4. Successful debit card payment flow");
        System.out.println("5. Unsuccessful debit card payment - fraud detected flow");
        System.out.println("6. Unsuccessful debit card payment - payment gateway error");

        System.out.println("7. Successful rewards card payment flow");
        System.out.print("Just select the flow you want to simulate, no other inputs required: ");

        PaymentProcessor paymentProcessor = new PaymentProcessor();
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
                Date expiryDate = new Date();
                IPaymentInstrumentValidator validator = new BankCardBasicValidator(name, cardNum, code, expiryDate);
                IFraudChecker fraudChecker = new ThirdPartyFraudChecker(name, cardNum, code, expiryDate);
                IPaymentGatewayHandler gatewayHandler = new PaymentGatewayHandler(name, cardNum, code, expiryDate);
                CreditCard card = new CreditCard(name, cardNum, code, new Date(), validator, fraudChecker, gatewayHandler);

                System.out.println("Starting payment processing for customer " + name + " with credit card number " + cardNum);
                paymentProcessor.process(order, card);
                System.out.println("Credit card payment completed successfully!");

                break;
            case 2:
                String cardNum2 = "0567876523455432";
                Date expiryDate2 = new Date();
                IPaymentGatewayHandler gatewayHandler2 = new PaymentGatewayHandler(name, cardNum2, code, expiryDate2);
                IFraudChecker fraudChecker2 = new ThirdPartyFraudChecker(name, cardNum2, code, expiryDate2);
                IPaymentInstrumentValidator validator2 = new BankCardBasicValidator(name, cardNum2, code, expiryDate2);
                CreditCard card2 = new CreditCard(name, cardNum2, code, new Date(), validator2, fraudChecker2, gatewayHandler2);

                System.out.println("Starting payment processing for customer " + name + " with credit card number " + cardNum2);
                paymentProcessor.process(order, card2);
                System.out.println("Credit card payment failed!");

                break;
            case 3:
                String cardNum3 = "1567876523455432";
                Date expiryDate3 = new Date();
                IPaymentGatewayHandler gatewayHandler3 = new PaymentGatewayHandler(name, cardNum3, code, expiryDate3);
                IPaymentInstrumentValidator validator3 = new BankCardBasicValidator(name, cardNum3, code, expiryDate3);
                IFraudChecker fraudChecker3 = new ThirdPartyFraudChecker(name, cardNum3, code, expiryDate3);
                CreditCard card3 = new CreditCard(name, cardNum3, code, new Date(), validator3, fraudChecker3, gatewayHandler3);

                System.out.println("Starting payment processing for customer " + name + " with credit card number " + cardNum3);
                paymentProcessor.process(order, card3);
                System.out.println("Credit card payment failed!");

                break;
            case 4:
                String cardNum4 = "6567876523455432";
                Date expiryDate4 = new Date();
                IPaymentGatewayHandler gatewayHandler4 = new PaymentGatewayHandler(name, cardNum4, code, expiryDate4);
                IPaymentInstrumentValidator validator4 = new BankCardBasicValidator(name, cardNum4, code, expiryDate4);
                IFraudChecker fraudChecker4 = new ThirdPartyFraudChecker(name, cardNum4, code, expiryDate4);
                DebitCard card4 = new DebitCard(name, cardNum4, code, new Date(), validator4, fraudChecker4, gatewayHandler4);

                System.out.println("Starting payment processing for customer " + name + " with debit card number " + cardNum4);
                paymentProcessor.process(order, card4);
                System.out.println("Debit card payment completed successfully!");

                break;
            case 5:
                String cardNum5 = "0567876523455432";
                Date expiryDate5 = new Date();
                IPaymentGatewayHandler gatewayHandler5 = new PaymentGatewayHandler(name, cardNum5, code, expiryDate5);
                IPaymentInstrumentValidator validator5 = new BankCardBasicValidator(name, cardNum5, code, expiryDate5);
                IFraudChecker fraudChecker5 = new ThirdPartyFraudChecker(name, cardNum5, code, expiryDate5);

                DebitCard card5 = new DebitCard(name, cardNum5, code, new Date(), validator5, fraudChecker5, gatewayHandler5);

                System.out.println("Starting payment processing for customer " + name + " with debit card number " + cardNum5);
                paymentProcessor.process(order, card5);
                System.out.println("Debit card payment failed!");

                break;
            case 6:
                String cardNum6 = "1567876523455432";
                Date expiryDate6 = new Date();
                IPaymentGatewayHandler gatewayHandler6 = new PaymentGatewayHandler(name, cardNum6, code, expiryDate6);
                IPaymentInstrumentValidator validator6 = new BankCardBasicValidator(name, cardNum6, code, expiryDate6);
                IFraudChecker fraudChecker6 = new ThirdPartyFraudChecker(name, cardNum6, code, expiryDate6);

                DebitCard card6 = new DebitCard(name, cardNum6, code, new Date(), validator6, fraudChecker6, gatewayHandler6);

                System.out.println("Starting payment processing for customer " + name + " with debit card number " + cardNum6);
                paymentProcessor.process(order, card6);
                System.out.println("Debit card payment failed!");

                break;
            case 7:
                String cardNum7 = "25678765";
                RewardsCard card7 = new RewardsCard(name, cardNum7);

                System.out.println("Starting payment processing for customer " + name + " with rewards card number " + cardNum7);
                paymentProcessor.process(order, card7);
                System.out.println("Rewards card payment successful!");

                break;

            default:
                System.out.println("Invalid flow selected");
                break;
        }
    }
}