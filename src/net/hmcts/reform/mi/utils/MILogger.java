package net.hmcts.reform.mi.utils;

import org.apache.commons.lang.StringUtils;

public class MILogger {

		private static final int INDENT_SIZE = 3;
		private static int indent = 0;
	
		private static boolean debugMode = false;
	
		private MILogger(){}

		public static void setDebugMode(boolean debugMode) {
			MILogger.debugMode = debugMode;
		}

		public static void print(String outStr){
			System.out.print(outStr);
		}

		public static void printLine(String outStr){
			System.out.println(indent()+outStr);
			System.out.flush();
		}

		public static void printLine(){
			System.out.println();
			System.out.flush();
		}

		public static void err(String errStr){
			System.err.print(errStr);
		}

		public static void errLine(String errStr){
			System.err.println(indent()+errStr);
			System.err.flush();
		}

		public static void debug(String debugStr){
			if (MILogger.debugMode) System.out.print(debugStr);
		}

		public static void debugLine(String debugStr){
			if (MILogger.debugMode) { 
				System.out.println(indent()+debugStr);
				System.out.flush();
			}
		}

		public static void debugLine(){
			if (MILogger.debugMode) { 
				System.out.println();
				System.out.flush();
			}
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
