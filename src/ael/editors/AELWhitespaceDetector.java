package ael.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class AELWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
