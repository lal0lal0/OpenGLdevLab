package vector3.mx.skinnerapp;

import android.content.Context;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by miMiau on 10/04/2018.
 * Este programa se usa para dibujar los puntos (player red y blue)
 */

public class ColorShaderProgram extends ShaderProgram {

    //Uniform locations
    private final int uMatrixLocation;
    private final int uColorLocation;

    //Attribute Locations
    private final int aPositionLocation;

    //private final int aColorLocation;

    public ColorShaderProgram(Context context){

        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        //Retrieve uniform locations for the shader program
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        //Retrieve attribute location for the shader program
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        //Set uniform location
        uColorLocation = glGetUniformLocation(program, U_COLOR);

    }



    public void setUniforms(float[] matrix, float r, float g, float b){

        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        //Pass the matrix into the shader program.
        glUniform4f(uColorLocation, r , g, b, 1f);


    }


    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }


}
