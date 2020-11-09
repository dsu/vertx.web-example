package pl.dsu.model;

/**
 * Serializes a product into JSONObject with attribute productname, description,
 * imageurl and price.
 * 
 * @author dsu
 *
 */
public class Product {

	private String productname;

	private String description;

	private String imageurl;

	private String price;

	public Product() {
		super();
	}

	public Product(String productname, String description, String imageurl, String price) {
		super();
		this.productname = productname;
		this.description = description;
		this.imageurl = imageurl;
		this.price = price;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}