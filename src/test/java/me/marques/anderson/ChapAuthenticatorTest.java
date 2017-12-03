package me.marques.anderson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.*;


public class ChapAuthenticatorTest {
    
    @Mock
    Entity fulano;

    @Mock
    EntitiesRepository entitiesRepository;

    ChapAuthenticator authenticator;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.authenticator = new ChapAuthenticator(entitiesRepository);
        // Entity repository searching for 'beltrano@example.com' mock
        Mockito.when(entitiesRepository.entity("beltrano@example.com")).thenReturn(null);
        // Entity repository searching for 'fulano@example.com' mock
        Mockito.when(entitiesRepository.entity("fulano@example.com")).thenReturn(fulano);
        // Fulano entity mock
        Mockito.when(fulano.challenge()).thenReturn("challenge-example");
        Mockito.when(fulano.passwordHashSalted()).thenReturn("JdVa0oOqQAr0ZMdtcTwHrQ=="); // Simple hash MD5 of '12345678' password + base64
        Mockito.when(fulano.username()).thenReturn("fulano@example.com");
    }

    @Test
    public void testShouldReturnChallengeNullWhenUsernameIsNull(){
        assertNull(this.authenticator.challege(null));
    }

    @Test
    public void testShouldReturnChallengeNullWhenUsernameIsEmpty(){
        assertNull(this.authenticator.challege(""));
    }

    @Test
    public void testBeltranoChallengeShouldBeNull(){
        assertNull(this.authenticator.challege("beltrano@example.com"));
    }

    @Test
    public void testFulanoChallengeShoulBeChallengeExample(){
        final String username = "fulano@example.com";
        final String challengeExample = "challenge-example";

        assertEquals(challengeExample,  this.authenticator.challege(username));
    }

    @Test
    public void testAuthenticationShoudFailIfUsernameNull(){
        assertFalse(this.authenticator.authenticate(null, "somehash"));
    }

    @Test
    public void testAuthenticationShoudFailIfUsernameEmpty(){
        assertFalse(this.authenticator.authenticate("", "somehash"));
    }

    @Test
    public void testAuthenticationShoudFailIfUserNotFound(){
        assertFalse(this.authenticator.authenticate("beltrano@example.com", "somehash"));
    }

    @Test
    public void testAuthenticationShoudFailIfHashNull(){
        assertFalse(this.authenticator.authenticate("fulano@example.com", null));
    }

    @Test
    public void testAuthenticationShoudFailIfPasswordNotMatch() throws NoSuchAlgorithmException {
        final String username = "fulano@example.com";
        final String challenge = this.authenticator.challege(username);
        final String password = "87654321";

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] passwordHashByteArray = md.digest(password.getBytes(StandardCharsets.UTF_8));
        final String passwordHashBase64 = Base64.getEncoder().encodeToString(passwordHashByteArray);

        byte[] chapHashByteArray = md.digest(challenge.concat(passwordHashBase64).getBytes(StandardCharsets.UTF_8));
        final String chapHashBase64 = Base64.getEncoder().encodeToString(chapHashByteArray);

        assertFalse(this.authenticator.authenticate(username, chapHashBase64));
    }

    @Test
    public void testAuthenticationShoudSucceedIfPasswordMatch() throws NoSuchAlgorithmException {
        final String username = "fulano@example.com";
        final String challenge = this.authenticator.challege(username);
        final String password = "12345678";

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] passwordHashByteArray = md.digest(password.getBytes(StandardCharsets.UTF_8));
        final String passwordHashBase64 = Base64.getEncoder().encodeToString(passwordHashByteArray);

        byte[] chapHashByteArray = md.digest(challenge.concat(passwordHashBase64).getBytes(StandardCharsets.UTF_8));
        final String chapHashBase64 = Base64.getEncoder().encodeToString(chapHashByteArray);


        // In chap authentication the user (entity) never sent his password. Only the hash of password + challenge.
        // The challenge is under control of the server. So, the server can update the challenge whenever it wants.
        assertTrue(this.authenticator.authenticate(username, chapHashBase64));
    }

}
