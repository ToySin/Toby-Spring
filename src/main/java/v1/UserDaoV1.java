package v1;

import java.sql.*;

/**
 * Database 접속 암호가 변경된다면?
 * 수백개의 Dao를 변경해야될지도 모른다.
 *
 * 관심사의 분리. 관심이 다른 것은 서로 떨어져서 영향을 주지 않도록 한다.
 * 처음에는 한 곳에 넣는게 편하다. 하지만 언젠가는 적절하게 분리하는 작업이 필요
 *
 */
public class UserDaoV1 {
    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection(
            "jdbc:h2:tcp://localhost/~/h2/toby", "sa", "");

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
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost/~/h2/toby", "sa", "");

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

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDaoV1 userDao = new UserDaoV1();

        User user = new User();
        user.setId("donshin");
        user.setName("신동빈");
        user.setPassword("1234");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
