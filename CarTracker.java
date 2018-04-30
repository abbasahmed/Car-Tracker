import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CarTracker {

	private static Scanner input = new Scanner(System.in);
	private static MyCarQueue carQueue = new MyCarQueue(); // Initialize
															// MyCarQueue
	private static boolean start = true; // For the welcome message in the
											// beginning of the program.

	public static void main(String[] args) {

		boolean active = true;

		// Check if the file cars.txt exists, and if it does add the cars to
		// database.

		try {
			FileReader dict = new FileReader("cars.txt");
			BufferedReader bufRead = new BufferedReader(dict);
			String fileRead = bufRead.readLine();
			fileRead = bufRead.readLine();
			while (fileRead != null) {
				String[] data = fileRead.split(":");
				String vin = data[0].toUpperCase();
				String make = data[1].toLowerCase();
				String model = data[2].toLowerCase();
				double price = Double.parseDouble(data[3]);
				double mileage = Double.parseDouble(data[4]);
				String color = data[5];
				Car newCar = new Car(vin, make, model, price, mileage, color);
				carQueue.add(newCar);
				fileRead = bufRead.readLine();
			}
			System.out.println("\t[Cars from cars.txt added!]");
			bufRead.close();
		} catch (FileNotFoundException e) {
			// System.out.println("cars.txt not found");
		} catch (IOException e) {
			System.out.println("IO Exception while adding cars from cars.txt");
		}

		int response = 0;

		while (active) {

			// Display menu
			displayMenu('d');

			boolean accept = true;

			// Make sure that response is an integer

			while (accept) {
				accept = false;
				try {
					response = input.nextInt();
				} catch (InputMismatchException e) {
					response = 9;
					input.nextLine();
				}
			}

			switch (response) {
			case 1:
				addCar(); // Add a car
				break;
			case 2:
				updateCar(); // Update a car
				break;
			case 3:
				removeCar(); // Remove a car
				break;
			case 4:
				retrieve(true); // Retrieve a car based on its price
				break;
			case 5:
				retrieve(false); // Retrieve a car based on its mileage
				break;
			case 6:
				retrieveByMnM(true); // Retrieve the cheapest car based on its
										// make and model
				break;
			case 7:
				retrieveByMnM(false); // Retrieve the lowest driven car based on
										// its make and model
				break;
			case 8:
				active = false; // Exit the loop
				break;
			default:
				System.out.println("\nPlease enter a number between 1 to 8 only"); // If
																					// the
																					// user
																					// enters
																					// a
																					// wrong
																					// choice
				break;
			}

		}
		System.out.println("\nProgram exited. goodbye!");
	}

	/*
	 * This method asks the user for all the details of the car about to be
	 * added and adds it to the MyCarQueue.
	 */

	public static void addCar() {

		String make;
		String model;
		String vin;
		String color;
		double mileage;
		double price;

		vin = getVin();
		System.out.println("\nEnter the make: ");
		make = input.nextLine().toLowerCase();
		System.out.println("\nEnter the model: ");
		model = input.nextLine().toLowerCase();
		price = getPrice(true);
		mileage = getMileage(true);
		System.out.println("\nEnter the color");
		color = input.nextLine().toLowerCase();

		Car newCar = new Car(vin, make, model, price, mileage, color);
		boolean done = carQueue.add(newCar);
		if (done)
			System.out.println("\nAdded Succesfully!");
	}

	/*
	 * This method asks the user for the VIN of car and asks what needs to be
	 * updated The user is given the option to change the price, change the
	 * mileage or the color of the car.
	 */

	public static void updateCar() {
		String vin;
		double newAttribute;
		String newColor;
		char mode;
		boolean done = false;
		boolean active = true;

		if (MyCarQueue.indices.isEmpty()) {
			System.out.println("\nThere is no car in the database. Please add a car first.");
			return;
		}

		vin = getVin();

		if (!MyCarQueue.indices.containsKey(vin)) {
			System.out
					.println("\nThere is no car in the database that matches with the provided pin. Please try again!");
			return;
		}
		while (active) {
			active = false;
			displayMenu('u');
			int answer = input.nextInt();
			switch (answer) {
			case 1:
				newAttribute = getPrice(false);
				mode = 'p';
				done = carQueue.updateCar(vin, newAttribute, mode, "");
				break;
			case 2:
				newAttribute = getMileage(false);
				mode = 'm';
				done = carQueue.updateCar(vin, newAttribute, mode, "");
				break;
			case 3:
				System.out.println("\nWhat would you like the new color to be?");
				input.nextLine();
				newColor = input.nextLine().toLowerCase();
				mode = 'c';
				done = carQueue.updateCar(vin, 0, mode, newColor);
				break;
			case 4:
				return;
			default:
				active = true;
				System.out.println("\nInvalid Option. Please try again.\n");
				break;
			}
		}

		if (done) {
			System.out.println("\nUpdated Succesfully!\n");
		} else {
			System.out.println("\nFailed to update. Please Try Again.\n");
		}
	}

	/*
	 * This method asks the user for the VIN of the car and removes it from the
	 * MyCarQueue.
	 */

	public static void removeCar() {
		String vin = getVin();
		boolean done = carQueue.removeCar(vin);
		if (done) {
			System.out.println("\nRemoved Successfully!");
		}

	}

	/*
	 * This method returns the lowest priced car or the lowest driven car
	 * available. Boolean type is true when the user asks for the cheapest car
	 * and false if the user asks for the lowest driven car.
	 */

	public static void retrieve(boolean type) {

		Car car;

		if (type) {
			car = carQueue.minPrice();
		} else {
			car = carQueue.minMileage();
		}
		if (car == null) {
			System.out.println("\nQueue is Empty.\n");
		} else {
			if (type)
				System.out.println("\nLowest Price Car Available:");
			else
				System.out.println("\nLeast driven car available:");
			car.toString();
		}

	}

	/*
	 * This method returns the lowest priced car or the lowest driven car
	 * available based on the make and model of the car. Boolean type is true
	 * when the user asks for the cheapest car and false if the user asks for
	 * the lowest driven car.
	 */

	public static void retrieveByMnM(boolean type) {
		System.out.println("\nEnter the make: ");
		input.nextLine();
		String make = input.nextLine().toLowerCase();
		System.out.println("\nEnter the model: ");
		String model = input.nextLine().toLowerCase();
		Car minCar;

		if (type) {
			minCar = carQueue.retrievePriceByMnM(make, model);
		} else {
			minCar = carQueue.retrieveMilesByMnM(make, model);
		}

		if (minCar == null) {
			System.out.println("\nQueue is Empty.\n");
		} else {
			if (type)
				System.out.println("\nLowest Price Car Available:");
			else
				System.out.println("\nLeast driven car available:");
			minCar.toString();
		}
	}

	// --------Helper Methods---------------

	/*
	 * This method prints the menu out to the user and displays a welcome
	 * message for the first run of the program. Mode = 'd' if the menu is for
	 * main menu and Mode = 'u' if the menu is for update menu.F
	 */

	private static void displayMenu(char mode) {

		if (mode == 'd') {
			if (start)
				System.out.println("\nWelcome to the Car Tracker!\n");
			start = false;
			System.out.println("1. \tAdd a car");
			System.out.println("2. \tUpdate a car");
			System.out.println("3. \tRemove a specific car from consideration");
			System.out.println("4. \tRetrieve the lowest price car");
			System.out.println("5. \tRetrieve the least driven car");
			System.out.println("6. \tRetrieve the lowest price car by Make and Model");
			System.out.println("7. \tRetrieve the least driven car by Make and Model");
			System.out.println("8. \tExit the program");
		} else if (mode == 'u') {
			System.out.println("\nWhat would you like to do?\n");
			System.out.println("1. \tUpdate the Price of a Car");
			System.out.println("2. \tUpdate the Mileage of a Car");
			System.out.println("3. \tUpdate the Color of a Car");
			System.out.println("4. \tReturn to the main Menu.");
		}

	}

	/*
	 * This method gets the Price input by the user and handles all the errors
	 * that might occur when the user types in invalid arguments. Mode is true
	 * when the user is entering the price for a new car and false if the user
	 * is being asked for new price for an existing car to update it.
	 */

	public static double getPrice(boolean mode) {
		double price = 0;
		boolean active = true;
		while (active) {
			active = false;
			if (mode) {
				System.out.println("\nEnter the price: ");
			} else {
				System.out.println("\nWhat would you like the new price to be?");
			}
			try {
				price = input.nextDouble();
				input.nextLine();
				if (price < 0) {
					active = true;
					System.out.println("\nIncorrect format. Please Try again!");
				}
			} catch (InputMismatchException e) {
				active = true;
				System.out.println("\nIncorrect format. Please Try again!");
				input.nextLine();

			}
		}
		return price;
	}

	/*
	 * This method gets the Mileage input by the user and handles all the errors
	 * that might occur when the user types in invalid arguments. Mode is true
	 * when the user is entering the mileage for a new car and false if the user
	 * is being asked for new mileage for an existing car to update it.
	 */

	public static double getMileage(boolean mode) {
		double mileage = 0;
		boolean active = true;
		while (active) {
			active = false;
			if (mode) {
				System.out.println("\nEnter the mileage: ");
			} else {
				System.out.println("\nWhat would you like the new mileage to be?");
			}
			try {
				mileage = input.nextDouble();
				input.nextLine();
				if (mileage < 0) {
					active = true;
					System.out.println("\nIncorrect format. Please Try again!");
				}
			} catch (InputMismatchException e) {
				active = true;
				System.out.println("\nIncorrect format. Please Try again!");
				input.nextLine();
			}
		}
		return mileage;

	}

	// This simply gets a VIN by the user.

	public static String getVin() {

		System.out.println("Enter the VIN: ");
		input.nextLine();
		String vin = input.nextLine().toUpperCase();

		return vin;
	}

}