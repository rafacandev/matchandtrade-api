package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.util.ImageUtil;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ImageUtilUT {
	
	String thumbnailStorageLocation = "target/";
	int shortEdgeMaxLength = 25;
	
	@Test
	public void shouldGenerate50x25ShortEdgeThumbnailForA100x50Image() throws IOException {
		InputStream imageInputStream = null;
		try {			
			// The image-landscape.png has width=100 and height=50
			String imageResource = "image-landscape.png";
			imageInputStream = ImageUtilUT.class.getClassLoader().getResource(imageResource).openStream();
			Image image = ImageIO.read(imageInputStream);
			Image resizedImage = ImageUtil.obtainShortEdgeResizedImage(image, shortEdgeMaxLength);
			// We expect a 50x25 Thumbnail for a shortEdgeMaxLength=25 against a 100x50 image
			assertEquals(50, resizedImage.getWidth(null));
			assertEquals(25, resizedImage.getHeight(null));
			// Saving the image for visual validation
			File resizedFile = new File(thumbnailStorageLocation + imageResource + "-resized.jpg");
			ImageIO.write(ImageUtil.buildBufferedImage(resizedImage), "JPG", resizedFile);
		} catch (Exception e) {
			throw e;
		} finally {
			if (imageInputStream != null) {
				imageInputStream.close();
			}
		}
	}

	@Test
	public void shouldGenerate25x50ShortEdgeThumbnailForA50x100Image() throws IOException {
		InputStream imageInputStream = null;
		try {			
			// The image-portrait.png has width=50 and height=100
			String imageResource = "image-portrait.png";
			imageInputStream = ImageUtilUT.class.getClassLoader().getResource(imageResource).openStream();
			Image image = ImageIO.read(imageInputStream);
			Image resizedImage = ImageUtil.obtainShortEdgeResizedImage(image, shortEdgeMaxLength);
			// We expect a 25x50 Thumbnail for a shortEdgeMaxLength=25 against a 50x100 image
			assertEquals(25, resizedImage.getWidth(null));
			assertEquals(50, resizedImage.getHeight(null));
			// Saving the image for visual validation
			File resizedFile = new File(thumbnailStorageLocation + imageResource + "-resized.jpg");
			ImageIO.write(ImageUtil.buildBufferedImage(resizedImage), "JPG", resizedFile);
		} catch (Exception e) {
			throw e;
		} finally {
			if (imageInputStream != null) {
				imageInputStream.close();
			}
		}
	}
	
	@Test
	public void shouldCrop25x25FromTheCenterOf100x50Image() throws IOException {
		InputStream imageInputStream = null;
		try {			
			// The image-landscape.png has width=100 and height=50
			String imageResource = "image-landscape.png";
			imageInputStream = ImageUtilUT.class.getClassLoader().getResource(imageResource).openStream();
			Image image = ImageIO.read(imageInputStream);
			Image croppedImage = ImageUtil.obtainCenterCrop(image, 25, 25);
			/*
			 * ALERT! Asserting the dimensions is a very poor validation.
			 * However, a it is not worth to come up with a pixel by pixel test.
			 * Hence, validating the file visually is also required.
			 */
			assertEquals(25, croppedImage.getWidth(null));
			assertEquals(25, croppedImage.getHeight(null));
			File cropedFile = new File(thumbnailStorageLocation + imageResource + "-center-crop-25x25.jpg");
			ImageIO.write(ImageUtil.buildBufferedImage(croppedImage), "JPG", cropedFile);
		} catch (Exception e) {
			throw e;
		} finally {
			if (imageInputStream != null) {
				imageInputStream.close();
			}
		}
	}
	
}
