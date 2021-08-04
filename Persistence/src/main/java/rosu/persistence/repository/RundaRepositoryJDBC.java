package rosu.persistence.repository;

import domain.Runda;
import org.springframework.stereotype.Component;
import rosu.persistence.JucatorRepository;
import rosu.persistence.RundaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class RundaRepositoryJDBC implements RundaRepository {
    private JdbcUtils dbUtils;

    public RundaRepositoryJDBC(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Runda findOne(Integer id) throws IllegalArgumentException {
        Connection con = dbUtils.getConnection();
        List<Runda> runde = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Runde where idRunda=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idJ = result.getInt("idJoc");
                    int idR = result.getInt("idRunda");
                    int nr = result.getInt("nrCartiPrimite");
                    int nrRunda = result.getInt("nrRunda");
                    String username = result.getString("username");
                    String carteAleasa = result.getString("carteAleasa");
                    Runda runda = new Runda(nrRunda, idJ, username, carteAleasa,nr);
                    runda.setId(idR);
                    runde.add(runda);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return runde.get(0);
    }

    @Override
    public Iterable<Runda> findAll() {
        Connection con = dbUtils.getConnection();
        List<Runda> runde = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Runde")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idJ = result.getInt("idJoc");
                    int idR = result.getInt("idRunda");
                    int nr = result.getInt("nrCartiPrimite");
                    int nrRunda = result.getInt("nrRunda");
                    String username = result.getString("username");
                    String carteAleasa = result.getString("carteAleasa");
                    Runda runda = new Runda(nrRunda, idJ, username, carteAleasa,nr);
                    runda.setId(idR);
                    runde.add(runda);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return runde;
    }

    @Override
    public void save(Runda entity) {
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("insert into Runde (idJoc,username, carteAlasa, nrRunda, nrCartiCastigate) values (?, ?, ?, ?, ?)")) {
            preStmt.setInt(1,entity.getIdJoc());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getCarteAleasa());
            preStmt.setInt(3, entity.getNrRunda());
            preStmt.setInt(3, entity.getNrCartiCastigate());

            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error DB " + ex);
        }
    }


    @Override
    public Iterable<Runda> findByPlayerGame(String username, int idJoc) {
        Connection con = dbUtils.getConnection();
        List<Runda> runde = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Runde where idJoc=? and username=?")) {
            preStmt.setInt(1, idJoc);
            preStmt.setString(2,username);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idJ = result.getInt("idJoc");
                    int idR = result.getInt("idRunda");
                    int nr = result.getInt("nrCartiCastigate");
                    int nrRunda = result.getInt("nrRunda");
                    String username1 = result.getString("username");
                    String carteAleasa = result.getString("carteAleasa");
                    Runda runda = new Runda(nrRunda, idJ, username1, carteAleasa,nr);
                    runda.setId(idR);
                    runde.add(runda);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return runde;
    }

    @Override
    public int nrCartiCastigate(int idJoc, String username) {
        Connection con = dbUtils.getConnection();
        int rez=0;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Runde where idJoc=? and username=?")) {
            preStmt.setInt(1, idJoc);
            preStmt.setString(2,username);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int nr = result.getInt("nrCartiCastigate");
                    rez+=nr;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return rez;
    }
}
