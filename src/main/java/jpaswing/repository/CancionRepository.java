package jpaswing.repository;

import jpaswing.entity.Cancion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CancionRepository extends CrudRepository<Cancion, Long> {
    Cancion findFirstByOrderByIdAsc();
}
