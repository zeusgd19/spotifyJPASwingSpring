package jpaswing.ui;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import jpaswing.api.ManejoSpotify;
import jpaswing.controller.CancionController;
import jpaswing.entity.Artista;
import jpaswing.entity.Cancion;
import jpaswing.repository.CancionRepository;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Component
public class SpotifyUI extends JFrame implements MouseListener {
    private JPanel panelSpotifyLateral;
    private JPanel panelSpotifyArriba;
    private JPanel panelSpotifyAbajoBuscar;
    private JPanel panelSpotifyAbajoBiblioteca;
    private JPanel panelSpotifyCentral1;
    private JPanel panelSpotifyCentral2;
    private int screen = 0;

    private JLabel biblioteca;
    private JLabel buscar;
    private JLabel siguiente;
    private JLabel anterior;
    private JLabel reproducir;
    private JLabel pause;
    private Icon icono;
    private JLabel titulo;
    private Cancion cancion;
    private JLabel imagenPortada;
    private JLabel nombreCancion;
    private JLabel artista;
    private JProgressBar progressBar;
    private ManejoSpotify manejoSpotify;
    private CancionController cancionController;

    private JTextField buscador;
    private boolean paused;
    private Player player;

    public SpotifyUI(CancionRepository cancionRepository, ManejoSpotify manejoSpotify,CancionController cancionController) throws IOException, ParseException, SpotifyWebApiException, JavaLayerException {
        super("Spotify");
        this.setLayout(null);
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.cancion = cancionRepository.findFirstByOrderByIdAsc();
        this.manejoSpotify = manejoSpotify;
        this.cancionController = cancionController;
        initComponents();
        panelSpotifyAbajoBiblioteca.setVisible(false);
        panelSpotifyCentral2.setVisible(false);
        pause.setVisible(false);
        updateData();
        paused = false;
    }

