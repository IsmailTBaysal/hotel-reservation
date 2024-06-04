import api.AdminResource;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.Scanner;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        String line;
        do {
            System.out.println("\nAdmin Menu");
            System.out.println("------------------------------------------------");
            System.out.println("1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Back to Main Menu");
            System.out.println("------------------------------------------------");
            line = scanner.nextLine();
            switch (line) {
                case "1":
                    seeAllCustomers();
                    break;
                case "2":
                    seeAllRooms();
                    break;
                case "3":
                    seeAllReservations();
                    break;
                case "4":
                    addRooms(scanner);
                    break;
                case "5":
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while (!line.equals("5"));
    }

    private static void seeAllCustomers() {
        adminResource.getAllCustomers().forEach(System.out::println);
    }

    private static void seeAllRooms() {
        adminResource.getAllRooms().forEach(System.out::println);
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addRooms(Scanner scanner) {
        String addAnotherRoom;
        do {
            System.out.println("Enter room number:");
            String roomNumber = scanner.nextLine();
            System.out.println("Enter room price:");
            Double price = Double.valueOf(scanner.nextLine());
            System.out.println("Enter room type (1 for single, 2 for double):");
            RoomType roomType = scanner.nextLine().equals("1") ? RoomType.SINGLE : RoomType.DOUBLE;
            IRoom room = new Room(roomNumber, price, roomType);
            try {
                adminResource.addRoom(room);
                System.out.println("Room added successfully!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

            do {
                System.out.println("Would you like to add another room? (y/n)");
                addAnotherRoom = scanner.nextLine();
                if (!addAnotherRoom.equalsIgnoreCase("y") && !addAnotherRoom.equalsIgnoreCase("n")) {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                }
            } while (!addAnotherRoom.equalsIgnoreCase("y") && !addAnotherRoom.equalsIgnoreCase("n"));

        } while (addAnotherRoom.equalsIgnoreCase("y"));
    }
}
