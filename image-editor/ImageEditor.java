package imageEditor;

import java.io.FileReader;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class ImageEditor {

	private static final String IMAGE_HEADER = "P3";
	private static final String BLUR = "motionblur";
	private static final String GRAY = "grayscale";
	private static final String INVERT = "invert";
	private static final String EMBOSS = "emboss";
	private static final int MIN_ARGS = 3;
	private static final int MAX_ARGS = 4;
    
	private static Image loadFile(Scanner s) {
		s.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");

		s.next(); //p3

		int width = s.nextInt();
		int height = s.nextInt();

		s.nextInt(); //255

		Image image = new Image(width, height);

		for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					int red = s.nextInt();
					int green = s.nextInt();
					int blue = s.nextInt();
					Pixel p = new Pixel(red, green, blue);
					image.addToArray(p);
				}
		}
		return image;
	}

	private static void modifyImage(Image image, String action) {
		if (action.equals(INVERT)) {
				image.invert();
		}
		if (action.equals(GRAY)) {
				image.grayScale();
		}
		if (action.equals(EMBOSS)) {
				image.emboss();
		}
		if (action.equals(BLUR)) {
				image.motionBlur();
		}
	}

    private static void writeFile(Image image, PrintWriter pw) {
        pw.println(IMAGE_HEADER);
        pw.println(image.getWidth());
        pw.println(image.getHeight());
        pw.println(Pixel.MAX_COLOR_VALUE);

        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                pw.println(image.getImage()[row][col].getRed());
                pw.println(image.getImage()[row][col].getGreen());
                pw.println(image.getImage()[row][col].getBlue());
            }
        }
        pw.close();
    }

    public static void main(String[] args) throws Exception {
		try {
			if (args.length != MIN_ARGS && args.length != MAX_ARGS) {
				System.out.println("Incorrect number of args");
				return;
			}
			if (args[2].equals(BLUR) && args.length == MIN_ARGS) {
				System.out.println("You need to provide a blur amount");
				return;
			}

			String inFile = args[0];
			String outFile = args[1];
			String action = args[2];

			int blurLength = 0;

			if (action.equals(BLUR)) {
				blurLength = Integer.parseInt(args[3]);
				if (blurLength <= 0) {
					System.out.println("blur length must be non-negative");
					return;
				}
			}

			FileReader r = new FileReader(inFile);
			Scanner s = new Scanner(r);
			PrintWriter pw = new PrintWriter(outFile);
			Image image = loadFile(s);
			image.setBlur(blurLength);
			modifyImage(image, action);
			writeFile(image, pw);
		}

		catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		catch (NumberFormatException e) {
			System.out.println("blur amount must be an int");
		}
    }
}
