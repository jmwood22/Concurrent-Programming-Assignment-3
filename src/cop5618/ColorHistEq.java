package cop5618;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ColorHistEq {

    //Use these labels to instantiate you timers.  You will need 8 invocations of now()
	static String[] labels = { "getRGB", "convert to HSB", "create brightness map", "probability array",
			"parallel prefix", "equalize pixels", "setRGB" };

	static Timer colorHistEq_serial(BufferedImage image, BufferedImage newImage) {
		Timer times = new Timer(labels);
		/**
		 * IMPLEMENT SERIAL METHOD
		 */
		ColorModel colorModel = ColorModel.getRGBdefault();
		int w = image.getWidth();
		int h = image.getHeight();
		times.now();
		int[] sourcePixelArray = image.getRGB(0, 0, w, h, new int[w*h], 0, w);
		times.now();
		
		class HSB{
			float h,s,b;
			public HSB(float[] hsb)
			{
				this.h=hsb[0];
				this.s=hsb[1];
				this.b=hsb[2];
			}
		}
		
		//convert the RGB to HSB and store in the HSBPixelArray
		HSB[] HSBPixelArray = 
				(HSB[]) Arrays.stream(sourcePixelArray)
			.mapToObj(pixel -> new HSB(Color.RGBtoHSB(colorModel.getRed(pixel), colorModel.getGreen(pixel), colorModel.getBlue(pixel), new float[w*h])))
		.toArray();
		times.now();
		
		
		//create the brightness map
		/*int[] brightness = 
				Arrays.stream(HSBPixelArray)
				.collect(Collectors.groupingBy(hsb -> hsb.b))
				*/
		times.now();
		
		//create probability array
		times.now();
		
		//parallel prefix
		times.now();
		
		//equalize pixels
		times.now();
		
		//set rgb
		times.now();
				
		
		return times;
	}



	static Timer colorHistEq_parallel(FJBufferedImage image, FJBufferedImage newImage) {
		Timer times = new Timer(labels);
		/**
		 * IMPLEMENT PARALLEL METHOD
		 */
		
		ColorModel colorModel = ColorModel.getRGBdefault();
		int w = image.getWidth();
		int h = image.getHeight();
		times.now();
		int[] sourcePixelArray = image.getRGB(0, 0, w, h, new int[w*h], 0, w);
		times.now();
		
		class HSB{
			float h,s,b;
			public HSB(float[] hsb)
			{
				this.h=hsb[0];
				this.s=hsb[1];
				this.b=hsb[2];
			}
		}
		
		//convert the RGB to HSB and store in the HSBPixelArray
		HSB[] HSBPixelArray = 
				(HSB[]) Arrays.stream(sourcePixelArray)
			.mapToObj(pixel -> new HSB(Color.RGBtoHSB(colorModel.getRed(pixel), colorModel.getGreen(pixel), colorModel.getBlue(pixel), new float[w*h])))
		.toArray();
		times.now();
		
		
		//create the brightness map
		/*int[] brightness = 
				Arrays.stream(HSBPixelArray)
				.collect(Collectors.groupingBy(hsb -> hsb.b))
				*/
		times.now();
		
		//create probability array
		times.now();
		
		//parallel prefix
		times.now();
		
		//equalize pixels
		times.now();
		
		//set rgb
		times.now();
				
		
		
		return times;
	}

}
