package jpaswing.repository;

import jpaswing.entity.Cancion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CancionRepositoryPagination extends PagingAndSortingRepository<Cancion, Long> {
    @Query("SELECT COUNT(c) FROM Cancion c")
    public int countAllRecords();
}
