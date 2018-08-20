package vector3.mx.skinnerapp;

import java.util.List;

import vector3.mx.skinnerapp.Geometry.Cylinder;
import vector3.mx.skinnerapp.Geometry.Point;
import vector3.mx.skinnerapp.ObjectBuilder.DrawCommand;
import vector3.mx.skinnerapp.ObjectBuilder.GeneratedData;

public class Puck {


    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = ( POSITION_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT ) * Constants.BYTES_PER_FLOAT;
    public final float radius, height;
    private final VertexArray vertexArray;
    private final List<DrawCommand> drawList;

    public Puck( float radius, float height, int numPointsAroundPuck){

        GeneratedData generatedData = ObjectBuilder.createPuck(
                new Cylinder(new Point( 0f, 0f, 0f),radius, height), numPointsAroundPuck);

        this.radius = radius;
        this.height = height;



        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData( ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getaColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw(){
        for ( DrawCommand drawCommand : drawList ){
            drawCommand.draw();
        }
    }

}
