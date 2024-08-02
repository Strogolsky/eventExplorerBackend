package cz.cvut.iarylser.service;

import java.util.List;

public interface CrudService<T, ID> {
    T create(T entity);

    T update(ID id, T entity);

    boolean delete(ID id);

    List<T> getAll();

    T getById(ID id);
}
