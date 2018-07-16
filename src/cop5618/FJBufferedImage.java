package cop5618;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;

public class FJBufferedImage extends BufferedImage {

	/**Constructors*/

	public FJBufferedImage(int width, int height, int imageType) {
		super(width, height, imageType);
	}

	public FJBufferedImage(int width, int height, int imageType, IndexColorModel cm) {
		super(width, height, imageType, cm);
	}

	public FJBufferedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
			Hashtable<?, ?> properties) {
		super(cm, raster, isRasterPremultiplied, properties);
	}


	/**
	 * Creates a new FJBufferedImage with the same fields as source.
	 * @param source
	 * @return
	 */
	public static FJBufferedImage BufferedImageToFJBufferedImage(BufferedImage source){
		Hashtable<String,Object> properties=null; 
		String[] propertyNames = source.getPropertyNames();
		if (propertyNames != null) {
			properties = new Hashtable<String,Object>();
			for (String name: propertyNames){properties.put(name, source.getProperty(name));}
		}
		return new FJBufferedImage(source.getColorModel(), source.getRaster(), source.isAlphaPremultiplied(), properties);		
	}
	
	public void setBIRGB(int xStart, int yStart, int w, int h, int[] rgbArray, int offset, int scansize)
	{
		super.setRGB(xStart, yStart, w, h, rgbArray, offset, scansize);
	}

	@Override
	public void setRGB(int xStart, int yStart, int w, int h, int[] rgbArray, int offset, int scansize){
		/****IMPLEMENT THIS METHOD USING PARALLEL DIVIDE AND CONQUER*****/
		class SetRGBTask extends RecursiveAction {
			int xStart, yStart, w, h, offset, scansize;
			int[] rgbArray;
			FJBufferedImage image;

			public SetRGBTask(int xStart, int yStart, int w, int h, int[] rgbArray, int offset, int scansize, FJBufferedImage image) {
				this.xStart = xStart;
				this.yStart = yStart;
				this.w = w;
				this.h= h;
				this.rgbArray = rgbArray;
				this.offset = offset;
				this.scansize = scansize;
				this.image = image;
			}

			@Override
			protected void compute() {
				if(h<=8)
				{
					image.setBIRGB(xStart, yStart, w, h, rgbArray, offset, scansize);
					return;
				}
				else
				{
					{
						invokeAll(	new SetRGBTask(xStart, yStart, w, (int)Math.floor(((double)h)/2), rgbArray, offset, scansize, image),
								new SetRGBTask(xStart, yStart+(int)Math.floor(((double)h)/2), w, (int)Math.ceil(((double)h)/2), rgbArray, offset+((int)Math.floor(((double)h)/2)*scansize), scansize, image));
					}

					return;
				}
			}
		}

		//creates our pool with the parallelism being the number of available processors which for my machine is 8
		ForkJoinPool pool = new ForkJoinPool();
		SetRGBTask task = new SetRGBTask(xStart, yStart, w, h, rgbArray, offset, scansize, this);
		pool.invoke(task);

		return;
	}

	public int[] getBIRGB(int xStart, int yStart, int w, int h, int[] rgbArray, int offset, int scansize)
	{
		return super.getRGB(xStart, yStart, w, h, rgbArray, offset, scansize);
	}

	@Override
	public int[] getRGB(int xStart, int yStart, int w, int h, int[] rgbArray, int offset, int scansize){
		/****IMPLEMENT THIS METHOD USING PARALLEL DIVIDE AND CONQUER*****/

		class GetRGBTask extends RecursiveTask<int[]> {
			int xStart, yStart, w, h, offset, scansize;
			int[] rgbArray;
			FJBufferedImage image;

			public GetRGBTask(int xStart, int yStart, int w, int h, int[] rgbArray, int offset, int scansize, FJBufferedImage image) {
				this.xStart = xStart;
				this.yStart = yStart;
				this.w = w;
				this.h= h;
				this.rgbArray = rgbArray;
				this.offset = offset;
				this.scansize = scansize;
				this.image = image;
			}

			@Override
			protected int[] compute() {
				if(h<=8)
				{
					return image.getBIRGB(xStart, yStart, w, h, rgbArray, offset, scansize);
				}
				else
				{
					{
						invokeAll(	new GetRGBTask(xStart, yStart, w, (int)Math.floor(((double)h)/2), rgbArray, offset, scansize, image),
								new GetRGBTask(xStart, yStart+(int)Math.floor(((double)h)/2), w, (int)Math.ceil(((double)h)/2), rgbArray, offset+((int)Math.floor(((double)h)/2)*scansize), scansize, image));
					}

					return rgbArray;
				}
			}
		}

		//creates our pool with the parallelism being the number of available processors which for my machine is 8
		ForkJoinPool pool = new ForkJoinPool();
		GetRGBTask task = new GetRGBTask(xStart, yStart, w, h, rgbArray, offset, scansize, this);
		pool.invoke(task);

		return rgbArray;
	}
}


