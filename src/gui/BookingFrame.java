package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.Booking;
import model.PG;
import model.Student;
import service.BookingService;

public class BookingFrame extends JFrame {

    private Student student;
    private PG pg;

    private BookingService bookingService;

    public BookingFrame(Student student, PG pg) {

        this.student = student;
        this.pg = pg;

        bookingService =
                new BookingService();

        setTitle("LIVIT - Booking");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel =
                new JLabel("Booking Confirmation");

        titleLabel.setBounds(
                140,
                20,
                200,
                30
        );

        JLabel studentLabel =
                new JLabel(
                        "Student: "
                                + student.getName()
                );

        studentLabel.setBounds(
                60,
                80,
                300,
                25
        );

        JLabel pgLabel =
                new JLabel(
                        "PG: "
                                + pg.getName()
                );

        pgLabel.setBounds(
                60,
                115,
                300,
                25
        );

        JLabel rentLabel =
                new JLabel(
                        "Rent: ₹"
                                + pg.getMonthlyRent()
                );

        rentLabel.setBounds(
                60,
                150,
                300,
                25
        );

        JButton bookButton =
                new JButton("Send Booking Request");

        bookButton.setBounds(
                70,
                200,
                180,
                30
        );

        JButton cancelButton =
                new JButton("Cancel");

        cancelButton.setBounds(
                270,
                200,
                100,
                30
        );

        add(titleLabel);
        add(studentLabel);
        add(pgLabel);
        add(rentLabel);
        add(bookButton);
        add(cancelButton);

        bookButton.addActionListener(
                e -> createBooking()
        );

        cancelButton.addActionListener(
                e -> dispose()
        );

        setVisible(true);
    }

    private void createBooking() {

        try {

            Booking booking =
                    bookingService.createBooking(
                            student.getStudentId(),
                            pg.getPgId()
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Booking request sent!\nStatus: "
                            + booking.getStatus()
            );

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }
}