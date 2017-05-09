/**
 * Filter that converts the color of image into negative.
 */
public class NegativeFilter implements Filter {
	public void filter(PixelImage pi) {
		// Get all information about the picture.
		Pixel[][] data = pi.getData();

		for (int row = 0; row < pi.getHeight(); row++) {
			for (int col = 0; col < pi.getWidth(); col++) {
				// Get current red, green and blue of the picture.
				Pixel color = data[row][col];
				int red = color.red;
				int green = color.green;
				int blue = color.blue;

				// Convert color.
				red = 255 - red;
				green = 255 - green;
				blue = 255 - blue;

				// Change color.
				color.red = red;
				color.green = green;
				color.blue = blue;
			}
		}
		// Set color back.
		pi.setData(data);
	}

}
