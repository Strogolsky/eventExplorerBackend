package cz.cvut.iarylser.facade;

import java.util.List;

public interface CrudFacade<T, ID>{
    T create(T dto);

    T update(ID id, T dto) throws IllegalAccessException;

    boolean delete(ID id) throws IllegalAccessException;

    List<T> getAll();

    T getById(ID id);
}
