package com.zooth.jt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JTGame
{
  JTField field;

  public JTGame()
  {
    field = new JTField();
  }

  public void create()
  {
    field.create();
  }

  public void render()
  {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    field.render(null);
    float dt = Gdx.graphics.getDeltaTime();
    for (int i = 0; i < objs.size(); ++i)
    {
      objs.step(dt); 
    }
    for (int i = 0; i < objs.size(); ++i)
    {
      objs.draw(null); 
    }
  }
}
