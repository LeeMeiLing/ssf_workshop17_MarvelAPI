package sg.edu.nus.iss.ssf_workshop17_marvel.services;

import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.ssf_workshop17_marvel.models.AllCharacters;
import sg.edu.nus.iss.ssf_workshop17_marvel.models.Character;

@Service
public class CharacterService {

    @Value("${marvelapi.publickey}")
    private String publicKey;

    @Value("${marvelapi.privatekey}")
    private String privateKey;

    public static final String CHARACTERS = "https://gateway.marvel.com:443/v1/public/characters";

    // private static Map<Integer, String> charList = new HashMap<>();

    private static Integer offset = 0;
    private static Boolean cont = true;

    // this api has a limit of 100 characters loaded for one request
    public void loadAllCharacters() throws NoSuchAlgorithmException{

        while (cont) {

            
            // GET /http://gateway.marvel.com/v1/public/comics?ts=1&apikey=1234&hash=ffd275c5130566a2916217b101f26150

            // generate ts
            Instant ts = Instant.now();

            // generate hash: md5(ts+privateKey+publicKey)

            String toHash = ts.toString() + privateKey + publicKey;

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest of an input, digest() return array of byte
            byte[] digest = md.digest(toHash.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, digest);

            // convert to hash value
            // Convert message digest into hex value
            String hash = no.toString(16);
            while (hash.length() < 32) {
                hash = "0" + hash;
            }

            // System.out.println(">>>>>>>>>>>>>>>>> hash= " + hash); // debug

            // build the url
            String url = UriComponentsBuilder.fromUriString(CHARACTERS)
                    .queryParam("ts", ts)
                    .queryParam("apikey", publicKey)
                    .queryParam("hash", hash)
                    .queryParam("limit", 100)
                    .queryParam("offset", offset)
                    .toUriString();

            // System.out.println(url); // debug

            // contruct request entity
            RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();

            // construct REST template
            RestTemplate template = new RestTemplate();

            // Make the request & receive response entity, the payload of the response will be a string
            ResponseEntity<String> resp = template.exchange(req, String.class);

            // read status code
            Integer statusCode = resp.getStatusCode().value();

            // read payload into Json Object
            String body = resp.getBody();

            JsonReader reader = Json.createReader(new StringReader(body));
            JsonObject obj = reader.readObject();
            JsonObject json = obj.getJsonObject("data");
            Integer totalChar = json.getInt("total");
            JsonArray arr = json.getJsonArray("results");

            // // ====Using for loop to add characters====
            // Integer id;
            // String name;

            // for (int i = 0; i < arr.size(); i++){

            //     json = arr.getJsonObject(i);

            //     // Extract only the data field we want
            //     id = json.getInt("id");
            //     name = json.getString("name");
            //     AllCharacters.addCharacter(id,name);

            // }


            // ====Using stream instead of for loop to add characters====
            AllCharacters.setCharactersMap( arr.stream()
                        .map(v -> v.asJsonObject())
                        .collect(Collectors.toMap(j -> j.getInt("id"), j -> j.getString("name"))));


            offset = offset + 100;
            if (!(offset <= (totalChar - 100))){
                cont = false;
            }
            System.out.printf(">>>>>>>>> offset = %d\n",offset);

        }

        AllCharacters.sortCharactersMap();

        // // ========if using for loop========
        // charList = AllCharacters.getCharactersMap();
        // //System.out.println("charList = :" + charList); // debug
        // return charList;  // // Alternatively, return AllCharacters.getCharactersMap();


        // // ========if using stream========
        // // System.out.println("charList = :" + charList); // debug
        // return charList;
    }


    public Map<Integer, String> getCharactersMap (){

        return AllCharacters.getCharactersMap();

    }

    public Character getCharacter(Integer id) throws NoSuchAlgorithmException{

        // GET /https://gateway.marvel.com:443/v1/public/characters/1009664?apikey=apiKey

        // generate ts
        Instant ts = Instant.now();

        // generate hash: md5(ts+privateKey+publicKey)
        String toHash = ts.toString() + privateKey + publicKey;

        // Static getInstance method is called with hashing MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // digest() method is called to calculate message digest of an input, digest() return array of byte
        byte[] digest = md.digest(toHash.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, digest);

        // convert to hash value
        // Convert message digest into hex value
        String hash = no.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }

        // build the url
        String url = UriComponentsBuilder.fromUriString(CHARACTERS + "/" + id)
                .queryParam("ts", ts)
                .queryParam("apikey", publicKey)
                .queryParam("hash", hash)
                .toUriString();

        // System.out.println(url); // debug

        // contruct request entity
        RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();

        // construct REST template
        RestTemplate template = new RestTemplate();

        // Make the request & receive response entity, the payload of the response will be a string
        ResponseEntity<String> resp = template.exchange(req, String.class);

        // read status code
        Integer statusCode = resp.getStatusCode().value();

        // read payload into Json Object
        String body = resp.getBody();
        // System.out.println(">>>>>>>> body = " + body); // debug

        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject obj = reader.readObject();
        JsonObject json = obj.getJsonObject("data");
        JsonArray arr = json.getJsonArray("results");
        json = arr.getJsonObject(0);
        String thumbnail_path = json.getJsonObject("thumbnail").getString("path");
        // String thumbnail_ext = json.getJsonObject("thumbnail").getString("extension");
        // String thumbnail = thumbnail_path + thumbnail_ext;

        Character character = new Character(json.getInt("id"), json.getString("name"), json.getString("description"), thumbnail_path);
        // System.out.println(character); // debug

        return character;
    }

}
