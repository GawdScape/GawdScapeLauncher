package com.gawdscape.launcher.util;

import java.awt.Color;

/**
 *
 * @author Vinnie
 */
public class ColorCodes {

	public static final Color Black = new Color(0, 0, 0);
	public static final Color Dark_Blue = new Color(0, 0, 170);
	public static final Color Dark_Green = new Color(0, 170, 0);
	public static final Color Dark_Aqua = new Color(0, 170, 170);
	public static final Color Dark_Red = new Color(170, 0, 0);
	public static final Color Dark_Purple = new Color(170, 0, 170);
	public static final Color Gold = new Color(255, 170, 0);
	public static final Color Gray = new Color(170, 170, 170);
	public static final Color Dark_Gray = new Color(85, 85, 85);
	public static final Color Blue = new Color(85, 85, 255);
	public static final Color Green = new Color(85, 255, 85);
	public static final Color Aqua = new Color(85, 255, 255);
	public static final Color Red = new Color(255, 85, 85);
	public static final Color Light_Purple = new Color(255, 85, 255);
	public static final Color Yellow = new Color(255, 255, 85);
	public static final Color White = new Color(255, 255, 255);
	public static final Color Default = new Color(205, 205, 205);

	public static Color getColorFromCode(String code) {
		switch (code) {
			case "0":
				return Black;
			case "1":
				return Dark_Blue;
			case "2":
				return Dark_Green;
			case "3":
				return Dark_Aqua;
			case "4":
				return Dark_Red;
			case "5":
				return Dark_Purple;
			case "6":
				return Gold;
			case "7":
				return Gray;
			case "8":
				return Dark_Gray;
			case "9":
				return Blue;
			case "a":
				return Green;
			case "b":
				return Aqua;
			case "c":
				return Red;
			case "d":
				return Light_Purple;
			case "e":
				return Yellow;
			case "f":
				return White;
			default:
				return Default;
		}
	}
}
