import java.awt.image.*;

/**
 * Provides an interface to a picture as an array of Pixels
 */
public class PixelImage {
	private BufferedImage myImage;
	private int width;
	private int height;

	/**
	 * Map this PixelImage to a real image
	 * 
	 * @param bi
	 *            The image
	 */
	public PixelImage(BufferedImage bi) {
		// initialize instance variables
		this.myImage = bi;
		this.width = bi.getWidth();
		this.height = bi.getHeight();
	}

	/**
	 * Return the width of the image
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Return the height of the image
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Return the BufferedImage of this PixelImage
	 */
	public BufferedImage getImage() {
		return this.myImage;
	}

	/**
	 * Return the image's pixel data as an array of Pixels. The first coordinate
	 * is the x-coordinate, so the size of the array is [width][height], where
	 * width and height are the dimensions of the array
	 * 
	 * @return The array of pixels
	 */
	public Pixel[][] getData() {
		Raster r = this.myImage.getRaster();
		Pixel[][] data = new Pixel[r.getHeight()][r.getWidth()];
		int[] samples = new int[3];

		for (int row = 0; row < r.getHeight(); row++) {
			for (int col = 0; col < r.getWidth(); col++) {
				samples = r.getPixel(col, row, samples);
				Pixel newPixel = new Pixel(samples[0], samples[1], samples[2]);
				data[row][col] = newPixel;
			}
		}

		return data;
	}

	/**
	 * Set the image's pixel data from an array. This array matches that
	 * returned by getData(). It is an error to pass in an array that does not
	 * match the image's dimensions or that has pixels with invalid values (not
	 * 0-255)
	 * 
	 * @param data
	 *            The array to pull from
	 */
	public void setData(Pixel[][] data) {
		int[] pixelValues = new int[3]; // a temporary array to hold r,g,b
										// values
		WritableRaster wr = this.myImage.getRaster();

		if (data.length != wr.getHeight()) {
			throw new IllegalArgumentException("Array size does not match");
		} else if (data[0].length != wr.getWidth()) {
			throw new IllegalArgumentException("Array size does not match");
		}

		for (int row = 0; row < wr.getHeight(); row++) {
			for (int col = 0; col < wr.getWidth(); col++) {
				pixelValues[0] = data[row][col].red;
				pixelValues[1] = data[row][col].green;
				pixelValues[2] = data[row][col].blue;
				wr.setPixel(col, row, pixelValues);
			}
		}
	}

	// transformImage is used for edgy, gaussian, unsharpmasking and laplacian filter.
	public void transformImage(int[][] weights) {
		// Write the code
		// new red at (i,j) = weights[0][0] * red at (i-1,j-1) + ....
		Pixel[][] data = getData();
		Pixel[][] data2 = getData();
		int average = weightsAverage(weights);
		for (int i = 1; i < this.height - 1; i++) {
			for (int j = 1; j < this.width - 1; j++) {
				int red = (weights[0][0] * data[i - 1][j - 1].red + 
						   weights[0][1] * data[i - 1][j].red + 
						   weights[0][2] * data[i - 1][j + 1].red + 
						   
						   weights[1][0] * data[i][j - 1].red + 
						   weights[1][1] * data[i][j].red + 
						   weights[1][2] * data[i][j + 1].red + 
						   
						   weights[2][0] * data[i + 1][j - 1].red + 
						   weights[2][1] * data[i + 1][j].red + 
						   weights[2][2] * data[i + 1][j + 1].red) / average;

				int blue = (weights[0][0] * data[i - 1][j - 1].blue + 
						    weights[0][1] * data[i - 1][j].blue + 
						    weights[0][2] * data[i - 1][j + 1].blue + 
						    
						    weights[1][0] * data[i][j - 1].blue + 
						    weights[1][1] * data[i][j].blue	+ 
						    weights[1][2] * data[i][j + 1].blue + 
						    
						    weights[2][0] * data[i + 1][j - 1].blue + 
						    weights[2][1] * data[i + 1][j].blue + 
						    weights[2][2] * data[i + 1][j + 1].blue) / average;

				int green = (weights[0][0] * data[i - 1][j - 1].green + 
							 weights[0][1] * data[i - 1][j].green + 
							 weights[0][2] * data[i - 1][j + 1].green + 
							 weights[1][0] * data[i][j - 1].green + 
							 weights[1][1] * data[i][j].green + 
							 weights[1][2] * data[i][j + 1].green + 
							 weights[2][0] * data[i + 1][j - 1].green + 
							 weights[2][1] * data[i + 1][j].green + 
							 weights[2][2] * data[i + 1][j + 1].green) / average;

				if (red < 0) {
					red = 0;
				} else if (red > 255) {
					red = 255;
				}
				if (blue < 0) {
					blue = 0;
				} else if (blue > 255) {
					blue = 255;
				}
				if (green < 0) {
					green = 0;
				} else if (green > 255) {
					green = 255;
				}

				Pixel newColor = new Pixel(red, green, blue);
				data2[i][j] = newColor;
			}

		}
		setData(data2);
	}

