package ntou.soselab.easylearn.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ntou.soselab.easylearn.domain.entity.User;
import ntou.soselab.easylearn.model.json.request.AuthenticationRequest;
import ntou.soselab.easylearn.model.json.response.AuthenticationResponse;
import ntou.soselab.easylearn.repository.UserRepository;
import ntou.soselab.easylearn.security.TokenUtils;

@RestController
public class AuthController {
	private final Logger logger = Logger.getLogger(this.getClass());

	@Value("${cerberus.token.header}")
	private String tokenHeader;

	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	@CrossOrigin
	public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest) {

		// Perform the authentication
//		Authentication authentication = this.authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(
//				authenticationRequest.getUserId(), authenticationRequest.getToken()));
//		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Reload password post-authentication so we can generate token


		User user = new User(authenticationRequest.getId(), authenticationRequest.getToken());
		userRepository.save(user);
		String token = this.tokenUtils.generateToken(user);
		
		logger.info(user.toString());


		// Return the token
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}

	@RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
	@CrossOrigin
	public ResponseEntity<?> authenticationRequest(HttpServletRequest request) {
		String token = request.getHeader(this.tokenHeader);
		String id = this.tokenUtils.getUserIdFromToken(token);
		User user = userRepository.findById(id);
		
		logger.info(user.toString());

		// if (this.tokenUtils.canTokenBeRefreshed(token,
		// user.getLastPasswordReset())) {
		// String refreshedToken = this.tokenUtils.refreshToken(token);
		// return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
		// } else {
		// return ResponseEntity.badRequest().body(null);
		// }
		// check fb token again
		String refreshedToken = this.tokenUtils.refreshToken(token);
		return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
	}
}
