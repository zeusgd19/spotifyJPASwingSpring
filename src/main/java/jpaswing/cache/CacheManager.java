package jpaswing.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {

    private static final File TRACK_CACHE_FILE = new File("trackCache.json");
    private static final File ARTIST_CACHE_FILE = new File("artistCache.json");
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static Map<String, Track[]> loadTrackCache() {
        return loadCache(TRACK_CACHE_FILE, new TypeReference<Map<String, Track[]>>() {});
    }

    public static Map<String, Artist[]> loadArtistCache() {
        return loadCache(ARTIST_CACHE_FILE, new TypeReference<Map<String, Artist[]>>() {});
    }

    public static void saveTrackCache(Map<String, Track[]> cache,String busqueda) {
        saveCache(TRACK_CACHE_FILE, cache,busqueda);
    }

    public static void saveArtistCache(Map<String, Artist[]> cache,String busqueda) {
        saveCache(ARTIST_CACHE_FILE, cache,busqueda);
    }

    private static <T> Map<String, T[]> loadCache(File fileName, TypeReference<Map<String, T[]>> typeReference) {
        try {
            if (fileName.exists()) {
                return objectMapper.readValue(fileName, typeReference);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static <T> void saveCache(File fileName, Map<String, T[]> cache, String busqueda) {
        try {

            Map<String, Object[]> existingData = new HashMap<>();

            // Check if the file exists and load existing data
            if (fileName.exists()) {
                existingData = objectMapper.readValue(fileName, new TypeReference<Map<String, Object[]>>() {});
            }

            for (T[] value : cache.values()){
                if(value instanceof Track[]){
                    // Crear un nuevo ObjectNode solo con los campos deseados
                    ObjectNode[] filteredNode = new ObjectNode[cache.get(busqueda).length];
                    Map<String,Object[]> mapa = new HashMap<>();
                    for (int i = 0; i<cache.get(busqueda).length; i++) {
                        filteredNode[i] = objectMapper.createObjectNode();
                        JsonNode node = objectMapper.valueToTree(cache.get(busqueda)[i]);
                        filteredNode[i].set("id", node.get("id"));
                        filteredNode[i].set("name", node.get("name"));
                        filteredNode[i].set("previewUrl", node.get("previewUrl"));

                        ArrayNode imagesNode = objectMapper.createArrayNode();
                        JsonNode imagesArray = node.get("album").get("images");
                        if (imagesArray != null && imagesArray.isArray()) {
                            for (JsonNode imageNode : imagesArray) {
                                imagesNode.add(imageNode);
                            }
                        }
                        ObjectNode albumNode = objectMapper.createObjectNode();
                        albumNode.set("images", imagesNode);
                        filteredNode[i].set("album", albumNode);
                    }
                    existingData.put(busqueda, mergeArrays(existingData.get(busqueda), filteredNode));

                } else if(value instanceof Artist[]){
                    ObjectNode[] filteredNode = new ObjectNode[cache.get(busqueda).length];
                    Map<String,Object[]> mapa = new HashMap<>();
                    for (int i = 0; i<cache.get(busqueda).length; i++) {
                        filteredNode[i] = objectMapper.createObjectNode();
                        JsonNode node = objectMapper.valueToTree(cache.get(busqueda)[i]);
                        filteredNode[i].set("id", node.get("id"));
                        filteredNode[i].set("name", node.get("name"));
                        filteredNode[i].set("images", node.get("images"));

                    }
                    existingData.put(busqueda, mergeArrays(existingData.get(busqueda), filteredNode));
                }
                objectMapper.writeValue(fileName,existingData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ObjectNode[] mergeArrays(Object[] existingArray, ObjectNode[] newArray) {
        if (existingArray == null) {
            return newArray;
        }

        ObjectNode[] mergedArray = new ObjectNode[existingArray.length + newArray.length];
        System.arraycopy(existingArray, 0, mergedArray, 0, existingArray.length);
        System.arraycopy(newArray, 0, mergedArray, existingArray.length, newArray.length);
        return mergedArray;
    }
}