	// transformImage2 is used for emboss filter.
	public void transformImage2(int[][] weights) {
		// Write the code
		// new red at (i,j) = weights[0][0] * red at (i-1,j-1) + ....
		Pixel[][] data = getData();
		Pixel[][] data2 = getData();
		int average = weightsAverage(weights);
		for (int i = 1; i < this.height - 1; i++) {
			for (int j = 1; j < this.width - 1; j++) {
				int red = (weights[0][0] * data[i - 1][j - 1].red + 
						   weights[0][1] * data[i - 1][j].red + 
						   weights[0][2] * data[i - 1][j + 1].red + 
						   
						   weights[1][0] * data[i][j - 1].red + 
						   weights[1][1] * data[i][j].red + 
						   weights[1][2] * data[i][j + 1].red + 
						   
						   weights[2][0] * data[i + 1][j - 1].red + 
						   weights[2][1] * data[i + 1][j].red + 
						   weights[2][2] * data[i + 1][j + 1].red) / average + 128;

				int blue = (weights[0][0] * data[i - 1][j - 1].blue + 
						    weights[0][1] * data[i - 1][j].blue + 
						    weights[0][2] * data[i - 1][j + 1].blue + 
						    
						    weights[1][0] * data[i][j - 1].blue + 
						    weights[1][1] * data[i][j].blue	+ 
						    weights[1][2] * data[i][j + 1].blue + 
						    
						    weights[2][0] * data[i + 1][j - 1].blue + 
						    weights[2][1] * data[i + 1][j].blue + 
						    weights[2][2] * data[i + 1][j + 1].blue) / average + 128;

				int green = (weights[0][0] * data[i - 1][j - 1].green + 
							 weights[0][1] * data[i - 1][j].green + 
							 weights[0][2] * data[i - 1][j + 1].green + 
							 weights[1][0] * data[i][j - 1].green + 
							 weights[1][1] * data[i][j].green + 
							 weights[1][2] * data[i][j + 1].green + 
							 weights[2][0] * data[i + 1][j - 1].green + 
							 weights[2][1] * data[i + 1][j].green + 
							 weights[2][2] * data[i + 1][j + 1].green) / average + 128;

				if (red < 0) {
					red = 0;
				} else if (red > 255) {
					red = 255;
				}
				if (blue < 0) {
					blue = 0;
				} else if (blue > 255) {
					blue = 255;
				}
				if (green < 0) {
					green = 0;
				} else if (green > 255) {
					green = 255;
				}

				Pixel newColor = new Pixel(red, green, blue);
				data2[i][j] = newColor;
			}

		}
		setData(data2);
	}
	
