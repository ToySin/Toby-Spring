package v1;

import java.sql.*;

/**
 * 이번에는 상속을 사용하지 않고, 다른 클래스로 아예 분리해서 만들어보자
 * 성격이 다른 코드를 아예 다른 클래스로 잘 분리하였지만, N, D사의 요구사항을 적용할 수 없다.
 * 소스코드를 제공해야만 하는 상황으로 돌아와버렸다.
 *
 * 1. makeNewConnection() 메서드가 아니라 openConnection() 메서드를 사용하는 회사가 있다면?
 * 2. UserDao가 SimpleConnectionMaker를 구체적으로 알고 있어야한다. 만약 다른 회사에서 다른 클래스를 구현하면 UserDao가 변해야한다.
 * -> 오히려 상속보다 못한 방법처럼 보이기도 한다!
 */
public class UserDaoV4 {

    private SimpleConnectionMaker simpleConnectionMaker;

    public UserDaoV4() {
        simpleConnectionMaker = new SimpleConnectionMaker();
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = simpleConnectionMaker.makeNewConnection();

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
        Connection c = simpleConnectionMaker.makeNewConnection();

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
        UserDaoV4 userDao = new UserDaoV4();

        User user = new User();
        user.setId("donshin");
        user.setName("신동빈");
        user.setPassword("1234");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공!");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공!");
    }
}

class SimpleConnectionMaker {
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost/~/h2/toby", "sa", "");
        return c;
    }
}