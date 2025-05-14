package com.shortner.url_shortner.util;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;

import static org.apache.commons.text.CharacterPredicates.ASCII_ALPHA_NUMERALS;

@Component
public class RandomStringGeneratorUtil {

    private static final char[] BASE62 = (
        "abcdefghijklmnopqrstuvwxyz" +
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "0123456789"
    ).toCharArray();

    private final RandomStringGenerator generator =
      new RandomStringGenerator.Builder()
        .selectFrom(BASE62)
        .build();

    public String generateRandomString(int length){
        return generator.generate(length);
    }

}
