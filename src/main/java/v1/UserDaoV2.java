package v1;

import java.sql.*;

/**
 * 먼저 Dao의 관심사항을 세가지로 분리해보자.
 * 1. DB와 연결을 위한 커넥션을 가져오는 관심 -> getConnection() 메소드로 분리 -> 팩토리 메서드 패턴
 * 2. 사용할 Statement를 만들고 실행하는 관심
 * 3. 작업이 끝나면 사용한 리소스를 돌려주는 관심
 *
 * 1번 관심사를 분리하였지만, 기능에는 전혀 영향을 주지 않았다.
 *
 * 그런데 만약 N, D사에 UserDao를 판매하게 되었을때, 각각의 회사에서 다른 DB를 사용한다면?
 * 소스코드를 공개하고 싶지 않다면 어떡할까?
 */
public class UserDaoV2 {
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

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost/~/h2/toby", "sa", "");
        return c;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDaoV2 userDao = new UserDaoV2();

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
