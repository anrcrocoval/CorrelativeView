package plugins.perrine.correlativeview;




import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import icy.canvas.IcyCanvas;
import icy.common.exception.UnsupportedFormatException;
import icy.file.Loader;
import icy.gui.dialog.LoaderDialog;
import icy.gui.viewer.Viewer;
import icy.image.IcyBufferedImage;
import icy.image.IcyBufferedImageUtil;
import icy.plugin.abstract_.Plugin;
import icy.plugin.interface_.PluginCanvas;
import icy.util.XMLUtil;
import plugins.perrine.correlativeview.TestVisu;

public class CorrelativeView extends Plugin implements PluginCanvas {

	@Override
	public String getCanvasClassName() {
		return "CorrelativeView";
	}
	
	@Override
	public IcyCanvas createCanvas(Viewer viewer) {
		LoaderDialog dialog = new LoaderDialog(false);
	
		dialog.setDialogTitle("Select the correlated image");
		
				
		File path=dialog.getSelectedFile();
		
		LoaderDialog dialogtransfo = new LoaderDialog(false);
		dialogtransfo.setDialogTitle("Select the transfo from correlated image to opened image");
		File pathtransfo=dialogtransfo.getSelectedFile();
		
		IcyBufferedImage image = null;
		BufferedImage correlatedimagebuf =null;
		try {
			image = Loader.loadImage(path.getPath());
			correlatedimagebuf = IcyBufferedImageUtil.toBufferedImage(image, BufferedImage.TYPE_INT_ARGB);
		} catch (UnsupportedFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Document document = XMLUtil.loadDocument( pathtransfo);
		Element root = XMLUtil.getRootElement(document);
		ArrayList<Element> transfoElementArrayList = XMLUtil.getElements(root, "transformation");
		
		
		double ss= 1.0;
		double st=1.0;
		ArrayList<Element> infoimages=XMLUtil.getElements(transfoElementArrayList.get(0),"sequenceSize");
		for (Element info:infoimages) {
			String type=XMLUtil.getAttributeValue(info, "type","0");
			ArrayList<Element> dim=XMLUtil.getElements(info,"dimensionSize");
			if (type.compareToIgnoreCase("source")==0)
    			{
				
				ss=XMLUtil.getAttributeDoubleValue(dim.get(0), "pixelSize",1); //TO BE CHANGED when refactored , not checking now that this is X
    			}
			else //should be target
			{
				st=XMLUtil.getAttributeDoubleValue(dim.get(0), "pixelSize",1); //TO BE CHANGED when refactored , not checking now that this is X
			}
		}
    	
    	
Element transfo=XMLUtil.getElements(transfoElementArrayList.get(0),"MatrixTransformation").get(0);
		
		double[][] m = new double[4][4];
		for (int i=0;i<4;i++)
    	{
    		for(int j=0;j<4;j++) {
    			m[i][j] = XMLUtil.getAttributeDoubleValue(transfo, "m"+String.valueOf(i)+String.valueOf(j),0);
    			
    		}
    	}
		return new TestVisu(viewer,correlatedimagebuf, m, ss, st);
	}
}


