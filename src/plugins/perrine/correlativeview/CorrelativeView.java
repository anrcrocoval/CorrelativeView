package plugins.perrine.correlativeview;




import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Jama.Matrix;
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
			 MessageDialog.showDialog("image format not supported");
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageDialog.showDialog("no image selected");
		}
		
		final Document document = XMLUtil.loadDocument( pathtransfo);

		
		
		double ss= 17.0/1000;
		double st=62.5/1000;

    	
    	
		Element transfo=XMLUtil.getElements(document.getDocumentElement(),"MatrixTransformation").get(0);
		
		double[][] m = new double[4][4];
		for (int i=0;i<4;i++)
    	{
    		for(int j=0;j<4;j++) {
    			m[i][j] = XMLUtil.getAttributeDoubleValue(transfo, "m"+String.valueOf(i)+String.valueOf(j),0);
    			System.out.println(" ypos"+i+" " +j+" value "+m[i][j]);
    		}
    	}
		
		return new TestVisu(viewer,correlatedimagebuf, m, ss, st);
	}
}


