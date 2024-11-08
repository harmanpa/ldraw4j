package com.sigpwned.ldraw4j.io;

import com.sigpwned.ldraw4j.io.handle.ldraw.AbstractLDRAWReadHandler;
import com.sigpwned.ldraw4j.io.handle.ldraw.MultipleLDRAWReadHandler;
import com.sigpwned.ldraw4j.io.model.ModelState;
import com.sigpwned.ldraw4j.io.model.MultiPartFile;
import com.sigpwned.ldraw4j.io.model.Winding;
import com.sigpwned.ldraw4j.model.Colour;
import com.sigpwned.ldraw4j.model.colour.ColourReference;
import com.sigpwned.ldraw4j.model.geometry.Matrix3f;
import com.sigpwned.ldraw4j.model.geometry.Point3f;
import com.sigpwned.ldraw4j.model.winding.BFC;
import com.sigpwned.ldraw4j.x.LDRAWException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileReader;

public class LDRAWModelReader {
	private static class State {
		private boolean inverted;
		private boolean invertNext;
		private ModelState modelState;
		private Colour currentColour;

		public State(String name) {
			this.modelState = new ModelState(name);
			this.inverted = false;
			this.invertNext = false;
			this.currentColour = Colour.defaultColour();
		}

		public boolean isInverted() {
			return inverted;
		}

		public void setInverted(boolean inverted) {
			this.inverted = inverted;
		}

		public boolean isInvertNext() {
			return invertNext;
		}

		public void setInvertNext(boolean invertNext) {
			this.invertNext = invertNext;
		}

		public ModelState getModelState() {
			return modelState;
		}

		public Colour getCurrentColour() {
			return currentColour;
		}

		public void setCurrentColour(Colour currentColour) {
			this.currentColour = currentColour;
		}
	}

	private final LDRAWConfiguration config;
	private final LDRAWModelReadHandler handler;
	private final List<State> stateStack;

	public static void read(File f, LDRAWLibrary library, LDRAWModelReadHandler handler) throws IOException, LDRAWException {
		String extension = f.getName().substring(f.getName().lastIndexOf('.') + 1);
		switch (extension) {
			case "ldr":
			case "dat":
				new LDRAWModelReader(LDRAWConfiguration.load(library), handler).read(f.getName(), new FileReader(f));
				return;
			case "mpd":
				MultiPartFile mpf = new MultiPartFile(new FileReader(f));
				new LDRAWModelReader(LDRAWConfiguration.load(LDRAWLibrary.combine(mpf, library)), handler)
						.read(f.getName(), mpf.getReader());
				return;
		}
	}

	public LDRAWModelReader(LDRAWConfiguration config, LDRAWModelReadHandler handler) {
		this.config = config;
		this.handler = handler;
		this.stateStack = new ArrayList<State>();
	}

	public void read(String name) throws IOException, LDRAWException {
		read(name, getConfig().getLibrary().find(name));
	}

	public void read(String name, Reader in) throws IOException, LDRAWException {
		if (in == null) {
			throw new LDRAWException("Could not find " + name);
		}
		try (in) {
			try (BufferedReader lines = new BufferedReader(in)) {
				push(new State(name));
				try {
					parse(in);
				} finally {
					pop();
				}
			}
		}
	}

	protected LDRAWConfiguration getConfig() {
		return config;
	}

	protected State top() {
		if (stateStack.size() == 0) {
                    throw new IllegalStateException("State stack empty");
        }
		return stateStack.get(stateStack.size() - 1);
	}

	protected State pop() {
		State result = top();
		stateStack.remove(stateStack.size() - 1);
		return result;
	}

	protected void push(State state) {
		stateStack.add(state);
	}

	protected Colour eval(ColourReference colour) {
		return colour.eval(getConfig().getColours(), top().getCurrentColour());
	}

	private int depth = 0;

