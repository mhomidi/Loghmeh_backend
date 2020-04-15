package services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import java.util.Calendar;
import java.util.Date;

public class Authentication {
    public static Algorithm algorithm = Algorithm.HMAC256("loghmeh");

    public static String  createToken(String id) {
        Date expire = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(expire);
        calender.add(Calendar.HOUR_OF_DAY, 2);
        expire = calender.getTime();

        return JWT.create()
            .withIssuer("loghmeh.com")
            .withIssuedAt(new Date())
            .withExpiresAt(expire)
            .withClaim("id", id)
            .sign(algorithm);
    }
}
