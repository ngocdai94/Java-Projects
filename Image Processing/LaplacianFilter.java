/**
 * Filter will convert the color of image into lapacian.
 */
public class LaplacianFilter implements Filter {

	public void filter(PixelImage pi) {
		// Matrix algorithm for laplacian filter.
		int[][] weights = { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 } };
		pi.transformImage(weights);
		// transformIamge is where the computation of the
		// new pixel is done

	}
}
