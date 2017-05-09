/**
 * Filter that flips the image vertically.
 */
public class FlipVerticalFilter implements Filter {
	public void filter(PixelImage pi) {
		Pixel[][] data = pi.getData();
		// Get all information about the picture.
		for (int row = 0; row < pi.getHeight() / 2; row++) {
			for (int col = 0; col < pi.getWidth(); col++) {
				// Swap pixel.
				Pixel temp = data[row][col];
				data[row][col] = data[pi.getHeight() - row - 1][col];
				data[pi.getHeight() - row - 1][col] = temp;
			}
		}
		// Set back pixel.
		pi.setData(data);
	}
}
