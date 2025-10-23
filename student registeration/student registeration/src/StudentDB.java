import java.sql.*;

public class StudentDB {
    private Connection con;

    public StudentDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studentdb",
                "root",
                "Garima@2021"  // 🔸 replace with your MySQL password
            );
            System.out.println("✅ Database connected successfully!");
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    public boolean insertStudent(String name, int age, String gender, String email, String phone) {
        try {
            String sql = "INSERT INTO students (name, age, gender, email, phone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, gender);
            pst.setString(4, email);
            pst.setString(5, phone);
            pst.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Email already exists.");
            return false;
        } catch (SQLException e) {
            System.out.println("❌ Insert failed: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("❌ Error closing connection: " + e.getMessage());
        }
    }
}
