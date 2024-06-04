package service;

import model.*;

import java.util.*;

public class ReservationService {
    private static ReservationService instance = null;
    private Map<String, IRoom> rooms = new HashMap<>();
    private Map<String, List<Reservation>> reservations = new HashMap<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room number " + room.getRoomNumber() + " already exists.");
        }
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        if (!isRoomAvailable(room, checkInDate, checkOutDate)) {
            throw new IllegalArgumentException("Room is not available for the given dates.");
        }
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        List<Reservation> customerReservations = reservations.getOrDefault(customer.getEmail(), new ArrayList<>());
        customerReservations.add(reservation);
        reservations.put(customer.getEmail(), customerReservations);
        return reservation;
    }

    public Collection<Reservation> getCustomerReservation(Customer customer) {
        return reservations.getOrDefault(customer.getEmail(), Collections.emptyList());
    }

    public Collection<Reservation> getAllReservations() {
        List<Reservation> allReservations = new ArrayList<>();
        for (List<Reservation> reservationList : reservations.values()) {
            allReservations.addAll(reservationList);
        }
        return allReservations;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> availableRooms = new ArrayList<>(rooms.values());
        for (Reservation reservation : getAllReservations()) {
            if ((checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate()))) {
                availableRooms.remove(reservation.getRoom());
            }
        }
        return availableRooms;
    }
    public Collection<IRoom> findRecommendedRooms(Date checkInDate, Date checkOutDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date newCheckInDate = calendar.getTime();
        calendar.setTime(checkOutDate);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date newCheckOutDate = calendar.getTime();
        return findRooms(newCheckInDate, newCheckOutDate);
    }

    private boolean isRoomAvailable(IRoom room, Date checkInDate, Date checkOutDate) {
        for (Reservation reservation : getAllReservations()) {
            if (reservation.getRoom().equals(room) && checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate())) {
                return false;
            }
        }
        return true;
    }
}
