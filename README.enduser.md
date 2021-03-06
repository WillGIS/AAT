# README

## Map Tiles
AAT does not automatically remove downloaded map tiles. This is to...

- reduce network traffic as much as possible.
- reduce battery drain.
- speed up the map view.

This means that you have to remove old tiles manually from time to time.
Go to `Settings->Map Tiles` to remove old map tiles.


## Offline Maps
Enable offline map rendering:

- Download map files from the [Mapsforge download server](http://download.mapsforge.org/).
- Provide the directory that contains the map files in the settings dialog (`Settings->Map Tiles`).
- Disable Mapnik and enable Mapsforge in `Menu->Map`.


## Where to find files?
AAT let's you create and view files in GPX format. This files are exchangeable with many other applications. They can also be edited in a text editor. 
These and other files are stored in subdirectories of the data directory. You can choose the location of the data directory from the settings.
The structure of the data directory is as follows: 

- `aat_data/activity[0-4]` - tracked activities
- `aat_data/log`           - activity that gets tracked at the moment
- `aat_data/nominatim`     - search result from OpenStreetMap Nominatim
- `aat_data/osm_features`  - OSM Features original file and parsed files
- `aat_data/overlay`       - overlays such as planned tracks or search results
- `aat_data/overpass`      - queries from the OSM Overpass server
- `aat_data/dem3`          - cached [Digital elevation data](http://viewfinderpanoramas.org/dem3.html) from the Shuttle Radar Topography Mission and other sources


## Overpass
### Overview
AAT lets you query the [Overpass API](http://wiki.openstreetmap.org/wiki/Overpass_API). Any result of a query can be saved to  `aat_data/overlay`. It then can be displayed as an overlay inside the map-view whenever needed.

### Example
`[amenity=restaurant]` will get you list of all restaurants located inside the area that was visible on the map-view before opening the Overpass dialog. 


## Issues and feedback
For questions, feedback and bugreports send an e-mail to aat@bailu.ch

