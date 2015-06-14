import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;

/* This class loads an image from an external file and can perform some basic image
 * processing algorithms on it. */
public class ImageProcessing extends Component {
    BufferedImage img;

    public void paint(Graphics g) {
    	g.drawImage(img, 0, 0, null);
    }

    public ImageProcessing(String filename) {
    	try {
    		img = ImageIO.read(new File(filename));
       	}
       	catch (IOException e) {
			System.out.println("Problem opening " + filename + ": " + e);
       	}
    }

 	public Dimension getPreferredSize() {
        if (img == null) {
        	return new Dimension(100,100);
        } else {
       		return new Dimension(img.getWidth(), img.getHeight());
       	}
    }

	//Sets the color at pixel (x,y) in the specified image.
    public void setPixel(BufferedImage image, int x, int y, RGB color) {
		image.setRGB(x, y, color.getR()<<16|color.getG()<<8|color.getB());
	}

	//Returns the color in the specified image at the specified (x,y) coordinates.
	public RGB getPixel(BufferedImage image, int x, int y) {
		int rgb = image.getRGB(x,y);
		int r = rgb>>16 & 0xFF;
		int g = rgb>>8 & 0xFF;
		int b = rgb & 0xFF;

		return new RGB(r, g, b);
	}

    public static void main(String[] args) {
        JFrame f = new JFrame("Image Processor");
        f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
        });

		String location = "";
		location = args[args.length-1];
		ImageProcessing imgProc = new ImageProcessing(location);

		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to the Image Processor");
		System.out.println("Please select one of the following options:");
		System.out.println("1.) Invert");
		System.out.println("2.) Scale");
		System.out.println("3.) Grey Scale");
		System.out.println("4.) Horizontal Flip");
		System.out.println("5.) Vertical Flip");
		System.out.println("6.) Rotation");
		System.out.println("7.) Quit\n");
		int input;

		//check for image operation
		input = in.nextInt();
		switch(input) {
			case 1: imgProc.invert();
					break;
			case 2: imgProc.scaleSize();
					break;
			case 3: imgProc.greyScale();
					break;
			case 4: imgProc.flipHorizontal();
					break;
			case 5: imgProc.flipVertical();
					break;
			case 6: imgProc.rotate();
					break;
			case 7:	System.out.println("Exiting the Image Processor");
					System.exit(0);			
		}

		f.add(imgProc);
    	f.pack();
   		f.setVisible(true);
		
	}

	//invert operation
	public void invert() {
		int w = img.getWidth();
		int h = img.getHeight();

		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				RGB color = getPixel(img, i,j);
				int r = color.getR();
				int g = color.getG();
				int b = color.getB();
				r = 255-r;
				g = 255-g;
				b = 255-b;

				setPixel(img, i, j, new RGB(r, g, b));
			}
		}
	}

	//scaling operation
	private void scaleSize() {
		int newWidth = 2*(img.getWidth() - (img.getWidth()%3))/3;
		int newHeight = 2*(img.getHeight() - (img.getHeight()%3))/3;
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, img.TYPE_INT_RGB);

		for(int x = 0; x < img.getWidth() - (img.getWidth()%3); x+=3) {
		    for(int y = 0; y < img.getHeight() - (img.getHeight()%3); y+=3) {
				RGB P11 = getPixel(img, x, y);
				RGB P12 = getPixel(img, x+1, y);
				RGB P13 = getPixel(img, x+2, y);
				RGB P21 = getPixel(img, x, y+1);
				RGB P22 = getPixel(img, x+1, y+1);
				RGB P23 = getPixel(img, x+2, y+1);
				RGB P31 = getPixel(img, x, y+2);
				RGB P32 = getPixel(img, x+1, y+2);
				RGB P33 = getPixel(img, x+2, y+2);

				RGB N1 = Ncalculator(P11, P12, P21, P22);
				RGB N2 = Ncalculator(P13, P12, P23, P22);
				RGB N3 = Ncalculator(P31, P21, P32, P22);
				RGB N4 = Ncalculator(P33, P23, P32, P22);

				setPixel(newImage, (x*2/3), (y*2/3), N1);
				setPixel(newImage, (x*2/3)+1, (y*2/3), N2);
				setPixel(newImage, (x*2/3), (y*2/3)+1, N3);
				setPixel(newImage, (x*2/3)+1, (y*2/3)+1, N4);
			}
		}
        img = newImage; 
	}

	//calculate the new RGB values
	private RGB Ncalculator(RGB a, RGB b, RGB c, RGB d) {
		int Par, Pag, Pab, Pbr, Pbg, Pbb, Pcr, Pcg, Pcb, Pdr, Pdg, Pdb;
		int Nr, Ng, Nb;
		Par = a.getR();
		Pag = a.getG();
		Pab = a.getB();
		Pbr = b.getR();
		Pbg = b.getG();
		Pbb = b.getB();
		Pcr = c.getR();
		Pcg = c.getG();
		Pcb = c.getB();
		Pdr = d.getR();
		Pdg = d.getG();
		Pdb = d.getB();

		Nr = (4*Par + 2*Pbr + 2*Pcr + Pdr)/9;
		Ng = (4*Pag + 2*Pbg + 2*Pcg + Pdg)/9;
		Nb = (4*Pab + 2*Pbb + 2*Pcb + Pdb)/9;

		RGB N = new RGB(Nr, Ng, Nb);
		return N;
	}

	//grey scale operation
	private void greyScale() {
		int w = img.getWidth();
		int h = img.getHeight();

		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				RGB color = getPixel(img, i,j);
				int r = color.getR();
				int g = color.getG();
				int b = color.getB();

				int y = (299 * r + 587 * g + 114 * b + 499) / 1000;

				setPixel(img, i, j, new RGB(y, y, y));
			}
		}
	}

	//horizontal flip operation
	private void flipHorizontal() {
		int w = img.getWidth();
		int h = img.getHeight();

		for(int i = 0; i < w/2; i++) {
			for(int j = 0; j < h; j++) {
				int rgb1 = img.getRGB(w-i-1, j);
				int rgb2 = img.getRGB(i,j);
				img.setRGB(i, j, rgb1);
				img.setRGB(w-i-1, j, rgb2);
			}
		}
	}

	//vertical flip operation
	private void flipVertical() {
		int w = img.getWidth();
		int h = img.getHeight();

		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h/2; j++) {
				int rgb1 = img.getRGB(i, h-j-1);
				int rgb2 = img.getRGB(i,j);
				img.setRGB(i, j, rgb1);
				img.setRGB(i, h-j-1, rgb2);
			}
		}
	}

	//rotation operation
	private void rotate() {
		int w = img.getHeight();
        int h = img.getWidth();
        BufferedImage temp = new BufferedImage(w, h, img.TYPE_INT_RGB);

        for(int i = 0; i < h/2; i++){
            for(int j = 0; j < w; j++){
                int rgb1 = img.getRGB(h-1-i,j);
                int rgb2 = img.getRGB(i, j);
                img.setRGB(i,j,rgb1);
                img.setRGB(h-1-i,j,rgb2);
            }
        }

        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight()-1; y++) {
                int pixel = img.getRGB(x,y);
                temp.setRGB(y, x, pixel);
            }
        }
        
        img = temp;
	}
}