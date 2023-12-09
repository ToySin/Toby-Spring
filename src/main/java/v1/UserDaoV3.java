package v1;

import java.sql.*;

/**
 * 한가지 방법은 추상 메서드로 제공하는 것이다.
 * 커넥션을 만드는 부분만 각자의 회사에서 만들어 쓰면 된다.
 * -> 템플릿 메서드 패턴이다.
 *
 * 상속을 통해서 문제를 해결했지만, 상속을 사용했다는 단점이 되기도 한다.
 * 자바는 다중상속을 지원하지 않는다. 이미 UserDao가 다른 목적을 위해 상속을 사용하고 있다면?
 * 커넥션을 가져오는 방법을 분리하기 위해 상속을 사용했다면 다른 목적을 위해 상속을 사용할 수 없다.
 *
 * 또한 상속을 사용하면 상하위 클래스의 관계가 쫀쫀해진다. 여전히 슈퍼클래스가 변화하면 서브 클래스도 변화가 필요하다.
 * 혹은 슈퍼클래스를 변화시키지 않도록 제약해야한다. -> 개발은 항상 변화가 일어난다.
 *
 * UserDao 외에 다른 Dao들이 나타나면, 모든 Dao에 getConnection() 메서드를 추가해야한다. -> 중복 생성
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