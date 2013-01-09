package ael.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class AELWordDetector implements IWordDetector
{

	public boolean isWordStart(char c)
	{
		return Character.isLetter(c);
	}

	public boolean isWordPart(char c)
	{
		return Character.isLetterOrDigit(c);
	}
}
