package tokenManager;

import exception.Reason;
import exception.TokenException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class TokenControl {
    private final String prefix = "Bearer ";
    private final String saltPath;
    private final String tokenName;
    private final String issuer;
    private final ModelMapper mapper;


    public TokenControl(String saltPath, String tokenName, String issuer) {
        this.saltPath = saltPath;
        this.tokenName = tokenName;
        this.issuer = issuer;
        this.mapper = new ModelMapper();
    }

    private String getSecretKey() {
        Path path = Path.of(saltPath);
        String privateKey = null;
        try {
        if(!Files.exists(path)) privateKey =  this.readDefault();
            privateKey = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
            privateKey = this.readDefault();
        }


        return  privateKey;
    }
    private String readDefault() {
        Set<Character> upperCase = IntStream.rangeClosed(65, 90)
                                    .mapToObj(unicode -> (char) unicode)
                                    .collect(Collectors.toSet());
        Set<Character> lowerCase = IntStream.rangeClosed(97, 122)
                                    .mapToObj(unicode -> (char) unicode)
                                    .collect(Collectors.toSet());
        Set<Character> number = IntStream.rangeClosed(48, 57)
                                    .mapToObj(unicode -> (char) unicode)
                                    .collect(Collectors.toSet());

        Set<Character> reference = new HashSet<>();

        reference.addAll(upperCase);
        reference.addAll(lowerCase);
        reference.addAll(number);

        return reference.stream().map(String::valueOf).sorted((o1, o2) -> {
            Random random = new Random();
            return (random.nextInt(2) - 1);
        }).collect(Collectors.joining());
    }

    public String encrypt(Map<String, Object> map) {
        return String.format("%s%s", prefix, Jwts.builder()
                .setHeaderParam(Header.TYPE, tokenName)
                .setClaims(map)
                .setIssuer(this.issuer)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, this.getSecretKey()).compact()
        );
    }

    public <T> T decrypt(String token, Class<T> clazz) throws TokenException {
        Map<String, Object> claims = null;
        if(!token.startsWith(prefix)) throw new TokenException(Reason.INVALID_TOKEN);
        String withoutBearer = token.replace(prefix, "");
        claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJwt(withoutBearer).getBody();
        return mapper.map(claims, clazz);
    }
}
