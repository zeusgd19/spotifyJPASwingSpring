package jpaswing.repository;

import jpaswing.entity.Cancion;
import jpaswing.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Usuario findByCorreo(String email);
    Boolean existsByCorreo(String correo);
    Boolean existsByPassword(String password);
}
