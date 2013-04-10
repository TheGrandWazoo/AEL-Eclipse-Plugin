package ael.editors;

import java.util.ArrayList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;

public class AELReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {

	private AELEditor editor;
	private IDocument fDocument;

	/**
	 * holds the calculated positions
	 */
	protected final ArrayList<Object> fPositions = new ArrayList<Object>();

	/**
	 * The offset of the next character to be read 
	 */
	protected int fOffset;

	/**
	 * The end offset of the range to be scanned
	 */
	protected int fRangeEnd;

	/**
	 * next character position - used locally and only valid shile
	 * {@link #calculatePositions()} is in progress.
	 */
	protected int cNextPos = 0;

	/**
	 * number of newlines found by {@link @classifyTag()}
	 */
	protected int cNewLines = 0;

	protected char cLastNLChar = ' ';

	protected final int START_BLOCK = 1;
	protected final int END_BLOCK = 2;
	protected final int COMMENT_TAG = 3;
	protected final int EOR = 4;

	public AELEditor getEditor()
	{
		return editor;
	}

	public void setEditor(AELEditor editor)
	{
		this.editor = editor;
	}

	public void setDocument(IDocument document)
	{
		this.fDocument = document;
	}

	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion)
	{
		this.initialReconcile();
	}

	public void reconcile(IRegion partition)
	{
		this.initialReconcile();
	}

	public void setProgressMonitor(IProgressMonitor monitor)
	{
	}

	public void initialReconcile()
	{
		this.fOffset = 0;
		this.fRangeEnd = this.fDocument.getLength();
		this.calculatePositions();
	}

	protected void calculatePositions()
	{
		this.fPositions.clear();
		this.cNextPos = this.fOffset;

		try {
			recursiveTokens(0);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				editor.updateFoldingStructure(fPositions);
			}
		});
	}

	protected int recursiveTokens(int depth) throws BadLocationException {
		int newLines = 0;
		int classification;
		int startOffset;
		int startNewLines;
		String tagString;
		while (this.cNextPos < this.fRangeEnd) {
			while (this.cNextPos < this.fRangeEnd) {
				char ch = this.fDocument.getChar(this.cNextPos++);
				switch (ch) {
				case '/' :
					startOffset = this.cNextPos - 1;
					startNewLines = newLines;
					tagString = fDocument.get(startOffset, Math.min(this.cNextPos - startOffset, this.fRangeEnd - startOffset));
					if (classifyComment() == COMMENT_TAG) {
						newLines += this.cNewLines;
						if (newLines > startNewLines + 1) {
							emitPosition(startOffset, this.cNextPos - startOffset);
						}
					}
					break;
				case '{' :
					startOffset = this.cNextPos - 1;
					startNewLines = newLines;
					tagString = fDocument.get(startOffset, Math.min(this.cNextPos - startOffset, this.fRangeEnd - startOffset));
					newLines += recursiveTokens(depth + 1);
					if (newLines > startNewLines + 1) {
						emitPosition(startOffset, this.cNextPos - startOffset);
					}
					break;
				case '}' :
					return newLines;
				case '\n' :
				case '\r' :
					if ((ch == this.cLastNLChar) || (' ' == this.cLastNLChar)) {
						newLines++; 
						this.cLastNLChar = ch;
					}
					break;
				default:
					break;
				}
			}
		}
		return newLines;
	}

	protected void emitPosition(int startOffset, int length)
	{
		this.fPositions.add(new Position(startOffset, length));
	}

	/**
	 * 
	 */
	protected int classifyComment()
	{
		try {
			char ch = this.fDocument.getChar(this.cNextPos++);
			this.cNewLines = 0;
			/*
			 * Process a comment?
			 */
			if ('*' == ch) {
				while (this.cNextPos < this.fRangeEnd) {
					ch = this.fDocument.getChar(this.cNextPos++);
					if ('*' == ch) {
						ch = this.fDocument.getChar(this.cNextPos++);
						if ('/' == ch) {
							return COMMENT_TAG;
						}
					}
					if (('\n' == ch) || ('\r' == ch)) {
						if ((ch == this.cLastNLChar) || (' ' == this.cLastNLChar)) {
							this.cNewLines++;
							this.cLastNLChar = ch;
						}
					}
				}
				return EOR;
			}
			while ((' ' == ch) || ('\t' == ch) || ('\n' == ch) || ('\r' == ch)) {
				ch = this.fDocument.getChar(this.cNextPos++);
				if (this.cNextPos > this.fRangeEnd) {
					return 0;
				}
			}
		} catch (BadLocationException e) {
			return EOR;
		}
		return 0;
	}

	/**
	 * 
	 */
	protected int classifyCodeBlock(char token)
	{
		this.cNewLines = 0;
		try {
			if ('{' == token) {
				this.cNewLines += this.eatToEndOfLine();
				return START_BLOCK;
			}
			if ('}' == token) {
				this.cNewLines += this.eatToEndOfLine();
				return END_BLOCK;
			}
		} catch (BadLocationException e) {
			return EOR;
		}
		return EOR;
	}

	protected int eatToEndOfLine() throws BadLocationException {
		if (this.cNextPos >= this.fRangeEnd) {
			return 0;
		}
		char ch = this.fDocument.getChar(this.cNextPos++);

		/*
		 * Eat all spaces and tabs
		 */
		while ((this.cNextPos < this.fRangeEnd) && ((' ' == ch) || ('\t' == ch))) {
			ch = this.fDocument.getChar(this.cNextPos++);
		}
		if (this.cNextPos >= this.fRangeEnd) {
			this.cNextPos--;
			return 0;
		}
		if ('\n' == ch) {
			if (this.cNextPos < this.fRangeEnd) {
				ch = this.fDocument.getChar(this.cNextPos++);
				if ('\r' != ch) {
					this.cNextPos--;
				}
			} else {
				this.cNextPos--;
			}
			return 1;
		}
		if ('\r' == ch) {
			if (this.cNextPos < this.fRangeEnd) {
				ch = this.fDocument.getChar(this.cNextPos++);
				if ('\n' != ch) {
					this.cNextPos--;
				}
			} else {
				this.cNextPos--;
			}
			return 1;
		}
		return 0;
	}
}