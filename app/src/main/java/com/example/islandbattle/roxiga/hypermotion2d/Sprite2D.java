package com.example.islandbattle.roxiga.hypermotion2d;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Sprite2D
{
    //
    public boolean _visible = true;
	//�e�N�X�`��ID�ԍ�
    public int _textureNo;
    //�z�u����ʒu
    public Vector3D _pos = new Vector3D(0,0,0);
    //�z�u����X�g���b�`��
    public float _width;
    //�z�u����X�g���b�`����
    public float _height;
    //�e�N�X�`�����X�ʒu
    public int _texX;
    //�e�N�X�`�����Y�ʒu(0���ŏ��łȂ��摜�̍�������v��)
    public int _texY;
    //�e�N�X�`����̕�
    public int _texWidth;
    //�e�N�X�`����̍���
    public int _texHeight;

    //�e�N�X�`����ǂݍ���ŃZ�b�g
    public void setTexture(GL10 gl,Resources res,int id)
    {
    	//�@�e�N�X�`����res����ǂݏo��
    	InputStream is = res.openRawResource(id);

    	Bitmap bitmap;
    	try
    	{
    		bitmap = BitmapFactory.decodeStream(is);
    	}
    	finally
    	{
    		try
    		{
    			is.close();
    		}
    		catch(IOException e)
    		{
    		}
    	}
    	gl.glEnable(GL10.GL_ALPHA_TEST);
    	gl.glEnable(GL10.GL_BLEND);
    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
    	//�e�N�X�`��ID�����蓖�Ă�
    	int[] textureNo = new int[1];
    	gl.glGenTextures(1, textureNo, 0);
    	_textureNo = textureNo[0];
    	//�e�N�X�`��ID�̃o�C���h
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);	
     	//OpenGL ES�p�̃������̈�ɉ摜�f�[�^��n���B��Ńo�C���h���ꂽ�e�N�X�`��ID�ƌ��ѕt������B
    	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
    	//�e�N�X�`�����W��1.0f�𒴂����Ƃ��́A�e�N�X�`����S�������ɌJ��Ԃ��ݒ�
    	gl.glTexParameterx(GL10.GL_TEXTURE_2D,
    			GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );
    	//�e�N�X�`�����W��1.0f�𒴂����Ƃ��́A�e�N�X�`����T�������ɌJ��Ԃ��ݒ�
    	gl.glTexParameterx(GL10.GL_TEXTURE_2D,
    			GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );
    	//�e�N�X�`�������̃T�C�Y����g��A�k�����Ďg�p�����Ƃ��̐F�̎g������ݒ�
    	gl.glTexParameterx(GL10.GL_TEXTURE_2D,
    			GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
    	//�e�N�X�`�������̃T�C�Y����g��A�k�����Ďg�p�����Ƃ��̐F�̎g������ݒ�
    	gl.glTexParameterx(GL10.GL_TEXTURE_2D,
    			GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    	
    	setPos(
    		0,bitmap.getHeight(),
    		bitmap.getWidth(),-bitmap.getHeight(),
    		0,0,0,
    		bitmap.getWidth(),bitmap.getHeight());
    }

    //�e�N�X�`���ʒu�Z�b�g
    public void setPos(int texX,int texY,int texW,int texH,
    		float x,float y,float z,float w,float h)
    {
    	_texX = texX;
    	_texY = texY;
    	_texWidth = texW;
       	_texHeight = texH;
     	_pos = new Vector3D(x,y,z);
     	_width = w;
     	_height = h;
    }

    public void draw(GL10 gl)
    {
    	if ( !_visible )
    	{
    		return;
    	}
    	gl.glDisable(GL10.GL_DEPTH_TEST);
    	//���F�Z�b�g
   		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
   		//�e�N�X�`��0�Ԃ��A�N�e�B�u�ɂ���
   		gl.glActiveTexture(GL10.GL_TEXTURE0);
   		//�e�N�X�`��ID(_textureNo)�ɑΉ�����e�N�X�`�����o�C���h����
   		gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);

		//�e�N�X�`���̍��W�ƕ��������w��
   		int rect[] = {_texX, _texY, _texWidth, _texHeight};
   
   		//�o�C���h����Ă���e�N�X�`���摜�̂ǂ̕������g�������w��
   		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
   			GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
   		//����2D�`��
   		((GL11Ext) gl).glDrawTexfOES(
   				_pos._x, _pos._y, _pos._z, _width, _height);
   		//
   	   	gl.glEnable(GL10.GL_DEPTH_TEST);
	}

    //2D�X�v���C�g�`��
    public void draw(GL10 gl,float ratio)
    {
    	if ( !_visible )
    	{
    		return;
    	}
    	gl.glDisable(GL10.GL_DEPTH_TEST);

   		//���F�Z�b�g
   		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
   		//�e�N�X�`��0�Ԃ��A�N�e�B�u�ɂ���
   		gl.glActiveTexture(GL10.GL_TEXTURE0);
   		//�e�N�X�`��ID(_textureNo)�ɑΉ�����e�N�X�`�����o�C���h����
   		gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);

		//�e�N�X�`���̍��W�ƕ��������w��
   		int rect[] = {_texX, _texY, _texWidth, _texHeight};
   
   		//�o�C���h����Ă���e�N�X�`���摜�̂ǂ̕������g�������w��
   		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
   			GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
   		//����2D�`��
   		((GL11Ext) gl).glDrawTexfOES(
   				_pos._x, _pos._y, _pos._z,
   				_width*ratio, _height*ratio);
   		//
   	   	gl.glEnable(GL10.GL_DEPTH_TEST);
	}
    
    public boolean hit(float x,float y)
    {
    	if ( x >= _pos._x && x <= _pos._x + _width )
    	{
        	if ( y >= _pos._y && y <= _pos._y + _height )
        	{
        		return true;
        	}
        }
    	return false;
    }
}
