import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fathzer.games.perft.PerfTParser;
import com.fathzer.games.perft.PerfTTestData;
import com.fathzer.jchess.perft.EPDParser;

public class EPDConverter {
    public List<String> toEpd(List<PerfTTestData> tests) {
        return tests.stream().map(this::toEpd).toList();
    }

    private String toEpd(PerfTTestData test) {
        StringBuilder sb = new StringBuilder();
        sb.append(test.getStartPosition());
        for (int i = 1; i < test.getSize(); i++) {
            sb.append(";D");
            sb.append(i);
            sb.append(' ');
            sb.append(test.getCount(i));
        }
        return sb.toString();
    }

    private List<String> toTxt(List<PerfTTestData> tests) {
        return tests.stream().map(this::toText).flatMap(x->x.stream()).toList();
    }

    public List<String> toText(PerfTTestData test) {
        final List<String> result = new ArrayList<>();
        result.add("name " + test.getName());
        result.add("position fen " + removeMoveCountFromFen(test.getStartPosition()));
        for (int i = 1; i < test.getSize(); i++) {
            result.add("perft"+i+" "+test.getCount(i));
        }
        result.add("");
        return result;
    }

    private String removeMoveCountFromFen(String fen) {
        String[] parts = fen.trim().split(" ");
        if (parts.length <= 4) {
            return fen;  // Return as is if it doesn't have move counts
        }
        // Reconstruct FEN without the last two numbers (halfmove clock and fullmove number)
        return String.join(" ", parts[0], parts[1], parts[2], parts[3]);
    }

/*
    public static void main(String[] args) throws IOException {
		try (InputStream stream = EPDConverter.class.getResourceAsStream("/Perft.txt")) {
            final EPDConverter converter = new EPDConverter();
            List<PerfTTestData> list = new PerfTParser().withStartPositionPrefix("position fen").withStartPositionCustomizer(s -> s+" 0 1").read(stream, StandardCharsets.UTF_8);
            List<String> epd = converter.toEpd(list);
            Files.write(Paths.get("jchess-perft-dataset/src/main/resources/Perft.epd"),epd,Charset.defaultCharset());
		}
    }*/

    public static void main(String[] args) throws IOException {
        try (InputStream stream = EPDConverter.class.getResourceAsStream("/com/fathzer/jchess/perft/Perft960.epd")) {
            final EPDConverter converter = new EPDConverter();
            List<PerfTTestData> list = new EPDParser().read(stream, StandardCharsets.UTF_8);
            List<String> epd = converter.toTxt(list);
            epd.stream().forEach(System.out::println);
            Files.write(Paths.get("jchess-perft-dataset/src/main/resources/com/fathzer/jchess/perft/Perft960.txt"),epd,Charset.defaultCharset());
		}
      
    }
}
