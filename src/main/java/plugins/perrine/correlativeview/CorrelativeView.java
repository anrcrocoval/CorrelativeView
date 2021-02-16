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
import icy.gui.dialog.MessageDialog;
import icy.gui.frame.progress.ToolTipFrame;
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
		new ToolTipFrame(
				"<html>"
						+ "<br> After having used <b>ec-clem</b>:"
						+"<br> This open image is the \"target\" image: "
						+"<br> you will now display the source image on the source image, AT FULL RESOLUTION."
						+ "<br> <li> First select your \"source\" image in the dialog</li> "
						+ "<br> <li> Then select the transformation .xml generated when using ec-clem (source to target)</li> "
						+ "<br> <li> In the layer panel, you can change the opacity of the target image layer, "
						+ "<br> which will balance the opacity of the source as well</li> "
						+ "<br> Note that you can use <b>ecClemTransformationSchemaInverter</b>"
						+ "<br> to generate this transform if you do it in the reverse way"
						+ "</html>","startmessagecorview"
					);
		LoaderDialog dialog = new LoaderDialog(false);
	
		dialog.setDialogTitle("Select the correlated image");
		dialog.updateUI();
				
		File path=dialog.getSelectedFile();
		
		LoaderDialog dialogtransfo = new LoaderDialog(false);
		dialogtransfo.setDialogTitle("Select the transfo from correlated image to opened image");
		dialog.updateUI();
		File pathtransfo=dialogtransfo.getSelectedFile();
		
		IcyBufferedImage image = null;
		BufferedImage correlatedimagebuf =null;
		try {
			image = Loader.loadImage(path.getPath());
			correlatedimagebuf = IcyBufferedImageUtil.toBufferedImage(image, BufferedImage.TYPE_INT_ARGB);
		} catch (UnsupportedFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 MessageDialog.showDialog("image format not supported");
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageDialog.showDialog("no image selected");
		}
		
		final Document document = XMLUtil.loadDocument( pathtransfo);

		
		
		
    	
    	
		Element transfo=XMLUtil.getElements(document.getDocumentElement(),"MatrixTransformation").get(0);
		Element transfoinfo=XMLUtil.getElements(document.getDocumentElement(),"transformation").get(0);
		 ArrayList<Element> sequenceSizeElements = XMLUtil.getElements(transfoinfo, "sequenceSize");
	        if(sequenceSizeElements.size() != 2) {
	            throw new RuntimeException("Element should contain exactly 2 sequenceSize");
	        }
	        double sourceSequenceSize = readSequenceSize(sequenceSizeElements.get(0));
	        double targetSequenceSize = readSequenceSize(sequenceSizeElements.get(1));
	       
	        if(sequenceSizeElements.get(0).getAttribute("type").equals("target")) {
	            double tmp=sourceSequenceSize;
	            sourceSequenceSize=targetSequenceSize;
	            targetSequenceSize=tmp;
	        } 
		double[][] m = new double[4][4];
		for (int i=0;i<4;i++)
    	{
    		for(int j=0;j<4;j++) {
    			m[i][j] = XMLUtil.getAttributeDoubleValue(transfo, "m"+String.valueOf(i)+String.valueOf(j),0);
    			//System.out.println(" ypos"+i+" " +j+" value "+m[i][j]);
    		}
    	}
		
		return new TestVisu(viewer,correlatedimagebuf, m, sourceSequenceSize, targetSequenceSize);
	}

	private double readSequenceSize(Element element) {
		double pixelsizeum=1.0;
		 ArrayList<Element> elements = XMLUtil.getElements(element);
	        for(Element dimension : elements) {
	           
	                String dim = (dimension.getAttribute("name"));
	                if (dim.compareTo("X")==0) 
	                	pixelsizeum=Double.parseDouble(dimension.getAttribute("pixelSize"));
	           
	        }
		return pixelsizeum;
	}
}


