/**
 * This will convert the color of image into black and white.
 */
public class BlackandWhiteFilter implements Filter {
	public void filter(PixelImage pi) {
		// Get all information about the picture.
		Pixel[][] data = pi.getData();

		for (int row = 0; row < pi.getHeight(); row++) {
			for (int col = 0; col < pi.getWidth(); col++) {
				// Get the current red, green and blue color of the picture.
				Pixel color = data[row][col];
				int red = color.red;
				int green = color.green;
				int blue = color.blue;

				// Convert color.
				int sum = (red + green + blue) / 3;

				// Change color.
				color.red = sum;
				color.green = sum;
				color.blue = sum;
			}
		}
		// Set colors back.
		pi.setData(data);
	}
}
