package jpaswing.repository;

import jpaswing.entity.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ArtistaRepository extends CrudRepository<Artista, Long> {
}
