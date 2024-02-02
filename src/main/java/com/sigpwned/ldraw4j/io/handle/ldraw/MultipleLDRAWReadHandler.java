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
package com.sigpwned.ldraw4j.io.handle.ldraw;

import com.sigpwned.ldraw4j.io.LDRAWReadHandler;
import com.sigpwned.ldraw4j.model.colour.ColourReference;
import com.sigpwned.ldraw4j.model.colour.Material;
import com.sigpwned.ldraw4j.model.colour.RGBA;
import com.sigpwned.ldraw4j.model.file.FileType;
import com.sigpwned.ldraw4j.model.file.FileVersion;
import com.sigpwned.ldraw4j.model.geometry.Matrix3f;
import com.sigpwned.ldraw4j.model.geometry.Point3f;
import com.sigpwned.ldraw4j.model.name.Name;
import com.sigpwned.ldraw4j.model.name.RealName;
import com.sigpwned.ldraw4j.model.name.UserName;
import com.sigpwned.ldraw4j.model.winding.BFC;
import com.sigpwned.ldraw4j.x.LDRAWException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MultipleLDRAWReadHandler implements LDRAWReadHandler {
	private final List<LDRAWReadHandler> handlers;

	public MultipleLDRAWReadHandler(LDRAWReadHandler... handlers) {
		this.handlers = Arrays.asList(handlers);
	}

	public void partDescription(String description) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.partDescription(description);
		}
	}

	public void name(String filename) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.name(filename);
		}
	}

	public void author(RealName realName, UserName userName) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.author(realName, userName);
		}
	}

	public void ldraworg(FileType partType, String qualifiers, FileVersion version) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.ldraworg(partType, qualifiers, version);
		}
	}

	public void license(String licenseStatement, boolean redistributable) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.license(licenseStatement, redistributable);
		}
	}

	public void bfc(BFC bfc) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.bfc(bfc);
		}
	}

	public void history(Date date, Name name, String message) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.history(date, name, message);
		}
	}

	public void category(String category) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.category(category);
		}
	}

	public void commandLine(String commandLine) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.commandLine(commandLine);
		}
	}

	public void keywords(List<String> keywords) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.keywords(keywords);
		}
	}

	public void help(String message) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.help(message);
		}
	}

	public void clear() throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.clear();
		}
	}

	public void colour(String name, int code, RGBA value, ColourReference edge, Integer luminance, Material material)
			throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.colour(name, code, value, edge, luminance, material);
		}
	}

	public void pause() throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.pause();
		}
	}

	public void print(String message) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.print(message);
		}
	}

	public void save() throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.save();
		}
	}

	public void step() throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.step();
		}
	}

	public void write(String message) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.write(message);
		}
	}

	public void meta(String line) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.meta(line);
		}
	}

	public void subfile(ColourReference colour, Point3f location, Matrix3f rotation, String file)
			throws IOException, LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.subfile(colour, location, rotation, file);
		}
	}

	public void line(ColourReference colour, Point3f[] line) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.line(colour, line);
		}
	}

	public void triangle(ColourReference colour, Point3f[] triangle) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.triangle(colour, triangle);
		}
	}

	public void quadrilateral(ColourReference colour, Point3f[] quad) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.quadrilateral(colour, quad);
		}
	}

	public void optionalLine(ColourReference colour, Point3f[] line, Point3f[] controlPoints) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.optionalLine(colour, line, controlPoints);
		}
	}

	public void comment(String comment) throws LDRAWException {
		for (LDRAWReadHandler handler : handlers) {
			handler.comment(comment);
		}
	}
}
