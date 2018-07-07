package vector3.mx.skinnerapp;

/**
 * Created by miMiau on 10/06/2018.
 */

public class Geometry {

    //Clsse Punto define un punto en el espacio 3d
    public static class Point {
        //Los puntos para definir un vertice.
        public final float x, y, z;

        //Constructor para un punto en una posicion determinada
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        //Mover en vertical its a helper function
        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }

        //Mover en vertical its a helper function
        public Point translateX(float distance) {
            return new Point(x + distance, y , z);
        }
    }

    public static class Triangulo{
        //el centro del circulo inicia con una instancia de Punto Point
        // el cual tiene las 3 coordenadas x,y z
        public final Point center;

        public Triangulo(Point center){
            this.center = center;
        }
    }

    //Un Circulo representado con un Punto
    public static class Circle {
        //el centro del circulo inicia con una instancia de Punto Point
        // el cual tiene las 3 coordenadas x,y z
        public final Point center;
        public final float radius;

        //En el constructor se requiere x parametro un centro
        // por eso se llama asi la variable del primer parametro
        //porque se requiere su centro definido por x y ,z
        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        //Se requiere naturalmente un metodo Escalar  scale
        // para incrementar su tamao a partir de un  numero decimal
        //donde para escalar el circulo  se multiplica el radio
        //por la escala
        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }


    }

    //Clase para un Cilindro Cilinder, requiere un centro,
    //representado por un Point, tambien ocupa un radio
    //representado por radius y una altura en Height
    public static class Cylinder {

        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }


    }



    public static class Linea{
        public final Point center;
        public Linea(Point center){
            this.center = center;
        }
    }



}
