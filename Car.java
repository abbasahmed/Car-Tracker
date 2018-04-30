/*
 * Car Object
 * 
 * Stores VIN, Make, Model, Price, Mileage, and Color of the car.
 * 
 * Also contains the current index of each car in its respective priority index
 */
public class Car {

	private String vinNumber;
	private String make;
	private String model;
	private double price;
	private double mileage;
	private String color;
	public int priceIndex;
	public int milesIndex;
	public int MnMPriceIndex;
	public int MnMMilesIndex;

	public Car(String vinNumber, String make, String model, double price, double mileage, String color) {
		this.vinNumber = vinNumber;
		this.make = make;
		this.model = model;
		this.price = price;
		this.mileage = mileage;
		this.color = color;
	}
	
	/*
	 * SETTERS
	 */
	
	public void setPrice(double newPrice) {
		this.price = newPrice;
	}

	public void setMileage(double mileage) {
		this.mileage = mileage;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setPricePQIndex(int priceIndex) {
		this.priceIndex = priceIndex;
	}

	public void setMilesPQIndex(int milesIndex) {
		this.milesIndex = milesIndex;
	}

	public void setMnMPI(int pI) {
		this.MnMPriceIndex = pI;
	}

	public void setMnMMI(int mI) {
		this.MnMMilesIndex = mI;
	}
	
	/*
	 * GETTERS
	 */

	public String getVin() {
		return vinNumber;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public double getMileage() {
		return mileage;
	}

	public double getPrice() {
		return price;
	}

	public String getColor() {
		return color;
	}

	public int getMMPI() {
		return MnMPriceIndex;
	}

	public int getMMMI() {
		return MnMMilesIndex;
	}

	public int getPI() {
		return priceIndex;
	}

	public int getMI() {
		return milesIndex;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 
	 * Overrides the Java toString method.
	 */

	public String toString() {
		String name  = "stackoverflow"; 
		name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
		System.out.println("");
		System.out.println("\tVin Number: " + vinNumber);
		System.out.println("\tCar Make: " + make.substring(0, 1).toUpperCase()+make.substring(1).toLowerCase());
		System.out.println("\tCar Model: " + model.substring(0, 1).toUpperCase()+model.substring(1).toLowerCase());
		System.out.println("\tPrice to purchase (in dollars): " + "$" + price);
		System.out.println("\tMileage of the car: " + mileage + " miles");
		System.out.println("\tColor of the car: " + color.substring(0, 1).toUpperCase()+color.substring(1).toLowerCase());
		System.out.println("");
		return "";
	}
}
