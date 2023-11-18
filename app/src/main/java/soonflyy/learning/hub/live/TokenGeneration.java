package soonflyy.learning.hub.live;

import static soonflyy.learning.hub.Common.Constant.API_KEY;
import static soonflyy.learning.hub.Common.Constant.SECRETE_KEY;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenGeneration {

    public static String getTocken() {
        String token = "";

        Map<String, Object> payload = new HashMap<>();
        payload.put("apikey", API_KEY);
        payload.put("permissions", new String[]{"allow_join", "allow_mod"});

        token = Jwts.builder().setClaims(payload)
                .setExpiration(new Date(System.currentTimeMillis() + 86400 * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRETE_KEY.getBytes()).compact();
        return token;
    }

}

