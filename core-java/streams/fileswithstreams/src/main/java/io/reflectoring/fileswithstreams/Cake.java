package io.reflectoring.fileswithstreams;

public class Cake {
    private int id;
    private String name;
    private int price;
    
    public Cake(int id, String name, int price) {
    	this.id = id;
    	this.name = name;
    	this.price = price;
    }

	@Override
	public String toString() {
		return "Cake [id=" + id + ", name=" + name + ", price=" + price + "]";
	}
}
