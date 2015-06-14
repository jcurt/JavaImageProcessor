This is a basic image processor written in Java.

Compile/Run Instructions: 
1.) Compile using "javac ImageProcessor.java" 
2.) Run using "java ImageProcessor path-to-image-file" 
3.) Select the image operation and view the changes to your image

Image Operations: 
1.) Invert - inverts your image by taking the RGB values of the pixels of your image and calculates the new RGP values by computing the formula: 255 - RGB values. 
2.) Scale - scales your image to a third of the original size by taking the RGP values and compressing them. 
3.) Grey Scale - converts your image to grey scale by taking the RGB values and converting them into a new pixel using the formula: (299 * r + 587 * g + 114 * b + 499) / 1000 
4.) Horizontal Flip - flips your image horizontally by setting the pixels on one end of the image to the opposite end 
5.) Vertical Flip - flips your image vertically by setting the pixels on one end of the image to the opposite end 
6.) Rotation - rotates your image counter-clockwise by 90 degrees manipulating the x and y coordinates of the pixels