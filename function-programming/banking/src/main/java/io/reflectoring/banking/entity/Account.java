package io.reflectoring.banking.entity;

public class Account {
	private long id;
	private String customerName, mobileNo;
	private double balance;
	public long getId() {
		return id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public double getBalance() {
		return balance;
	}
	public Account(long id, String customerName, String mobileNo, double balance) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.mobileNo = mobileNo;
		this.balance = balance;
	}
	public Account() {
		super();
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", customerName=" + customerName + ", mobileNo=" + mobileNo + ", balance="
				+ balance + "]";
	}
}
