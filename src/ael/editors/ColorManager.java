package ael.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager {
	public static final RGB MULTI_LINE_COMMENT = new RGB(128, 0, 0);
	public static final RGB AEL_COMMENT = new RGB(128, 0, 0);
	public static final RGB SINGLE_LINE_COMMENT = new RGB(128, 0, 0);
	public static final RGB KEYWORD = new RGB(0, 0, 128);
	public static final RGB TYPE = new RGB(0, 0, 128);
	public static final RGB STRING = new RGB(0, 128, 0);
	public static final RGB PROC_INSTR = new RGB(128, 128, 128);
	public static final RGB DEFAULT = new RGB(0, 0, 0);


	@SuppressWarnings("rawtypes")
	protected Map fColorTable = new HashMap(10);

	public void dispose() {
		@SuppressWarnings("rawtypes")
		Iterator e = fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}
	@SuppressWarnings("unchecked")
	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
