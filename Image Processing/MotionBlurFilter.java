
public class MotionBlurFilter implements Filter {
	public void filter(PixelImage pi) {
		// Matrix algorithm for gaussian filter.
		int[][] weights = { 
				
							
				{ 1, 0, 0, 0, 0 }, 
				{ 0, 1, 0, 0, 0 }, 
				{ 0, 0, 1, 0, 0 },
				{ 0, 0, 0, 1, 0 },
				{ 0, 0, 0, 0, 1 }
				
				
				
				
				
				
				
				
							/* { 2, 4, 5, 4, 2 }, 
							{ 4, 9, 12, 9, 4 }, 
							{ 5, 12, 15, 12, 5 },
							{ 4, 9, 12, 9, 4},
							{ 2, 4, 5, 4, 2} */ };
		pi.transformImage3(weights);
		// transformIamge3 is where the computation of the
		// new pixel is done

	}
}
