package authorizationserver.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import authorizationserver.entity.Document;
import authorizationserver.repositories.DocumentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.authentication.AuthenticationManager;

import authorizationserver.entity.AuthRequest;
import authorizationserver.entity.RegistingRequest;
import authorizationserver.entity.User;
import authorizationserver.repositories.UserRepository;
import authorizationserver.service.CustomUserDetailsService;
import authorizationserver.util.JwtUtil;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
@RequestMapping(value = "connect")
public class AuthenticateController {

    static class Examples {
        public static final String example = "{\"userName\": \"tiktzuki\", \"password\":\"P@ssword1\" }";
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<Object> generateToken(
            @RequestHeader Map<String, String> headers,
            @RequestBody(content = @Content(examples = {
                    @ExampleObject(
                            name = "Authentication sample",
                            summary = "Auth example",
                            value = Examples.example
                    )
            })) @org.springframework.web.bind.annotation.RequestBody AuthRequest authRequest
    ) throws Exception {
        //Kiem tra thong tin user
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        //Tra ve access token
        return new ResponseEntity<Object>(new Object() {
            public final String token = jwtUtil.generateToken(authRequest.getUserName(), headers.get("origin"));
            public final String refreshToken = "token";
        }, HttpStatus.OK);
    }

    @GetMapping("/userinfo")
    @Operation(security = {@SecurityRequirement(name = "authorization")})
    public ResponseEntity<User> getUserInfor(@RequestHeader Map<String, String> headers) {
        jwtUtil.validateToken(headers, userRepository::existsByUserName);
        String userName = jwtUtil.extractUsername(jwtUtil.getTokenFromHeaders(headers));
        User user = userRepository.findByUserName(userName);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createAccount(@RequestBody RegistingRequest request, @RequestHeader Map<String, String> headers) {
        //Tao user moi
        if (userDetailService.createUser(request))
            //Tra ve token
            return new ResponseEntity<Object>(new Object() {
                public final String token = jwtUtil.generateToken(request.getUserName(), headers.get("origin"));
                public final String refreshToken = "";
            }, HttpStatus.OK);
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUser() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/documents")
    public ResponseEntity<Object> createDocument(@RequestBody HashMap<Object, Object> document) throws SQLException {
        Document doc = new Document(1, document);
        documentRepository.save(doc);
        return ResponseEntity.ok(
                doc.getDoc()
        );
    }

    @GetMapping("/documents")
    public ResponseEntity<List<Object>> getDocuments() {
        List<Object> docs = documentRepository.findAll().stream().map(document -> document.getDoc()).collect(Collectors.toList());
        return ResponseEntity.ok(docs);
    }
}
