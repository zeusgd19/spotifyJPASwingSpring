package jpaswing.repository;

import jpaswing.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Usuario findByCorreo(String email);
    Boolean existsByCorreo(String correo);
    Boolean existsByPassword(String password);
}
