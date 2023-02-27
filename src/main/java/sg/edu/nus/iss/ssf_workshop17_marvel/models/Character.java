package sg.edu.nus.iss.ssf_workshop17_marvel.models;

public class Character {
   
    private Integer id;
    private String name;
    private String description;
    private String thumbnail; 
   // private List<String> comics;

    public Character(Integer id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
       // this.comics = comics;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    // public List<String> getComics() {
    //     return comics;
    // }
    // public void setComics(List<String> comics) {
    //     this.comics = comics;
    // }

    @Override
    public String toString() {
        return "Character [id=" + id + ", name=" + name + ", description=" + description + 
        ", thumbnail=" + thumbnail + "]";
    } 

    
        
}
