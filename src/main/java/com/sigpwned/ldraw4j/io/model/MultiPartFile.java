package com.sigpwned.ldraw4j.io.model;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import com.sigpwned.ldraw4j.model.file.FileType;
import com.sigpwned.ldraw4j.io.LDRAWLibrary;

public class MultiPartFile implements LDRAWLibrary {
    private final Map<String, String> chunks;
    private final String firstFile;

    public MultiPartFile(Reader reader) throws IOException {
        this.chunks = new HashMap<>();
        this.firstFile = split(reader, this.chunks);
    }

    public Reader getReader() {
        return new StringReader(firstFile);
    }

    @Override
    public Reader find(FileType type, String name) {
        if (chunks.containsKey(name)) {
            return new StringReader(chunks.get(name));
        }
        return null;
    }

    static String split(Reader reader, Map<String, String> chunks) throws IOException {
        String currentName = null;
        StringBuilder currentFile = new StringBuilder();
        String firstFile = null;
        BufferedReader br = new BufferedReader(reader);
        while (true) {
            String line = br.readLine();
            if (line == null) {
                if (currentName != null) {
                    chunks.put(currentName, currentFile.toString());
                    if (firstFile == null) {
                        firstFile = currentFile.toString();
                    }
                }
                reader.close();
                return firstFile;
            } else if (!line.isBlank()) {
                Matcher m = Pattern.compile("0\\s*(?:FILE|!DATA)\\s*(.*)").matcher(line);
                if (m.matches()) {
                    if (currentName != null) {
                        chunks.put(currentName, currentFile.toString());
                        if (firstFile == null) {
                            firstFile = currentFile.toString();
                        }
                    }
                    currentName = m.group(1).trim();
                    currentFile = new StringBuilder();
                } else {
                    currentFile.append(line).append('\n');
                }
            }
        }
    }
}
