/**
 * Filter will convert the color of image by gaussian filter.
 */
public class GaussianFilter implements Filter {

	public void filter(PixelImage pi) {
		// Matrix algorithm for gaussian filter.
		int[][] weights = { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };
		pi.transformImage(weights);
		// transformIamge is where the computation of the
		// new pixel is done

	}
}
