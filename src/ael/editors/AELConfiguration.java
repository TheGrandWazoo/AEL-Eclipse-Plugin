package ael.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class AELConfiguration extends SourceViewerConfiguration {
	private AELDoubleClickStrategy doubleClickStrategy;
	private AELTagScanner tagScanner;
	private AELScanner scanner;
	private ColorManager colorManager;
	private AELEditor editor;

	public AELConfiguration(ColorManager colorManager, AELEditor editor)
	{
		this.colorManager = colorManager;
		this.editor = editor;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
	{
		return new String[] {
				IDocument.DEFAULT_CONTENT_TYPE,
				AELPartitionScanner.AEL_MULTILINE_COMMENT,
				AELPartitionScanner.AEL_SINGLELINE_COMMENT,
				AELPartitionScanner.AEL_COMMENT,
				AELPartitionScanner.AEL_CODE_BLOCK,
				AELPartitionScanner.AEL_TEXT
		};
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType)
	{
		if (doubleClickStrategy == null) {
			doubleClickStrategy = new AELDoubleClickStrategy();
		}
		return doubleClickStrategy;
	}

	protected AELScanner getAELScanner() {
		if (scanner == null) {
			scanner = new AELScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IAELColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected AELTagScanner getAELTagScanner() {
		if (tagScanner == null) {
			tagScanner = new AELTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IAELColorConstants.AEL_CODE_BLOCK))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer ddr =
			new DefaultDamagerRepairer(getAELTagScanner());
		reconciler.setDamager(ddr, AELPartitionScanner.AEL_CODE_BLOCK);
		reconciler.setRepairer(ddr, AELPartitionScanner.AEL_CODE_BLOCK);

		ddr = new DefaultDamagerRepairer(getAELScanner());
		reconciler.setDamager(ddr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(ddr, IDocument.DEFAULT_CONTENT_TYPE);

/*
		NonRuleBasedDamagerRepairer ndr =
				new NonRuleBasedDamagerRepairer(
					new TextAttribute(
						colorManager.getColor(IAELColorConstants.AEL_COMMENT)));
		reconciler.setDamager(ndr, AELPartitionScanner.AEL_COMMENT);
		reconciler.setRepairer(ndr, AELPartitionScanner.AEL_COMMENT);
*/

		return reconciler;
	}

	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
	{
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(new AELContentAssistantProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.enableAutoActivation(true);
		return assistant;
	}

	public IReconciler getReconciler(ISourceViewer souceViewer)
	{
		AELReconcilingStrategy strategy = new AELReconcilingStrategy();
		strategy.setEditor(editor);
		MonoReconciler reconciler = new MonoReconciler(strategy, false);
		return reconciler;
	}
}