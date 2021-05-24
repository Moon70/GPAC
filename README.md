GPAC - a colour quantizer
==
**GPAC** stands for 'Gif Piece A Change'.

The name came up while listening to the album 'Shaved Fish' by John Lennon.



I needed a colour quantizer for the [Ganimed](https://github.com/Moon70/Ganimed) tool, because i wasn´t satisfied with Java´s GIF image quality.

GIF is a lossless graphics file format but limited to 256 colours. Therefore, when converting a 24-bit-truecolour image to GIF, a colour-reduction has to be applied to the image first, so that it uses at most 256 colours.

Java does this out-of-the-box, so converting an image to GIF looks very simple:

```java
BufferedImage image=ImageIO.read(new File("the-image-file-to-load"));
ImageIO.write(image,"GIF",new File("the-GIF-file-to-save"));
```

However, depending on the source image, the image quality of the resulting GIF file can be anything from 'good' to 'ugly'.



This is where GPAC jumps in:

```java
BufferedImage image=ImageIO.read(new File("the-image-file-to-load"));
new GPAC().quantizeColours(image,256);
ImageIO.write(image, "GIF", new File("the-GIF-file-to-save"));
```
The BufferedImage is converted to use at most 256 colours.



```java
BufferedImage image=ImageIO.read(new File("the-image-file-to-load"));
new GPAC()
    .setDitheringAlgorithm(DitheringAlgorithms.FLOYD_STEINBERG)
    .quantizeColours(image,256);
ImageIO.write(image, "GIF", new File("the-GIF-file-to-save"));
```
This example also applies a dithering algorithm to the image.

Algorithms available:

- Floyd-Steinberg
- Jarvis, Judice and Ninke
- Stucki
- Atkinson
- Burkes
- Sierra
- Two-Row-Sierra
- Sierra Lite



For further information, please visit the [GPAC Wiki](https://github.com/Moon70/GPAC/wiki).