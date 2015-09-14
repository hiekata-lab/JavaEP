package jp.ac.u_tokyo.k.is.logic;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;;

public class JavaStringSourceObject extends SimpleJavaFileObject {
	private final String code;
	
	public JavaStringSourceObject(String name, String code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}
	
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}
