package me.marques.anderson;

public class Entity {
    
    private String username;
    private String password;

    private Entity(){
            
    }
    
    public static Entity create(){
        return new Entity();
    }
    
    public Entity username(final String username){
        this.username = username;
        return this;
    }
    
    public Entity password(final String password){
        this.password = password;
        return this;
    }
    
    public String username(){
        return this.username;
    }

    public String password(){
        return this.password;
    }
}
