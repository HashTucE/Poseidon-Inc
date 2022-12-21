package com.openclassrooms.poseidon.util;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordConstraintValidatorTest {



    @InjectMocks
    private PasswordConstraintValidator passwordConstraintValidator;

    @Mock
    private ConstraintValidatorContext context;



    @Test
    public void isValidTest() {

        //given
        String password = "Abcd1234!";

        //when
        boolean result = passwordConstraintValidator.isValid(password, context);

        //then
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());

    }
}
