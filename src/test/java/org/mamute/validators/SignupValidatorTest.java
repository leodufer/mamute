package org.mamute.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ResourceBundle;

import javax.validation.Validation;

import org.junit.Before;
import org.junit.Test;
import org.mamute.controllers.BrutalValidator;
import org.mamute.dao.TestCase;
import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.User;
import org.mamute.validators.EmailValidator;
import org.mamute.validators.SignupValidator;
import org.mamute.validators.UserValidator;

import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.Validator;

public class SignupValidatorTest extends TestCase {

    private UserDAO users;
    private Validator validator;
    private SignupValidator signupValidator;
	private UserValidator userValidator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;
	private ResourceBundle bundle;
    
    @Before
    public void setup() {
    	bundle = mock(ResourceBundle.class);
        users = mock(UserDAO.class);
        validator = new MockValidator();
        messageFactory = new MessageFactory(bundle);
        emailValidator = new EmailValidator(validator, users, messageFactory);
        javax.validation.Validator javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();
        BrutalValidator brutalValidator = new BrutalValidator(javaxValidator, validator, messageFactory);
        userValidator = new UserValidator(validator, emailValidator, messageFactory, brutalValidator);
        signupValidator = new SignupValidator(validator, userValidator, messageFactory);
    }

    @Test
    public void should_verify_email() {
        when(users.existsWithEmail("used@gmail.com")).thenReturn(true);
        User user = user("nome muito grande ai meu deus", "used@gmail.com");
        boolean valid = signupValidator.validate(user, "123456", "123456");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_email_without_domain() {
    	User user = user("nome muito grande ai meu deus", "usedgmail.com");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_passwords() throws Exception {
        when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
        User user = user("nome muito grande ai meu deus", "valid@gmail.com");
        boolean valid = signupValidator.validate(user, "123456", "1234567");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_tiny_password() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	User user = user("nome muito grande ai meu deus", "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, "123", "123");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_large_password() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	String password = 666*100 + "";
    	User user = user("nome", "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, password, password);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_empty_name() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	User user = user("", "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_large_name() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    	User user = user(name, "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_null() throws Exception {
        boolean valid = signupValidator.validate(null, "123", "1234");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_null_password() throws Exception {
    	User user = user("valid user valid user", "valid@gmail.com");
    	boolean valid = signupValidator.validate(user, null, null);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_valid_user() throws Exception {
        when(users.existsWithEmail("used@gmail.com")).thenReturn(false);
        User user = user("nome muito grande ai meu deus", "used@gmail.com");
        boolean valid = signupValidator.validate(user, "123456", "123456");
        
        assertTrue(valid);
    }

}
