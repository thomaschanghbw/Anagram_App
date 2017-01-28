/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;


public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private int wordLength = DEFAULT_WORD_LENGTH;
    private String sortLetters(String word)
    {
        char[] ar = word.toCharArray();
        Arrays.sort(ar);
        return new String(ar);
    }

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            int word_len = word.length();
            String sorted_word = sortLetters(word);
            wordList.add(word);
            if(sizeToWords.containsKey(word_len))
            {
                sizeToWords.get(word_len).add(word);
            }
            else
            {
                sizeToWords.put(word_len, new ArrayList<String>());
                sizeToWords.get(word_len).add(word);
            }

            wordSet.add(word);
            if(lettersToWord.containsKey(sorted_word))
            {
                lettersToWord.get(sorted_word).add(word);//Does this work?
            }
            else
            {
                lettersToWord.put(sorted_word, new ArrayList<String>());//Does THIS work?
                lettersToWord.get(sorted_word).add(word); //
            }
        }
    }

    public boolean isGoodWord(String word, String base)
    {
        if(!wordSet.contains(word))
            return false;
        if(word.contains(base))
            return false;

        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        for(String word_in_list : wordList)
        {
            if(word_in_list.length() != targetWord.length())
                continue;
            if(sortLetters(word_in_list).equals(sortLetters(targetWord)))
            {
                result.add(word_in_list);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char alphabet_letter = 'a'; alphabet_letter <= 'z'; alphabet_letter++)
        {
            String word_plus_letter = word + alphabet_letter;
            result.addAll(getAnagrams(word_plus_letter));
        }
        return result;
    }

    public String pickGoodStarterWord()
    {
        Random rand = new Random();

        ArrayList<String> string_pool = sizeToWords.get(wordLength);
        int sizeOfWordPool = string_pool.size();
        int rand_val = rand.nextInt(sizeOfWordPool);
        for(int i = rand_val; i<sizeOfWordPool; i++)
        {
            String curr_word = string_pool.get(i);
            if(getAnagrams(curr_word).size() >= MIN_NUM_ANAGRAMS)
            {
                if(wordLength != MAX_WORD_LENGTH)
                    wordLength++;
                return curr_word;
            }
        }
        for(int i = 0; i< rand_val; i++)
        {
            String curr_word = string_pool.get(i);
            if(getAnagrams(curr_word).size() >= MIN_NUM_ANAGRAMS)
            {
                if(wordLength != MAX_WORD_LENGTH)
                    wordLength++;
                return curr_word;
            }
        }
        wordLength++;
        return pickGoodStarterWord();
    }
}
