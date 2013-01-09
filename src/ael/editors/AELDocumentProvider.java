package ael.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import ael.AELPlugin;

public class AELDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocumentProvider(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			IDocumentPartitioner partitioner =
					new FastPartitioner(
							new AELPartitionScanner(),
							new String[] {
								AELPartitionScanner.AEL_DEFAULT,
								AELPartitionScanner.AEL_MULTILINE_COMMENT,
								AELPartitionScanner.AEL_SINGLELINE_COMMENT,
								AELPartitionScanner.AEL_CODE_BLOCK,
								AELPartitionScanner.AEL_TEXT,
								AELPartitionScanner.AEL_STRING,
								AELPartitionScanner.AEL_COMMENT
							}
					);
			extension3.setDocumentPartitioner(AELPlugin.MY_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
		return document;
	}
}