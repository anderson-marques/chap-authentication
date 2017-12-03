package me.marques.anderson;

public class ChapAuthentication {
    
    public static ChapAuthentication create(){
        return new ChapAuthentication();
    }
    
    private ChapAuthentication(){}
    
    public Boolean authenticate(final String username, final String password){
        if (username == null || username.isEmpty()){
            return false;
        }
        return null;
    }
}
