package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import model.Owner;

public class OwnerDAO {

    public Owner addOwner(Owner owner) {

        String sql = """
                INSERT INTO owners
                (name, phone_number, email, password)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setString(1, owner.getName());
            ps.setString(2, owner.getPhoneNumber());
            ps.setString(3, owner.getEmail());
            ps.setString(4, owner.getPassword());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        owner.setOwnerId(rs.getInt(1));
                    }
                }

                return owner;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Owner findByPhone(String phoneNumber) {

        String sql = """
                SELECT owner_id, name, phone_number, email, password
                FROM owners
                WHERE phone_number = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, phoneNumber);

            try (ResultSet rs = ps.executeQuery()) {

                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet cachedRowSet = factory.createCachedRowSet();

                cachedRowSet.populate(rs);

                if (cachedRowSet.next()) {
                    return createOwnerFromRow(cachedRowSet);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Owner findOwnerById(Integer ownerId) {

        String sql = """
                SELECT owner_id, name, phone_number, email, password
                FROM owners
                WHERE owner_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, ownerId);

            try (ResultSet rs = ps.executeQuery()) {

                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet cachedRowSet = factory.createCachedRowSet();

                cachedRowSet.populate(rs);

                if (cachedRowSet.next()) {
                    return createOwnerFromRow(cachedRowSet);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Owner createOwnerFromRow(CachedRowSet rowSet)
            throws Exception {

        Owner owner = new Owner();

        owner.setOwnerId(rowSet.getInt("owner_id"));
        owner.setName(rowSet.getString("name"));
        owner.setPhoneNumber(rowSet.getString("phone_number"));
        owner.setEmail(rowSet.getString("email"));
        owner.setPassword(rowSet.getString("password"));

        return owner;
    }
}