    public void initComponents() throws ParseException, SpotifyWebApiException, IOException, JavaLayerException {
        player = getPlayer();
        panelSpotifyLateral = new JPanel();
        panelSpotifyLateral.setLayout(null);
        panelSpotifyLateral.setBounds(0, 40, 50, 540);
        panelSpotifyLateral.setBackground(Color.BLACK);

        biblioteca = new JLabel();
        icono = new ImageIcon("src/fotos/biblioteca.png");
        biblioteca.setIcon(icono);
        biblioteca.setBounds(10, 90, 28, 30);
        biblioteca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelSpotifyAbajoBiblioteca.setVisible(true);
                panelSpotifyCentral2.setVisible(true);
                panelSpotifyAbajoBuscar.setVisible(false);
                panelSpotifyCentral1.setVisible(false);
            }
        });
        panelSpotifyLateral.add(biblioteca);

        buscar = new JLabel();
        icono = new ImageIcon("src/fotos/buscar.png");
        buscar.setIcon(icono);
        buscar.setBounds(10, 50, 28, 30);
        buscar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelSpotifyAbajoBiblioteca.setVisible(false);
                panelSpotifyCentral2.setVisible(false);
                panelSpotifyAbajoBuscar.setVisible(true);
                panelSpotifyCentral1.setVisible(true);
            }
        });
        panelSpotifyLateral.add(buscar);

        panelSpotifyArriba = new JPanel();
        panelSpotifyArriba.setLayout(null);
        panelSpotifyArriba.setBounds(0, 0, 800, 40);
        panelSpotifyArriba.setBackground(Color.BLACK);

        titulo = new JLabel("SPOTIFY");
        titulo.setFont(new Font("Tahoma", Font.PLAIN, 20));
        titulo.setForeground(Color.GREEN);
        titulo.setBackground(Color.white);
        titulo.setBounds(350, 10, 100, 20);
        panelSpotifyArriba.add(titulo);

        panelSpotifyAbajoBiblioteca = new JPanel();
        panelSpotifyAbajoBiblioteca.setLayout(null);
        panelSpotifyAbajoBiblioteca.setBounds(40, 495, 745, 80);
        panelSpotifyAbajoBiblioteca.setBackground(Color.BLACK);

        panelSpotifyAbajoBuscar = new JPanel();
        panelSpotifyAbajoBuscar.setLayout(null);
        panelSpotifyAbajoBuscar.setBounds(40, 495, 745, 80);
        panelSpotifyAbajoBuscar.setBackground(Color.BLACK);


        siguiente = new JLabel();
        icono = new ImageIcon("src/fotos/siguiente.png");
        siguiente.setIcon(icono);
        siguiente.setToolTipText("Haz click aqui");
        siguiente.setBounds(panelSpotifyAbajoBiblioteca.getWidth() / 2 + 30, panelSpotifyAbajoBiblioteca.getHeight() / 2 - 30, 28, 30);
        siguiente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Se ejecutará cuando se haga clic en el JLabel
                try {
                    next();
                    player.close();
                    reproducir.setVisible(true);
                    pause.setVisible(false);
                    try {
                        player = getPlayer();
                    } catch (JavaLayerException | IOException | ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (ParseException | IOException | SpotifyWebApiException ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                siguiente.setForeground(Color.RED); // Cambiar el color del texto cuando el mouse ingresa al JLabel
            }

            @Override
            public void mouseExited(MouseEvent e) {
                siguiente.setForeground(Color.BLUE); // Restaurar el color del texto cuando el mouse sale del JLabel
            }
        });
        panelSpotifyAbajoBiblioteca.add(siguiente);

        anterior = new JLabel();
        icono = new ImageIcon("src/fotos/anterior.png");
        anterior.setIcon(icono);
        anterior.setToolTipText("Haz click aqui");
        anterior.setBounds(panelSpotifyAbajoBiblioteca.getWidth() / 2 - 60, panelSpotifyAbajoBiblioteca.getHeight() / 2 - 28, 28, 30);
        anterior.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Se ejecutará cuando se haga clic en el JLabel
                try {
                    previous();
                    player.close();
                    reproducir.setVisible(true);
                    pause.setVisible(false);
                    try {
                        player = getPlayer();
                    } catch (JavaLayerException | IOException | ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (ParseException | IOException | SpotifyWebApiException ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                anterior.setForeground(Color.RED); // Cambiar el color del texto cuando el mouse ingresa al JLabel
            }

            @Override
            public void mouseExited(MouseEvent e) {
                anterior.setForeground(Color.BLUE); // Restaurar el color del texto cuando el mouse sale del JLabel
            }
        });

        panelSpotifyAbajoBiblioteca.add(anterior);

        reproducir = new JLabel();
        icono = new ImageIcon("src/fotos/reproducir.png");
        reproducir.setIcon(icono);
        reproducir.setBounds(panelSpotifyAbajoBiblioteca.getWidth() / 2 - 25, panelSpotifyAbajoBiblioteca.getHeight() / 2 - 30, 46, 32);
        panelSpotifyAbajoBiblioteca.add(reproducir);

        pause = new JLabel();
        icono = new ImageIcon("src/fotos/pause.png");
        pause.setIcon(icono);
        pause.setBounds(panelSpotifyAbajoBiblioteca.getWidth() / 2 - 22, panelSpotifyAbajoBiblioteca.getHeight() / 2 - 33, 39, 37);


        reproducir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Iniciar la reproducción de la canción en un hilo separado
                paused = false;
                reproducir.setVisible(false);
                pause.setVisible(true);
                new Thread(() -> playMusic(player)).start();
            }
        });

        pause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Pausar la reproducción de la canción
                paused = true;
                reproducir.setVisible(true);
                pause.setVisible(false);
            }
        });
        panelSpotifyAbajoBiblioteca.add(pause);

        panelSpotifyCentral2 = new JPanel();
        panelSpotifyCentral2.setLayout(null);
        panelSpotifyCentral2.setBounds(50, 40, 735, 455);
        panelSpotifyCentral2.setBackground(Color.GRAY);

        panelSpotifyCentral1 = new JPanel();
        panelSpotifyCentral1.setLayout(null);
        panelSpotifyCentral1.setBounds(50, 40, 735, 455);
        panelSpotifyCentral1.setBackground(Color.GRAY);

        buscador  = new JTextField();
        buscador.setBackground(Color.WHITE);
        buscador.setForeground(Color.BLACK);
        buscador.setBounds(0,0,panelSpotifyCentral1.getWidth(), 30);
        panelSpotifyCentral1.add(buscador);

        imagenPortada = new JLabel();
        imagenPortada.setBounds(panelSpotifyCentral2.getWidth() / 2 - 150, panelSpotifyCentral2.getHeight() / 2 - 170, 300, 300);
        panelSpotifyCentral2.add(imagenPortada);

        nombreCancion = new JLabel();
        nombreCancion.setFont(new Font("Tahoma", Font.PLAIN, 20));
        nombreCancion.setForeground(Color.cyan);
        nombreCancion.setBounds(panelSpotifyCentral2.getWidth() / 2 - 120, panelSpotifyCentral2.getHeight() / 2 + 150, 200, 30);
        panelSpotifyCentral2.add(nombreCancion);

        artista = new JLabel();
        artista.setFont(new Font("Tahoma", Font.PLAIN, 20));
        artista.setForeground(Color.cyan);
        artista.setBounds(panelSpotifyCentral2.getWidth() / 2 + 60, panelSpotifyCentral2.getHeight() / 2 + 150, 200, 30);
        panelSpotifyCentral2.add(artista);

        this.add(panelSpotifyLateral);
        this.add(panelSpotifyArriba);
        this.add(panelSpotifyAbajoBiblioteca);
        this.add(panelSpotifyCentral2);
        this.add(panelSpotifyAbajoBuscar);
        this.add(panelSpotifyCentral1);
    }

    private void updateData() throws ParseException, SpotifyWebApiException {
        if (this.cancion != null){
            nombreCancion.setText(cancion.getName());
            artista.setText(" - " + cancion.getArtista().getName());
            imagenPortada.setIcon(manejoSpotify.getImage(cancion));
        }else{
            nombreCancion.setText("SIN CANCION");
            imagenPortada.setText("NO HAY IMAGEN");
        }
    }

    public void next() throws ParseException, IOException, SpotifyWebApiException {
        this.cancion = cancionController.next().orElse(null);
        updateData();
    }

    public void previous() throws ParseException, IOException, SpotifyWebApiException {
        this.cancion = cancionController.previous().orElse(null);
        updateData();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void playMusic(Player player) {
        try {
            // Reproducir la canción

            // Esperar hasta que la canción termine de reproducirse
            while (paused && !player.isComplete()) {
                Thread.sleep(100);
            }

            while (!paused){
            if(!player.isComplete()){
                player.play(1);
            }
            }
        } catch (JavaLayerException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public Player getPlayer() throws JavaLayerException, IOException, ParseException {
        BufferedInputStream in = new BufferedInputStream(new URL(manejoSpotify.getSong(cancion, new Artista())).openStream());
        Player player = new Player(in);
        return player;
    }
}
