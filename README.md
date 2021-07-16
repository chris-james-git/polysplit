# polysplit

## Synopsis - Precisely Equal Parts

The purpose of this project is to split a given [JTS](http://www.vividsolutions.com/jts/JTSHome.htm) polygon into any 
number of equal areas, while ensuring minimum length of line based cuts. The solution is based on 
[this algorithm by Sumit Khetarpal](http://www.khetarpal.org/polygon-splitting/). It works for both convex and concave 
polygons, as long as they don't have any intersecting edges and are defined by a single exterior ring.

## Code Example
```
    Polygon polygon = (Polygon) new WKTReader().read("POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");
    List<Polygon> parts = new GreedyPolygonSplitter().split(polygon, 2);
```

## Synopsis - Approximately Equal Parts

This particular fork also includes the addition of an "approximate cut" algorithm that is based on 
[this concept by Darafei Praliaskouski](https://lists.osgeo.org/pipermail/postgis-users/2018-June/042795.html).

## Code Example
```
    Polygon polygon = (Polygon) new WKTReader().read("POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");
    List<Polygon> parts = new RandomPolygonSplitter().split(polygon, 2);
```


## Command Line UI

There is also a command line input feature that lets you choose either the precise
method or the approximate method.

The inputs can be either "Standard" (WKT polygon + any number of parts)
or Demo (4 vertex input, 5 part split).

The output is a WKT format MULTIPOLYGON.

## Known issues

**Caution: carefully test the code before considering it production-ready!**

A list of known issues can be found under the issues tab.

## Build

The project is built using Maven.

Currently requires JDK 11.

## Tests

Unit tests are present for most methods.

Test cases covering real-world or randomly generated scenarios could be added.

## Contributors

Original fork (GreedyPolygonSplitter, EdgePair, Cut,
GeometryFactoryUtils and GeometryUtils):

* Developed by: Gediminas Rimša

* Sponsored by: Incentergy GmbH

Additional work (RandomPolygonSplitter, RandomSplitterGeometryFactoryUtils)

* Developed by: Chris James
* Inspired by: Darafei "Komяpa" Praliaskouski
* Dependency authors: Jason Altschuler (KMeans) and all JTS contributors.

**Contributions are welcome!**

## Licence

Licensed under the [MIT license](https://github.com/grimsa/polysplit/blob/master/LICENSE).