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
            String roomNumber = "";
            while (roomNumber.isEmpty()) {
                System.out.println("Enter room number:");
                roomNumber = scanner.nextLine().trim();
                if (roomNumber.isEmpty()) {
                    System.out.println("Room number cannot be empty. Please enter a valid room number.");
                }
            }

            Double price = null;
            while (price == null) {
                System.out.println("Enter room price:");
                String priceInput = scanner.nextLine();
                try {
                    price = Double.valueOf(priceInput);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number for the room price.");
                }
            }

            RoomType roomType = null;
            while (roomType == null) {
                System.out.println("Enter room type (1 for single, 2 for double):");
                String typeInput = scanner.nextLine();
                if (typeInput.equals("1")) {
                    roomType = RoomType.SINGLE;
                } else if (typeInput.equals("2")) {
                    roomType = RoomType.DOUBLE;
                } else {
                    System.out.println("Invalid input. Please enter '1' for single or '2' for double.");
                }
            }

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
