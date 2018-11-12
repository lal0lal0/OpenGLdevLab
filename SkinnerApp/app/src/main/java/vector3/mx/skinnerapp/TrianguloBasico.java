package vector3.mx.skinnerapp;

import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

public class TrianguloBasico {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;


    //Inicializa el vertex Array
    public TrianguloBasico(float x, float y, float z) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createTrianguloBasico(
                new Geometry.Point(x, y, z), 3);
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
