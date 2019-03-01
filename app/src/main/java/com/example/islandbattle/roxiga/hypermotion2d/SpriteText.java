package com.example.islandbattle.roxiga.hypermotion2d;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

public class SpriteText extends Sprite2D
{
    //2D�X�v���C�g�`��
    public void draw(GL10 gl,int num,float ratio)
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

   		String str =  String.valueOf(num);
   		for ( int i = 0; i < str.length(); i++ )
   		{
   			int j = (int)(num/Math.pow(10, i)) % 10;
   			//�e�N�X�`���̍��W�ƕ��������w��
   			int rect[] = {j*_texWidth, _texY, _texWidth, _texHeight};
   
   			//�o�C���h����Ă���e�N�X�`���摜�̂ǂ̕������g�������w��
   			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
   				GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
   			//2D�`��
   			((GL11Ext) gl).glDrawTexfOES(
   				_pos._x-(i-str.length())*_width*ratio,
   				_pos._y, _pos._z,
				_width*ratio, _height*ratio);
   		}
   	   	gl.glEnable(GL10.GL_DEPTH_TEST);
	}
}
