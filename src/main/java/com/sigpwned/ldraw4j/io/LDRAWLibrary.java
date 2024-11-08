package com.sigpwned.ldraw4j.io;

import com.sigpwned.ldraw4j.model.file.FileType;
import java.io.Reader;

public interface LDRAWLibrary {

    public default Reader find(String name) {
        Reader r = null;
        for (FileType t : FileType.values()) {
            r = find(t, name);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    public Reader find(FileType type, String name);

    public static LDRAWLibrary combine(LDRAWLibrary... libraries) {
        return new LDRAWLibrary() {
            public Reader find(FileType type, String name) {
                for(LDRAWLibrary library : libraries) {
                    Reader r = library.find(type, name);
                    if(r!=null) {
                        return r;
                    }
                }
                return null;
            }
        };
    }
}
