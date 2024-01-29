package com.sigpwned.ldraw4j.io.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileSystemLDRAWLibrary extends AbstractLDRAWLibrary {
    private final File home;

    public FileSystemLDRAWLibrary(File home) {
        this.home = home;
    }

    public Reader findAtPath(String path) {
        File f = new File(home, path);
        if (f.canRead()) {
            try {
                return new FileReader(f);
            } catch (FileNotFoundException ex) {
                return null;
            }
        }
        return null;
    }
}