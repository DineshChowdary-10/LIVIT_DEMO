package repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import model.PG;

public class PGDAO {

    public PG addPG(PG pg) {

        String sql = """
                INSERT INTO pgs
                (name, location, owner_id, monthly_rent, available_beds,
                 total_beds, sharing_type, rating, distance, facilities)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setString(1, pg.getName());
            ps.setString(2, pg.getLocation());
            ps.setInt(3, pg.getOwnerId());
            ps.setBigDecimal(4, pg.getMonthlyRent());
            ps.setInt(5, pg.getAvailableBeds());
            ps.setInt(6, pg.getTotalBeds());
            ps.setString(7, pg.getSharingType());
            ps.setBigDecimal(8, pg.getRating());
            ps.setBigDecimal(9, pg.getDistance());
            ps.setString(10, convertFacilitiesToString(pg.getFacilities()));

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        pg.setPgId(rs.getInt(1));
                    }
                }

                return pg;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<PG> getAllPGs() {

        String sql = """
                SELECT pg_id, name, location, owner_id, monthly_rent,
                       available_beds, total_beds, sharing_type,
                       rating, distance, facilities
                FROM pgs
                """;

        List<PG> pgs = new ArrayList<>();

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = factory.createCachedRowSet();

            cachedRowSet.populate(rs);

            while (cachedRowSet.next()) {
                pgs.add(createPGFromRow(cachedRowSet));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pgs;
    }

    public PG findPGById(Integer pgId) {

        String sql = """
                SELECT pg_id, name, location, owner_id, monthly_rent,
                       available_beds, total_beds, sharing_type,
                       rating, distance, facilities
                FROM pgs
                WHERE pg_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, pgId);

            try (ResultSet rs = ps.executeQuery()) {

                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet cachedRowSet = factory.createCachedRowSet();

                cachedRowSet.populate(rs);

                if (cachedRowSet.next()) {
                    return createPGFromRow(cachedRowSet);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<PG> findPGsByOwner(Integer ownerId) {

        String sql = """
                SELECT pg_id, name, location, owner_id, monthly_rent,
                       available_beds, total_beds, sharing_type,
                       rating, distance, facilities
                FROM pgs
                WHERE owner_id = ?
                """;

        List<PG> pgs = new ArrayList<>();

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
                    pgs.add(createPGFromRow(cachedRowSet));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pgs;
    }

    public boolean updateAvailableBeds(Integer pgId, Integer availableBeds) {

        String sql = """
                UPDATE pgs
                SET available_beds = ?
                WHERE pg_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, availableBeds);
            ps.setInt(2, pgId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private PG createPGFromRow(CachedRowSet rowSet)
            throws Exception {

        PG pg = new PG();

        pg.setPgId(rowSet.getInt("pg_id"));
        pg.setName(rowSet.getString("name"));
        pg.setLocation(rowSet.getString("location"));
        pg.setOwnerId(rowSet.getInt("owner_id"));
        pg.setMonthlyRent(rowSet.getBigDecimal("monthly_rent"));
        pg.setAvailableBeds(rowSet.getInt("available_beds"));
        pg.setTotalBeds(rowSet.getInt("total_beds"));
        pg.setSharingType(rowSet.getString("sharing_type"));
        pg.setRating(rowSet.getBigDecimal("rating"));
        pg.setDistance(rowSet.getBigDecimal("distance"));

        String facilitiesString = rowSet.getString("facilities");

        pg.setFacilities(
                convertStringToFacilities(facilitiesString)
        );

        return pg;
    }

    private String convertFacilitiesToString(HashSet<String> facilities) {

        if (facilities == null || facilities.isEmpty()) {
            return "";
        }

        return String.join(",", facilities);
    }

    private HashSet<String> convertStringToFacilities(
            String facilitiesString) {

        HashSet<String> facilities = new HashSet<>();

        if (facilitiesString == null ||
            facilitiesString.trim().isEmpty()) {

            return facilities;
        }

        String[] values = facilitiesString.split(",");

        for (String value : values) {
            facilities.add(value.trim());
        }

        return facilities;
    }
}