	public void transformImage3(int[][] weights) {
		// Write the code
		// new red at (i,j) = weights[0][0] * red at (i-1,j-1) + ....
		Pixel[][] data = getData();
		Pixel[][] data2 = getData();
		int average = weightsAverage(weights);
		for (int i = 2; i < this.height - 2; i++) {
			for (int j = 2; j < this.width - 2; j++) {
				int red = (weights[0][0] * data[i - 2][j - 2].red + 
						   weights[0][1] * data[i - 2][j - 1].red + 
						   weights[0][2] * data[i - 2][j].red + 
						   weights[0][3] * data[i - 2][j + 1].red + 
						   weights[0][4] * data[i - 2][j + 2].red +
						
						   weights[1][0] * data[i - 2][j - 2].red + 
						   weights[1][1] * data[i - 2][j - 1].red + 
						   weights[1][2] * data[i - 2][j].red + 
						   weights[1][3] * data[i - 2][j + 1].red + 
						   weights[1][4] * data[i - 2][j + 2].red +
						   
						   weights[2][0] * data[i - 2][j - 2].red + 
						   weights[2][1] * data[i - 2][j - 1].red + 
						   weights[2][2] * data[i - 2][j].red + 
						   weights[2][3] * data[i - 2][j + 1].red + 
						   weights[2][4] * data[i - 2][j + 2].red +
						   
						   weights[3][0] * data[i - 2][j - 2].red + 
						   weights[3][1] * data[i - 2][j - 1].red + 
						   weights[3][2] * data[i - 2][j].red + 
						   weights[3][3] * data[i - 2][j + 1].red + 
						   weights[3][4] * data[i - 2][j + 2].red +
						   
						   weights[4][0] * data[i - 2][j - 2].red + 
						   weights[4][1] * data[i - 2][j - 1].red + 
						   weights[4][2] * data[i - 2][j].red + 
						   weights[4][3] * data[i - 2][j + 1].red + 
						   weights[4][4] * data[i - 2][j + 2].red) / average;

				int blue = (weights[0][0] * data[i - 2][j - 2].blue + 
						   weights[0][1] * data[i - 2][j - 1].blue + 
						   weights[0][2] * data[i - 2][j].blue + 
						   weights[0][3] * data[i - 2][j + 1].blue + 
						   weights[0][4] * data[i - 2][j + 2].blue +
						
						   weights[1][0] * data[i - 2][j - 2].blue + 
						   weights[1][1] * data[i - 2][j - 1].blue + 
						   weights[1][2] * data[i - 2][j].blue + 
						   weights[1][3] * data[i - 2][j + 1].blue + 
						   weights[1][4] * data[i - 2][j + 2].blue +
						   
						   weights[2][0] * data[i - 2][j - 2].blue + 
						   weights[2][1] * data[i - 2][j - 1].blue + 
						   weights[2][2] * data[i - 2][j].blue + 
						   weights[2][3] * data[i - 2][j + 1].blue + 
						   weights[2][4] * data[i - 2][j + 2].blue +
						   
						   weights[3][0] * data[i - 2][j - 2].blue + 
						   weights[3][1] * data[i - 2][j - 1].blue + 
						   weights[3][2] * data[i - 2][j].blue + 
						   weights[3][3] * data[i - 2][j + 1].blue + 
						   weights[3][4] * data[i - 2][j + 2].blue +
						   
						   weights[4][0] * data[i - 2][j - 2].blue + 
						   weights[4][1] * data[i - 2][j - 1].blue + 
						   weights[4][2] * data[i - 2][j].blue + 
						   weights[4][3] * data[i - 2][j + 1].blue + 
						   weights[4][4] * data[i - 2][j + 2].blue) / average;

				int green = (weights[0][0] * data[i - 2][j - 2].green + 
						   weights[0][1] * data[i - 2][j - 1].green + 
						   weights[0][2] * data[i - 2][j].green + 
						   weights[0][3] * data[i - 2][j + 1].green + 
						   weights[0][4] * data[i - 2][j + 2].green +
						
						   weights[1][0] * data[i - 2][j - 2].green + 
						   weights[1][1] * data[i - 2][j - 1].green + 
						   weights[1][2] * data[i - 2][j].green + 
						   weights[1][3] * data[i - 2][j + 1].green + 
						   weights[1][4] * data[i - 2][j + 2].green +
						   
						   weights[2][0] * data[i - 2][j - 2].green + 
						   weights[2][1] * data[i - 2][j - 1].green + 
						   weights[2][2] * data[i - 2][j].green + 
						   weights[2][3] * data[i - 2][j + 1].green + 
						   weights[2][4] * data[i - 2][j + 2].green +
						   
						   weights[3][0] * data[i - 2][j - 2].green + 
						   weights[3][1] * data[i - 2][j - 1].green + 
						   weights[3][2] * data[i - 2][j].green + 
						   weights[3][3] * data[i - 2][j + 1].green + 
						   weights[3][4] * data[i - 2][j + 2].green +
						   
						   weights[4][0] * data[i - 2][j - 2].green + 
						   weights[4][1] * data[i - 2][j - 1].green + 
						   weights[4][2] * data[i - 2][j].green + 
						   weights[4][3] * data[i - 2][j + 1].green + 
						   weights[4][4] * data[i - 2][j + 2].green) / average;

				if (red < 0) {
					red = 0;
				} else if (red > 255) {
					red = 255;
				}
				if (blue < 0) {
					blue = 0;
				} else if (blue > 255) {
					blue = 255;
				}
				if (green < 0) {
					green = 0;
				} else if (green > 255) {
					green = 255;
				}

				Pixel newColor = new Pixel(red, green, blue);
				data2[i][j] = newColor;
			}

		}
		setData(data2);
	}
	
	// add a method to compute a new image given weighted averages
	// Comput total weight for matrix weight.
	private int weightsAverage(int[][] weights) {
		int sum = 0;
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				sum += weights[i][j];
			}
		}
		// Avoid out of bound error for Laplacian filter.
		if (sum == 0) {
			sum = 1;
		}
		return sum;
	}
}
