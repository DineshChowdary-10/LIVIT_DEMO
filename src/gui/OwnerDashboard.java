package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Booking;
import model.Owner;
import model.PG;
import service.BookingService;
import service.PGService;

public class OwnerDashboard extends JFrame {

    private Owner owner;

    private PGService pgService;
    private BookingService bookingService;

    private JTable pgTable;
    private JTable bookingTable;

    private DefaultTableModel pgTableModel;
    private DefaultTableModel bookingTableModel;

    public OwnerDashboard(Owner owner) {

        this.owner = owner;

        pgService = new PGService();
        bookingService = new BookingService();

        setTitle("LIVIT - Owner Dashboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        topPanel.add(
                new JLabel(
                        "Welcome "
                                + owner.getName()
                )
        );

        JButton addPGButton =
                new JButton("Add PG");

        JButton refreshButton =
                new JButton("Refresh");

        JButton acceptButton =
                new JButton("Accept");

        JButton rejectButton =
                new JButton("Reject");

        JButton logoutButton =
                new JButton("Logout");

        topPanel.add(addPGButton);
        topPanel.add(refreshButton);
        topPanel.add(acceptButton);
        topPanel.add(rejectButton);
        topPanel.add(logoutButton);

        add(
                topPanel,
                BorderLayout.NORTH
        );

        pgTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "PG Name",
                                "Location",
                                "Rent",
                                "Sharing",
                                "Available Beds",
                                "Total Beds"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        pgTable = new JTable(pgTableModel);

        bookingTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Booking ID",
                                "Student ID",
                                "PG ID",
                                "Date",
                                "Status"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        bookingTable =
                new JTable(bookingTableModel);

        JPanel centerPanel =
                new JPanel(
                        new java.awt.GridLayout(
                                2,
                                1
                        )
                );

        centerPanel.add(
                new JScrollPane(pgTable)
        );

        centerPanel.add(
                new JScrollPane(bookingTable)
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        addPGButton.addActionListener(
                e -> showAddPGDialog()
        );

        refreshButton.addActionListener(
                e -> loadData()
        );

        acceptButton.addActionListener(
                e -> updateBooking(true)
        );

        rejectButton.addActionListener(
                e -> updateBooking(false)
        );

        logoutButton.addActionListener(
                e -> {
                    new LoginFrame();
                    dispose();
                }
        );

        loadData();

        setVisible(true);
    }

    private void loadData() {

        pgTableModel.setRowCount(0);
        bookingTableModel.setRowCount(0);

        List<PG> pgs =
                pgService.findPGsByOwner(
                        owner.getOwnerId()
                );

        for (PG pg : pgs) {

            pgTableModel.addRow(
                    new Object[]{
                            pg.getPgId(),
                            pg.getName(),
                            pg.getLocation(),
                            pg.getMonthlyRent(),
                            pg.getSharingType(),
                            pg.getAvailableBeds(),
                            pg.getTotalBeds()
                    }
            );
        }

        List<Booking> bookings =
                bookingService.getOwnerBookings(
                        owner.getOwnerId()
                );

        for (Booking booking : bookings) {

            bookingTableModel.addRow(
                    new Object[]{
                            booking.getBookingId(),
                            booking.getStudentId(),
                            booking.getPgId(),
                            booking.getBookingDate(),
                            booking.getStatus()
                    }
            );
        }
    }

    private void showAddPGDialog() {

        JTextField nameField =
                new JTextField();

        JTextField locationField =
                new JTextField();

        JTextField rentField =
                new JTextField();

        JTextField availableBedsField =
                new JTextField();

        JTextField totalBedsField =
                new JTextField();

        JTextField sharingField =
                new JTextField();

        JTextField ratingField =
                new JTextField("0.0");

        JTextField distanceField =
                new JTextField("0.0");

        JTextField facilitiesField =
                new JTextField();

        Object[] fields = {

                "PG Name:",
                nameField,

                "Location:",
                locationField,

                "Monthly Rent:",
                rentField,

                "Available Beds:",
                availableBedsField,

                "Total Beds:",
                totalBedsField,

                "Sharing Type:",
                sharingField,

                "Rating:",
                ratingField,

                "Distance:",
                distanceField,

                "Facilities (comma separated):",
                facilitiesField
        };

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        fields,
                        "Add PG",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            HashSet<String> facilities =
                    new HashSet<>();

            String[] facilityValues =
                    facilitiesField
                            .getText()
                            .split(",");

            for (String value : facilityValues) {

                if (!value.trim().isEmpty()) {
                    facilities.add(
                            value.trim()
                    );
                }
            }

            PG pg = new PG(
                    null,
                    nameField.getText().trim(),
                    locationField.getText().trim(),
                    owner.getOwnerId(),
                    new BigDecimal(
                            rentField.getText().trim()
                    ),
                    Integer.valueOf(
                            availableBedsField
                                    .getText()
                                    .trim()
                    ),
                    Integer.valueOf(
                            totalBedsField
                                    .getText()
                                    .trim()
                    ),
                    sharingField.getText().trim(),
                    new BigDecimal(
                            ratingField.getText().trim()
                    ),
                    new BigDecimal(
                            distanceField.getText().trim()
                    ),
                    facilities
            );

            PG savedPG =
                    pgService.addPG(pg);

            if (savedPG != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "PG added successfully!\nPG ID: "
                                + savedPG.getPgId()
                );

                loadData();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to add PG: "
                            + e.getMessage()
            );
        }
    }

    private void updateBooking(
            boolean accept) {

        int selectedRow =
                bookingTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a booking first"
            );

            return;
        }

        Integer bookingId =
                (Integer) bookingTableModel
                        .getValueAt(
                                selectedRow,
                                0
                        );

        try {

            boolean success;

            if (accept) {

                success =
                        bookingService.acceptBooking(
                                bookingId,
                                owner.getOwnerId()
                        );

            } else {

                success =
                        bookingService.rejectBooking(
                                bookingId,
                                owner.getOwnerId()
                        );
            }

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        accept
                                ? "Booking accepted"
                                : "Booking rejected"
                );

                loadData();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Operation failed"
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }
}