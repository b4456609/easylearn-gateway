package soselab.easylearn.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TokenUtilsTest {

    @Autowired
    TokenUtils tokenUtils;

    @Test
    public void getUserIdFromToken() throws Exception {
        String token = tokenUtils.generateToken("teste");
        String result = tokenUtils.getUserIdFromToken(token);
        System.out.println(token);
        assertThat(result).isEqualTo("teste");
    }
}