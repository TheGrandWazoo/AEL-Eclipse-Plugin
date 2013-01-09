package ael.editors;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class AELContentAssistantProcessor implements IContentAssistProcessor
{

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset)
	{
		ICompletionProposal[] completionProposals = new ICompletionProposal[1];
		completionProposals[0] = new CompletionProposal("macro",0,5,1);
		completionProposals[1] = new CompletionProposal("return",0,6,1);
		completionProposals[2] = new CompletionProposal("Return",0,6,1);
		return completionProposals;
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset)
	{
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters()
	{
		return new char[] {'a'};
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
