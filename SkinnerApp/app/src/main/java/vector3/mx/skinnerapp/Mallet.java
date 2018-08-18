package vector3.mx.skinnerapp;

import java.util.List;

import vector3.mx.skinnerapp.Geometry.Point;
import vector3.mx.skinnerapp.ObjectBuilder.DrawCommand;
import vector3.mx.skinnerapp.ObjectBuilder.GeneratedData;


/**
 * Created by miMiau on 06/04/2018.
 */

public class Mallet {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    public final float radius , height ;
    //public final float height;
    private final VertexArray vertexArray ;
    private final List<DrawCommand> drawList ;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;


    //Inicializa el vertexArray
    public Mallet(float radius, float height, int numPointsAroundMallet) {

        GeneratedData generatedData = ObjectBuilder.createMallet(
                new Point(0f,0f,0f),radius, height, numPointsAroundMallet);

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }


    public void bindData(ColorShaderProgram colorProgram){

        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0 );

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getaColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }


    public void draw(){
        for(DrawCommand drawCommand : drawList){
            drawCommand.draw();
        }
    }


}
