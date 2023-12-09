package v1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 이번에는 인터페이스를 도입하자, 인터페이스는 느슨한 결합을 제공하는 필수 요소이다.
 * 상세한 구현을 몰라도 동작할 수 있다.
 */
public class UserDaoV5 {

    /**
     * 인터페이스를 통해 객체에 접근하므로 구체적인 클래스를 몰라도 된다.
     */
    private ConnectionMaker connectionMaker;

    /**
     * 그런데 여전히 구체적인 클래스 이름이 나타난다.
     * 이는 완전히 새로운 관심사이다.
     * UserDao에서는 1. DB연결, 2. SQL생성 및 실행 / 이렇게 크게 두가지의 관심사로 나눌 수 있었는데
     * 이제는 어떤 ConnectionMaker 구현 클래스를 사용할지 결정하는 관심사가 추가되었다.
     * 이 관심사를 담은 코드를 UserDao에서 분리해야만 한다. -> 성공하면 UserDao는 독립적(외부의 변경사항과 관계없이)으로 확장가능한 코드가 된다.
     */
    public UserDaoV5() {
        connectionMaker = new DConnectionMaker();
    }

    interface ConnectionMaker {
        Connection makeConnection() throws ClassNotFoundException, SQLException;
    }

    class DConnectionMaker implements ConnectionMaker {

        @Override
        public Connection makeConnection() throws ClassNotFoundException, SQLException {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(
                    "jdbc:h2:tcp://localhost/~/h2/toby", "sa", "");
        }
    }

    /**
     * 인터페이스의 메서드 이름을 사용하므로 클래스가 변경돼도 메서드 이름은 동일하다.
     */
    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        // ...생략
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        // ...생략

        return null;
    }
}
