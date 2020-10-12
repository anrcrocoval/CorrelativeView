# CorrelativeView
This plugin is a new viewer allowing to add to a sequence (image) another image aligned with ec-clem, 
with NO rescaling of the images : both images keep their original resolution.
The principle is to change only the "camera" and "screen" position of the source image, according to the transformation pre-computed by ec-clem 
(in the transformation matrix)

**Usage: **
* open the image considered as the target image in the ec-clem , change viewer to correlativbe view (instead of 2D viewer, 3D ciever, Orthoslice vower...)
* use the openned dialog to load the source image
* use the next openned dialog to load the transformation file
* You can play on the transparency mixing in the overlay panel.


Tip: you can always inverse a transformation schema using invert transformation schema in ec-clem in order to compute the inverse transforme 
if you need one image to be the source instead of target.