package springmvc.board.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import springmvc.board.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserH2Repository implements UserRepository {
    private final DataSource dataSource;

    @SneakyThrows
    @Override
    public User save(User user) {
        String sql = "insert into member_table(member_id, password) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUser_id());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
            return user;
        }
        catch (SQLException e) {
            log.info("user create error", e);
            throw e;
        }
        finally {
            close(con, pstmt, null);
        }
    }
    @SneakyThrows
    @Override
    public User findById(String userId) {
        String sql = "select * from member_table where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getString("member_id"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            else {
                throw new NoSuchElementException("member not found userId=" + userId);
            }

        }
        catch (SQLException e) {
            log.info("user select error", e);
            throw e;
        }
        finally {
            close(con, pstmt, rs);
        }
    }

    @SneakyThrows
    @Override
    public List<User> findAll() {
        String sql = "select * from member_table";
        List<User> user = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                user.add(new User(rs.getString("member_id"), rs.getString("password")));
            }
            return user;

        }
        catch (SQLException e) {
            log.info("user find all error", e);
            throw e;
        }
        finally {
            close(con, pstmt, rs);
        }
    }

    @SneakyThrows
    @Override
    public void update(String userId, String memberPassword) {
        String sql = "update member_table set password=? where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberPassword);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            log.info("update error", e);
            throw e;
        }
        finally {
            close(con, pstmt, null);
        }

    }
    @SneakyThrows
    @Override
    public void delete(String userId) {
        String sql = "delete from member_table where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            log.info("delete error", e);
            throw e;
        }
        finally {
            close(con, pstmt, null);
        }
    }

    @SneakyThrows
    public void clear() {
        String sql = "delete from member_table";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            log.info("delete all error", e);
            throw e;
        }
        finally {
            close(con, pstmt, null);
        }
    }
    private Connection getConnection() {
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
    private void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        DataSourceUtils.releaseConnection(con, dataSource);
    }
}
