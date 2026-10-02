package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.PG;
import model.Student;
import service.PGService;

public class StudentDashboard extends JFrame {

    private Student student;
    private PGService pgService;

    private JTextField locationField;
    private JTextField maxRentField;

    private JComboBox<String> sharingComboBox;
    private JComboBox<String> sortComboBox;

    private JCheckBox availableCheckBox;

    private JTable pgTable;
    private DefaultTableModel tableModel;

    public StudentDashboard(Student student) {

        this.student = student;
        pgService = new PGService();

        setTitle("LIVIT - Student Dashboard");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        locationField = new JTextField(12);
        maxRentField = new JTextField(8);

        sharingComboBox = new JComboBox<>(
                new String[]{
                        "Any",
                        "2 Sharing",
                        "3 Sharing",
                        "4 Sharing"
                }
        );

        sortComboBox = new JComboBox<>(
                new String[]{
                        "None",
                        "Lowest Rent",
                        "Highest Rating",
                        "Closest Distance",
                        "Most Available Beds"
                }
        );

        availableCheckBox =
                new JCheckBox("Available Only");

        JButton searchButton =
                new JButton("Search");

        JButton resetButton =
                new JButton("Reset");

        JButton detailsButton =
                new JButton("View Details");

        JButton logoutButton =
                new JButton("Logout");

        filterPanel.add(new JLabel("Welcome "
                + student.getName()));

        filterPanel.add(new JLabel("Location:"));
        filterPanel.add(locationField);

        filterPanel.add(new JLabel("Max Rent:"));
        filterPanel.add(maxRentField);

        filterPanel.add(new JLabel("Sharing:"));
        filterPanel.add(sharingComboBox);

        filterPanel.add(availableCheckBox);

        filterPanel.add(new JLabel("Sort:"));
        filterPanel.add(sortComboBox);

        filterPanel.add(searchButton);
        filterPanel.add(resetButton);
        filterPanel.add(detailsButton);
        filterPanel.add(logoutButton);

        add(filterPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "PG Name",
                        "Location",
                        "Rent",
                        "Sharing",
                        "Available Beds",
                        "Rating",
                        "Distance"
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

        pgTable = new JTable(tableModel);

        add(
                new JScrollPane(pgTable),
                BorderLayout.CENTER
        );

        searchButton.addActionListener(
                e -> loadPGs()
        );

        resetButton.addActionListener(e -> {

            locationField.setText("");
            maxRentField.setText("");
            sharingComboBox.setSelectedIndex(0);
            sortComboBox.setSelectedIndex(0);
            availableCheckBox.setSelected(false);

            loadPGs();
        });

        detailsButton.addActionListener(
                e -> openPGDetails()
        );

        logoutButton.addActionListener(e -> {

            new LoginFrame();
            dispose();
        });

        loadPGs();

        setVisible(true);
    }

    private void loadPGs() {

        try {

            List<PG> results =
                    new ArrayList<>(
                            pgService.getAllPGs()
                    );

            String location =
                    locationField.getText().trim();

            if (!location.isEmpty()) {

                results.clear();

                for (PG pg : pgService.getAllPGs()) {

                    if (pg.getLocation() != null &&
                        pg.getLocation()
                          .toLowerCase()
                          .contains(location.toLowerCase())) {

                        results.add(pg);
                    }
                }
            }

            String maxRentText =
                    maxRentField.getText().trim();

            BigDecimal maxRent = null;

            if (!maxRentText.isEmpty()) {
                maxRent = new BigDecimal(maxRentText);
            }

            String sharing =
                    (String) sharingComboBox.getSelectedItem();

            List<PG> filtered =
                    new ArrayList<>();

            for (PG pg : results) {

                boolean matches = true;

                if (maxRent != null &&
                    pg.getMonthlyRent()
                      .compareTo(maxRent) > 0) {

                    matches = false;
                }

                if (!"Any".equalsIgnoreCase(sharing) &&
                    !pg.getSharingType()
                      .equalsIgnoreCase(sharing)) {

                    matches = false;
                }

                if (availableCheckBox.isSelected() &&
                    pg.getAvailableBeds() <= 0) {

                    matches = false;
                }

                if (matches) {
                    filtered.add(pg);
                }
            }

            String sortOption =
                    (String) sortComboBox.getSelectedItem();

            switch (sortOption) {

                case "Lowest Rent":

                    filtered.sort(
                            Comparator.comparing(
                                    PG::getMonthlyRent
                            )
                    );
                    break;

                case "Highest Rating":

                    filtered.sort(
                            Comparator.comparing(
                                    PG::getRating
                            ).reversed()
                    );
                    break;

                case "Closest Distance":

                    filtered.sort(
                            Comparator.comparing(
                                    PG::getDistance
                            )
                    );
                    break;

                case "Most Available Beds":

                    filtered.sort(
                            Comparator.comparing(
                                    PG::getAvailableBeds
                            ).reversed()
                    );
                    break;

                default:
                    break;
            }

            displayPGs(filtered);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid filter value: "
                            + e.getMessage()
            );
        }
    }

    private void displayPGs(List<PG> pgs) {

        tableModel.setRowCount(0);

        for (PG pg : pgs) {

            tableModel.addRow(
                    new Object[]{
                            pg.getPgId(),
                            pg.getName(),
                            pg.getLocation(),
                            pg.getMonthlyRent(),
                            pg.getSharingType(),
                            pg.getAvailableBeds(),
                            pg.getRating(),
                            pg.getDistance()
                    }
            );
        }
    }

    private void openPGDetails() {

        int selectedRow =
                pgTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a PG first"
            );

            return;
        }

        Integer pgId =
                (Integer) tableModel.getValueAt(
                        selectedRow,
                        0
                );

        PG pg = pgService.findPGById(pgId);

        if (pg != null) {
            new PGDetailsFrame(pg, student);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "PG not found"
            );
        }
    }
}