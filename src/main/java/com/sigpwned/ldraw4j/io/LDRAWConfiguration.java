// Copyright 2012 Andy Boothe
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.sigpwned.ldraw4j.io;

import com.sigpwned.ldraw4j.io.handle.ldraw.AbstractLDRAWReadHandler;
import com.sigpwned.ldraw4j.io.library.FileSystemLDRAWLibrary;
import com.sigpwned.ldraw4j.model.Colour;
import com.sigpwned.ldraw4j.model.colour.ColourReference;
import com.sigpwned.ldraw4j.model.colour.Colours;
import com.sigpwned.ldraw4j.model.colour.Material;
import com.sigpwned.ldraw4j.model.colour.RGBA;
import com.sigpwned.ldraw4j.model.file.FileType;
import com.sigpwned.ldraw4j.x.LDRAWException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

public class LDRAWConfiguration {
	public static LDRAWConfiguration load(File home) throws IOException, LDRAWException {
		return load(new FileSystemLDRAWLibrary(home));
	}

	public static LDRAWConfiguration load(LDRAWLibrary library) throws IOException, LDRAWException {
		Reader configReader = library.find(FileType.CONFIGURATION, "LDConfig.ldr");
		if (configReader == null) {
			throw new LDRAWException("Invalid configuration directory: No LDConfig.ldr");
		}
		LDRAWConfiguration result = new LDRAWConfiguration(library);
		result.read(configReader);
		return result;
	}

	private LDRAWLibrary library;
	private Colours colours;

	LDRAWConfiguration(LDRAWLibrary library) {
		this.library = library;
		this.colours = new Colours(Colour.defaultColour());
	}

	public LDRAWLibrary getLibrary() {
		return library;
	}

	public Colours getColours() {
		return colours;
	}

	protected void read(Reader in) throws IOException, LDRAWException {
		new LDRAWReader(new AbstractLDRAWReadHandler() {
			public void colour(String name, int code, RGBA value, ColourReference edgeref, Integer luminance,
					Material material) throws LDRAWException {
				RGBA edge = edgeref.eval(colours, Colour.defaultColour()).getValue();
				Colour colour = new Colour(name, code, material, luminance, value, edge);
				colours.defineColour(colour);
			}
		}).read(in);
	}
}
