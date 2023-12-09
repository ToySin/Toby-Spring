package v1;

import java.sql.*;

/**
 * 한가지 방법은 추상 메서드로 제공하는 것이다.
 * 커넥션을 만드는 부분만 각자의 회사에서 만들어 쓰면 된다.
 */
public abstract class UserDaoV3 {
    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO users (id, name, password) VALUES (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM users WHERE id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

}

/**
 * 아래의 코드처럼 각각의 회사에서 UserDao를 상속받아서 getConnection() 메소드를 구현하면 된다.
 */
class NUserDaoV3 extends UserDaoV3 {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // N사 DB Connection 생성코드
        return null;
    }
}

class DUserDaoV3 extends UserDaoV3 {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // D사 DB Connection 생성코드
        return null;
    }
}