package com.example.shortenurl.common.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for generating long url to short url anc vice versa
 * Date 2021-04-18 11:12:22
 * @author khoitd
 */
@Component
public class UrlConverter {

    /**
     * Method for generate for uniqueId use for generating short url
     * @param id
     * @return {@link String}
     */
    public static String createUniqueID(Long id) {
        List<Character> indexToCharTable = initializeIndexToCharTable();
        List<Integer> base62ID = convertBase10ToBase62ID(id);
        StringBuilder uniqueURLID = new StringBuilder();
        for (int digit: base62ID) {
            uniqueURLID.append(indexToCharTable.get(digit));
        }
        System.out.println("uniqueURLID: " + uniqueURLID.toString());
        return uniqueURLID.toString();
    }

    /**
     * Method for revert uniqueId
     * @param uniqueID
     * @return {@link Long}
     */
    public static Long getDictionaryKeyFromUniqueID(String uniqueID) {
        List<Character> base62IDs = new ArrayList<>();
        for (int i = 0; i < uniqueID.length(); ++i) {
            base62IDs.add(uniqueID.charAt(i));
        }
        Long dictionaryKey = convertBase62ToBase10ID(base62IDs);
        return dictionaryKey;
    }

    private static List<Integer> convertBase10ToBase62ID(Long id) {
        List<Integer> digits = new LinkedList<>();
        while(id > 0) {
            int remainder = (int)(id % 62);
            ((LinkedList<Integer>) digits).addFirst(remainder);
            id /= 62;
        }
        return digits;
    }

    private static Long convertBase62ToBase10ID(List<Character> ids) {
        HashMap<Character, Integer> charToIndexTable = initializeCharToIndexTable();
        long id = 0L;
        for (int i = 0, exp = ids.size() - 1; i < ids.size(); ++i, --exp) {
            int base10 = charToIndexTable.get(ids.get(i));
            id += (base10 * Math.pow(62.0, exp));
        }
        return id;
    }

    private static HashMap<Character, Integer> initializeCharToIndexTable() {
        HashMap<Character, Integer> charToIndexTable = new HashMap<>();
        // 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
        for (int i = 0; i < 26; ++i) {
            char c = 'a';
            c += i;
            charToIndexTable.put(c, i);
        }
        for (int i = 26; i < 52; ++i) {
            char c = 'A';
            c += (i-26);
            charToIndexTable.put(c, i);
        }
        for (int i = 52; i < 62; ++i) {
            char c = '0';
            c += (i - 52);
            charToIndexTable.put(c, i);
        }
        return charToIndexTable;
    }

    private static List<Character> initializeIndexToCharTable() {
        // 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
        List<Character> indexToCharTable = new ArrayList<>();
        for (int i = 0; i < 26; ++i) {
            char c = 'a';
            c += i;
            indexToCharTable.add(c);
        }
        for (int i = 26; i < 52; ++i) {
            char c = 'A';
            c += (i-26);
            indexToCharTable.add(c);
        }
        for (int i = 52; i < 62; ++i) {
            char c = '0';
            c += (i - 52);
            indexToCharTable.add(c);
        }
        return indexToCharTable;
    }

    public static void main(String[] args) {
        Long id = getDictionaryKeyFromUniqueID("b");
        System.out.println(id);
    }
}
