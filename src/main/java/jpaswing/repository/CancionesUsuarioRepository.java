package jpaswing.repository;

import jpaswing.entity.Cancion;
import jpaswing.entity.Cancionesusuario;
import jpaswing.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface CancionesUsuarioRepository extends CrudRepository<Cancionesusuario, Long> {
    Cancionesusuario findCancionesusuarioByCancionAndUsuario(Cancion cancion, Usuario usuario);
    boolean existsCancionesusuarioByCancionAndUsuario(Cancion cancion, Usuario usuario);
}
