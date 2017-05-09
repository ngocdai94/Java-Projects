/**
 * Convert the color of image by using unsharp masking filter.
 */
public class UnsharpMaskingFilter implements Filter {
	public void filter(PixelImage pi) {
		// Matrix algorithm for unsharp masking filter.
		int[][] weights = { { -1, -2, -1 }, { -2, 28, -2 }, { -1, -2, -1 } };
		pi.transformImage(weights);
		// transformIamge is where the computation of the
		// new pixel is done

	}

}
