package com.sigpwned.ldraw4j.io.library;

import com.sigpwned.ldraw4j.io.LDRAWLibrary;
import com.sigpwned.ldraw4j.model.file.FileType;
import java.io.Reader;

public abstract class AbstractLDRAWLibrary implements LDRAWLibrary {

    public Reader find(FileType type, String name) {
        switch (type) {
            case CONFIGURATION:
                return findAtPath("/" + name.replace('\\', '/'));
            case MODEL:
                return findAtPath("/models/" + name.replace('\\', '/'));
            case PART:
                return findAtPath("/parts/" + name.replace('\\', '/'));
            case SUBPART:
                return findAtPath("/parts/s/" + name.replace('\\', '/'));
            case PRIMITIVE:
                return findAtPath("/p/" + name.replace('\\', '/'));
            case PRIMITIVE8:
                return findAtPath("/p/8/" + name.replace('\\', '/'));
            case PRIMITIVE48:
                return findAtPath("/p/48/" + name.replace('\\', '/'));
        }
        return null;
    }

    public abstract Reader findAtPath(String path);

}