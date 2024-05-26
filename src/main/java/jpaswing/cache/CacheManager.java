package jpaswing.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {

    private static final String TRACK_CACHE_FILE = "trackCache.json";
    private static final String ARTIST_CACHE_FILE = "artistCache.json";
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static Map<String, Track[]> loadTrackCache() {
        return loadCache(TRACK_CACHE_FILE, new TypeReference<Map<String, Track[]>>() {});
    }

    public static Map<String, Artist[]> loadArtistCache() {
        return loadCache(ARTIST_CACHE_FILE, new TypeReference<Map<String, Artist[]>>() {});
    }

    public static void saveTrackCache(Map<String, Track[]> cache) {
        saveCache(TRACK_CACHE_FILE, cache);
    }

    public static void saveArtistCache(Map<String, Artist[]> cache) {
        saveCache(ARTIST_CACHE_FILE, cache);
    }

    private static <T> Map<String, T> loadCache(String fileName, TypeReference<Map<String, T>> typeReference) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                return objectMapper.readValue(file, typeReference);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static <T> void saveCache(String fileName, Map<String, T> cache) {
        try {
            objectMapper.writeValue(new File(fileName), cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
