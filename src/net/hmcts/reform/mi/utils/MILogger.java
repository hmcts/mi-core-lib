package net.hmcts.reform.mi.utils;

import org.apache.commons.lang.StringUtils;

public class MILogger {

		private static final int INDENT_SIZE = 3;
		private static int indent = 0;
	
		private static boolean debugMode = false;

		private static boolean printInline = false;
		private static boolean debugInline = false;
		private static boolean errInline = false;

		public static final String tab = StringUtils.leftPad("", indent*MILogger.INDENT_SIZE);
		public static final String nl = "\n";
		
		private MILogger(){}

		public static void setDebugMode(boolean debugMode) {
			MILogger.debugMode = debugMode;
		}

		public static void print(String outStr){
			outStr = (debugInline) ? outStr : indent()+outStr;
			System.out.print(outStr);
			errInline = !outStr.endsWith("\n");
		}

		public static void printLine(String outStr){
			System.out.println(indent()+outStr);
			System.out.flush();
			printInline = false;
		}

		public static void printLine(){
			System.out.println();
			System.out.flush();
			printInline = false;
		}

		public static void err(String errStr){
			errStr = (debugInline) ? errStr : indent()+errStr;
			System.out.print(errStr);
			errInline = !errStr.endsWith("\n");
		}

		public static void errLine(String errStr){
			System.err.println(indent()+errStr);
			System.err.flush();
			errInline = false;
		}

		public static void debug(String debugStr){
			if (MILogger.debugMode) { 
				debugStr = (debugInline) ? debugStr : indent()+debugStr;
				System.out.print(debugStr);
				debugInline = !debugStr.endsWith("\n");
			}
		}

		public static void debugLine(String debugStr){
			if (MILogger.debugMode) { 
				System.out.println(indent()+debugStr);
				System.out.flush();
			}
			debugInline = false;
		}

		public static void debugLine(){
			if (MILogger.debugMode) { 
				System.out.println();
				System.out.flush();
			}
			debugInline = false;
		}

		public static void flush(){
			System.out.flush();
			System.err.flush();
		}
	
		public static String indent() {
			return StringUtils.leftPad("", indent*MILogger.INDENT_SIZE);
		}
		
		public static int increaseIndent() {
			return indent++;
		}

		public static int decreaseIndent() {
			return indent--;
		}
}
