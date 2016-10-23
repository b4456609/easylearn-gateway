package soselab.easylearn.client;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import soselab.easylearn.json.response.AuthenticationResponse;
import soselab.easylearn.json.response.FBReponse;

/**
 * Created by bernie on 10/23/16.
 */
@Component
public class FBClient {
    private final Logger logger = Logger.getLogger(this.getClass());

    public boolean isAuth(String token, String userId){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FBReponse> response = restTemplate.getForEntity(
                "https://graph.facebook.com/me?access_token=" + token,
                FBReponse.class);;
        logger.info("FB not response status code: " + response.getStatusCode().value());
        if (response.getStatusCode().is2xxSuccessful() && userId.equals(response.getBody().getId())) {
            return true;
        }
        return false;
    }
}
