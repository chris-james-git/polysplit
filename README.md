# polysplit

## Synopsis

The purpose of this project is to split a given [JTS](http://www.vividsolutions.com/jts/JTSHome.htm) polygon
into any number of parts.

The project implements two different splitting algorithms:

1) GreedyPolygonSplitter ("Khetarpal"): Split a polygon into exactly equal areas whilst ensuring minimum length of line
   based cuts. The solution is based on [this algorithm by Sumit Khetarpal](http://www.khetarpal.org/polygon-splitting/).
   It works for both convex and concave polygons, as long as they don't have any intersecting edges and are defined by a
   single exterior ring.
   
2) RoughPolygonSplitter: Split a polygon into roughly equal areas. The solution for the "approximate cut" algorithm
   is based on [this concept by Darafei Praliaskouski](https://lists.osgeo.org/pipermail/postgis-users/2018-June/042795.html).

## Code Examples
```
    Polygon polygon = (Polygon) new WKTReader().read("POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");
    List<Polygon> parts = new GreedyPolygonSplitter().split(polygon, 2);
```
```
    Polygon polygon = (Polygon) new WKTReader().read("POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");
    List<Polygon> parts = new RandomPolygonSplitter().split(polygon, 2);
```

## Build

The project is built using Maven.

Currently requires JDK 11.

## Command Line UI

There is also a command line input feature that lets you choose either the precise
method or the approximate method.

The inputs can be either "Standard" (WKT polygon + any number of parts)
or Demo (4 vertex input, 5 part split).

The output is a WKT format MULTIPOLYGON.

To build the project, from the root directory, run:

```
mvn clean package
```
Then, run the following:
```
java -jar target/polysplit-1.0-SNAPSHOT-jar-with-dependencies.jar
```
The following output is shown:
```
PolySplit
Authors: Gediminas Rim?a and Chris James
https://github.com/chris-james-git/polysplit

Options:
1. RandomPolygonSplitter
2. Khetarpal Splitter
Enter choice: [ 1 or 2 ]
```
Type `1` to split polygons using the random splitter, or `2` to use the exactly equal splitter.
You will then have another choice:
```
Options:
1. Standard (any number of vertices / parts)
2. 5-part split demo (4 vertex polygon split into 5 parts)
Enter choice: [ 1 or 2 ]
```
Select `1` for the standard method which takes a WKT format polygon as input and splits it into n parts.
Select `2` for a demo that asks the user to enter the coordinates of the vertices of a 4-sided polygon and then
splits it into 5 parts.

Example using the equal part ("Khetarpal") splitter on a trapezium, split into 7 parts:
```
PolySplit
Authors: Gediminas Rim?a and Chris James
https://github.com/chris-james-git/polysplit

Options:
1. RandomPolygonSplitter
2. Khetarpal Splitter
Enter choice: [ 1 or 2 ]
2
Khetarpal Splitter
Split polygons into exactly equal parts using the shortest cuts.

Options:
1. Standard (any number of vertices / parts)
2. 5-part split demo (4 vertex polygon split into 5 parts)
Enter choice: [ 1 or 2 ]
1
1. Standard (any number of vertices / parts)
Enter a polygon in WKT format:
POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))
Enter the number of parts (min 2):
5
MULTIPOLYGON (((77 0, 100 0, 90 50, 77 50, 77 0)), ((23 0, 0 0, 10 50, 23 50, 23 0)), ((41 0, 23 0, 23 50, 41 50, 41 0)), ((41 25, 41 50, 77 50, 77 25, 41 25)), ((77 0, 41 0, 41 25, 77 25, 77 0)))

```
And another example, using the random splitter for "approximately equal" parts with the 5-part 4-vertex demo. The polygon
used is the same as in the previous example:
```
PolySplit
Authors: Gediminas Rim?a and Chris James
https://github.com/chris-james-git/polysplit

Options:
1. RandomPolygonSplitter
2. Khetarpal Splitter
Enter choice: [ 1 or 2 ]
1
RandomPolygonSplitter
Split polygons into roughly equal parts.

Options:
1. Standard (any number of vertices / parts)
2. 5-part split demo (4 vertex polygon split into 5 parts)
Enter choice: [ 1 or 2 ]
2
2. 5-part split demo (4 vertex polygon split into 5 parts)
Enter double value for vertex 0 X:
0
Enter double value for vertex 0 Y:
0
Enter double value for vertex 1 X:
100
Enter double value for vertex 1 Y:
0
Enter double value for vertex 2 X:
90
Enter double value for vertex 2 Y:
50
Enter double value for vertex 3 X:
10
Enter double value for vertex 3 Y:
50
MULTIPOLYGON (((33.94130307858691 0, 0 0, 7.380541680243349 36.90270840121674, 34.78684336351697 21.232652033648577, 33.94130307858691 0)), ((68.69106121193533 0, 33.94130307858691 0, 34.78684336351697 21.232652033648577, 50.39151921885848 35.225794740219996, 67.94653771547277 21.03610479746363, 68.69106121193533 0)), ((7.380541680243349 36.90270840121674, 10 50, 50.853823137071316 50, 50.39151921885848 35.225794740219996, 34.78684336351697 21.232652033648577, 7.380541680243349 36.90270840121674)), ((50.853823137071316 50, 90 50, 92.76812495705238 36.159375214738084, 67.94653771547277 21.03610479746363, 50.39151921885848 35.225794740219996, 50.853823137071316 50)), ((92.76812495705238 36.159375214738084, 100 0, 68.69106121193533 0, 67.94653771547277 21.03610479746363, 92.76812495705238 36.159375214738084)))
```
## Known issues

**Caution: carefully test the code before considering it production-ready!**

A list of known issues can be found under the issues tab.

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