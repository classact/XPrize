package classact.com.xprize.utils;

import classact.com.xprize.activity.drill.math.MathsDrillTwoActivity;

/**
 * Created by hyunchanjeong on 2017/05/13.
 */

public class SquarePacker {

    private int width;
    private int height;
    private int totalArea;
    private double widthToHeightRatio;

    public SquarePacker(int width, int height) {
        this.width = width;
        this.height = height;
        totalArea = width * height;
        widthToHeightRatio = (double) width / height;
    }

    public Square[] get(int n) {
        // Setup squares
        Square[] squares = new Square[n];

        // Setup unit data
        int numHorizontalUnits = 0;
        int numVerticalUnits = 0;
        int unit = 0;
        int offset = 0;
        int areaDifference = 0;

        // Setup previous unit data
        int prevNumHorizontalUnits = 0;
        int prevNumVerticalUnits = 0;
        int prevUnit = 0;
        int prevOffset = 0;
        int prevAreaDifference = 0;

        for (int i = 0; i < n; i++) {
            // Assign unit data
            numHorizontalUnits = i + 1;
            numVerticalUnits = (int) (Math.ceil((double) n / numHorizontalUnits));
            unit = width / numHorizontalUnits;

            // Set up other data
            int combinedWidth = unit * numHorizontalUnits;
            int combinedHeight = unit * numVerticalUnits;

            // Calculate area and area difference
            int area = combinedWidth * combinedHeight;
            areaDifference = totalArea - area;
            offset = 0;

            // Print out
            /* System.out.println("Square packer (" +
                    numHorizontalUnits + "x" +
                    numVerticalUnits + "): combined height is " + combinedHeight); */

            // Recalibrate area if required
            if (combinedHeight > height) {
                // Get ratio
                double ratio = (double) height / combinedHeight;
                // Get new unit data
                int newUnit = (int) (Math.floor((double) unit * ratio));
                // Get combined old and new widths
                int newCombinedWidth = newUnit * numHorizontalUnits;
                // Get offset
                offset = (combinedWidth - newCombinedWidth)/2;
                // Change old unit data to new unit data
                unit = newUnit;
                combinedWidth = newCombinedWidth;
                combinedHeight = unit * numVerticalUnits;
                area = combinedWidth * combinedHeight;
                areaDifference = totalArea - area;
            }

            // Print out
            /* System.out.println("Area difference: " + areaDifference); */

            // As long as it's not the first index ...
            if (i > 0) {
                // Compare area difference with previous area difference
                if (prevAreaDifference < areaDifference) {
                    numHorizontalUnits = prevNumHorizontalUnits;
                    numVerticalUnits = prevNumVerticalUnits;
                    unit = prevUnit;
                    offset = prevOffset;

                    // We've found the highest dude
                    // Let's start packing
                    break;
                }
            }

            // Last stretch logic
            if (i == n - 1) {
                // don't care, just end it
                break;
            // Otherwise ...
            } else {
                // Archive current data
                prevNumHorizontalUnits = numHorizontalUnits;
                prevNumVerticalUnits = numVerticalUnits;
                prevUnit = unit;
                prevOffset = offset;
                prevAreaDifference = areaDifference;
            }
        }
        // Start packing!
        return pack(squares, unit, offset, numHorizontalUnits, numVerticalUnits);
    }

    private Square[] pack(Square[] squares, int unit, int offset, int numHorizontalUnits, int numVerticalUnits) {
        // Setup data
        int squaresPacked = 0;

        // Print out
        /* System.out.println("Number of horizontal units: " + numHorizontalUnits);
        System.out.println("Number of vertical units: " + numVerticalUnits); */

        // Start packing!
        for (int i = 0; i < numVerticalUnits; i++) {

            // Process horizontal units
            for (int j = 0; j < numHorizontalUnits; j++) {

                // Init square
                squares[squaresPacked] = new Square(unit);

                // Pack the square
                squares[squaresPacked].x = (j * unit) + offset;
                squares[squaresPacked].y = i * unit;

                // Print out
                /* System.out.println("Square (" +
                    squares[squaresPacked].x + "," +
                    squares[squaresPacked].y + "," +
                    squares[squaresPacked].w + ") packed!"); */

                // increase number of squares packed
                squaresPacked++;

                // Print out
                /* System.out.println("# of squares: " + squares.length);
                System.out.println("# of squares packed: " + squaresPacked); */

                // If all squares packed, break it and take it!
                if (squaresPacked >= squares.length) {
                    break;
                }
            }
            // If all squares packed, break it and take it!
            if (squaresPacked >= squares.length) {
                break;
            }
        }
        return squares;
    }
}