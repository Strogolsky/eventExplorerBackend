package cz.cvut.iarylser.dao.mappersDTO;

import java.util.List;

public interface MapperDTO<T, E> {
    T toDTO(E entity);
    List<T> toDTOList(List<E> entities);

    E toEntity(T dto);

}
