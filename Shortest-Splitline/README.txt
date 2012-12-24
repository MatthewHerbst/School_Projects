Welcome to a basic implementation of the Shortest-Splitline algorithm. By basic, I 
mean that it does not find the shortest splitline. Rather, the program uses the median 
splitline. A future version will use ths shortest splitline (very hard to calculate!).

To use this program, use one of the provided input files, or create your own.
When you first run the program, you will need to enter an input file. containing map 
and population information.

The output, which will be the coordinates of each of the final districts, as well as
the population of each district, will be printed to the command line.

Input should should look as follows:
4
1,1
5,2
5,5
2,4
5
2.5,2
3,2.5
3,3
4,3.5
4,4
5

The first line of input specifies the P number of points that will be used to form 
the boundary of the region. These points are then on the next P lines of input. 
This boundary will be used to create a polygon area by forming edges between the 
points specified in counter-clockwise order. So, for example, there would be an 
edge between (1,1) and (5,2), but there would not be an edge between (1,1) and (5,5).
There would also be an edge between (2,4) and (1,1).

The next line of input after these points contains the U number of points representing
population units within the polygon specified before.

The last line of input represents the N number of districts that the user wishes to
create from the previously read data.

All numbers related to quantity must be positive integers. Points representing the
polygon of the area must also be positive integers (negative could cause some funcky
math erros, and integers is a restriction of the Java Polygon class). Points
representing population units must be positive, but they may be in integer or
double form.

Also please note, all points representing the polygon must be unique.