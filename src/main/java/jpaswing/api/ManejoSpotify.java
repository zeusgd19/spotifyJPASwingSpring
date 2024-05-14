package jpaswing.api;

import jpaswing.entity.Artista;
import jpaswing.entity.Cancion;
import jpaswing.repository.ArtistaRepository;
import jpaswing.repository.CancionRepository;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Component
public class ManejoSpotify {

    private final String clientId = "";
    private final String clientSecret = "";

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;

    public ManejoSpotify(CancionRepository cancionRepository, ArtistaRepository artistaRepository) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
    }
    public String getSong(Cancion cancion, Artista artista) throws IOException, ParseException {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();

        try {
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        GetTrackRequest getTrackRequest = spotifyApi.getTrack(cancion.getIdreal()).build();
        try {
            Track track = getTrackRequest.execute();
            artista.setName(track.getArtists()[0].getName());
            artistaRepository.save(artista);
            cancion.setUrl(track.getPreviewUrl());
            cancion.setArtista(artista);
            cancionRepository.save(cancion);
            return track.getPreviewUrl();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return "";
    }

    public Icon getImage(Cancion cancion) throws ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();

        try {
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            System.out.println(clientCredentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        GetTrackRequest getTrackRequest = spotifyApi.getTrack(cancion.getIdreal()).build();
        try {
            Track track = getTrackRequest.execute();
            Image[] images = track.getAlbum().getImages();
            for (Image image: images){
                return obtenerIconoDesdeURL(image.getUrl());
            }
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static ImageIcon obtenerIconoDesdeURL(String imageUrl) {
        try {
            // Cargar la imagen desde la URL
            BufferedImage image = ImageIO.read(new URL(imageUrl));

            // Escalar la imagen para que se ajuste al tamaño deseado
            java.awt.Image scaledImage = image.getScaledInstance(100, 100, image.SCALE_SMOOTH);

            // Crear y devolver el ImageIcon
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
