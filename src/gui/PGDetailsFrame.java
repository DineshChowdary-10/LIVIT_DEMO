package gui;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.PG;
import model.Student;

public class PGDetailsFrame extends JFrame {

    private PG pg;
    private Student student;

    public PGDetailsFrame(PG pg, Student student) {

        this.pg = pg;
        this.student = student;

        setTitle("LIVIT - PG Details");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel();

        detailsPanel.setLayout(
                new java.awt.GridLayout(8, 2, 10, 10)
        );

        detailsPanel.add(new JLabel("PG Name:"));
        detailsPanel.add(new JLabel(pg.getName()));

        detailsPanel.add(new JLabel("Location:"));
        detailsPanel.add(new JLabel(pg.getLocation()));

        detailsPanel.add(new JLabel("Monthly Rent:"));
        detailsPanel.add(
                new JLabel(String.valueOf(
                        pg.getMonthlyRent()
                ))
        );

        detailsPanel.add(new JLabel("Sharing Type:"));
        detailsPanel.add(
                new JLabel(pg.getSharingType())
        );

        detailsPanel.add(new JLabel("Available Beds:"));
        detailsPanel.add(
                new JLabel(String.valueOf(
                        pg.getAvailableBeds()
                ))
        );

        detailsPanel.add(new JLabel("Rating:"));
        detailsPanel.add(
                new JLabel(String.valueOf(
                        pg.getRating()
                ))
        );

        detailsPanel.add(new JLabel("Distance:"));
        detailsPanel.add(
                new JLabel(
                        pg.getDistance() + " km"
                )
        );

        detailsPanel.add(new JLabel("Facilities:"));

        Set<String> facilities =
                pg.getFacilities();

        JList<String> facilityList =
                new JList<>(
                        facilities.toArray(
                                new String[0]
                        )
                );

        detailsPanel.add(
                new JScrollPane(facilityList)
        );

        add(
                detailsPanel,
                BorderLayout.CENTER
        );

        JPanel buttonPanel =
                new JPanel();

        JButton bookButton =
                new JButton("Book Now");

        JButton closeButton =
                new JButton("Close");

        buttonPanel.add(bookButton);
        buttonPanel.add(closeButton);

        add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        bookButton.addActionListener(e -> {

            if (pg.getAvailableBeds() <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No vacancy available"
                );

                return;
            }

            new BookingFrame(
                    student,
                    pg
            );

            dispose();
        });

        closeButton.addActionListener(
                e -> dispose()
        );

        setVisible(true);
    }
}