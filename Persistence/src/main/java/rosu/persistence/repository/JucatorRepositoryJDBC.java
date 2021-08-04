package rosu.persistence.repository;

import domain.Jucator;
import rosu.persistence.JocRepository;
import rosu.persistence.JucatorRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JucatorRepositoryJDBC implements JucatorRepository {
    private JdbcUtils dbUtils;

    public JucatorRepositoryJDBC(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Jucator findOne(String id) throws IllegalArgumentException {
        Connection con = dbUtils.getConnection();
        List<Jucator> jucatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Jucatori where idJucator=?")) {
            preStmt.setString(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String idJ = result.getString("idJucator");
                    String parola = result.getString("parola");
                    Jucator jucator = new Jucator(parola);
                    jucator.setId(idJ);
                    jucatori.add(jucator);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jucatori.get(0);
    }

    @Override
    public Iterable<Jucator> findAll() {
        Connection con = dbUtils.getConnection();
        List<Jucator> jucatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Jucatori")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String idJ = result.getString("idJucator");
                    String parola = result.getString("parola");
                    Jucator jucator = new Jucator(parola);
                    jucator.setId(idJ);
                    jucatori.add(jucator);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jucatori;
    }

    @Override
    public void save(Jucator entity) {
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("insert into Jucatori (idJucator, parola) values (?, ?)")) {
            preStmt.setString(1,entity.getId());
            preStmt.setString(2, entity.getParola());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error DB " + ex);
        }
    }


}
