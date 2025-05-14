package com.shortner.url_shortner.util;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;

import static org.apache.commons.text.CharacterPredicates.ASCII_ALPHA_NUMERALS;

@Component
public class RandomStringGeneratorUtil {

    private final RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', '9').withinRange('a', 'z').withinRange('A', 'Z').build();

    public String generateRandomString(int length){
        return generator.generate(length);
    }

}
