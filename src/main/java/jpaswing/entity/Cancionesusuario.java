package jpaswing.entity;

import jakarta.persistence.*;

@Entity
public class Cancionesusuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_cancion")
    @ManyToOne(fetch = FetchType.EAGER)
    private Cancion cancion;

    @JoinColumn(name = "id_usuario")
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    public Cancionesusuario() {

    }
    public Cancionesusuario(Cancion cancion, Usuario usuario) {
        this.cancion = cancion;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
