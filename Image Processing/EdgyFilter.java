/**
 * Filter will convert the color of image by using edgy filter.
 */
public class EdgyFilter implements Filter {

	public void filter(PixelImage pi) {
		// Matrix algorithm for edgy filter.
		int[][] weights = { { -1, -1, -1 }, { -1, 9, -1 }, { -1, -1, -1 } };
		pi.transformImage(weights);
		// transformIamge is where the computation of the
		// new pixel is done

	}
}
