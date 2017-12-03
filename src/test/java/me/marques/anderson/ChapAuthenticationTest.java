package me.marques.anderson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.marques.anderson.Entity;
import me.marques.anderson.ChapAuthentication;


public class ChapAuthenticationTest {
    
    ChapAuthentication chapAuthenticator;
    Entity entity;
    
    @Before
    public void init(){
        this.entity = Entity.create();
        this.chapAuthenticator = ChapAuthentication.create();
    }
    
    @Test
    public void testShouldReturnFalseWhenUsernameIsNull(){
        Assert.assertFalse("Should return false when username is null", chapAuthenticator.authenticate(null, null));
    }
    
    @Test
    public void testShouldReturnFalseWhenUsernameIsEmpty(){
        Assert.assertFalse("Should return false when username is empty", chapAuthenticator.authenticate("", null));
    }
    
}
