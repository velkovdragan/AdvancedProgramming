package ZaVezbanje.zavezbanje6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

enum Color {
    RED, GREEN, BLUE
}
interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}

class Canvas {
    private List<Shape>shapes=new ArrayList<>();

    public Canvas(){}

    public void add(String id, Color color, float radius) {
        Circle circle=new Circle(id,color,radius);
        insert(circle);
    }

    public void add(String id, Color color, float width, float height) {
        Rectangle rectangle=new Rectangle(id,color,width,height);
        insert(rectangle);
    }

    private void insert(Shape s) {
        for(int i=0;i<shapes.size();i++){
            if(shapes.get(i).weight()<s.weight()){
                shapes.add(i,s);
                return;
            }
        }
        shapes.add(s);
    }
    void scale(String id, float scaleFactor){
        for(Shape shape:shapes){
            if(shape.getId().equals(id)){
                shapes.remove(shape);
                shape.scale(scaleFactor);
                insert(shape);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return shapes.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }



}
abstract class Shape implements Scalable,Stackable{
    private String id;
    private Color color;

    public Shape(String id,Color color){
        this.id=id;
        this.color=color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }
}
class Circle extends Shape{
    private float radius;

    public Circle(String id,Color color,float radius){
        super(id,color);
        this.radius=radius;
    }
    @Override
    public void scale(float scaleFactor){
        radius*=scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius*radius*Math.PI);
    }

    @Override
    public String toString(){
        return String.format("C: %-5s%-10s%10.2f\n", getId(), getColor(), weight());

    }
}
class Rectangle extends Shape {
    private float width;
    private float height;

    public Rectangle(String id,Color color,float width,float height){
        super(id,color);
        this.width=width;
        this.height=height;
    }
    @Override
    public void scale(float scaleFactor){
        width*=scaleFactor;
        height*=scaleFactor;
    }

    @Override
    public float weight() {
        return width*height;
    }
    @Override
    public String toString(){
        return String.format("R: %-5s%-10s%10.2f\n", getId(), getColor(), weight());
    }
}