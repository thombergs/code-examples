/**
 * 
 */
package io.pratik.elasticsearch.productsearchapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.pratik.elasticsearch.models.Product;
import io.pratik.elasticsearch.services.ProductSearchServiceWithRepo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Pratik Das
 *
 */
@SpringBootTest
@Slf4j
class ProductSearchServiceWithRepoTest {
	
	@Autowired
	private ProductSearchServiceWithRepo productSearchServiceWithRepo;


	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link io.pratik.elasticsearch.services.ProductSearchService#createProductIndexBulk(java.util.List)}.
	 */
	@Test
	void testCreateProductIndexBulk() {
		
		List<Product> products = new ArrayList<Product>();
		products.add(Product.builder()
				.id(UUID.randomUUID().toString())
				.name("New Apple iPhone 12 Pro Max (128GB) - Pacific Blue")
				.category("phone")
				.price(2300.0)
				.quantity(55)
				.manufacturer("apple")
				.description("6.7-inch Super Retina XDR display\n" + 
						"Ceramic Shield, tougher than any smartphone glass\n" + 
						"A14 Bionic chip, the fastest chip ever in a smartphone\n" + 
						"Pro camera system with 12MP Ultra Wide, Wide and Telephoto cameras; 5x optical zoom range; Night mode, Deep Fusion, Smart HDR 3, Apple ProRAW, 4K Dolby Vision HDR recording")
				.build()
				);
		
		products.add(Product.builder()
				.id(UUID.randomUUID().toString())
				.name("New Apple iPhone 12 Mini (64GB) - Blue")
				.category("phone")
				.price(2300.0)
				.quantity(15)
				.manufacturer("apple")
				.description("5.4-inch Super Retina XDR display\n" + 
						"Ceramic Shield, tougher than any smartphone glass\n" + 
						"A14 Bionic chip, the fastest chip ever in a smartphone\n" + 
						"Advanced dual-camera system with 12MP Ultra Wide and Wide cameras; Night mode, Deep Fusion, Smart HDR 3, 4K Dolby Vision HDR recording\n" + 
						"12MP TrueDepth front camera with Night mode, 4K Dolby Vision HDR recording\n" + 
						"Industry-leading IP68 water resistance")
				.build()
				);
		
		products.add(Product.builder()
				.id(UUID.randomUUID().toString())
				.name("Samsung Galaxy Note10 Lite (Aura Glow, 8GB RAM, 128GB Storage)")
				.category("phone")
				.price(2100.0)
				.quantity(25)
				.manufacturer("samsung")
				.description("12MP Ultra wide (123°) FF + F2.2 \" Wide (77°) 12MP AF F1.7 Dual Pixel + Tele (45°) 12MP AF F2.4 OIS, 2x Zoom camera | 32MP front facing camera\n" + 
						"17.04 centimeters (6.7-inch) super Amoled infinity-O display and FHD+ capacitive multi-touch touchscreen with 2400 x 1080 pixels resolution | 16M color support\n" + 
						"Memory, Storage & SIM: 8GB RAM | 128GB internal memory expandable up to 1TB | Dual SIM dual-standby (4G+4G)\n" + 
						"Android v10.0 operating system with 2.7GHz+1.8GHz Exynos 9810 octa core processor")
				.build()
				);
		
		products.add(Product.builder()
				.id(UUID.randomUUID().toString())
				.name("Samsung 163 cm (65 Inches) Q Series 4K Ultra HD QLED")
				.category("television")
				.price(2100.0)
				.quantity(5)
				.manufacturer("samsung")
				.description("Resolution: 4K Ultra HD (3840x2160) | Refresh Rate: 120 hertz\n" + 
						"Connectivity: 3 HDMI ports to connect set top box, Blu Ray players, gaming console | 2 USB ports to connect hard drives and other USB devices\n" + 
						"Sound : 40 Watts Output\n" + 
						"Display : QLED Panel | Q HDR Elite (HDR 10+) | Slim and stylish design")
				.build()
				);
		productSearchServiceWithRepo.createProductIndexBulk(products );
	}
	
	/**
	 * Test method for {@link io.pratik.elasticsearch.services.ProductSearchService#createProductIndex(io.pratik.elasticsearch.models.Product)}.
	 */
	@Test
	void testCreateProductIndex() {
	  final Product product = Product.builder()
				.id(UUID.randomUUID().toString())
				.name("Dell Vostro 3401 14inch FHD AG 2 Side Narrow Border Display Laptop")
				.category("laptop")
				.price(2100.0)
				.quantity(28)
				.manufacturer("dell")
				.description("Processor:10th Generation Intel Core i3-1005G1 Processor (4MB Cache, Base frequency 1.2 GHz)\n" + 
						"Memory & Storage:4GB RAM 1TB 5400 rpm 2.5\" SATA Hard Drive\n" + 
						"Display:14.0-inch FHD (1920 x 1080) Anti-glare LED Backlight 2 Side Narrow Border WVA Display")
				.build();
		productSearchServiceWithRepo.createProductIndex(product);

	}
	
	@Test
	void testFetchProductByName() {
		List<Product> products = productSearchServiceWithRepo.findByProductName("apple");
	    log.info("products {}",products);
	}
	
	@Test
	void testFetchProductByManufacturerAndCategory() {
		List<Product> products = productSearchServiceWithRepo.findProductsByManufacturerAndCategory("samsung", "laptop");
	    log.info("products {}",products);
	}
	
	@Test
	void testFetchProductByManufacturer() {
		List<Product> products = productSearchServiceWithRepo.findProductsByManufacturerAndCategory("samsung", "laptop");
	    log.info("products {}",products);
	}

}
