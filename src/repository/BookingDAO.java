package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import model.Booking;

public class BookingDAO {

    public Booking addBooking(Booking booking) {

        String sql = """
                INSERT INTO bookings
                (student_id, pg_id, booking_date, status)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setInt(1, booking.getStudentId());
            ps.setInt(2, booking.getPgId());

            if (booking.getBookingDate() != null) {
                ps.setTimestamp(
                        3,
                        java.sql.Timestamp.valueOf(
                                booking.getBookingDate()
                        )
                );
            } else {
                ps.setTimestamp(
                        3,
                        java.sql.Timestamp.valueOf(
                                LocalDateTime.now()
                        )
                );
            }

            ps.setString(4, booking.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        booking.setBookingId(rs.getInt(1));
                    }
                }

                return booking;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Booking> getStudentBookings(Integer studentId) {

        String sql = """
                SELECT booking_id, student_id, pg_id,
                       booking_date, status
                FROM bookings
                WHERE student_id = ?
                ORDER BY booking_date DESC
                """;

        List<Booking> bookings = new ArrayList<>();

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet cachedRowSet = factory.createCachedRowSet();

                cachedRowSet.populate(rs);

                while (cachedRowSet.next()) {
                    bookings.add(createBookingFromRow(cachedRowSet));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }

    public List<Booking> getOwnerBookings(Integer ownerId) {

        String sql = """
                SELECT b.booking_id, b.student_id, b.pg_id,
                       b.booking_date, b.status
                FROM bookings b
                INNER JOIN pgs p
                    ON b.pg_id = p.pg_id
                WHERE p.owner_id = ?
                ORDER BY b.booking_date DESC
                """;

        List<Booking> bookings = new ArrayList<>();

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, ownerId);

            try (ResultSet rs = ps.executeQuery()) {

                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet cachedRowSet = factory.createCachedRowSet();

                cachedRowSet.populate(rs);

                while (cachedRowSet.next()) {
                    bookings.add(createBookingFromRow(cachedRowSet));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }

    public Booking findBookingById(Integer bookingId) {

        String sql = """
                SELECT booking_id, student_id, pg_id,
                       booking_date, status
                FROM bookings
                WHERE booking_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet cachedRowSet = factory.createCachedRowSet();

                cachedRowSet.populate(rs);

                if (cachedRowSet.next()) {
                    return createBookingFromRow(cachedRowSet);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateBookingStatus(Integer bookingId, String status) {

        String sql = """
                UPDATE bookings
                SET status = ?
                WHERE booking_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, status);
            ps.setInt(2, bookingId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private Booking createBookingFromRow(CachedRowSet rowSet)
            throws Exception {

        Booking booking = new Booking();

        booking.setBookingId(
                rowSet.getInt("booking_id")
        );

        booking.setStudentId(
                rowSet.getInt("student_id")
        );

        booking.setPgId(
                rowSet.getInt("pg_id")
        );

        java.sql.Timestamp timestamp =
                rowSet.getTimestamp("booking_date");

        if (timestamp != null) {
            booking.setBookingDate(
                    timestamp.toLocalDateTime()
            );
        }

        booking.setStatus(
                rowSet.getString("status")
        );

        return booking;
    }
}