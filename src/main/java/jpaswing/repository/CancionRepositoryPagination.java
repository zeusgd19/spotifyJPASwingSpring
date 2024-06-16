package jpaswing.repository;

import jpaswing.entity.Cancion;
import jpaswing.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CancionRepositoryPagination extends PagingAndSortingRepository<Cancion, Long> {
    @Query("SELECT COUNT(c) FROM Cancion c INNER JOIN Cancionesusuario cu ON cu.cancion = c INNER JOIN Usuario u ON cu.usuario = u WHERE u.correo = :correoUsuario")
    public int countAllRecords(@Param("correoUsuario") String correoUsuario);

}
