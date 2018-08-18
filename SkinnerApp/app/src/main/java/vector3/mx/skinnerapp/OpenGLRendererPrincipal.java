package vector3.mx.skinnerapp; /**
 * Created by miMiau on 21/01/2018.
 */

import static android.opengl.GLES20.*;
import android.opengl.GLES20;
import static android.opengl.Matrix.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;



public class OpenGLRendererPrincipal implements Renderer {

    private final Context context;

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table table;
    private Mallet mallet;
    private Puck puck;
    
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;

    //Constructor
    public OpenGLRendererPrincipal(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 0f);
        table = new Table();
        mallet = new Mallet(0.08f, 0.15f, 3 * 9);
        puck = new Puck(006f, 0.02f, 32);
        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45,
                (float) width / (float) height, 1f, 10f);

        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f,
                0f, 0f, 0f, 0f, 1f, 0f);

        /*
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            //LandScape
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio,
                    -1f, 1f, -1f, 1f);
        } else {
            //Portrait or Square
            orthoM(projectionMatrix, 0, -1f, 1f,
                    -aspectRatio, aspectRatio, -1f, 1f);
        }
        MatrixHelper.perspectiveM(projectionMatrix, 45,
                (float) width / (float) height, 1f, 10f);
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -2f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
        */
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //Clear the rendering surface
        GLES20.glClear(GL_COLOR_BUFFER_BIT);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        //positionTableInScene();
        //colorProgram.setUniforms(modelViewProjectionMatrix);
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectionMatrix, texture);
        //table.bindData(textureProgram);
        //table.draw();

        //draw the mallets

        positionObjectInScene(0f, 0f, 0f );
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();

        //positionObjectInScene(0f, mallet.height / 2f, 0.4f);
        //colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        // note that we dont  have to define the object data twice  - we just
        // draw the same mallet again but in different position and with a
        // different color
        //mallet.draw();

        // draw the puck


        //positionObjectInScene(0f, puck.height / 2f, 0f);
        //colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        //puck.bindData(colorProgram);
        //puck.draw();
    }

    private void positionTableInScene(){
        // the table is define in terms of X & Y coordinates
        // , so we rotate it 90 degress to lie flat on the XZ Plane
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }


    private void positionObjectInScene(float x, float y, float z){
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix, 0, x, y , z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }
    private void positionObjectWithRotateInScene(float x, float y, float z, float rotate){
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix, 0, x, y , z);
        rotateM(modelMatrix, 0, rotate, 1f, 0f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }

}
