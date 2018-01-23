# Sketch_SVG
An Android app to save sketches in SVG format
This application provides a canvas for the user to draw and allows them to save the drawing as a SVG file on the server. For this it extracts vertices from the drawings and sends them over to a server, where cubic bezier curves are fit to those vertices and finally the curves are saved as an SVG.

### Setup

1. Run the ```server.py``` program on a server or computer.
2. Feed in the ```ip address``` and ```port``` in the app script.
3. Build the app and run it on a phone or emulator.

The app has been tested on a Nexus 9 tablet.

### Usage

<img src="/screenshot.png" alt="Screenshot" width="50%">

1. The app interface in minimal and looks as above.
2. The majority is a blank canvas, with three buttons at the bottom.
3. The first two are self explanatory, while the third one ```Done``` finishes the drawing and saves it as SVG on the server.
