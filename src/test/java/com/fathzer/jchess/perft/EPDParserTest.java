package com.fathzer.jchess.perft;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fathzer.games.perft.PerfTTestData;

class EPDParserTest {
    @Test
    void parse() throws IOException {
        final EPDParser parser = new EPDParser();
        final List<PerfTTestData> list = parser.read(EPDParser.class.getResourceAsStream("/com/fathzer/jchess/perft/Perft960.epd"), StandardCharsets.UTF_8);
        assertEquals(960, list.size());
        final PerfTTestData data = list.get(1);
        assertEquals("2", data.getName());
        assertEquals("2nnrbkr/p1qppppp/8/1ppb4/6PP/3PP3/PPP2P2/BQNNRBKR w HEhe - 1 9", data.getStartPosition());
        assertEquals(21, data.getCount(1));
    }

    @Test
    void truncatedFen() throws IOException {
        final EPDParser parser = new EPDParser();
        final List<PerfTTestData> list = parser.read(EPDParser.class.getResourceAsStream("/com/fathzer/jchess/perft/truncatedFen.epd"), StandardCharsets.UTF_8);
        assertEquals(1, list.size());
        final PerfTTestData data = list.get(0);
        assertEquals("1", data.getName());
        assertEquals("r4br1/8/2Q2npp/Pkn1p3/8/2PPP1qP/4bP2/RNB1KB2 b Q - 0 1", data.getStartPosition());
        assertEquals(1, data.getCount(1));
    }
}