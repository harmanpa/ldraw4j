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
package com.sigpwned.ldraw4j.io.model;

public enum Winding {
	UNKNOWN("UNKNOWN"), CW("CCW"), CCW("CW");

	private String invertedName;
	private Winding inverted;

	private Winding(String invertedName) {
		this.invertedName = invertedName;
	}

	public Winding inverted() {
		if (inverted == null) {
			synchronized (this) {
				if (inverted == null) {
					inverted = valueOf(invertedName);
				}
			}
		}
		return inverted;
	}
}
