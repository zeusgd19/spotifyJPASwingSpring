package jpaswing.api;

import com.neovisionaries.i18n.CountryCode;
import jpaswing.Codigos;
import jpaswing.cache.CacheManager;
import jpaswing.entity.Artista;
import jpaswing.entity.Cancion;
import jpaswing.entity.Cancionesusuario;
import jpaswing.entity.Usuario;
import jpaswing.repository.ArtistaRepository;
import jpaswing.repository.CancionRepository;
import jpaswing.repository.CancionesUsuarioRepository;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

@Component
public class ManejoSpotify {

    private final String clientId = Codigos.clientID;
    private final String clientSecret = Codigos.secretCliente;

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;
    private final CancionesUsuarioRepository cancionesUsuarioRepository;

    private boolean isNull = false;

    private Map<String, Track[]> searchCache;
    private Map<String, Artist[]> artistCache;

    public ManejoSpotify(CancionRepository cancionRepository, ArtistaRepository artistaRepository, CancionesUsuarioRepository cancionesUsuarioRepository) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
        this.searchCache = CacheManager.loadTrackCache();
        this.artistCache = CacheManager.loadArtistCache();
        this.cancionesUsuarioRepository = cancionesUsuarioRepository;
    }
    public void saveSong(Track track,Usuario usuario) throws IOException, ParseException, SpotifyWebApiException {
        isNull = false;
        Artista artista;
        Cancion cancion;
        Cancionesusuario cancionesusuario;
        getSpotify();


        if(track.getPreviewUrl() == null){
            JOptionPane.showMessageDialog(null,"Este track no tiene una cancion previa, porfavor seleccione otra canción");
            isNull = true;
            return;
        }
        if(artistaRepository.findByName(track.getArtists()[0].getName()) == null) {
            artista = new Artista();
            artista.setName(track.getArtists()[0].getName());
            Artist artist = getArtist(track.getArtists()[0].getId());
            artista.setImage(artist.getImages()[0].getUrl());
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
        }else {
            cancion = cancionRepository.findByName(track.getName());
        }
        cancionesusuario = new Cancionesusuario();
        cancionesusuario.setCancion(cancion);
        cancionesusuario.setUsuario(usuario);
        cancionesUsuarioRepository.save(cancionesusuario);
    }

    public Track[] getTracks(String busqueda) throws IOException, ParseException, SpotifyWebApiException {
        if (searchCache.containsKey(busqueda)) {
            return searchCache.get(busqueda);
        }

        SpotifyApi spotifyApi = getSpotify();


        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(busqueda)
                .limit(19).build();

        Paging<Track> pagingtracks = searchTracksRequest.execute();

        searchCache.put(busqueda, pagingtracks.getItems());
        CacheManager.saveTrackCache(searchCache,busqueda);
        return pagingtracks.getItems();
    }

    private SpotifyApi getSpotify() {
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
        return spotifyApi;
    }

    public Artist getArtist(String id) throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = getSpotify();
        GetArtistRequest artistRequest = spotifyApi.getArtist(id).build();

        return artistRequest.execute();
    }

    public Track[] getArtistTracks(String artista) throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = getSpotify();

        GetArtistsTopTracksRequest searchTracksRequest = spotifyApi.getArtistsTopTracks(artista,CountryCode.ES).build();

        Track[] tracks = searchTracksRequest.execute();
        return tracks;
    }

    public Artist[] getArtists(String artista) throws IOException, ParseException, SpotifyWebApiException {
        if (artistCache.containsKey(artista)) {
            return artistCache.get(artista);
        }

        SpotifyApi spotifyApi = getSpotify();

        SearchArtistsRequest getArtistRequest = spotifyApi.searchArtists(artista).limit(20).build();
        Paging<Artist> artistPaging = getArtistRequest.execute();
        artistCache.put(artista, artistPaging.getItems());
        CacheManager.saveArtistCache(artistCache,artista);
        return artistPaging.getItems();
    }

    public Track getSong(String id) throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = getSpotify();

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
