[![Maven Central](https://img.shields.io/maven-central/v/com.fathzer/jchess-perft-dataset)](https://central.sonatype.com/artifact/com.fathzer/jchess-perft-dataset)
[![License](https://img.shields.io/badge/License-GNU%20GPL-brightgreen.svg)](https://github.com/fathzer-games/jchess-perft-dataset/blob/master/LICENSE)

# jchess-perft-dataset
A chess [Perft](https://www.chessprogramming.org/Perft) DataSet

This artifact only contains the **/Perft.txt** resource file.

This file is also available [here](https://fathzer-games.github.io/jchess-perft-dataset/Perft.txt).

It was compiled by the author of [this video on perft method](https://www.youtube.com/watch?v=HGpH28hCw7E&t=2s).


This file can be parsed by a Java program using the PerfTParser of [com.fathzer:games-core library](https://github.com/fathzer-games/games-core).  
For instance, the following will return a list of PerfT test with start position in [FEN format](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation).
```java
try (InputStream stream = getClass().getResourceAsStream("/Perft.txt")) {
	return new PerfTParser().withStartPositionPrefix("position fen").withStartPositionCustomizer(s -> s+" 0 1").read(stream, StandardCharsets.UTF_8);
}
```

If you use another language or need your own parser here is the format description.
- Lines starting with # are comments.
- Empty or blank lines before first test data should be ignored.
- A perfT test starts with a name line and ends at the next name line (or the end of file).
  - A name line starts with '*name* ' followed by an id of the test.
  - The line immediately after the name line contains the position in [FEN format](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation) variant where half move count and move number are missing.  
  The position is prefixed by '*position fen* '.
  - The other lines are result lines. They start with '*perft* ' followed by a depth and the number of leaves at that depth in the tree obtained by playing all possible moves.  
  The first result line is for depth 1. Second for depth 2, etc ... With no missing depth between 1 and the maximum depth for that test.  
  The maximum depth can vary from one test to another.
