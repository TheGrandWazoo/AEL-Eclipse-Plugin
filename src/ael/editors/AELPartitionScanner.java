package ael.editors;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class AELPartitionScanner extends RuleBasedPartitionScanner {
	/*
	 * string constants for different partition types
	 */
	public final static String AEL_DEFAULT = "__ael_default";
	public final static String AEL_MULTILINE_COMMENT= "__ael_multiline_comment";
	public final static String AEL_SINGLELINE_COMMENT= "__ael_singleline_comment";
	public final static String AEL_CODE_BLOCK= "__ael_code_block";
	public final static String AEL_TEXT = "__ael_text";
	public final static String AEL_STRING ="__ael_string";
	public final static String AEL_COMMENT ="__ael_singleline_comment";

	public AELPartitionScanner() {
		super();
		IToken aelMultilineComment= new Token(AEL_MULTILINE_COMMENT);
		IToken aelSinglelineComment= new Token(AEL_SINGLELINE_COMMENT);
		IToken aelCodeBlock= new Token(AEL_CODE_BLOCK);
		IToken aelString = new Token(AEL_STRING);

		IPredicateRule[] rules = new IPredicateRule[5];

		rules[0] = new MultiLineRule("/*", "*/", aelMultilineComment);
		rules[1] = new MultiLineRule("{", "}", aelCodeBlock);
		rules[2] = new EndOfLineRule("//", aelSinglelineComment);
		// Add rule for strings and character constants.
		rules[3] = new SingleLineRule("\"", "\"", aelString, '\\');
		rules[4] = new SingleLineRule("'", "'", aelString, '\\');

		setPredicateRules(rules);
	}
}
