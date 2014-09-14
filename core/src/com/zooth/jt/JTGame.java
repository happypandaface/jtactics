package com.zooth.jt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.*;

public class JTGame
{
  JTField field;
  List<Guy> objs;
  SpriteBatch sb;
  Guy selectedGuy;

  public JTGame()
  {
    field = new JTField();
    objs = new ArrayList<Guy>();
  }

  public void create()
  {
    sb = new SpriteBatch();
    field.create();
  }

  public void startGame()
  {
    objs = new ArrayList<Guy>();
    addObj(new Guy());
    Guy g = new Guy();
    g.setGame(this);
    objs.add(g);
    
  }
  public void addObj(Guy g)
  {
    g.setGame(this);
    objs.add(g);
  }

  public void render()
  {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    sb.begin();
    field.render(sb, null);
    float dt = Gdx.graphics.getDeltaTime();
    if (Gdx.input.justTouched())
    {
      if (field.selectedTile != null)
      {
        if (Gdx.input.isButtonPressed(1) && selectedGuy != null)
          selectedGuy.moveTo(field.selectedTile);
        else
        if (Gdx.input.isButtonPressed(0))
        {
          // alert the last selected guy
          if (selectedGuy != null)
            selectedGuy.setSelected(false);
          // find the next one (default: null)
          selectedGuy = null;
          for (int i = 0; i < objs.size(); ++i)
          {
            Guy obj = objs.get(i);
            if (obj.checkSelect(field.selectedTile))
            {
              selectedGuy = obj;
              // tell the new guy he was selected
              selectedGuy.setSelected(true);
              break;
            }
          }
        }
      }
    }
    for (int i = 0; i < objs.size(); ++i)
    {
      objs.get(i).step(dt); 
    }
    for (int i = 0; i < objs.size(); ++i)
    {
      objs.get(i).draw(sb, null); 
    }

    sb.end();
  }
}