	protected void parse(Reader in) throws IOException, LDRAWException {
		handler.enterFile(top().getModelState().getName(), top().getModelState());
		try {
			LDRAWReadHandler readHandler = new AbstractLDRAWReadHandler() {
				public void bfc(BFC bfc) throws LDRAWException {
					if (top().isInvertNext()) {
						System.err.println("WARNING: Ignoring illegal INVERTNEXT");
					}
					top().setInvertNext(false);

					switch (bfc) {
						case NOCERTIFY:
							top().getModelState().setWinding(winding(Winding.UNKNOWN));
							break;
						case CERTIFY:
						case CERTIFY_CCW:
							top().getModelState().setWinding(winding(Winding.CCW));
							break;
						case CERTIFY_CW:
							top().getModelState().setWinding(winding(Winding.CW));
							break;
						case CCW:
							Winding winding = winding(Winding.CCW);
							if (!top().getModelState().getWinding().equals(winding)) {
								top().getModelState().setWinding(winding);
								handler.winding(top().getModelState());
							}
							break;
						case CW:
							Winding windingCw = winding(Winding.CW);
							if (!top().getModelState().getWinding().equals(windingCw)) {
								top().getModelState().setWinding(windingCw);
								handler.winding(top().getModelState());
							}
							break;
						case INVERTNEXT:
							top().setInvertNext(true);
							break;
						case CLIP:
							if (!top().getModelState().isClipping()) {
								top().getModelState().setClipping(true);
								handler.clipping(top().getModelState());
							}
							break;
						case NOCLIP:
							if (top().getModelState().isClipping()) {
								top().getModelState().setClipping(false);
								handler.clipping(top().getModelState());
							}
					}
				}

				public void line(ColourReference colour, Point3f[] line) throws LDRAWException {
					if (top().isInvertNext()) {
                                            System.err.println("WARNING: Ignoring illegal INVERTNEXT");
                    }
					handler.line(top().getModelState(), eval(colour), transform(line));
					top().setInvertNext(false);
				}

				public void triangle(ColourReference colour, Point3f[] triangle) throws LDRAWException {
					if (top().isInvertNext()) {
                                            System.err.println("WARNING: Ignoring illegal INVERTNEXT");
                    }
					handler.triangle(top().getModelState(), eval(colour), transform(triangle));
					top().setInvertNext(false);
				}

				public void quadrilateral(ColourReference colour, Point3f[] quad) throws LDRAWException {
					if (top().isInvertNext()) {
                                            System.err.println("WARNING: Ignoring illegal INVERTNEXT");
                    }
					handler.quadrilateral(top().getModelState(), eval(colour), transform(quad));
					top().setInvertNext(false);
				}

				public void optionalLine(ColourReference colour, Point3f[] line, Point3f[] controlPoints)
						throws LDRAWException {
					if (top().isInvertNext()) {
                                            System.err.println("WARNING: Ignoring illegal INVERTNEXT");
                    }
					handler.optionalLine(top().getModelState(), eval(colour), transform(line),
							transform(controlPoints));
					top().setInvertNext(false);
				}

				protected Point3f[] transform(Point3f[] points) {
					Point3f[] mypoints = new Point3f[points.length];
					for (int i = 0; i < points.length; i++) {
                                            mypoints[i] = top().getModelState().getRotation().mul(points[i])
                                                    .add(top().getModelState().getTranslation());
                    }
					return mypoints;
				}

				protected Winding winding(Winding winding) {
					switch (winding) {
						case CW:
							return top().isInverted() ? Winding.CCW : Winding.CW;
						case CCW:
							return top().isInverted() ? Winding.CW : Winding.CCW;
						default:
							return winding;
					}
				}

				public void subfile(ColourReference colour, Point3f st, Matrix3f sr, String file)
						throws LDRAWException, IOException {
					State newstate = new State(file);

					newstate.setInverted(top().isInverted() ^ top().isInvertNext());

					newstate.setCurrentColour(colour.eval(getConfig().getColours(), top().getCurrentColour()));

					if (newstate.isInverted()) {
                                            newstate.getModelState().setWinding(Winding.CW);
                    } else {
                                            newstate.getModelState().setWinding(Winding.CCW);
                    }

					Point3f t = top().getModelState().getTranslation();
					Matrix3f r = top().getModelState().getRotation();

					newstate.getModelState().setRotation(r.mul(sr));

					newstate.getModelState().setTranslation(new Point3f(new float[] {
							r.get(0, 0) * st.x() + r.get(0, 1) * st.y() + r.get(0, 2) * st.z() + t.x(),
							r.get(1, 0) * st.x() + r.get(1, 1) * st.y() + r.get(1, 2) * st.z() + t.y(),
							r.get(2, 0) * st.x() + r.get(2, 1) * st.y() + r.get(2, 2) * st.z() + t.z()
					}));

					String padding = "";
					for (int i = 0; i < depth; i++) {
                                            padding = padding + " ";
                    }
					// System.err.println(padding+"subfile -> "+newstate.isInverted()+" =
					// "+newstate.getModelState().getName()+"
					// ("+newstate.getModelState().getWinding()+")");
					// System.err.println(padding+sr);
					// System.err.println(padding+newstate.getModelState().getRotation());
					// System.err.println(padding+newstate.getModelState().getTranslation());
					depth = depth + 1;

					push(newstate);
					Reader reader = getConfig().getLibrary().find(file);
					if (reader == null) {
						throw new LDRAWException("Could not find " + file);
					}
					try (reader) {
						parse(reader);
					} finally {
						pop();
					}

					depth = depth - 1;

					top().setInvertNext(false);
				}

				public void meta(String line) throws LDRAWException {
					handler.meta(line);
				}
			};
			new LDRAWReader(handler instanceof LDRAWReadHandler
					? new MultipleLDRAWReadHandler((LDRAWReadHandler) handler, readHandler)
					: readHandler).read(in);
		} finally {
			handler.leaveFile(top().getModelState().getName());
		}
	}
}
