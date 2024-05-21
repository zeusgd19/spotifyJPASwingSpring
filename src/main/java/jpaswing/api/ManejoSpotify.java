package jpaswing.api;

import com.neovisionaries.i18n.CountryCode;
import jpaswing.Codigos;
import jpaswing.entity.Artista;
import jpaswing.entity.Cancion;
import jpaswing.repository.ArtistaRepository;
import jpaswing.repository.CancionRepository;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import javax.swing.*;
import java.io.IOException;

@Component
public class ManejoSpotify {

    private final String clientId = Codigos.clientID;
    private final String clientSecret = Codigos.secretCliente;

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;

    private boolean isNull = false;

    public ManejoSpotify(CancionRepository cancionRepository, ArtistaRepository artistaRepository) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
    }
    public void saveSong(Track track) {
        isNull = false;
        Artista artista;
        Cancion cancion;
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


        if(track.getPreviewUrl() == null){
            JOptionPane.showMessageDialog(null,"Este track no tiene una cancion previa, porfavor seleccione otra canción");
            isNull = true;
            return;
        }
        if(artistaRepository.findByName(track.getArtists()[0].getName()) == null) {
            artista = new Artista();
            artista.setName(track.getArtists()[0].getName());
            artistaRepository.save(artista);
        } else {
            artista= artistaRepository.findByName(track.getArtists()[0].getName());
        }
        if(cancionRepository.findByName(track.getName()) == null) {
            cancion = new Cancion();
            cancion.setIdreal(track.getId());
            cancion.setUrl(track.getPreviewUrl());
            cancion.setArtista(artista);
            cancion.setName(track.getName());
            cancion.setImage(track.getAlbum().getImages()[0].getUrl());
            cancionRepository.save(cancion);
        }
    }

    public Track[] getTracks(String busqueda) throws IOException, ParseException, SpotifyWebApiException {
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

        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(busqueda).limit(19).build();

        Paging<Track> pagingtracks = searchTracksRequest.execute();

        for (Track track : pagingtracks.getItems()) {
            System.out.println(track.getPreviewUrl());
            System.out.println(track.getId());
        }

        return pagingtracks.getItems();
    }

    public Track[] getArtistTracks(String artista) throws IOException, ParseException, SpotifyWebApiException {
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

        GetArtistsTopTracksRequest searchTracksRequest = spotifyApi.getArtistsTopTracks(artista,CountryCode.ES).build();

        Track[] tracks = searchTracksRequest.execute();
        return tracks;
    }

    public Artist[] getArtists(String artista) throws IOException, ParseException, SpotifyWebApiException {
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

        SearchArtistsRequest getArtistRequest = spotifyApi.searchArtists(artista).limit(40).build();
        Paging<Artist> artistPaging = getArtistRequest.execute();
        return artistPaging.getItems();
    }

    public Track getSong(String id) throws IOException, ParseException, SpotifyWebApiException {
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

        GetTrackRequest getTrackRequest = spotifyApi.getTrack(id).build();
        return getTrackRequest.execute();
    }


    /*public Icon getImage(Cancion cancion) throws ParseException, SpotifyWebApiException {
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
            Image[] images = track.getAlbum().getImages();
            for (Image image: images){
                return obtenerIconoDesdeURL(image.getUrl());
            }
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

     */

   /* public static ImageIcon obtenerIconoDesdeURL(String imageUrl) {
        try {
            // Cargar la imagen desde la URL
            BufferedImage image = ImageIO.read(new URL(imageUrl));

            // Escalar la imagen para que se ajuste al tamaño deseado
            java.awt.Image scaledImage = image.getScaledInstance(300, 300, image.SCALE_SMOOTH);

            // Crear y devolver el ImageIcon
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    */

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }
}
