package rosu.persistence;

import domain.Joc;

public interface JocRepository extends CrudRepository<Integer, Joc> {
    public Iterable<Joc> findByJocID(Integer id);
}
