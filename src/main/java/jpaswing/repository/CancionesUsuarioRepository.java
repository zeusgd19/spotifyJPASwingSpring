package jpaswing.repository;

import jpaswing.entity.Cancionesusuario;
import org.springframework.data.repository.CrudRepository;

public interface CancionesUsuarioRepository extends CrudRepository<Cancionesusuario, Long> {
}
