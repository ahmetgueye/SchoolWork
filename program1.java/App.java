package edu.sdsu.cs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class App {

    public static void main(String[] args) {

        String pathToGenerateStats = "."; //Default to current folder

        if(args.length >= 1)
            pathToGenerateStats = args[0]; // Assumption: the given path is valid

        FIleStatistics fileStats = new FIleStatistics();
        if (!fileStats.pullFilesAndGenerateStats(pathToGenerateStats))
            System.out.println("Please check for the given path or folder structure");
    }
}

class FIleStatistics {

    private List<File> files; // Store all files recursively from the givenFile
    private List<String> lines; // store file stats , once processed resetting
    // it.

    public FIleStatistics() {
        files = new ArrayList<File>();
    }

    private void resetLines() {
        lines = new ArrayList<String>();
    }

    /**
     * Pulls all file details to files List.
     *
     * @param givenFile
     */
    private void getFiles(File givenFile) {

        if (givenFile.isDirectory())
            for (File currentFile : givenFile.listFiles())
                getFiles(currentFile);
        else if (givenFile.isFile()) {
            String filefullPath = givenFile.getAbsolutePath();
            String fileExtension = filefullPath.substring(filefullPath.lastIndexOf("."));
            if (fileExtension.equalsIgnoreCase(".txt") || fileExtension.equalsIgnoreCase(".java")) {

                System.out.println(givenFile.getAbsolutePath());
                files.add(givenFile);
            }
        }

    }

    private void generateStats() throws IOException {
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
            resetLines();

            calCulateStats(file);

            writeToFile(Paths.get(file.getAbsolutePath() + ".stats"), lines);

        }

    }

    private void calCulateStats(File f) throws IOException {
        List<Token> caseSensitivitytokens = new ArrayList<Token>();
        List<Token> caseInSensitivitytokens = new ArrayList<Token>();

        int maxLineLength = 0;
        int totalLineLength = 0;

        List<String> linesfromFile = Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.defaultCharset());
        List<String> inLinewords = new ArrayList<String>();

        for (String line : linesfromFile) {
            totalLineLength = totalLineLength + line.length();

            if (line.length() > maxLineLength)
                maxLineLength = line.length();

            for (String word : line.split("\\s+"))
                inLinewords.add(word); // Store all words from the file to inLinewords

        }

        caseInSensitivitytokens = processWordsCountByCaseInsensitivity(inLinewords);
        caseSensitivitytokens = processWordsCountByCasesensitivity(inLinewords);

        this.lines.add("Length of longest line in file : " + maxLineLength);
        this.lines.add("Average line length : " + totalLineLength / linesfromFile.size());
        this.lines.add("Number of unique space-delineated tokens (case-sensitive) : " + caseSensitivitytokens.size());
        this.lines.add("Number of unique space-delineated tokens (case-insensitive) : " + caseInSensitivitytokens.size());
        this.lines.add("Number of all space-delineated tokens in file : " + (maxLineLength == 0 ? 0 : inLinewords.size()));

        Collections.sort(caseInSensitivitytokens);

        // if the tokens are less than 10 count
        StringBuffer sb = new StringBuffer(50);
        for (int i = 0; i < 10 && i < caseInSensitivitytokens.size() - 1; ++i)
            sb.append("\n" + String.format("%7s : %5d", caseInSensitivitytokens.get(i).word , caseInSensitivitytokens.get(i).o));

        this.lines.add("10 most frequent tokens with their counts (case-insensitive): " + sb.toString() + "\n");

        sb = new StringBuffer(50);
        int startFromIndex = caseInSensitivitytokens.size() >= 10 ? 9 : caseInSensitivitytokens.size() - 1;
        for (; startFromIndex >= 0; --startFromIndex)
            sb.append("\n" + String.format("%7s : %5d", caseInSensitivitytokens.get(startFromIndex).word, caseInSensitivitytokens.get(startFromIndex).o));
        this.lines.add("10 most least tokens with their counts (case-insensitive): " + sb.toString());

        int mostFrequentOccuringTokens = 0;
        this.lines.add("Most frequent token(s) : ");
        if (caseInSensitivitytokens.size() > 0) {

            int tempCount = caseInSensitivitytokens.get(0).o;
            Iterator<Token> it = caseInSensitivitytokens.iterator();
            while (it.hasNext()) {
                Token t = it.next();
                if (t.o != tempCount)
                    break;
                this.lines.add(t.word);
                ++mostFrequentOccuringTokens;
            }
        }

        this.lines.add("Most frequent tokens count : " + mostFrequentOccuringTokens);

    }

    private List<Token> processWordsCountByCaseInsensitivity(final List<String> inLineWords) {

        final List<String> stringTokens = new ArrayList<String>();
        final List<Integer> count = new ArrayList<Integer>();
        final List<Token> tokens = new ArrayList<Token>();

        for (final String inlineWord : inLineWords) {
            if (inlineWord.trim().equals(""))
                continue;
            final int index = stringTokens.indexOf(inlineWord.toLowerCase());
            if (index < 0) {
                stringTokens.add(inlineWord.toLowerCase());
                count.add(1);
            } else {
                int currentCount = count.get(index);
                count.set(index, ++currentCount);
            }

        }

        for (int index = 0; index < stringTokens.size() - 1; ++index)
            tokens.add(new Token(stringTokens.get(index), count.get(index)));
        return tokens;
    }

    private List<Token> processWordsCountByCasesensitivity(List<String> inLineWords) {
        List<String> stringTokens = new ArrayList<String>();
        List<Integer> count = new ArrayList<Integer>();
        List<Token> $ = new ArrayList<Token>();
        for (String inlineWord : inLineWords) {
            if (inlineWord.trim().equals(""))
                continue;
            int index = stringTokens.indexOf(inlineWord);
            if (index < 0) {
                stringTokens.add(inlineWord);
                count.add(1);
            } else {
                int currentCount = count.get(index);
                count.set(index, ++currentCount);
            }

        }
        for (int index = 0; index < stringTokens.size() - 1; ++index)
            $.add(new Token(stringTokens.get(index), count.get(index)));
        return $;
    }

    private void writeToFile(Path location, List<String> toWrite) throws IOException {
        Files.write(location, toWrite, Charset.defaultCharset());
    }

    public Boolean pullFilesAndGenerateStats(String pathToGenerateStats) {
        File givenFile = new File(pathToGenerateStats);

        if (!givenFile.exists())
            return false; // If given path is invalid or non existent

        getFiles(givenFile);

        try {
            generateStats(); // Formulate stats and generate files with .stats
        } catch (IOException e) {
            return false;
        }

        return true;
    }

}

class Token implements Comparable<Token> {
    String word;
    int o;

    public Token(String word, int count) {

        this.word = word;
        this.o = count;
    }

    @Override
    public int compareTo(Token t) {
        return t.o - this.o;
    }

}

