package rosu.persistence.repository;

import domain.Joc;
import org.springframework.stereotype.Component;
import rosu.persistence.JocRepository;
import rosu.persistence.RundaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class JocRepositoryJDBC implements JocRepository {
    private JdbcUtils dbUtils;

    public JocRepositoryJDBC(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Joc findOne(Integer id) throws IllegalArgumentException {
        Connection con = dbUtils.getConnection();
        List<Joc> jocList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Jocuri where id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idJ = result.getInt("idJoc");
                    String carte1 = result.getString("carte1");
                    String carte2 = result.getString("carte2");
                    String carte3 = result.getString("carte3");
                    String username = result.getString("username");
                    Joc joc = new Joc(username,carte1,carte2,carte3);
                    joc.setId(idJ);
                    jocList.add(joc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jocList.get(0);
    }

    public Iterable<Joc> findByJocID(Integer id) throws IllegalArgumentException {
        Connection con = dbUtils.getConnection();
        List<Joc> jocList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Jocuri where idJoc=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idJ = result.getInt("idJoc");
                    String carte1 = result.getString("carte1");
                    String carte2 = result.getString("carte2");
                    String carte3 = result.getString("carte3");
                    String username = result.getString("username");
                    Joc joc = new Joc(username,carte1,carte2,carte3);
                    joc.setId(idJ);
                    jocList.add(joc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jocList;
    }
    @Override
    public Iterable<Joc> findAll() {
        Connection con = dbUtils.getConnection();
        List<Joc> jocList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Jocuri")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idJ = result.getInt("idJoc");
                    String carte1 = result.getString("carte1");
                    String carte2 = result.getString("carte2");
                    String carte3 = result.getString("carte3");
                    String username = result.getString("username");
                    Joc joc = new Joc(username,carte1,carte2,carte3);
                    joc.setId(idJ);
                    jocList.add(joc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jocList;
    }

    @Override
    public void save(Joc entity) {
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("insert into Jocuri (idJoc, carte1, carte2, carte3, username) values (?, ?, ?, ?, ?)")) {
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getCarte1());
            preStmt.setString(3, entity.getCarte2());
            preStmt.setString(4, entity.getCarte3());
            preStmt.setString(5,entity.getUsername());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error DB " + ex);
        }
    }


}
