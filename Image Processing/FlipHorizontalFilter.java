/**
 * Filter that flips the image horizontally. This class is COMPLETE. Don't
 * change it. But model your other classes (such as FlipVerticalFilter) after
 * it.
 */
public class FlipHorizontalFilter implements Filter {
	public void filter(PixelImage pi) {
		// Get all information about the picture.

		Pixel[][] data = pi.getData();
		for (int row = 0; row < pi.getHeight(); row++) {
			for (int col = 0; col < pi.getWidth() / 2; col++) {
				// Swap pixel.
				Pixel temp = data[row][col];
				data[row][col] = data[row][pi.getWidth() - col - 1];
				data[row][pi.getWidth() - col - 1] = temp;
			}
		}
		// Set back pixel.
		pi.setData(data);
	}
}
