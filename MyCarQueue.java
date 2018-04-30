import java.util.HashMap;

public class MyCarQueue {
	public static int size; // no. of cars in the database
	public static HashMap<String, Car> indices; // this hash map maps String VIN
												// to it's car
	public static IndexMinPQ lowestMileage; // This is a priority queue for cars
											// based on their lowest mileage
	public static IndexMinPQ lowestPrice; // This is a priority queue for cars
											// based on their lowest price
	public static HashMap<String, IndexMinPQ> priceByMnM; // This is a hash map
															// that maps a
															// String
															// [make]#[model] to
															// the lowest price
															// priority queue of
															// the cars based on
															// the make and
															// model
	public static HashMap<String, IndexMinPQ> mileageByMnM; // This is a hash
															// map that maps a
															// String
															// [make]#[model] to
															// the lowest
															// mileage priority
															// queue of the cars
															// based on the make
															// and model
	public static IndexMinPQ lowestMileageSpecifics; // This is a mileage based
														// priority queue
														// created for every
														// make and model
	public static IndexMinPQ lowestPriceSpecifics; // This is a price based
													// priority queue created
													// for every make and model

	/*
	 * Constructor for myCarQueue
	 */
	public MyCarQueue() {
		size = 0; // set size
		indices = new HashMap<String, Car>();
		// initialize
		lowestMileage = new IndexMinPQ(128, true, false);
		lowestPrice = new IndexMinPQ(128, false, false);
		priceByMnM = new HashMap<String, IndexMinPQ>();
		mileageByMnM = new HashMap<String, IndexMinPQ>();
	}

	public boolean add(Car newCar) {
		// If the car doesn't exist return false

		if (newCar.getVin().equals(null))
			return false;

		// If the car with the same VIN already exists, return false

		if (indices.containsKey(newCar.getVin())) {
			System.out.println("Car already exists with the VIN you provided. Cannot add the car.\n");
			return false;
		}

		// Insert the car in the main priority queues

		lowestMileage.insert(newCar);
		lowestPrice.insert(newCar);

		// Insert the car in the main hash map of the cars
		indices.put(newCar.getVin(), newCar);

		// We get the concatenation of the make and model separated by '#' . No
		// intention to hint at Turing Machine inputs.

		String specs = newCar.getMake() + "#" + newCar.getModel();

		/*
		 * If the make and model already exists in the database, we retrieve the
		 * priority queue for that specific make and model and add the new car
		 * to it.
		 * 
		 * Else make a new priority queue for the make and model of the car and
		 * add the cars to the respective priority queues.
		 */

		if (priceByMnM.containsKey(specs) && mileageByMnM.containsKey(specs)) {
			priceByMnM.get(specs).insert(newCar);
			mileageByMnM.get(specs).insert(newCar);
		} else {
			lowestMileageSpecifics = new IndexMinPQ(9, true, true);
			lowestPriceSpecifics = new IndexMinPQ(9, false, true);
			lowestMileageSpecifics.insert(newCar);
			lowestPriceSpecifics.insert(newCar);
			priceByMnM.put(specs, lowestPriceSpecifics);
			mileageByMnM.put(specs, lowestMileageSpecifics);
		}
		// Increment the size
		size++;
		return true;
	}

	public boolean removeCar(String vin) {

		// If the Car associated to the VIN provided doesn't exist return false

		if (indices.get(vin) == null) {
			System.out.println("Couldn't remove. VIN doesn't exist.");
			return false;
		} else {
			// We get the concatenation of the make and model separated by '#' .
			// Again, no intention to hint at Turing Machine inputs.
			String specs = indices.get(vin).getMake() + "#" + indices.get(vin).getModel();

			// Delete the Car from the main PQ's
			lowestPrice.delete(indices.get(vin).getPI());
			lowestMileage.delete(indices.get(vin).getMI());

			// Delete the Car from the make and model PQ's

			priceByMnM.get(specs).delete(indices.get(vin).getMMPI());
			mileageByMnM.get(specs).delete(indices.get(vin).getMMMI());
			indices.remove(vin);

			// Decrement the size
			size--;
			return true;
		}

	}

	public boolean updateCar(String vin, double newAttribute, char mode, String color) {

		// If the Car associated to the VIN provided doesn't exist return false
		if (indices.get(vin) == null) {
			System.out.println("Couldn't update. VIN doesn't exist.");
			return false;
		}

		// Mode P = Change the price
		// Mode M = Change the mileage
		// Mode C = Change the color

		if (mode == 'p') {

			// We get the concatenation of the make and model separated by '#' .

			String specs = indices.get(vin).getMake() + "#" + indices.get(vin).getModel();

			// Set the new price for all the priority queues
			indices.get(vin).setPrice(newAttribute);
			lowestPrice.changeKey(indices.get(vin).priceIndex);
			priceByMnM.get(specs).changeKey(indices.get(vin).getMMPI());
			return true;
		} else if (mode == 'm') {

			// We get the concatenation of the make and model separated by '#' .

			String specs = indices.get(vin).getMake() + "#" + indices.get(vin).getModel();

			// Set the new price for all the priority queues
			indices.get(vin).setMileage(newAttribute);
			lowestMileage.changeKey(indices.get(vin).milesIndex);
			mileageByMnM.get(specs).changeKey(indices.get(vin).getMMPI());
			return true;
		} else if (mode == 'c') {

			// Set the new color for the color

			indices.get(vin).setColor(color);
			return true;
		}
		return false;
	}

	// Returns the cheapest car available from the main price priority queue
	public Car minPrice() {
		if (size == 0) {
			return null;
		}
		return lowestPrice.minKey();
	}

	// Returns the least driven car available from the main price priority queue
	public Car minMileage() {
		if (size == 0) {
			return null;
		}
		return lowestMileage.minKey();
	}

	// Returns the cheapest car available from the make and model based priority
	// queues

	public Car retrievePriceByMnM(String make, String model) {

		// We get the concatenation of the make and model separated by '#' .

		String concat = make + "#" + model;

		// If no cars exists return null
		if (size == 0) {
			return null;
		}

		// if the make and model doesn't exist return null
		if (!priceByMnM.containsKey(concat) || priceByMnM.get(concat).isEmpty()) {
			System.out.println("There is no car in the database with the make and model provided.");
			return null;
		}
		return priceByMnM.get(concat).minKey();
	}

	// Returns the least driven car available from the make and model based
	// priority
	// queues

	public Car retrieveMilesByMnM(String make, String model) {

		// We get the concatenation of the make and model separated by '#' .

		String concat = make + "#" + model;

		// If no cars exists return null
		if (size == 0) {
			return null;
		}

		// if the make and model doesn't exist return null
		if (!mileageByMnM.containsKey(concat) || mileageByMnM.get(concat).isEmpty()) {
			System.out.println("There is no car in the database with the make and model provided.");
			return null;
		}
		return mileageByMnM.get(concat).minKey();
	}
}
