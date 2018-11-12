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
import android.util.Log;
import vector3.mx.skinnerapp.Geometry.*;


public class OpenGLRendererPrincipal implements Renderer {

    private final Context context;
    private static final String TAG = "MainRenderer";

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] invertedViewProjectionMatrix = new float[16];

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table tabla;
    private Mallet redMallet;
    private Puck puck;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;

    private boolean malletPressed = false;
    private Point redMalletPosition;


    //Constructor
    public OpenGLRendererPrincipal(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 0f);

        puck = new Puck(0.035f, 0.015f, 30);
        //Point pos = new Point(0f, 0f, 0.2f);
        redMallet = new Mallet(0.060f, 0.090f, 30);
        //redMallet = new Mallet(0.060f, 0.090f, 30, pos);
        //redMalletPosition = new Point(0f, redMallet.height / 2f, 0f);
        redMalletPosition = new Point(0f, redMallet.height /2f , 0.4f);

        tabla = new Table();
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
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GL_COLOR_BUFFER_BIT);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix,0);

        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectionMatrix, texture);
        tabla.bindData(textureProgram);
        tabla.draw();

        positionObjectInScene(0.0f, 0.0f, 0.0f );
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix);
        puck.bindData(colorProgram);
        puck.draw();

        //positionObjectInScene(0.0f, -0.5f, 0.0f );
        positionObjectInScene(redMalletPosition.x, redMalletPosition.y, redMalletPosition.z);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix);
        redMallet.bindData(colorProgram);
        redMallet.draw();
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

    public void handleTouchPress(float normalizedX, float normalizedY){
        Log.i(TAG, " TouchPress -> movimiento en x = " + normalizedX + ", y = " + normalizedY);
        Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
        // Now test if this ray intersects with the mallet by creating
        //   a bounding sphere that wraps the mallet.
        Sphere malletBoundingSphere = new Sphere(new Point(
                redMalletPosition.x,
                redMalletPosition.y,
                redMalletPosition.z),
                redMallet.height / 2f);
        //if the ray intersects  ( if the user  touched  a part  of the screen that
        //intersects the mallets  bounding sphere ) ,then set mallet pressed  = true
        malletPressed = Geometry.intersects(malletBoundingSphere, ray);
    }

    private Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY){
        //we'll convert these normalized device coordinates into world space
        // coordinates. well pick a point on the near far planes , and draw
        // a line between them. To do this transform , we need to first multiply
        // by the inverse matrix , and then we need to undo the perspective divide.
        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc = {normalizedX, normalizedY, 1, 1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(
                nearPointWorld, 0, invertedViewProjectionMatrix, 0 , nearPointNdc, 0);
        multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);

        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Point nearPointRay =
                new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Point farPointRay =
                new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

        return new Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));
    }
    private void divideByW(float[] vector){
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

    public void handleTouchDrag(float normalizedX, float normalizedY){
        Log.i(TAG, "movimiento  x = " + normalizedX + " , y = " + normalizedY);
        if(malletPressed){
            Log.i(TAG, "mallet presionado :");
            Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
            //Define a plane representing our air hockey table.
            Plane plane = new Plane(new Point(0, 0, 0), new Vector(0, 1, 0));
            /*
            find out where the touched point intersects the plane
            representing our table. well move the mallet along this plane.
             */
            Point touchedPoint = Geometry.intersectionPoint(ray, plane);
            redMalletPosition = new Point(touchedPoint.x, redMallet.height / 2f, touchedPoint.z);
        }
    }
}
