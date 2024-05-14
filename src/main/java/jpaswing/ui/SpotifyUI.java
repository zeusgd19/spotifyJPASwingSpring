package jpaswing.ui;

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
import java.io.IOException;
import java.util.Optional;

@Component
public class SpotifyUI extends JFrame implements MouseListener {
    private JPanel panelSpotifyLateral;
    private JPanel panelSpotifyArriba;
    private JPanel panelSpotifyAbajo;
    private JPanel panelSpotifyCentral1;
    private JPanel panelSpotifyCentral2;
    private int screen = 0;

    private JLabel biblioteca;
    private JLabel buscar;
    private JLabel siguiente;
    private JLabel anterior;
    private JLabel reproducir;
    private Icon icono;
    private JLabel titulo;
    private Cancion cancion;
    private JLabel imagenPortada;
    private JLabel nombreCancion;
    private JLabel artista;
    private JProgressBar progressBar;
    private ManejoSpotify manejoSpotify;
    private CancionController cancionController;
    public SpotifyUI(CancionRepository cancionRepository, ManejoSpotify manejoSpotify,CancionController cancionController) throws IOException, ParseException, SpotifyWebApiException {
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
        updateData();
    }

    public void initComponents() throws ParseException, SpotifyWebApiException {
        panelSpotifyLateral = new JPanel();
        panelSpotifyLateral.setLayout(null);
        panelSpotifyLateral.setBounds(0, 40, 50, 540);
        panelSpotifyLateral.setBackground(Color.BLACK);

        biblioteca = new JLabel();
        icono = new ImageIcon("src/fotos/biblioteca.png");
        biblioteca.setIcon(icono);
        biblioteca.setBounds(10, 90, 28, 30);
        panelSpotifyLateral.add(biblioteca);

        buscar = new JLabel();
        icono = new ImageIcon("src/fotos/buscar.png");
        buscar.setIcon(icono);
        buscar.setBounds(10, 50, 28, 30);
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

        panelSpotifyAbajo = new JPanel();
        panelSpotifyAbajo.setLayout(null);
        panelSpotifyAbajo.setBounds(40, 495, 745, 80);
        panelSpotifyAbajo.setBackground(Color.BLACK);


        siguiente = new JLabel();
        icono = new ImageIcon("src/fotos/siguiente.png");
        siguiente.setIcon(icono);
        siguiente.setToolTipText("Haz click aqui");
        siguiente.setBounds(panelSpotifyAbajo.getWidth() / 2 + 30, panelSpotifyAbajo.getHeight() / 2 - 30, 28, 30);
        siguiente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Se ejecutará cuando se haga clic en el JLabel
                try {
                    next();
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
        panelSpotifyAbajo.add(siguiente);

        anterior = new JLabel();
        icono = new ImageIcon("src/fotos/anterior.png");
        anterior.setIcon(icono);
        anterior.setToolTipText("Haz click aqui");
        anterior.setBounds(panelSpotifyAbajo.getWidth() / 2 - 60, panelSpotifyAbajo.getHeight() / 2 - 28, 28, 30);
        anterior.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Se ejecutará cuando se haga clic en el JLabel
                try {
                    previous();
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
        panelSpotifyAbajo.add(anterior);

        reproducir = new JLabel();
        icono = new ImageIcon("src/fotos/reproducir.png");
        reproducir.setIcon(icono);
        reproducir.setBounds(panelSpotifyAbajo.getWidth() / 2 - 25, panelSpotifyAbajo.getHeight() / 2 - 30, 46, 32);
        panelSpotifyAbajo.add(reproducir);

        progressBar = new JProgressBar();
        progressBar.setBounds(panelSpotifyAbajo.getWidth() / 2 - 77, panelSpotifyAbajo.getHeight() / 2 + 10, 150, 5);
        panelSpotifyAbajo.add(progressBar);

        panelSpotifyCentral2 = new JPanel();
        panelSpotifyCentral2.setLayout(null);
        panelSpotifyCentral2.setBounds(50, 40, 750, 500);
        panelSpotifyCentral2.setBackground(Color.GRAY);

        imagenPortada = new JLabel();
        imagenPortada.setBounds(panelSpotifyCentral2.getWidth() / 2 - 80, panelSpotifyCentral2.getHeight() / 2, 100, 100);
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
        this.add(panelSpotifyAbajo);
        this.add(panelSpotifyCentral2);
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
}
