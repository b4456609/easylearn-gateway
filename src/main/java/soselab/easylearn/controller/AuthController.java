package soselab.easylearn.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import soselab.easylearn.entity.User;
import soselab.easylearn.json.request.AuthenticationRequest;
import soselab.easylearn.json.response.AuthenticationResponse;
import soselab.easylearn.json.response.FBReponse;
import soselab.easylearn.repository.UserRepository;
import soselab.easylearn.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Value("${cerberus.token.header}")
    private String tokenHeader;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/api/auth", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("login user id " + authenticationRequest.getId());
        if(authenticationRequest.getId() == null && authenticationRequest.getToken() == null)
            return ResponseEntity.badRequest().build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FBReponse> response = restTemplate.getForEntity(
                "https://graph.facebook.com/me?access_token="+authenticationRequest.getToken(),
                FBReponse.class);
        logger.info("FB not response status code: " + response.getStatusCode().value());

        if(!response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.unprocessableEntity().body("FB request fail");
        }

        if(response.getStatusCode().is2xxSuccessful() && authenticationRequest.getId().equals(response.getBody().getId())) {
            User user = new User(authenticationRequest.getId(), authenticationRequest.getToken());
            userRepository.save(user);
            String token = this.tokenUtils.generateToken(user);

            logger.info(user.toString());


            // Return the token
            return ResponseEntity.ok(new AuthenticationResponse(token));
        }
        return ResponseEntity.unprocessableEntity().build();
    }

}
