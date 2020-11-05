# CorrelativeView
This plugin is a new viewer allowing to add to a sequence (image) another image aligned with ec-clem, 
with NO rescaling of the images : both images keep their original resolution.
The principle is to change only the "camera" and "screen" position of the source image, according to the transformation pre-computed by ec-clem 
(in the transformation matrix)

## Install for Beta-test
Correlative View should be installed from ICY directly when released.
This is the dev version, not released yet under ICY plugin repository.
To test it, download the .jar under binaryhttps://github.com/anrcrocoval/ec-clem/raw/master/binary/ec_clem-2.0.1-SNAPSHOT.jar and copy it under 
`<your-icy-directory>/plugins/perrine/correlativeview` 

Both official icy ec-clem and this test version can coexist: call the old version by search ec-clem and the new one easyclem in the icy search bar.
## Usage:
+ open the image considered as the target image in the ec-clem , change viewer to correlativbe view (instead of 2D viewer, 3D ciever, Orthoslice vower...)
+ use the openned dialog to load the source image
+ use the next openned dialog to load the transformation file
+ You can play on the transparency mixing in the overlay panel.


Tip: you can always inverse a transformation schema using invert transformation schema in ec-clem in order to compute the inverse transforme 
if you need one image to be the source instead of target.
Demo video:
https://youtu.be/NDWa-sKOGtw
