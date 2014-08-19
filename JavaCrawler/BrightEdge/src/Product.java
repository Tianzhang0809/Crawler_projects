/**
 * //product type object, use to store the single piece of product information.
 * 
 * @author jiashengqiu
 * 
 */
public class Product {
	private String name;// Name of the product.
	private String price;// Price information about the product
	private String description;// The description about the product.

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Product(String name, String description, String price) {
		this.name = name;
		this.price = price;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Product Title: " + getName() + "\n" + "Description: "
				+ getDescription() + "\n" + "Price: " + getPrice() + "\n";
	}
}
