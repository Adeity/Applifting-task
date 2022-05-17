package cz.applifting.task.service;

import cz.applifting.task.exceptions.BadUrlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(
        MockitoExtension.class
)
class RequestSenderServiceTest {
    @InjectMocks
    RequestSenderService service;


    @Test
    void urlIsNotFound() {
        // arrange
        String url = "http://notfound.localhost.cz";

        // act
        boolean expected = true;
        boolean actual = service.urlIsNotFound(url);

        // verify
        assertEquals(expected, actual);
    }

    @Test
    void urlIsFound() {
        // arrange
        String url = "http://google.cz";

        // act
        boolean expected = false;
        boolean actual = service.urlIsNotFound(url);

        // verify
        assertEquals(expected, actual);
    }

    @Test
    void urlIsInvalid() {
        // arrange
        String url = "htp://google.cz";

        // act
        boolean expected = true;
        boolean actual = service.urlIsInvalid(url);

        // verify
        assertEquals(expected, actual);
    }


    @Test
    void urlIsValid() {
        // arrange
        String url = "http://google.cz";


        // act
        boolean expected = false;
        boolean actual = service.urlIsInvalid(url);

        // verify
        assertEquals(expected, actual);
    }

    @Test
    void urlIsInvalid2() {
        // arrange
        String url = "BAD_URL";


        // act
        boolean expected = true;
        boolean actual = service.urlIsInvalid(url);

        // verify
        assertEquals(expected, actual);
    }
}