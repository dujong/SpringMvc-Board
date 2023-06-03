//package springmvc.board.repository.member;
//
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.jdbc.datasource.DataSourceUtils;
//import org.springframework.jdbc.support.JdbcUtils;
//import org.springframework.stereotype.Repository;
//import springmvc.board.domain.member.Member;
//
//import javax.sql.DataSource;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@Slf4j
//@Repository
//@RequiredArgsConstructor
//public class MemberH2Repository implements MemberRepository_old {
//    private final DataSource dataSource;
//
//    @SneakyThrows
//    @Override
//    public Member save(Member member) {
//        String sql = "insert into member_table(member_id, password) values (?, ?)";
//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, member.getMember_id());
//            pstmt.setString(2, member.getPassword());
//            pstmt.executeUpdate();
//            return member;
//        }
//        catch (SQLException e) {
//            log.info("member create error", e);
//            throw e;
//        }
//        finally {
//            close(con, pstmt, null);
//        }
//    }
//    @SneakyThrows
//    @Override
//    public Member findById(String member_id) {
//        String sql = "select * from member_table where member_id = ?";
//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, member_id);
//            rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                Member member = new Member();
//                member.setMember_id(rs.getString("member_id"));
//                member.setPassword(rs.getString("password"));
//                return member;
//            }
//            else {
//                throw new NoSuchElementException("member not found memberId=" + member_id);
//            }
//
//        }
//        catch (SQLException e) {
//            log.info("member select error", e);
//            throw e;
//        }
//        finally {
//            close(con, pstmt, rs);
//        }
//    }
//
//    @SneakyThrows
//    @Override
//    public List<Member> findAll() {
//        String sql = "select * from member_table";
//        List<Member> members = new ArrayList<>();
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            rs = pstmt.executeQuery();
//            while(rs.next()){
//                members.add(new Member(rs.getString("member_id"), rs.getString("password")));
//            }
//            return members;
//
//        }
//        catch (SQLException e) {
//            log.info("user find all error", e);
//            throw e;
//        }
//        finally {
//            close(con, pstmt, rs);
//        }
//    }
//
//    @SneakyThrows
//    @Override
//    public void update(String member_id, String memberPassword) {
//        String sql = "update member_table set password=? where member_id = ?";
//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, memberPassword);
//            pstmt.setString(2, member_id);
//            pstmt.executeUpdate();
//        }
//        catch (SQLException e) {
//            log.info("update error", e);
//            throw e;
//        }
//        finally {
//            close(con, pstmt, null);
//        }
//
//    }
//    @SneakyThrows
//    @Override
//    public void delete(String member_id) {
//        String sql = "delete from member_table where member_id = ?";
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, member_id);
//            pstmt.executeUpdate();
//        }
//        catch (SQLException e) {
//            log.info("delete error", e);
//            throw e;
//        }
//        finally {
//            close(con, pstmt, null);
//        }
//    }
//
//    @SneakyThrows
//    public void clear() {
//        String sql = "delete from member_table";
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.executeUpdate();
//        }
//        catch (SQLException e) {
//            log.info("delete all error", e);
//            throw e;
//        }
//        finally {
//            close(con, pstmt, null);
//        }
//    }
//    private Connection getConnection() {
//        Connection con = DataSourceUtils.getConnection(dataSource);
//        log.info("get connection={}, class={}", con, con.getClass());
//        return con;
//    }
//    private void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
//        JdbcUtils.closeResultSet(rs);
//        JdbcUtils.closeStatement(pstmt);
//        DataSourceUtils.releaseConnection(con, dataSource);
//    }
//}
