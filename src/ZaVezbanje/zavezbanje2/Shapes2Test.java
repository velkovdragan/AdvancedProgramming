package ZaVezbanje.zavezbanje2;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

class Canvas {
    String id;
    double plostina;
    int circles;
    int squares;
    double minArea;
    double maxArea;
    double averageArea;

    public Canvas(String id, double plostina, int circles, int squares,
                  double minArea, double maxArea, double averageArea) {
        this.id = id;
        this.plostina = plostina;
        this.circles = circles;
        this.squares = squares;
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.averageArea = averageArea;
    }

    public double getPlostina() {
        return plostina;
    }

    public String getId() {
        return id;
    }

    public int getShapes() {
        return circles + squares;
    }

    public int getCircles() {
        return circles;
    }

    public int getSquares() {
        return squares;
    }

    public double getMinArea() {
        return minArea;
    }

    public double getMaxArea() {
        return maxArea;
    }

    public double getAverageArea() {
        return averageArea;
    }
}

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String message) {
        super(message);
    }
}

class ShapesApplication {
    private double maxArea;
    private List<Canvas> canvases;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.canvases = new ArrayList<>();
    }

    void readCanvases(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String vlez = scanner.nextLine().trim();

            if (vlez.isEmpty()) {
                continue;
            }

            String[] podeleno = vlez.split("\\s+");
            String id = podeleno[0];

            try {
                int countersquares = 0;
                int countercircles = 0;

                double plostina = 0;
                double minArea = Double.POSITIVE_INFINITY;
                double maxShapeArea = Double.NEGATIVE_INFINITY;

                for (int i = 1; i < podeleno.length; i += 2) {

                    String type = podeleno[i];
                    double size = Double.parseDouble(podeleno[i + 1]);

                    double area;

                    if (type.equals("C")) {
                        area = Math.PI * size * size;
                        countercircles++;
                    } else if (type.equals("S")) {
                        area = size * size;
                        countersquares++;
                    } else {
                        throw new IllegalArgumentException("Unknown shape: " + type);
                    }

                    // Validate each individual shape
                    if (area > maxArea) {
                        throw new IrregularCanvasException(
                                "Canvas " + id +
                                        " has a shape with area larger than " + maxArea + "0"
                        );
                    }

                    // Update statistics
                    plostina += area;
                    minArea = Math.min(minArea, area);
                    maxShapeArea = Math.max(maxShapeArea, area);
                }

                int totalShapes = countersquares + countercircles;

                if (totalShapes == 0) {
                    minArea = 0;
                    maxShapeArea = 0;
                }

                double averageArea = totalShapes == 0
                        ? 0
                        : plostina / totalShapes;

                // Create and store a valid canvas
                Canvas canvas = new Canvas(
                        id,
                        plostina,
                        countercircles,
                        countersquares,
                        minArea,
                        maxShapeArea,
                        averageArea
                );

                canvases.add(canvas);

            } catch (IrregularCanvasException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void printCanvases(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        // Sort by total area in descending order
        canvases.sort(
                Comparator.comparingDouble(Canvas::getPlostina).reversed()
        );

        for (Canvas canvas : canvases) {
            writer.printf(Locale.US, "%s %d %d %d %.2f %.2f %.2f%n",
                    canvas.getId(),
                    canvas.getShapes(),
                    canvas.getCircles(),
                    canvas.getSquares(),
                    canvas.getMinArea(),
                    canvas.getMaxArea(),
                    canvas.getAverageArea()
            );
        }

        writer.flush();
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);
    }
}