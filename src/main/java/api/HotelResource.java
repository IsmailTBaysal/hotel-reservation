package api;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    public Customer getCustomer(String email){

    }
    public void createAcustomer(String email, String firstName, String lastName){

    }
    public IRoom getRoom(String roomNumber){

    }
    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){

    }
    public Collection<Reservation> getCustomersReservations(String customerEmail){

    }
    public Collection<IRoom> findARoom(Date checkIn, Date checkOut){

    }
}
