package com.example.android.sips;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonParserTest {

    @Test
    public void formatString() {
        JsonParser j = new JsonParser(null);          //some of the API database's ingredients were irregular
        String actual = j.formatString(" ", "Ice");                 //extra spaces sometimes occurred, as did newline characters at the end of terms
        assertEquals("Ice", actual);                       //testing that a space will be removed if it is all that exists in the string for part1
    }

    @Test
    public void formatString2() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString("4 cubes", "Ice");           //we want to add a space to part1 so that it looks readable, if it isn't already present
        assertEquals("4 cubes Ice", actual);
    }

    @Test
    public void formatString3() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString("4 pieces\n ", "Ice");    //newline characters should be removed so that strings look right
        assertEquals("4 pieces Ice", actual);
    }

    @Test
    public void formatString4() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString("3 blocks", "Ice\n");   //newline characters in part2 also need to be removed
        assertEquals("3 blocks Ice", actual);
    }

    @Test
    public void formatString5() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString("1\n", "Ice\n");       //if they're present in both cases, that should be handled as well
        assertEquals("1 Ice", actual);
    }
}