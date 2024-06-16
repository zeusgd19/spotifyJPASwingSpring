package jpaswing.repository;

import jpaswing.entity.Cancion;
import jpaswing.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public interface CancionRepository extends CrudRepository<Cancion, Long> {
    Cancion findByName(String name);
    Cancion findByImage(String url);
    Cancion findFirstByUsuariosIs(Usuario usuarios);
    ArrayList<Cancion> findAllByUsuariosIs(Usuario usuarios);
}
