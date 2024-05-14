package jpaswing.controller;


import jpaswing.entity.Cancion;
import jpaswing.repository.CancionRepository;
import jpaswing.repository.CancionRepositoryPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

//Necesario para que se pueda inyectar con AutoWired
@Component
public class CancionController {
    private final CancionRepository cancionRepository;
    private final CancionRepositoryPagination cancionRepositoryPagination;
    private int currentPage = 0;
    private int count;
    private Optional<Cancion> currentCancion;
    @Autowired
    public CancionController(CancionRepository cancionRepository, CancionRepositoryPagination cancionRepositoryPagination){
        this.cancionRepository = cancionRepository;
        this.cancionRepositoryPagination = cancionRepositoryPagination;
        this.count = cancionRepositoryPagination.countAllRecords();
    }
    public Optional<Cancion> getCancion(){
        //El primer parámetro es el número de página y el segundo los registros que queremos que nos devuelva
        PageRequest pr = PageRequest.of(currentPage, 1);
        currentCancion = Optional.of(cancionRepositoryPagination.findAll(pr).getContent().get(0));
        return currentCancion;
    }

    public Optional<Cancion> next(){
        //Si ya estoy al final, devuelvo el libro actual
        this.count = cancionRepositoryPagination.countAllRecords();
        if (currentPage == this.count -1 ) return currentCancion;

        currentPage++;
        return getCancion();
    }

    public Optional<Cancion> previous(){
        //Si ya estoy al principio, devuelvo el libro actual
        if (currentPage == 0) return currentCancion;

        currentPage--;
        return getCancion();
    }

    public Optional<Cancion> first(){
        //Primer libro
        currentPage = 0;
        return getCancion();
    }
    public Optional<Cancion> last(){
        //Último libro
        this.count = cancionRepositoryPagination.countAllRecords();
        currentPage = count - 1;
        return getCancion();
    }

}
