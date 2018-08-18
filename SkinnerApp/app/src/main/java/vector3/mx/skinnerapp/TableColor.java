package vector3.mx.skinnerapp;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by miMiau on 05/04/2018.
 */

public class TableColor {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = ( POSITION_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT ) * Constants.BYTES_PER_FLOAT;
    //Vertex Data
    private static final float[] VERTEX_DATA = {
            //Order of coordinates  X Y R G B
            //Triangle fan
            // x     y      r     g    b
               0f,    0f, 0.5f, 0.5f, 0.2f,
            -0.5f, -0.8f,   0f, 0.9f, 0.3f,
             0.5f, -0.8f,   1f, 0.9f, 0.4f,
             0.5f,  0.8f,   1f, 0.1f, 0.5f,
            -0.5f,  0.8f,   0f, 0.1f, 0.6f,
            -0.5f, -0.8f,   0f, 0.9f, 0.7f  };
    private final VertexArray vertexArray;


    //Inicializa el vertex Array
    public TableColor() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getaColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);

    }

    public void draw(){
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
