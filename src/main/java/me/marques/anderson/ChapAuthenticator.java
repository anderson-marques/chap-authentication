package me.marques.anderson;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ChapAuthenticator {

    private final EntitiesRepository entitiesRepository;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        // In production Password is saved only hashed and salted ;)

        byte[] chapHashByteArray = md.digest("12345678".getBytes(StandardCharsets.UTF_8));
        final String chapHashBase64 = Base64.getEncoder().encodeToString(chapHashByteArray);
        System.out.println(chapHashBase64);
    }


    public ChapAuthenticator(EntitiesRepository entitiesRepository){
        this.entitiesRepository = entitiesRepository;
    }


    /**
     * Return actual challenge associated with the entity
     *
     * @param username - Entity username {@link String}
     * @return challenge {@link String}
     */
    public String challege(final String username){
        if (username == null || username.isEmpty() ){
            return  null;
        }

        Entity soughtEntity = this.entitiesRepository.entity(username);

        if (soughtEntity == null){
            return null;
        }

        return soughtEntity.challenge();
    }
    
    public Boolean authenticate(final String username, final String hash) {
        if (username == null || username.isEmpty()){
            return false;
        }
        if (hash == null || hash.isEmpty()){
            return false;
        }

        Entity soughtEntity = this.entitiesRepository.entity(username);

        if (soughtEntity == null){
            return false;
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // In production Password is saved only hashed and salted ;)
        final String passwordHashBase64 = soughtEntity.passwordHashSalted();
        final  String challenge = soughtEntity.challenge();

        byte[] chapHashByteArray = md.digest(challenge.concat(passwordHashBase64).getBytes(StandardCharsets.UTF_8));
        final String chapHashBase64 = Base64.getEncoder().encodeToString(chapHashByteArray);


        if (hash.equals(chapHashBase64)){
            return true;
        } else {
            return false;
        }
    }
}
