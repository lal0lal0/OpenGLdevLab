package vector3.mx.skinnerapp;

import java.util.List;

public class Linea {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Linea(){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createLinea();
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }
    public void bindData( ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(){
        for ( ObjectBuilder.DrawCommand drawCommand : drawList ){
            drawCommand.draw();
        }
    }

}
