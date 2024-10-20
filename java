import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Class to represent a single booking
class Booking {
    int number; // Booking number
    String date; // Booking date
    String time; // Booking time
    String passengerName; // Passenger's name
    String pickupLocation; // Pick-up location
    String dropLocation; // Drop-off location
    float distance; // Distance in kilometers
    float fare; // Fare in currency

    // Constructor to initialize a booking
    Booking(int number, String date, String time, String passengerName, 
            String pickupLocation, String dropLocation, float distance, float fare) {
        this.number = number;
        this.date = date;
        this.time = time;
        this.passengerName = passengerName;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.distance = distance;
        this.fare = fare;
    }
}

// Main class for managing the booking system
public class Main {
    private List<Booking> bookings = new ArrayList<>(); // List to store all bookings
    private Scanner scanner = new Scanner(System.in); // Scanner for user input

    // Method to display all bookings
    private void viewAllBookings() {
        // Print table header
        System.out.format("%-5s %-12s %-10s %-25s %-25s %-25s %-12s %-10s\n", 
                          "#", "Date", "Time", "Passenger Name", "Pick-up Location", 
                          "Drop-off Location", "Distance(km)", "Fare(PHP)");
        System.out.println("--------------------------------------------------------------------------");

        // Loop through each booking and print its details
        for (Booking booking : bookings) {
            System.out.format("%-5d %-12s %-10s %-25s %-25s %-25s %-12.1f %-10.2f\n",
                              booking.number, booking.date, booking.time, booking.passengerName,
                              booking.pickupLocation, booking.dropLocation, booking.distance, booking.fare);
        }
    }

    // Method to book a ride
    private void bookARide() {
        // Check if maximum bookings are reached
        if (bookings.size() >= 100) {
            System.out.println("Error! Maximum bookings reached.");
            return;
        }

        // Get user input for booking details
        String passengerName = getValidStringInput("Enter Passenger's name: ");
        String pickupLocation = getValidStringInput("Enter Pick-up Location: ");
        String dropLocation = getValidStringInput("Enter Drop-off Location: ");
        float distance = getValidDistanceInput("Enter Distance (in km): ");

        // Calculate fare based on distance
        float fare = ((distance - 1) * 20) + 25;

        // Get current date and time
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));

        // Add new booking to the list
        bookings.add(new Booking(bookings.size() + 1, date, time, passengerName, pickupLocation, dropLocation, distance, fare));
    }

    // Method to delete a booking
    private void deleteBooking() {
        // Check if there are any bookings to delete
        if (bookings.isEmpty()) {
            System.out.println("Error! No bookings available.");
            return;
        }

        // Get valid booking number from user
        int bookingNumber = getValidBookingNumber("Delete booking number: ");
        if (bookingNumber == -1) return; // Exit if invalid booking number

        // Remove the booking with the specified number
        bookings.removeIf(b -> b.number == bookingNumber);
        System.out.println("Booking #" + bookingNumber + " has been deleted.");
    }

    // Method to generate and display a booking report
    private void generateBookingReport() {
        // Print report header
        System.out.println("\t\t\t\t\t\t\tBOOKING REPORT\n");
        System.out.format("%-5s %-12s %-10s %-20s %-12s %-10s\n",
                          "#", "Date", "Time", "Passenger Name", "Distance(km)", "Fare(PHP)");
        System.out.println("-------------------------------------------------------------------------");

        // Loop through each booking and print its details
        for (Booking booking : bookings) {
            System.out.format("%-5d %-12s %-10s %-20s %-12.1f %-10.2f\n",
                              booking.number, booking.date, booking.time, booking.passengerName,
                              booking.distance, booking.fare);
        }

        // Generate report file
        generateFile();
    }

    // Method to generate a report file and save it
    private void generateFile() {
        String filename = "BookingReport.txt"; 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("\t\t\t\tREPORT\n\n"); // Write report title
            writer.write(String.format("%-5s %-12s %-10s %-20s %-12s %-10s\n", 
                                       "#", "Date", "Time", "Passenger Name", "Distance(km)", "Fare(PHP)"));
            writer.write("-------------------------------------------------------------------------\n");

            // Write each booking to the file
            for (Booking booking : bookings) {
                writer.write(String.format("%-5d %-12s %-10s %-20s %-12.1f %-10.2f\n",
                                           booking.number, booking.date, booking.time,
                                           booking.passengerName, booking.distance, booking.fare));
            }

            // Write total bookings count to the file
            writer.write("\nTotal Bookings: " + bookings.size() + "\n");
            System.out.println("Booking report saved to " + filename); // Notify user of successful save
        } catch (IOException e) { // Handle file writing errors
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    // Method to get valid string input from the user
    private String getValidStringInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim(); // Get and trim user input
            if (input.isEmpty()) {
                System.out.println("Error! Input cannot be empty.");
            }
        } while (input.isEmpty()); // Repeat until valid input is received
        return input;
    }

    // Method to get valid distance input from the user
    private float getValidDistanceInput(String prompt) {
        float distance;
        while (true) {
            System.out.print(prompt);
            try {
                distance = Float.parseFloat(scanner.nextLine());
                if (distance < 1) { // Validate distance
                    System.out.println("Error! Distance must be at least 1 km.");
                } else {
                    return distance; // Return valid distance
                }
            } catch (NumberFormatException e) { // Handle invalid input
                System.out.println("Error! Please enter a valid number.");
            }
        }
    }

    // Method to get a valid booking number from the user
    private int getValidBookingNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            int bookingNumber = getValidIntegerInput(); // Get integer input
            if (bookings.stream().anyMatch(b -> b.number == bookingNumber)) { // Check if booking exists
                return bookingNumber; // Return valid booking number
            } else {
                System.out.println("Error! Booking number does not exist.");
            }
        }
    }

    // Method to get a valid integer input from the user
    private int getValidIntegerInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine()); // Convert input to integer
            } catch (NumberFormatException e) { // Handle invalid input
                System.out.println("Error! Please enter a valid number.");
            }
        }
    }

    // Method for the main menu loop
    private void menu() {
        String selection;
        do {
            // Display the main menu options
            System.out.println("System Menu\n"
                             + "\ta. View All Bookings\n"
                             + "\tb. Book a Ride\n"
                             + "\tc. Delete a Booking\n"
                             + "\td. Generate a Booking Report\n"
                             + "\te. Exit Application\n");

            selection = getValidMenuSelection("Enter selection: "); // Get user selection
            // Execute corresponding action based on user selection
            switch (selection) {
                case "a": viewAllBookings(); break; // View all bookings
                case "b": bookARide(); break; // Book a new ride
                case "c": deleteBooking(); break; // Delete an existing booking
                case "d": generateBookingReport(); break; // Generate and print a booking report
                case "e": System.out.println("Thank you!"); return; // Exit the application
            }
        } while (true);
    }

    // Method to validate menu selection
    private String getValidMenuSelection(String prompt) {
        String selection;
        do {
            System.out.print(prompt);
            selection = scanner.nextLine().toLowerCase(); // Convert input to lowercase
            if ("abcde".contains(selection)) return selection; // Validate input
            System.out.println("Error! Invalid choice. Try again.");
        } while (true);
    }

    public static void main(String[] args) {
        new Main().menu();
    }
}
