package org.jboss.tools.vscode.java.handlers;

import org.eclipse.jdt.core.ITypeRoot;
import org.jboss.tools.langs.Hover;
import org.jboss.tools.langs.TextDocumentPositionParams;
import org.jboss.tools.langs.base.LSPMethods;
import org.jboss.tools.vscode.ipc.RequestHandler;
import org.jboss.tools.vscode.java.HoverInfoProvider;
import org.jboss.tools.vscode.java.JDTUtils;

public class HoverHandler implements RequestHandler<TextDocumentPositionParams, Hover>{
	
	public HoverHandler() {
	}

	@Override
	public boolean canHandle(String request) {
		return LSPMethods.DOCUMENT_HOVER.getMethod().equals(request);
	}

	@Override
	public Hover handle(TextDocumentPositionParams param) {
		ITypeRoot unit = JDTUtils.resolveTypeRoot(param.getTextDocument().getUri());
		
		String hover = computeHover(unit ,param.getPosition().getLine().intValue(),
				param.getPosition().getCharacter().intValue());
		Hover $ = new Hover();
		if (hover != null && hover.length() > 0) {
			return $.withContents(hover);
		}
		return $;
	}


	public String computeHover(ITypeRoot unit, int line, int column) {
		HoverInfoProvider provider = new HoverInfoProvider(unit);
		return provider.computeHover(line,column);
	}	

}