package org.unclazz.parsec;

final class ParsecUtility {
    private static final String[] code0to31 = new String[]
    {
        "NUL", "SOH", "STX", "ETX", "EOT", "ENQ", "ACK", "BEL", "BS", "HT",
        "LF", "VT", "FF", "CR", "SO", "SI", "DLE", "DC1", "DC2", "DC3",
        "DC4", "NAK", "SYN", "ETB", "CAN", "EM", "SUB", "ESC", "FS", "GS",
        "RS", "US"
    };
	private ParsecUtility() {}
	
	public static String charToString(int ch) {
		if (ch == -1) return "EOF";
		if (0 <= ch && ch <= 31) return code0to31[ch];
		return String.format("'%s'(%s)", (char) ch, ch);
	}
	
	public static void mustNotBeNull(String name, Object target){
		if (target == null) throw new NullPointerException
		(String.format("argument \"%s\" must not be null.", name));
	}
	public static void mustNotBeEmpty(String name, String target){
		mustNotBeNull(name, target);
		if (target.length() == 0) throw new IllegalArgumentException
		(String.format("argument \"%s\" must not be empty.", name));
	}
	public static void mustNotBeEmpty(String name, char[] target){
		mustNotBeNull(name, target);
		if (target.length == 0) throw new IllegalArgumentException
		(String.format("argument \"%s\" must not be empty.", name));
	}
	public static void mustBeGreaterThan(String name, int target, int threshold) {
		if (target <= threshold) throw new IllegalArgumentException
		(String.format("argument \"%s\" must be greater than %s.", name, threshold));
	}
	public static void mustBeGreaterThanOrEqual(String name, int target, int threshold) {
		if (target < threshold) throw new IllegalArgumentException
		(String.format("argument \"%s\" must be greater than or equal %s.", name, threshold));
	}
}
