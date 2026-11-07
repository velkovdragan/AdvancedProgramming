package ZaVezbanje.first;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Canvas{
    String id;
    int squaresCount;
    int perimeter;

    public Canvas(String id, int squaresCount, int perimeter) {
        this.id = id;
        this.squaresCount = squaresCount;
        this.perimeter = perimeter;
    }

    public int getPerimeter() {
        return perimeter;
    }

    public String getId() {
        return id;
    }

    public int getSquaresCount() {
        return squaresCount;
    }
}
class ShapesApplication{
    private List<Canvas>canvases;
    public ShapesApplication() {
        canvases=new ArrayList<>();
    }

    public int readCanvases (InputStream inputStream){
        Scanner scanner=new Scanner(inputStream);
        int counter=0;
        while(scanner.hasNextLine()){
            String linija=scanner.nextLine();
            String[] podeleni=linija.split(" ");
            String canvasId=podeleni[0];
            int squarescount=podeleni.length-1;
            counter+=squarescount;

            int perimeter=0;
            for(int i=1;i<podeleni.length;i++){
                int side=Integer.parseInt(podeleni[i]);

                perimeter+=4*side;
            }
            Canvas canvas = new Canvas(canvasId,squarescount,perimeter);
            canvases.add(canvas);
        }
        return counter;
    }

    void printLargestCanvasTo (OutputStream outputStream){
        PrintWriter writer=new PrintWriter(outputStream);
        int max=0;
        Canvas c=null;
        for(Canvas canvas:canvases){
            if(canvas.getPerimeter()>max){
                max=canvas.getPerimeter();
                c=canvas;
            }
        }

        writer.println(c.getId()+" "+c.getSquaresCount()+" "+c.getPerimeter());

        writer.flush();

    }

}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}