import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getInstance();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line;
        do {
            System.out.println("\nWelcome to the Hotel Reservation Application\n");
            System.out.println("------------------------------------------------");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            System.out.println("------------------------------------------------");
            line = scanner.nextLine();
            switch (line) {
                case "1":
                    findAndReserveRoom(scanner);
                    break;
                case "2":
                    seeMyReservations(scanner);
                    break;
                case "3":
                    createAccount(scanner);
                    break;
                case "4":
                    AdminMenu.adminMenu();
                    break;
                case "5":
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while (!line.equals("5"));
    }

    private static void findAndReserveRoom(Scanner scanner) {
        try {
            System.out.println("Enter check-in date (MM/dd/yyyy):");
            Date checkInDate = new SimpleDateFormat("MM/dd/yyyy").parse(scanner.nextLine());
            System.out.println("Enter check-out date (MM/dd/yyyy):");
            Date checkOutDate = new SimpleDateFormat("MM/dd/yyyy").parse(scanner.nextLine());
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for the given date range.");
                Collection<IRoom> recommendedRooms = hotelResource.findRecommendedRooms(checkInDate, checkOutDate);
                if (recommendedRooms.isEmpty()) {
                    System.out.println("No recommended rooms available for alternative dates.");
                } else {
                    System.out.println("Recommended rooms for alternative dates:");
                    recommendedRooms.forEach(System.out::println);
                }
            } else {
                availableRooms.forEach(System.out::println);

                String bookRoom;
                do {
                    System.out.println("Would you like to book a room? (y/n)");
                    bookRoom = scanner.nextLine();
                    if (!bookRoom.equalsIgnoreCase("y") && !bookRoom.equalsIgnoreCase("n")) {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    }
                } while (!bookRoom.equalsIgnoreCase("y") && !bookRoom.equalsIgnoreCase("n"));

                if (bookRoom.equalsIgnoreCase("y")) {
                    String hasAccount;
                    do {
                        System.out.println("Do you have an account with us? (y/n)");
                        hasAccount = scanner.nextLine();
                        if (!hasAccount.equalsIgnoreCase("y") && !hasAccount.equalsIgnoreCase("n")) {
                            System.out.println("Invalid input. Please enter 'y' or 'n'.");
                        }
                    } while (!hasAccount.equalsIgnoreCase("y") && !hasAccount.equalsIgnoreCase("n"));

                    if (hasAccount.equalsIgnoreCase("y")) {
                        System.out.println("Enter your email (format: name@domain.com):");
                        String email = scanner.nextLine();
                        if (!email.matches(Customer.EMAIL_PATTERN)) {
                            System.out.println("Invalid email format. Please try again.");
                            return;
                        }
                        Customer customer = hotelResource.getCustomer(email);
                        if (customer == null) {
                            System.out.println("No account found with this email. Please try again.");
                            return;
                        }

                        System.out.println("Enter room number:");
                        String roomNumber = scanner.nextLine();
                        IRoom room = hotelResource.getRoom(roomNumber);
                        if (room == null) {
                            System.out.println("Invalid room number. Please try again.");
                            return;
                        }

                        try {
                            Reservation reservation = hotelResource.bookARoom(email, room, checkInDate, checkOutDate);
                            System.out.println("Reservation confirmed: " + reservation);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Please create an account first.");
                    }
                }
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
        }
    }

    private static void seeMyReservations(Scanner scanner) {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(System.out::println);
        }
    }

    private static void createAccount(Scanner scanner) {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your first name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter your last name:");
        String lastName = scanner.nextLine();
        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully for " + firstName + " " + lastName + "! Your email is " + email);
        } catch (Exception e) {
            System.out.println("An error occurred while creating your account. Please try again later.");
        }
    }
}
