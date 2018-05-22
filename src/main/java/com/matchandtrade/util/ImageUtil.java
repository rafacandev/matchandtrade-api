package com.matchandtrade.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;

public class ImageUtil {

	/**
	 * <p>Obtains a proportionally resized image where its shortest edge is resized to match {@code shortEdgeLength}.</p>
	 * <p>Examples:<p>
	 * <ul>
	 * 	<li>an input image with width=100 and height=50 and shortEdgeLength=25 results in a image with width=50 and height=25</li>
	 * 	<li>an input image with width=50 and height=100 and shortEdgeLength=25 results in a image with width=25 and height=100</li>
	 * </ul>
	 * 
	 * @param image: the reference image
	 * @param shortEdgeLength: the target value of the resulting image's short edge
	 * @return RenderedImage
	 * @throws IOException
	 */
	public static RenderedImage obtainShortEdgeResizedImage(final Image image, final int shortEdgeLength) throws IOException {
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double shortEdgeRatio;
		int thumbnailHeight;
		int thumbnailWidth;

		boolean isLandscape = (imageWidth >= imageHeight);
		if (isLandscape) {
			shortEdgeRatio = (double) imageHeight / (double) shortEdgeLength;
			thumbnailWidth = (int) (imageWidth / shortEdgeRatio);
			thumbnailHeight = shortEdgeLength;
		} else {
			shortEdgeRatio = (double) imageWidth / (double) shortEdgeLength;
			thumbnailHeight = (int) (imageHeight / shortEdgeRatio);
			thumbnailWidth = shortEdgeLength;			
		}
		
		BufferedImage result = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = result.createGraphics();
		graphics2D.setBackground(Color.WHITE);
		graphics2D.setPaint(Color.WHITE);
		graphics2D.fillRect(0, 0, thumbnailWidth, thumbnailHeight);
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbnailWidth, thumbnailHeight, null);
		return result;
	}

	/**
	 * Builds a @{code BufferedImage} of an {@code Image}
	 * 
	 * @see java.awt.image.BufferedImage
	 * @see java.awt.Image
	 * 
	 * @param image
	 * @return BufferedImage
	 */
	public static BufferedImage buildBufferedImage(Image image) {
		BufferedImage result = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics = result.createGraphics();
	    graphics.drawImage(image, 0, 0, null);
	    graphics.dispose();
	    return result;
	}

	public static RenderedImage obtainCenterCrop(Image image, final int width, final int height) {
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		int x = (int) (imageWidth / 2) - (width / 2);
		int y = (int) (imageHeight / 2) - (height / 2);
		BufferedImage result = buildBufferedImage(image).getSubimage(x, y, width, height);
		return result;
	}


}
