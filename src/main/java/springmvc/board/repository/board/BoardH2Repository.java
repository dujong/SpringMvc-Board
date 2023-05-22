package springmvc.board.repository.board;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import springmvc.board.domain.Board;
import springmvc.board.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardH2Repository implements BoardRepository{
    private final DataSource dataSource;

    @Override
    public Board save(Board board, String userId) throws SQLException {
        String sql = "insert into board_table(board_id, user_id, board_name, board_content, time, hit) values (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, board.getBoard_id());
            pstmt.setString(2, userId);
            pstmt.setString(3, board.getBoard_name());
            pstmt.setString(4, board.getBoard_content());
            pstmt.setTimestamp(5, board.getTime());
            pstmt.setInt(6, board.getHit());
            pstmt.executeUpdate();
            return board;
        }
        catch (SQLException e) {
            log.info("user create error", e);
            throw e;
        }
        finally {
            close(con, pstmt, null);
        }

    }


    @Override
    public Board findById(Long board_id) throws SQLException {

        String sql = "select * from board_table where board_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, board_id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Board board = new Board(
                        rs.getLong("board_id"),
                        rs.getString("board_name"),
                        rs.getString("board_content"),
                        rs.getTimestamp("time"),
                        rs.getInt("hit")
                );
                return board;
            }
            else {
                throw new NoSuchElementException("member not found board_id=" + board_id);
            }

        }
        catch (SQLException e) {
            log.info("user create error", e);
            throw e;
        }
        finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void update(Board board, String userId) throws SQLException {
        String sql = "update board_table set board_name=?, board_content=?, time=? where user_id=? AND board_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, board.getBoard_name());
            pstmt.setString(2, board.getBoard_content());
            pstmt.setTimestamp(3, board.getTime());
            pstmt.setString(4, userId);
            pstmt.setLong(5, board.getBoard_id());
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


    @Override
    public void delete(Long board_id, String userId) throws SQLException {
        String sql = "delete from board_table where user_id=? AND board_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setLong(2, board_id);
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

    public Long sequence() throws SQLException {
        String sql = "select * from board_table";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            int size=0;
            while (rs.next()) {
                size++;
            }
            return new Long(++size);

        }
        catch (SQLException e) {
            log.info("user create error", e);
            throw e;
        }
        finally {
            close(con, pstmt, null);
        }
    }



    public void clear() throws SQLException {
        String sql = "delete from board_table";
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
