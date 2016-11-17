package com.boarbeard.bggauctions.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Leif on 11/16/2016.
 */

public class BidParserTest {

    @Test
    public void parsePrice() throws Exception {
        assertEquals("25.00", BidParser.parse("25.00"));
    }

    @Test
    public void parsePriceWithDollarSign() throws Exception {
        assertEquals("$25.00", BidParser.parse("other stuff and this $25.00"));
    }

    @Test
    public void parsePriceWithZip() throws Exception {
        assertEquals("$25.00", BidParser.parse("89332 $25.00"));
    }

    @Test
    public void parsePriceWithZip2() throws Exception {
        assertEquals("$25.00", BidParser.parse("$25.00 89332"));
    }

    @Test
    public void parsePriceWithZip3() throws Exception {
        assertEquals("$30.", BidParser.parse("$30...01826"));
    }

    @Test
    public void parsePriceWithZip4() throws Exception {
        assertEquals("$30", BidParser.parse("$30,01826"));
    }

}
