package ael.editors;

import java.util.ArrayList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class AELContentAssistantProcessor implements IContentAssistProcessor
{
	private final IContextInformation[] NO_CONTEXTS = { };
	private final char[] PROPOSAL_ACTIVIATION_CHARS = { 'm', 'r' };
	private ICompletionProposal[] NO_COMPLETIONS = { };

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset)
	{
		ArrayList<ICompletionProposal> result= new ArrayList<ICompletionProposal>();
		try {
			IDocument document = viewer.getDocument();
			String prefix = lastWord(document, offset);
			String indent = lastIndent(document, offset);
			for (Keywords Keyword : AELEditor.AssistantKeywords) {
				String keyword = Keyword.getValue();
				if (keyword.startsWith(prefix)) {
					IContextInformation info= new ContextInformation(keyword, "XXX");
					result.add(new CompletionProposal(
							keyword.substring(prefix.length(), keyword.length()) + " ",
							offset,
							0,
							keyword.length() - prefix.length() + 1,
							null,
							keyword,
							info,
							Keyword.getDescription()));
				}
			}
			return result.toArray(new ICompletionProposal[result.size()]);
		} catch (Exception e) {
			// ... log the exception ...
			return NO_COMPLETIONS;
		}
	}

	private String lastWord(IDocument doc, int offset)
	{
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (!Character.isJavaIdentifierPart(c)) {
					return doc.get(n + 1, offset - n - 1);
				}
			}
		} catch (BadLocationException e) {
			// ... log the exception ...
		}
		return "";
	}

	private String lastIndent(IDocument doc, int offset)
	{
		try {
			int start = offset - 1; 
			while (start >= 0 && doc.getChar(start) != '\n') {
				start--;
			}
			int end = start;
			while (end < offset && Character.isSpaceChar(doc.getChar(end))) {
				end++;
			}
			return doc.get(start + 1, end - start - 1);
		} catch (BadLocationException e) {
			// ... log the exception ...
		}
		return "";
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset)
	{
		return NO_CONTEXTS;
	}

	public char[] getCompletionProposalAutoActivationCharacters()
	{
		return PROPOSAL_ACTIVIATION_CHARS;
	}

	public char[] getContextInformationAutoActivationCharacters()
	{
		return null;
	}

	public String getErrorMessage()
	{
		return null;
	}

	public IContextInformationValidator getContextInformationValidator()
	{
		return null;
	}
}
