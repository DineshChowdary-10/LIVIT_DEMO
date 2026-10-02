package service;

import java.time.LocalDateTime;
import java.util.List;

import model.Booking;
import model.PG;
import repository.BookingDAO;
import repository.DataStore;
import repository.PGDAO;

public class BookingService {

    private BookingDAO bookingDAO = new BookingDAO();
    private PGDAO pgDAO = new PGDAO();

    public Booking createBooking(Integer studentId, Integer pgId) {

        if (studentId == null) {
            throw new IllegalArgumentException(
                    "Student ID cannot be null"
            );
        }

        if (pgId == null) {
            throw new IllegalArgumentException(
                    "PG ID cannot be null"
            );
        }

        if (!DataStore.students.containsKey(studentId)) {
            throw new IllegalArgumentException(
                    "Student not found"
            );
        }

        PG pg = pgDAO.findPGById(pgId);

        if (pg == null) {
            throw new IllegalArgumentException(
                    "PG not found"
            );
        }

        if (pg.getAvailableBeds() == null ||
            pg.getAvailableBeds() <= 0) {

            throw new IllegalArgumentException(
                    "No vacancy available"
            );
        }

        Booking booking = new Booking(
                null,
                studentId,
                pgId,
                LocalDateTime.now(),
                "PENDING"
        );

        Booking savedBooking =
                bookingDAO.addBooking(booking);

        if (savedBooking == null) {
            throw new IllegalArgumentException(
                    "Booking creation failed"
            );
        }

        DataStore.bookingQueue.add(savedBooking);

        return savedBooking;
    }

    public List<Booking> getStudentBookings(Integer studentId) {

        if (studentId == null) {
            throw new IllegalArgumentException(
                    "Student ID cannot be null"
            );
        }

        return bookingDAO.getStudentBookings(studentId);
    }

    public List<Booking> getOwnerBookings(Integer ownerId) {

        if (ownerId == null) {
            throw new IllegalArgumentException(
                    "Owner ID cannot be null"
            );
        }

        return bookingDAO.getOwnerBookings(ownerId);
    }

    public Booking findBookingById(Integer bookingId) {

        if (bookingId == null) {
            throw new IllegalArgumentException(
                    "Booking ID cannot be null"
            );
        }

        return bookingDAO.findBookingById(bookingId);
    }

    public boolean acceptBooking(Integer bookingId,
                                 Integer ownerId) {

        if (bookingId == null) {
            throw new IllegalArgumentException(
                    "Booking ID cannot be null"
            );
        }

        if (ownerId == null) {
            throw new IllegalArgumentException(
                    "Owner ID cannot be null"
            );
        }

        Booking booking =
                bookingDAO.findBookingById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException(
                    "Booking not found"
            );
        }

        if (!"PENDING".equalsIgnoreCase(
                booking.getStatus())) {

            throw new IllegalArgumentException(
                    "Only pending bookings can be accepted"
            );
        }

        PG pg = pgDAO.findPGById(booking.getPgId());

        if (pg == null) {
            throw new IllegalArgumentException(
                    "PG not found"
            );
        }

        if (!pg.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException(
                    "You are not the owner of this PG"
            );
        }

        if (pg.getAvailableBeds() == null ||
            pg.getAvailableBeds() <= 0) {

            throw new IllegalArgumentException(
                    "No vacancy available"
            );
        }

        Integer newAvailableBeds =
                pg.getAvailableBeds() - 1;

        boolean vacancyUpdated =
                pgDAO.updateAvailableBeds(
                        pg.getPgId(),
                        newAvailableBeds
                );

        if (!vacancyUpdated) {
            return false;
        }

        boolean statusUpdated =
                bookingDAO.updateBookingStatus(
                        bookingId,
                        "ACCEPTED"
                );

        if (!statusUpdated) {
            return false;
        }

        pg.setAvailableBeds(newAvailableBeds);
        booking.setStatus("ACCEPTED");

        return true;
    }

    public boolean rejectBooking(Integer bookingId,
                                 Integer ownerId) {

        if (bookingId == null) {
            throw new IllegalArgumentException(
                    "Booking ID cannot be null"
            );
        }

        if (ownerId == null) {
            throw new IllegalArgumentException(
                    "Owner ID cannot be null"
            );
        }

        Booking booking =
                bookingDAO.findBookingById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException(
                    "Booking not found"
            );
        }

        if (!"PENDING".equalsIgnoreCase(
                booking.getStatus())) {

            throw new IllegalArgumentException(
                    "Only pending bookings can be rejected"
            );
        }

        PG pg = pgDAO.findPGById(booking.getPgId());

        if (pg == null) {
            throw new IllegalArgumentException(
                    "PG not found"
            );
        }

        if (!pg.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException(
                    "You are not the owner of this PG"
            );
        }

        return bookingDAO.updateBookingStatus(
                bookingId,
                "REJECTED"
        );
    }
}