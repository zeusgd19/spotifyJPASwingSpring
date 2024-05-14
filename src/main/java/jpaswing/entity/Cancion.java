package jpaswing.entity;

import jakarta.persistence.*;

@Entity
public class Cancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
    private String idreal;

    @JoinColumn(name = "id_artista")
    @ManyToOne(fetch = FetchType.EAGER)
    private Artista artista;

    public Cancion() {

    }
    public Cancion(String name){
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdreal() {
        return idreal;
    }

    public void setIdreal(String idreal) {
        this.idreal = idreal;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }
}
