package com.example.testtransparent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;




import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	private GLSurfaceView mGLView;
	 
	
	float posx;
	float posy;
	
	boolean slam = false;
	
	private class point
	{
		public float x;
		public float y;
		
	}
	
	
	ArrayList<point> smashpoints;
	
	float windowwidth;
	float windowheight;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 mGLView = new MyGLES10SurfaceView(this);
		
		 smashpoints = new ArrayList<point>();
		
	     setContentView(mGLView);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	
	public boolean onOptionsItemSelected(MenuItem item)
	{

		
			
			if (item.getTitle().toString().compareTo("Clear")==0)
			{
			
				smashpoints.clear();
				
				
			}
		
		
		return true;
	}

	
	
	class MyGLES10SurfaceView extends GLSurfaceView {
		
		public MyGLES10SurfaceView(Context context){
		super(context);
		// Set the Renderer for drawing on the GLSurfaceView
		 this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	        // Tell the cube renderer that we want to render a translucent version
	        // of the cube:
		 this.setRenderer(new MyRenderer(context));
	        // Use a surface format with an Alpha channel:
		 this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		 
		
		
		
		}
		
		
		
		  public boolean onTouchEvent(final MotionEvent event) {
		        
			  Integer tempint = new Integer(event.getPointerCount());
			  
			  //Log.i("pointer",new Integer(tempint).toString());
			  
			  int action = event.getAction();
			 
			  //slam = false;
			  
			  if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN)
			  {
				 // slam = false;
			  
			  if (tempint ==2)
				  {
				  	slam = true;
				  	
				  	// add a point
				  	float widthval = windowwidth / 6;
				  	
				  	point temppoint = new point();
				  	
				  	temppoint.x = posx + widthval -  (windowwidth/22);
				  	temppoint.y = windowheight-posy - ( windowwidth/12);
				  	
				  	smashpoints.add(temppoint);
				  	
				  	// play a sound here!
				  	
				  	
				  	
				  } 
			  
			  }
			
			  if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP)
			  {
				  slam = false;
			  }
			  
			  
			  if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE)
			  {
				  posx = event.getX();
				  posy = event.getY();
				  
			  }
		
		        
		      return true;
		  }
		
		
	}
	
	
	
	public class MyRenderer implements GLSurfaceView.Renderer {

		
		float vertices[] = {0,0,0,300,300,0,300,0,0,300,300,300};
		float texcoord[] = {0,0,0,-1,1,0,1,0,0,-1,1,-1};
		
		float smashverts[] = {0,0,0,300,300,0,300,0,0,300,300,300};
		
		float testline[] = {0,0,1000,1000};
		
		
		private FloatBuffer  mVertexBuffer;
		private FloatBuffer  mSmashBuffer;
		private FloatBuffer  mTexCoordBuffer;
		private FloatBuffer  mLine;
		
		int mTextureID;
		int mSmashTexture;
		
		Context mContext;
		
		int[] textures = new int[2];
		
		public MyRenderer(Context contextin)
		{
			mContext = contextin;
			
		}
		
	    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	        // Set the background frame color
	        gl.glClearColor(0,0,0,0);
	  
	        gl.glEnable(GL10.GL_TEXTURE_2D);
	        gl.glEnable(GL10.GL_BLEND);
	        //gl.glDisable(GL10.GL_DEPTH_TEST);
	        //gl.glDisable(GL10.GL_LIGHTING);
	        gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        
	        
	        // set up vertices
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());
	        mVertexBuffer = vbb.asFloatBuffer();
	        mVertexBuffer.put(vertices);
	        mVertexBuffer.position(0);
	        
	        ByteBuffer vbb1 = ByteBuffer.allocateDirect(texcoord.length*4);
	        vbb1.order(ByteOrder.nativeOrder());
	        mTexCoordBuffer = vbb1.asFloatBuffer();
	        mTexCoordBuffer.put(texcoord);
	        mTexCoordBuffer.position(0);
	        
	        
	        ByteBuffer vbb2 = ByteBuffer.allocateDirect(smashverts.length*4);
	        vbb2.order(ByteOrder.nativeOrder());
	        mSmashBuffer = vbb2.asFloatBuffer();
	        mSmashBuffer.put(smashverts);
	        mSmashBuffer.position(0);
	        
	        ByteBuffer vbb3 = ByteBuffer.allocateDirect(testline.length*4);
	        vbb3.order(ByteOrder.nativeOrder());
	        mLine = vbb3.asFloatBuffer();
	        mLine.put(testline);
	        mLine.position(0);
	        
	       
	        gl.glGenTextures(2, textures, 0);

	        mTextureID = textures[0];
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);

	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
	                GL10.GL_NEAREST);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D,
	                GL10.GL_TEXTURE_MAG_FILTER,
	                GL10.GL_LINEAR);
	        
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
	                GL10.GL_REPEAT);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
	                GL10.GL_REPEAT);
	        
	        InputStream is = mContext.getResources().openRawResource(R.raw.hammer);
	        
	        Bitmap bitmap;
	            try {
	                bitmap = BitmapFactory.decodeStream(is);
	            } finally {
	                try {
	                    is.close();
	                } catch(IOException e) {
	                    // Ignore.
	                }
	            }

	         GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	         bitmap.recycle();

	        
	         mSmashTexture= textures[1];
		     gl.glBindTexture(GL10.GL_TEXTURE_2D, mSmashTexture);

		     gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
		                GL10.GL_NEAREST);
		     gl.glTexParameterf(GL10.GL_TEXTURE_2D,
		                GL10.GL_TEXTURE_MAG_FILTER,
		                GL10.GL_LINEAR);
		        
		     gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
		                GL10.GL_REPEAT);
		     gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
		                GL10.GL_REPEAT);
		        
		     is = mContext.getResources().openRawResource(R.raw.crack);
		        
		     
		     try {
		           bitmap = BitmapFactory.decodeStream(is);
		          } finally {
		           try {
		               is.close();
		           } catch(IOException e) {
		               // Ignore.
		           }
		       }

		     GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		     bitmap.recycle();

	         
	         
	         
	        
	    }

	    public void onDrawFrame(GL10 gl) {
	        // Redraw background color
	    	
	    	mVertexBuffer.put(3, windowwidth/6);
	    	mVertexBuffer.put(4, windowwidth/6);
	    	mVertexBuffer.put(6, windowwidth/6);
	    	mVertexBuffer.put(9, windowwidth/6);
	    	mVertexBuffer.put(10, windowwidth/6);
	    	mVertexBuffer.put(11, windowwidth/6);
	    	
	    	mSmashBuffer.put(3, windowwidth/8);
	    	mSmashBuffer.put(4, windowwidth/8);
	    	mSmashBuffer.put(6, windowwidth/8);
	    	mSmashBuffer.put(9, windowwidth/8);
	    	mSmashBuffer.put(10, windowwidth/8);
	    	mSmashBuffer.put(11, windowwidth/8);
	    	
	    	mLine.put(0,windowwidth/2);
	    	mLine.put(1,windowheight/2);
	    	
	    	gl.glEnable(GL10.GL_BLEND);
	    	gl.glEnable(GL10.GL_TEXTURE_2D);
	    	gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);	    	
	    	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    	
	    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	    	
	    	
	    	// smash images
	    	if (smashpoints.size() > 0)
	    	{
	    		
	    		
	    		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    			
	    		
	    		for (int counter = 0; counter < smashpoints.size(); counter++)
	    		{
	    			
	    			
	    			gl.glPushMatrix();
	    			
	    			gl.glTranslatef(smashpoints.get(counter).x, smashpoints.get(counter).y,0.0f);
	    			
	    			gl.glBindTexture(GL10.GL_TEXTURE_2D, mSmashTexture);
	    		    gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mSmashBuffer);
	    		    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
	    		    gl.glDrawArrays(GL10.GL_TRIANGLES, 0,6);
	    			
	    			
	    			gl.glPopMatrix();
	    			
	    		}
	    		
	    		
	    	}
	    	
	    	
	        
	        gl.glPushMatrix();
	        
	        gl.glTranslatef(posx, windowheight-posy, 0);
	        
	        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	        
	        if (slam == true)
	        {
	        	gl.glRotatef(-45f, 0.0f, 0.0f, 1.0f);
	        }
	        
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
	        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertexBuffer);
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
	        gl.glDrawArrays(GL10.GL_TRIANGLES, 0,6);
	        
	        
	        gl.glPopMatrix();
	        
	        /*
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        gl.glDisable(GL10.GL_BLEND);
	        gl.glDisable(GL10.GL_TEXTURE_2D);
	        // draw test line
	        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	        gl.glLineWidth(5.0f);
	        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mLine);
	        gl.glDrawArrays(GL10.GL_LINES, 0, 2);
	        */
	        
	        
	        
	    }

	    public void onSurfaceChanged(GL10 gl, int width, int height) {
	        gl.glViewport(0, 0, width, height);
	        gl.glOrthof(0, width, 0, height, -1.0f, 1.0f);
	        
	        windowwidth = width;
	        windowheight = height;
	        
	        posx = windowwidth/2;
	        posy = windowheight/2;
	        
	        
	    }
	}
	
	
	
	
}
