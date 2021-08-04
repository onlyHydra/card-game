package rosu.persistence;

import domain.Entity;

public interface CrudRepository<ID, E extends Entity<ID>>{
    E findOne(ID id) throws IllegalArgumentException;

    Iterable<E> findAll();

    void save(E entity);
}
