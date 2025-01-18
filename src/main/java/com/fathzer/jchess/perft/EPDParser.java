package com.fathzer.jchess.perft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.fathzer.games.perft.PerfTTestData;

/** An <a href="https://www.chessprogramming.org/Extended_Position_Description">EPD parser</a>
 */
public class EPDParser {
	private String fen;

	private static class UnclosedReader extends InputStreamReader {
		public UnclosedReader(InputStream in, Charset set) {
			super(in, set);
		}

		@Override
		public void close() throws IOException {
			// Don't close underlying stream
		}
	}

	/** Reads a list of PerfTTestData from an EPD input
	 * @param stream The stream to read (warning, this method does not close the stream)
	 * @param cs The charset of the stream
	 * @return The list of PerfTTestData
	 * @throws IOException If an I/O error occurs
	 */
	public List<PerfTTestData> read(InputStream stream, Charset cs) throws IOException {
		try (BufferedReader reader = new BufferedReader(new UnclosedReader(stream, cs))) {
			final List<PerfTTestData> result = new ArrayList<>();
			int lineNumber = 0;
			for (String line = reader.readLine(); line!=null; line = reader.readLine()) {
				lineNumber++;
				result.add(parse(Integer.toString(lineNumber), line));
			}
			return result;
		}
	}

	private PerfTTestData parse(String name, String line) {
		final String[] parts = line.split(";");
		final String fen = toFen(parts[0]);
		final PerfTTestData result = new PerfTTestData(name, fen);
		for (int i = 1; i < parts.length; i++) {
			String[] fields = parts[i].trim().split(" ");
			if (!fields[0].trim().equals("D"+i)) {
				throw new IllegalArgumentException("Seems depths are not sequential or don't start at 1");
			}
			result.add(Long.parseLong(fields[1]));
		}
		return result;
	}

	private String toFen(String content) {
		final String fen = content.trim();
		final String[] parts = fen.split(" ");
		if (parts.length==6) {
			return fen;
		}
		if (parts.length<4) {
			throw new IllegalArgumentException("This FEN definition is invalid: "+fen);
		}
		return parts[0]+" "+parts[1]+" "+parts[2]+" "+parts[3]+" 0 1";
	}
}
