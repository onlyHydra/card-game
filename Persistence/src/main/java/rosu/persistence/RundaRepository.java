package rosu.persistence;

import domain.Runda;

import java.util.List;

public interface RundaRepository extends CrudRepository<Integer, Runda> {
    public Iterable<Runda> findByPlayerGame(String username, int idJoc);

    public int nrCartiCastigate(int idJoc, String username);

}
