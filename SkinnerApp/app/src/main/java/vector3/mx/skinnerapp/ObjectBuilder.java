package vector3.mx.skinnerapp;


import java.util.ArrayList;
import java.util.List;

import vector3.mx.skinnerapp.Geometry.Circle;
import vector3.mx.skinnerapp.Geometry.Cylinder;
import vector3.mx.skinnerapp.Geometry.Point;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;


/**
 * Created by miMiau on 10/06/2018.
 */


public class ObjectBuilder {


    private static final int FLOATS_PER_VERTEX = 3;
    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
    private int offset = 0;


    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }


    //Calculate the size of a Cylinder Top
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }


    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }


    static GeneratedData createPuck(Cylinder puck, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints)
                + sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        Circle puckTop = new Circle(
                puck.center.translateY(puck.height / 2f),
                puck.radius);
        builder.appendCircle(puckTop, numPoints);
        builder.appendOpenCylinder(puck, numPoints);

        //return builder.build();
        return null;
    }



    //Holder Class
    static class GeneratedData {

        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }

        //Construye y retorna una instancia de GenratedData
        private GeneratedData build(){
            return new GeneratedData(vertexData, drawList);
        }

    }


    static GeneratedData createMallet(
            Point center, float radius, float height, int numPoints){

        // Calcular el tamanio requerido para almacenar los objetos a dibujar
        int size = sizeOfCircleInVertices(numPoints) * 2 +
                sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        //First genrate the mallet base
        float  baseHeight = height * 0.25f;

        // Creamos un nuevo circulo, pasandole el radio, y moviendolo de lugar
        //  movemos el circulo a la posicion inicial deseada
        //  usando el vertice denominado: center , que hereda de Point y
        //  usamos su funcion translacion en eje Y, con la cantidad negativa
        //  equivalente a la altura.
        Circle baseCircle = new Circle( center.translateY( -baseHeight ), radius);

        // Ahora creamos un cilindro, se requiere el radio, un vertice central
        // y la altura. el centro de este cilindro es en referencia a
        // el primer circulo , en este metodo la altura se divide entre 2
        // para que el vertice central del cilindro quede ubicado hacia abajo.
        Cylinder baseCylinder = new Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight);

        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);

        //Generate the handle
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Circle handleCircle = new Circle(center.translateY( height * 0.75f), handleRadius);
        Cylinder handleCylinder = new Cylinder(
                handleCircle.center.translateY( -handleHeight / 2f ), handleRadius, handleHeight);

        builder.appendCircle( handleCircle, numPoints);
        builder.appendOpenCylinder( handleCylinder, numPoints);

        return null;
    }



    public void appendCircle(Circle circle, int numPoints) {

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        //Center point of fan
        //Defino el vertice del centro del circulo
        //utilizando tres posiciones del arreglo para x y z

        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        //Por eso el punto del centro se define fuera del FOR


        // fan around center point. <= is used because
        // we want to generate the point at the
        // starting  angle twice  to complete the fan.

        for (int i = 0; i <= numPoints; i++) {

            //Calcular y convertir el angulo en Radianes
            // para ello, el indice actual i que incrementa en ++
            // se divide por la cantidad todal de puntos o vertices
            //  luego se mulitplica por el doble de pi
            float angleInRadians = ((float) i / (float) numPoints) *
                    ((float) Math.PI * 2f);

            vertexData[offset++] = (float) (circle.center.x +
                    circle.radius * Math.cos(angleInRadians));

            vertexData[offset++] = circle.center.y;

            vertexData[offset++] = (float) (circle.center.z +
                    circle.radius * Math.sin(angleInRadians));
            //correccion : FloatMath() esta deprecated,
            // x lo que se usa Math.cos() con casting a float

        }


        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });


    }


    static interface DrawCommand {


        void draw();


    }


    public void appendOpenCylinder(Cylinder cylinder, int numPoints) {


        final int startVertex = (offset / FLOATS_PER_VERTEX);

        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);

        final float yStart = cylinder.center.y - (cylinder.height / 2f);

        final float yEnd = cylinder.center.y + (cylinder.height / 2f);


        //Generate Triangle Strip

        for (int i = 0; i < numPoints; i++) {

            //Calcular el angulo en  radianes
            float angleInRadians = ((float) i / (float) numPoints) *
                    (float) (Math.PI * 2f);

            float xPosition = cylinder.center.x + cylinder.radius *
                    ((float) Math.cos(angleInRadians));

            float zPosition = cylinder.center.z + cylinder.radius *
                    ((float) Math.sin(angleInRadians));


            //Top cylinder vertice
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;


            //Bottom cyliinder vertice
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }


        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });

    }//fin de appendOpenCylinder()


}
