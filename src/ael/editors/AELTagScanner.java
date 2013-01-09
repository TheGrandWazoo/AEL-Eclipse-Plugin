package ael.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;

public class AELTagScanner extends RuleBasedScanner {

	public AELTagScanner(ColorManager manager) {
		IToken aelString = 
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.STRING)));

		IToken aelComment = 
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.AEL_COMMENT)));

		IToken aelDocs =
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.AEL_DOCS)));

		IToken aelCodeBlock =
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.DEFAULT)));

		List<IRule> rules = new ArrayList<IRule>();

		// Add rule for double quotes
		rules.add(new SingleLineRule("\"", "\"", aelString, '\\'));
		// Add a rule for single quotes
		rules.add(new SingleLineRule("'", "'", aelString, '\\'));
		// Add a rule for AEL Documentation
		rules.add(new MultiLineRule("/**", "*/", aelDocs));
		// Add a rule for Multiline comment
		rules.add(new MultiLineRule("/*", "*/", aelComment));
		// Add a rule for single line comment
		rules.add(new EndOfLineRule("//", aelComment));
		// Add a rule for context object block
		rules.add(new MultiLineRule("context", "}", aelCodeBlock));
		// Add a rule for macro object block
		rules.add(new MultiLineRule("macro", "}", aelCodeBlock));
		// Add a rule for macro object block
		rules.add(new MultiLineRule("gobals", "}", aelCodeBlock));
		// Add a rule for macro object block
		rules.add(new MultiLineRule("{", "}", aelCodeBlock));
		// Add a rule for single line comment
		rules.add(new EndOfLineRule("//", aelComment));
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new AELWhitespaceDetector()));

		setRules(rules.toArray(new IRule[rules.size()])); 
	}
}
