package com.sigpwned.ldraw.model.colour;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class RGBA {
	private static final Map<Integer,RGBA> palette=new HashMap<Integer,RGBA>();
	
	public static RGBA rgba(int red, int green, int blue, int alpha) {
		int rgba=0;
		rgba = rgba | ((alpha & 0xFF) << 24);
		rgba = rgba | ((red   & 0xFF) << 16);
		rgba = rgba | ((green & 0xFF) <<  8);
		rgba = rgba | ((blue  & 0xFF) <<  0);
		
		Integer lookup=Integer.valueOf(rgba);
		
		RGBA result=palette.get(lookup);
		if(result == null)
			palette.put(lookup, result = new RGBA(rgba));
		
		return result;
	}
	
	private int rgba;
	
	private RGBA(int rgba) {
		this.rgba = rgba;
	}
	
	public int getRed() {
		return (rgba & 0x00FF0000) >>> 16;
	}
	
	public int getGreen() {
		return (rgba & 0x0000FF00) >>>  8;
	}
	
	public int getBlue() {
		return (rgba & 0x000000FF) >>>  0;
	}
	
	public int getAlpha() {
		return (rgba & 0xFF000000) >>>  24;
	}
	
	public Color toColor() {
		return new Color(getRed(), getBlue(), getGreen(), getAlpha());
	}
	
	public String toString() {
		return getRGBString();
	}
	
	public String getRGBString() {
		StringBuilder result=new StringBuilder();
		result.append("#");
		if(getRed() < 16) result.append("0");
		result.append(Integer.toString(getRed(), 16));
		if(getGreen() < 16) result.append("0");
		result.append(Integer.toString(getGreen(), 16));
		if(getBlue() < 16) result.append("0");
		result.append(Integer.toString(getBlue(), 16));
		return result.toString();
	}
	
	public boolean isAlphaDefined() {
		return getAlpha() != 255;
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
}
