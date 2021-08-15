package cm.skysoft.app.web;

import cm.skysoft.app.config.ApplicationProperties;
import cm.skysoft.app.repository.UserRepository;
import cm.skysoft.app.security.AuthoritiesConstants;
import cm.skysoft.app.security.jwt.JWTFilter;
import cm.skysoft.app.security.jwt.TokenProvider;
import cm.skysoft.app.service.UserService;
import cm.skysoft.app.service.dto.UserDTO;
import cm.skysoft.app.web.vm.LoginVM;
import cm.skysoft.app.web.vm.TokenSocialVM;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final ApplicationProperties applicationProperties;

    private final UserService userService;

    private final UserRepository userRepository;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, ApplicationProperties applicationProperties, UserService userService, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.applicationProperties = applicationProperties;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/oauth/google")
    public ResponseEntity<JWTToken> google(@Valid @RequestBody TokenSocialVM tokenSocialVM) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                        .setAudience(Collections.singletonList(applicationProperties.getSocial().getGoogle().getClientId()));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenSocialVM.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();

        cm.skysoft.app.domain.User userNew = null;

        Optional<cm.skysoft.app.domain.User> userExist = userRepository.findOneByEmailIgnoreCase(payload.getEmail());

        if(userExist.isPresent()){
            userNew= userExist.get();
        }
        else{
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(payload.getEmail());
            userDTO.setLogin(payload.getEmail().split("@")[0]);

            Set<String> authorities = new HashSet<>();
            authorities.add(AuthoritiesConstants.ADMIN);
            userDTO.setAuthorities(authorities);

            userNew = userService.createUserSocial(userDTO, applicationProperties.getSocial().getSecretPwd());
        }

        return oauthSocial(userNew, applicationProperties.getSocial().getSecretPwd());
    }

    @PostMapping("/oauth/facebook")
    public ResponseEntity<JWTToken> facebook(@Valid @RequestBody TokenSocialVM tokenSocialVM) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenSocialVM.getValue());
        final String [] fields = {"email", "picture"};
        User user = facebook.fetchObject("me", User.class, fields);

        cm.skysoft.app.domain.User userNew = null;

        Optional<cm.skysoft.app.domain.User> userExist = userRepository.findOneByEmailIgnoreCase(user.getEmail());

        if(userExist.isPresent()){
            userNew = userExist.get();
        }
        else{
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(user.getEmail());
            userDTO.setLogin(user.getEmail().split("@")[0]);
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());

            Set<String> authorities = new HashSet<>();
            authorities.add(AuthoritiesConstants.ADMIN);
            userDTO.setAuthorities(authorities);

            userNew = userService.createUserSocial(userDTO, applicationProperties.getSocial().getSecretPwd());
        }

        return oauthSocial(userNew, applicationProperties.getSocial().getSecretPwd());
    }

    private ResponseEntity<JWTToken> oauthSocial(cm.skysoft.app.domain.User user, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getLogin(), password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
