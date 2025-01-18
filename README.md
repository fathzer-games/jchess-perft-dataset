[![Maven Central](https://img.shields.io/maven-central/v/com.fathzer/jchess-perft-dataset)](https://central.sonatype.com/artifact/com.fathzer/jchess-perft-dataset)
[![License](https://img.shields.io/badge/License-GNU%20GPL-brightgreen.svg)](https://github.com/fathzer-games/jchess-perft-dataset/blob/master/LICENSE)

# jchess-perft-dataset
A chess [Perft](https://www.chessprogramming.org/Perft) DataSet

This artifact contains standard chess and [chess960](https://en.wikipedia.org/wiki/Chess960) perft resource files in two formats:
- [EPD (Extended Position Description) format](https://www.chessprogramming.org/Extended_Position_Description)
- The format expected by ```com.fathzer.games.perft.PerftParser``` in [com.fathzer:games-core](https://github.com/fathzer-games/games-core) (see below).

These files are also available [here](https://github.com/fathzer-games/jchess-perft-dataset/tree/main/src/main/resources).

The standard chess data set was compiled by the author of [this video on perft method](https://www.youtube.com/watch?v=HGpH28hCw7E&t=2s).
The chess960 data set is a copy of [this page](https://www.chessprogramming.org/Chess960_Perft_Results).


## How to parse EPD files

Here is an example using the EPDParser class included in this library.
```java
try (InputStream stream = getClass().getResourceAsStream("/com/fathzer/jchess/perft/Perft.txt")) {
	List<PerfTTestData> tests = new EPDParser().read(stream, StandardCharsets.UTF_8);
}
```
Please note that the PerfTTestData class is from [com.fathzer:games-core library](https://github.com/fathzer-games/games-core). As the maven dependency to this library is optional, you have to add the games-core artifact in your pom.xml in order to run this example.

## How to parse [com.fathzer:games-core library](https://github.com/fathzer-games/games-core) files

These files can be parsed by a Java program using the PerfTParser of [com.fathzer:games-core library](https://github.com/fathzer-games/games-core).  
For instance, the following will return a list of PerfT test with start position in [FEN format](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation).
```java
try (InputStream stream = getClass().getResourceAsStream("/com/fathzer/jchess/perft/Perft.txt")) {
	List<PerfTTestData> tests = new PerfTParser().withStartPositionPrefix("position fen").withStartPositionCustomizer(s -> s+" 0 1").read(stream, StandardCharsets.UTF_8);
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
