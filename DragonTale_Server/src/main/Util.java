package main;

public class Util {

	public static void test() {
		javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new IllegalStateException("cannot find compiler");
		}
		
		//boolean success = compiler.getTask(null, fileManager, diagnosticListener, options, classes, compilationUnits).call();
	}
}
