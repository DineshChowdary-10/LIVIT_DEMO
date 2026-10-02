package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import model.Student;

public class StudentDAO {

    public Student addStudent(Student student) {

        String sql = """
                INSERT INTO students
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

            ps.setString(1, student.getName());
            ps.setString(2, student.getPhoneNumber());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPassword());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        student.setStudentId(rs.getInt(1));
                    }
                }

                return student;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Student findByPhone(String phoneNumber) {

        String sql = """
                SELECT student_id, name, phone_number, email, password
                FROM students
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
                    return createStudentFromRow(cachedRowSet);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Student findStudentById(Integer studentId) {

        String sql = """
                SELECT student_id, name, phone_number, email, password
                FROM students
                WHERE student_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet cachedRowSet = factory.createCachedRowSet();

                cachedRowSet.populate(rs);

                if (cachedRowSet.next()) {
                    return createStudentFromRow(cachedRowSet);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Student createStudentFromRow(CachedRowSet rowSet)
            throws Exception {

        Student student = new Student();

        student.setStudentId(rowSet.getInt("student_id"));
        student.setName(rowSet.getString("name"));
        student.setPhoneNumber(rowSet.getString("phone_number"));
        student.setEmail(rowSet.getString("email"));
        student.setPassword(rowSet.getString("password"));

        return student;
    }
}