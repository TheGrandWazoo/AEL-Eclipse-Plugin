package ael.editors;

import java.util.ArrayList;
import java.util.List;

//import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;


public class AELScanner extends RuleBasedScanner {

	ArrayList<String> keyWords = new ArrayList<String>();

	public AELScanner(ColorManager manager) {
		IToken aelDefault = 
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.DEFAULT)));

		IToken asteriskApp = 
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.ASTERISK_APP)));

		IToken aelKeyWords = 
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.AEL_OBJECT)));

		IToken aelVar = 
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.TYPE)));

		IToken aelComment =
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.AEL_COMMENT)));

		IToken aelDocs =
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.AEL_DOCS)));

		IToken aelString =
				new Token(
						new TextAttribute(
								manager.getColor(IAELColorConstants.STRING)));

		/*
		 * AEL Object Definitions
		 */
		WordRule aelWordRules = new WordRule(aelObjectDetector, aelDefault, true);

		if (AELEditor.doc != null) {
			NodeList objectLst = AELEditor.doc.getElementsByTagName("object");
			for (int s = 0; s < objectLst.getLength(); s++) {
				Node fstNode = objectLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fstElmnt = (Element) fstNode;
					NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("name");
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = ((Node) fstNmElmnt).getChildNodes();
					aelWordRules.addWord(fstNm.item(0).getNodeValue().toString(), aelKeyWords);
					AELEditor.AssistantKeywords.add(new Keywords(fstNm.item(0).getNodeValue().toString(), ""));
				}
			}
			NodeList appLst = AELEditor.doc.getElementsByTagName("app");
			for (int s = 0; s < appLst.getLength(); s++) {
				Node fstNode = appLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fstElmnt = (Element) fstNode;
					NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("name");
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = ((Node) fstNmElmnt).getChildNodes();
					aelWordRules.addWord(fstNm.item(0).getNodeValue().toString(), asteriskApp);
					AELEditor.AssistantKeywords.add(new Keywords(fstNm.item(0).getNodeValue().toString(), ""));
				}
			}
			NodeList funcLst = AELEditor.doc.getElementsByTagName("function");
			for (int s = 0; s < funcLst.getLength(); s++) {
				Node fstNode = funcLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fstElmnt = (Element) fstNode;
					NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("name");
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = ((Node) fstNmElmnt).getChildNodes();
					aelWordRules.addWord(fstNm.item(0).getNodeValue().toString(), asteriskApp);
					AELEditor.AssistantKeywords.add(new Keywords(fstNm.item(0).getNodeValue().toString(), ""));
				}
			}
		}

		List<IRule> rules = new ArrayList<IRule>();
		// Add rules for documentation.
		rules.add(new MultiLineRule("/**", "*/", aelDocs));
		// Add rules for comments.
		rules.add(new MultiLineRule("/*", "*/", aelComment));
		rules.add(new EndOfLineRule("//", aelComment));
		// Add rule for variables
//		rules.add(new SingleLineRule("$", "{", aelVar, '\\'));
		// Add rule for eval wrap
//		rules.add(new SingleLineRule("$[", "]", aelVar, '\\'));
		// Add rules for strings.
		rules.add(new SingleLineRule("\"", "\"", aelString, '\\'));
		rules.add(new SingleLineRule("'", "'", aelString, '\\'));
		// Add word rule for Asterisk apps, function and keywords.
		rules.add(aelWordRules);
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new AELWhitespaceDetector()));

		setRules(rules.toArray(new IRule[rules.size()])); 
	}
	private IWordDetector aelObjectDetector = new IWordDetector() {
		public boolean isWordStart(char c) {
			return Character.isLetter(c) || c == '_' || c == '#';
		}
		public boolean isWordPart(char c) {
			return Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == '.' || c == '-';

		}
	};

	private IWordDetector aelAppDetector = new IWordDetector() {
		public boolean isWordStart(char c) {
			return Character.isLetter(c) || c == '_' || c == ':';
		}
		public boolean isWordPart(char c) {
			return isWordStart(c) || Character.isDigit(c) || c == '.' || c == '-' || c == ' ';

		}
	};
}
