package jpaswing.ui;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SpotifyCentral2Panel extends JPanel {
    private JLabel imagenPortada;
    private JLabel nombreCancion;
    private JLabel artista;
    private JLabel pagina;
    private JLabel paginas;
    private JButton remove;
    private SpotifyUI spotifyUI;

    public SpotifyCentral2Panel(SpotifyUI spotifyUI) {
        this.spotifyUI = spotifyUI;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(50, 40, 750, 455);
        this.setBackground(Color.GRAY);
        pagina = new JLabel("");
        pagina.setFont(new Font("Tahoma", Font.PLAIN, 23));
        pagina.setForeground(Color.GREEN);
        pagina.setBounds(10, 10, 45, 50);
        this.add(pagina);

        paginas = new JLabel("");
        paginas.setFont(new Font("Tahoma", Font.PLAIN, 23));
        paginas.setForeground(Color.GREEN);
        paginas.setBounds(55, 10, 50, 50);
        this.add(paginas);


        imagenPortada = new JLabel();
        imagenPortada.setBounds(this.getWidth() / 2 - 150, this.getHeight() / 2 - 170, 300, 300);
        this.add(imagenPortada);

        nombreCancion = new JLabel();
        nombreCancion.setFont(new Font("Tahoma", Font.PLAIN, 20));
        nombreCancion.setForeground(Color.cyan);
        nombreCancion.setBounds(this.getWidth() / 2 - 120, this.getHeight() / 2 + 150, 200, 30);
        this.add(nombreCancion);

        artista = new JLabel();
        artista.setFont(new Font("Tahoma", Font.PLAIN, 20));
        artista.setForeground(Color.cyan);
        artista.setBounds(this.getWidth() / 2 + 80, this.getHeight() / 2 + 150, 300, 30);
        this.add(artista);

        remove = new JButton("Remove");
        remove.setBounds(this.getWidth() - 100,10,100,30);
        remove.addActionListener(e ->{
            try {
                spotifyUI.removeCancion();
            } catch (ParseException | IOException | SpotifyWebApiException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(remove);
    }

    public void updateSongData(String songName, String artistName, String coverImage) throws IOException {
        nombreCancion.setText(songName);
        artista.setText(" - " + artistName);
        if (coverImage != null) {
            BufferedImage image = ImageIO.read(new URL(coverImage));
            java.awt.Image scaledImage = image.getScaledInstance(300, 300, image.SCALE_SMOOTH);
            imagenPortada.setIcon(new ImageIcon(scaledImage));
        }
    }

    public void clearSongData(){
        // Clear the song data in your UI components
        nombreCancion.setText("SIN CANCION");
        artista.setText("");
        imagenPortada.setText("SIN IMAGEN");
    }

    public JLabel getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(JLabel imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public JLabel getPagina() {
        return pagina;
    }

    public void setPagina(JLabel pagina) {
        this.pagina = pagina;
    }

    public JLabel getPaginas() {
        return paginas;
    }

    public void setPaginas(JLabel paginas) {
        this.paginas = paginas;
    }
}
