package jpaswing.ui;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import jpaswing.api.ManejoSpotify;
import jpaswing.entity.Artista;
import jpaswing.entity.Cancion;
import jpaswing.entity.Usuario;
import jpaswing.repository.ArtistaRepository;
import jpaswing.repository.CancionRepository;
import jpaswing.repository.UsuarioRepository;
import org.apache.hc.core5.http.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class SpotifyUI extends JFrame {
    private SpotifyLateralPanel panelSpotifyLateral;
    private SpotifyArribaPanel panelSpotifyArriba;
    private SpotifyAbajoBuscarPanel panelSpotifyAbajoBuscar;
    private SpotifyAbajoBibliotecaPanel panelSpotifyAbajoBiblioteca;
    private SpotifyCentral1Panel panelSpotifyCentral1;
    private SpotifyCentral2Panel panelSpotifyCentral2;

    private Cancion cancion;
    private final ManejoSpotify manejoSpotify;
    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;

    private final UsuarioRepository usuarioRepository;
    private Usuario usuario;
    private boolean paused;
    private Player player;
    private Clip clip;
    private AudioInputStream audioInputStream;
    private int valor = 1;
    private Font font = null;
    private JPanel selectedPanel = null;
    private final ApplicationContext context;

    public SpotifyUI(CancionRepository cancionRepository, ManejoSpotify manejoSpotify, ArtistaRepository artistaRepository, UsuarioRepository usuarioRepository, ApplicationContext context) throws IOException, ParseException, SpotifyWebApiException, JavaLayerException {
        this.context = context;
        this.setFont(new Font("Gotham",Font.ITALIC,15));
        this.setTitle("Spotify");
        this.setLayout(null);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.usuarioRepository = usuarioRepository;
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
        this.manejoSpotify = manejoSpotify;
        initComponents();
        panelSpotifyAbajoBiblioteca.setVisible(false);
        panelSpotifyCentral2.setVisible(false);
        updateData();
        paused = false;
    }

    public void initComponents() {

        panelSpotifyLateral = new SpotifyLateralPanel(this);
        panelSpotifyArriba = new SpotifyArribaPanel(this);
        panelSpotifyAbajoBiblioteca = new SpotifyAbajoBibliotecaPanel(this);
        panelSpotifyAbajoBuscar = new SpotifyAbajoBuscarPanel(this);
        panelSpotifyCentral1 = new SpotifyCentral1Panel(this);
        panelSpotifyCentral2 = new SpotifyCentral2Panel();
        Icon ImagenCentral = new ImageIcon("src/fotos/mondongo.jpg");
        panelSpotifyCentral1.hideImage();
        panelSpotifyCentral1.setImage(ImagenCentral);

        this.add(panelSpotifyLateral);
        this.add(panelSpotifyArriba);
        this.add(panelSpotifyAbajoBiblioteca);
        this.add(panelSpotifyCentral2);
        this.add(panelSpotifyAbajoBuscar);
        this.add(panelSpotifyCentral1);
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }
    public Font customFont() {
        String fontName = "src/fuente/GothamBoldItalic.ttf" ;
        try {
            //Se carga la fuente
            File file = new File(fontName);
            font = Font.createFont(Font.TRUETYPE_FONT,file);
        } catch (Exception ex) {
            //Si existe un error se carga fuente por defecto ARIAL
            System.err.println(fontName + " No se cargo la fuente");
            font = new Font("Arial", Font.PLAIN, 14);
        }
        return font;
    }

    public void showBibliotecaPanel() throws IOException, ParseException, JavaLayerException, SpotifyWebApiException {
        if(this.cancion == null){
            this.cancion = cancionRepository.findFirstByUsuariosIs(usuario);
        }
        panelSpotifyAbajoBiblioteca.setVisible(true);
        panelSpotifyCentral2.setVisible(true);
        panelSpotifyAbajoBuscar.setVisible(false);
        panelSpotifyCentral1.setVisible(false);
        panelSpotifyCentral1.getBuscador().setText("");
        panelSpotifyCentral1.hideImage();
        panelSpotifyCentral1.clearTrackPanel();
        updateData();
        if(cancionRepository.findFirstByUsuariosIs(usuario) != null){
            panelSpotifyCentral2.getPagina().setText("0");
        }
        player = getPlayer();
    }

    public void showBuscarPanel() {
        panelSpotifyAbajoBiblioteca.setVisible(false);
        panelSpotifyCentral2.setVisible(false);
        panelSpotifyAbajoBuscar.setVisible(true);
        panelSpotifyCentral1.setVisible(true);
    }

    public void updateData() throws ParseException, SpotifyWebApiException, IOException {
        if (this.cancion != null) {
            panelSpotifyCentral2.getPaginas().setText("/"+(cancionRepository.findAllByUsuariosIs(usuario).size() - 1));
            panelSpotifyCentral2.updateSongData(cancion.getName(), cancion.getArtista().getName(), cancion.getImage());
        } else {
            panelSpotifyCentral2.clearSongData();
        }
    }


    public void search() throws JavaLayerException, IOException, LineUnavailableException, UnsupportedAudioFileException, ParseException, SpotifyWebApiException {
        audioInputStream = AudioSystem.getAudioInputStream(new File("src/sonido/mondongo.wav").getAbsoluteFile());
        clip = AudioSystem.getClip();

        panelSpotifyAbajoBuscar.getSearchSongsArtist().setEnabled(false);
        panelSpotifyCentral1.clearTrackPanel();

        // Buscar Easter Egg
        if (panelSpotifyCentral1.getSearchText().equalsIgnoreCase("Mondongo")) {
            panelSpotifyCentral1.showImage();
            clip.open(audioInputStream);
            clip.start();
        } else if(panelSpotifyCentral1.getSearchText().isEmpty()) {
            panelSpotifyCentral1.clearTrackPanel();
            panelSpotifyCentral1.hideImage();

            //Buscar por artista
        } else if(panelSpotifyCentral1.getSearchText().contains("Artista:")){
            panelSpotifyAbajoBuscar.getSearchSongsArtist().setEnabled(true);
            panelSpotifyAbajoBuscar.getGuardar().setEnabled(false);
            panelSpotifyCentral1.clearTrackPanel();
            String artista = panelSpotifyCentral1.getSearchText().substring(panelSpotifyCentral1.getSearchText().indexOf(":"));

            for (Artist artist : manejoSpotify.getArtists(artista)) {
                panelSpotifyCentral1.addTrack(addPanel(artist,artist.getId()));
            }
        }else {
            panelSpotifyAbajoBuscar.getGuardar().setEnabled(true);
            panelSpotifyCentral1.clearTrackPanel();

            for (Track track : manejoSpotify.getTracks(panelSpotifyCentral1.getSearchText())) {
                panelSpotifyCentral1.addTrack(addPanel(track,track.getId()));
            }
        }
    }

    public void searchArtistSongs() throws IOException, ParseException, SpotifyWebApiException {
        panelSpotifyAbajoBuscar.getGuardar().setEnabled(true);
        panelSpotifyCentral1.clearTrackPanel();

        for (Track track:manejoSpotify.getArtistTracks((String) selectedPanel.getClientProperty("panelId"))){
            panelSpotifyCentral1.addTrack(addPanel(track,track.getId()));
        }
    }
    public void nextSong() {
        ArrayList<Cancion> cancionArrayList = cancionRepository.findAllByUsuariosIs(usuario);

        nextOrPrevious("next",cancionArrayList);

        if(!cancionArrayList.isEmpty()) {
            try {
                player.close();
                player = getPlayer();
                paused = true;
                valor = 1;
                panelSpotifyAbajoBiblioteca.getProgressBar().setValue(valor);
                updateData();
                panelSpotifyAbajoBiblioteca.showPlayButton();
            } catch (IOException | JavaLayerException | ParseException | SpotifyWebApiException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void previousSong() {
        ArrayList<Cancion> cancionArrayList = cancionRepository.findAllByUsuariosIs(usuario);

        nextOrPrevious("previous",cancionArrayList);

        if(!cancionArrayList.isEmpty()) {
            try {
                player.close();
                player = getPlayer();
                paused = true;
                valor = 1;
                panelSpotifyAbajoBiblioteca.getProgressBar().setValue(valor);
                updateData();
                panelSpotifyAbajoBiblioteca.showPlayButton();
            } catch (IOException | JavaLayerException | ParseException | SpotifyWebApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void nextOrPrevious(String situation,ArrayList<Cancion> cancionArrayList){
        if (cancionArrayList.isEmpty()) {
            return;
        }

        int currentIndex = -1;
        for (int i = 0; i < cancionArrayList.size(); i++) {
            if (cancionArrayList.get(i).getId().equals(cancion.getId())) {
                currentIndex = i;
                break;
            }
        }

        if (situation.equals("previous")) {
            currentIndex = (currentIndex == -1) ? cancionArrayList.size() - 1 : (currentIndex - 1 + cancionArrayList.size()) % cancionArrayList.size();
        } else {
            currentIndex = (currentIndex == -1) ? 0 : (currentIndex + 1) % cancionArrayList.size();
        }

        // Actualiza la canción actual
        this.cancion = cancionArrayList.get(currentIndex);

        // Actualizar paginas actual
        panelSpotifyCentral2.getPagina().setText("" + currentIndex);
    }


    public void saveCancion() throws IOException, ParseException, SpotifyWebApiException {
        if (selectedPanel == null) {
            JOptionPane.showMessageDialog(panelSpotifyCentral1, "DEBES SELECCIONAR UNA CANCION");
        } else {
            Track track = manejoSpotify.getSong((String) selectedPanel.getClientProperty("panelId"));
            manejoSpotify.saveSong(track,usuario);
            if(!manejoSpotify.isNull()) {
                JOptionPane.showMessageDialog(panelSpotifyCentral1, "CANCION GUARDADA CORRECTAMENTE");
            }
            panelSpotifyCentral2.getPagina().setText("0");
            updateData();
        }
    }

    public JPanel addPanel(Object entity, String id) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(750, 100));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.putClientProperty("panelId", id);

        JLabel label = new JLabel();
        BufferedImage image = null;

        if (entity instanceof Track) {
            Track track = (Track) entity;
            Cancion cancion1 = cancionRepository.findByImage(track.getAlbum().getImages()[0].getUrl());
            image = cancion1 == null ? ImageIO.read(new URL(track.getAlbum().getImages()[0].getUrl())) : ImageIO.read(new URL(cancion1.getImage()));
        } else if (entity instanceof Artist) {
            Artist artist = (Artist) entity;
            if (hasPhoto(artist)) {
                Artista artista = artistaRepository.findByImage(artist.getImages()[0].getUrl());
                image = artista == null ? ImageIO.read(new URL(artist.getImages()[0].getUrl())) : ImageIO.read(new URL(artista.getImage()));
            }
        }

        if (image != null) {
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
            label.setBounds(0, 0, 50, 50);
        }

        JLabel label1 = new JLabel();
        if (entity instanceof Track) {
            label1.setText(((Track) entity).getName());
        } else if (entity instanceof Artist) {
            label1.setText(((Artist) entity).getName());
        }
        label1.setBounds(panel.getWidth() / 2, 0, 100, 40);

        panel.add(label, BorderLayout.WEST);
        panel.add(label1, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedPanel != null) {
                    selectedPanel.setBackground(new Color(238, 238, 238)); // Color original
                }
                panel.setBackground(Color.cyan);
                selectedPanel = panel;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != selectedPanel) {
                    panel.setBackground(Color.gray);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != selectedPanel) {
                    panel.setBackground(new Color(238, 238, 238));
                }
            }
        });

        return panel;
    }

    public void getLogin(){
        SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = context.getBean(LoginUI.class);
            loginUI.resetFields();
            loginUI.setVisible(true);
            try {
                resetState();
                panelSpotifyCentral2.getPagina().setText("");
            } catch (ParseException | IOException | SpotifyWebApiException e) {
                throw new RuntimeException(e);
            }
            this.dispose();
        });
    }

    private void resetState() throws ParseException, IOException, SpotifyWebApiException {
        this.cancion = null;
        this.usuario = null;
        panelSpotifyCentral2.getImagenPortada().setIcon(null);
        panelSpotifyCentral2.getPaginas().setText("");
        panelSpotifyCentral2.getPagina().setText("0");
        showBuscarPanel();
        updateData();
        panelSpotifyAbajoBiblioteca.getProgressBar().setValue(0);
        panelSpotifyAbajoBiblioteca.showPlayButton();
    }
    public boolean hasPhoto(Artist artist){
        return artist.getImages().length >= 1;
    }


    public void playMusic() throws IOException, ParseException, JavaLayerException {
        paused = false;
        panelSpotifyAbajoBiblioteca.showPauseButton();
        if(player.isComplete()) {
            player = getPlayer();
        }
        new Thread(() -> playMusic(player)).start();
    }

    public void pauseMusic() {
        paused = true;
        panelSpotifyAbajoBiblioteca.showPlayButton();
        valor+=2;
    }

    public void playMusic(Player player) {
        try {
            while (!paused) {
                if (!player.isComplete()) {
                    player.play(1);
                }
                if (player.isComplete()) {
                    panelSpotifyAbajoBiblioteca.showPlayButton();
                    paused = true;
                    valor = 1;
                }
            }
        } catch (JavaLayerException ex) {
            ex.printStackTrace();
        }
    }

    public void playProgres() {
        new Thread(() -> {
            while (!paused) {
                try {
                    panelSpotifyAbajoBiblioteca.getProgressBar().setValue(valor);
                    Thread.sleep(100);
                    valor++;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }

    public Player getPlayer() throws JavaLayerException, IOException, ParseException {
        if(cancion != null) {
            BufferedInputStream in = new BufferedInputStream(new URL(cancion.getUrl()).openStream());
            return new Player(in);
        }
        return null;
    }

    public SpotifyCentral2Panel getPanelSpotifyCentral2() {
        return panelSpotifyCentral2;
    }

    public void setPanelSpotifyCentral2(SpotifyCentral2Panel panelSpotifyCentral2) {
        this.panelSpotifyCentral2 = panelSpotifyCentral2;
    }
}