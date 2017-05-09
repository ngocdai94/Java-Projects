/**
 * Filter will convert the color of the image into emboss.
 */
public class EmbossFilter implements Filter {

	public void filter(PixelImage pi) {
		// Matrix algorithm for emboss filter.
		int[][] weights = { { 1, 0, 0 }, { 0, 0, 0 }, { 0, 0, -1 } };
		pi.transformImage2(weights);
		// transformIamge2 is where the computation of the
		// new pixel is done

	}
}
