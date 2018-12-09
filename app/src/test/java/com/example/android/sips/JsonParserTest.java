package com.example.android.sips;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonParserTest {

    @Test
    public void formatString() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString(" ", "Ice");
        assertEquals("Ice", actual);
    }

    @Test
    public void formatString2() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString("4 cubes", "Ice");
        assertEquals("4 cubes Ice", actual);
    }

    @Test
    public void formatString3() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString("4 pieces\n ", "Ice");
        assertEquals("4 pieces Ice", actual);
    }

    @Test
    public void formatString4() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString("3 blocks", "Ice\n");
        assertEquals("3 blocks Ice", actual);
    }

    @Test
    public void formatString5() {
        JsonParser j = new JsonParser(null);
        String actual = j.formatString("1\n", "Ice\n");
        assertEquals("1 Ice", actual);
    }
}