/**
 * 
 */
package io.pratik;

import java.util.Random;
import java.util.UUID;

/**
 * @author Pratik Das
 *
 */
public class AbstractProduct {

	private String name;
	private String id;
	private byte[] logo;
	private static final Random random = new Random();

	public AbstractProduct() {
		super();
		this.id = UUID.randomUUID().toString();
		this.logo = new byte[(5 ) * 1024 * 1024];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public long getSize() {
		return JavaAgent.getObjectSize(this) + JavaAgent.getObjectSize(logo) + JavaAgent.getObjectSize(id);
	}

}
