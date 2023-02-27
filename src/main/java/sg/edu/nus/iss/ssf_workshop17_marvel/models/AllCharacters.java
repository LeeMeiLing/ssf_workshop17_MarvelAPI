package sg.edu.nus.iss.ssf_workshop17_marvel.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.json.JsonObject;

public class AllCharacters {
    
    private static Map<Integer, String> charactersMap = new LinkedHashMap<>();


    public static void addCharacter (Integer id, String name){

        charactersMap.put(id, name);

    }

    public static Map<Integer, String> create(JsonObject jo) {

		addCharacter(jo.getInt("id"),jo.getString("name"));

        return getCharactersMap();
    }


    public static Map<Integer, String> getCharactersMap() {
        return charactersMap;
    }

    public static void setCharactersMap(Map<Integer, String> charactersMap) {
        AllCharacters.charactersMap.putAll(charactersMap);
    }

    public static void sortCharactersMap() {

        charactersMap = AllCharacters.getCharactersMap().entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e1, LinkedHashMap::new));
                        
    }
}
