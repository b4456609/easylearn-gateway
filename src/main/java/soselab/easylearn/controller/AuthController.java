package soselab.easylearn.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soselab.easylearn.client.FBClient;
import soselab.easylearn.json.request.AuthenticationRequest;
import soselab.easylearn.json.response.AuthenticationResponse;
import soselab.easylearn.security.TokenUtils;

@RestController
public class AuthController {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Value("${cerberus.token.header}")
    private String tokenHeader;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private FBClient fbClient;

    @RequestMapping(value = "/api/auth", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("login user id " + authenticationRequest.getId());
        if (authenticationRequest.getId() == null && authenticationRequest.getToken() == null)
            return ResponseEntity.badRequest().build();

        boolean isAuth = fbClient.isAuth(authenticationRequest.getToken(), authenticationRequest.getId());
        if (isAuth){
            String token = this.tokenUtils.generateToken(authenticationRequest.getId());
            return ResponseEntity.ok(new AuthenticationResponse(token));
        }

        return ResponseEntity.unprocessableEntity().build();
    }

}
