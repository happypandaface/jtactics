package com.zooth.jt;

import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;

public class JTField
{
  ShapeRenderer sRenderer;
  float hexSizeRatio = 400f/640f;

  float gap = 9;
  float width = 60;
  float height = width*hexSizeRatio;
  float heightGap = height+gap;
  float xGap = (width/640)*200;
  float widthGap = width+xGap+gap/2;
  float yOffset = heightGap/2;
  float xOffset = width-xGap;

  float closestDist = 0;
  JTTile closestTile = null;
  JTTile selectedTile = null;

  public JTField()
  {
  }

  public void setRatio(float r)
  {
    hexSizeRatio = r;
  }

  public void create()
  {
    //sRenderer = new ShapeRenderer();
  }
  
  // for overriding
  public void drawBackground(SpriteBatch sb, Camera cam)
  {
    
  }

  public void render(SpriteBatch sb, Camera cam)
  {
    //sRenderer.begin(ShapeType.Line);
    //sRenderer.setColor(0, 0, 0, 1);
    //sRenderer.circle(50, 50, 50, 50);
    //sRenderer.end();
        //Gdx.app.log("gap", ""+width+", "+height+", "+width);
    drawBackground(sb, cam);
    closestTile = null;
    closestDist = 0;
    for (int x = -2; x < 60; ++x)
      for (int y = -2; y < 60; ++y)
        checkPos(0, x, y);
    for (int x = -2; x < 60; ++x)
      for (int y = -2; y < 60; ++y)
        checkPos(1, x, y);
    if (Gdx.input.isTouched())
      selectedTile = closestTile;
    for (int x = -2; x < 60; ++x)
      for (int y = -2; y < 60; ++y)
        drawHex(sb, 0, x, y);
    for (int x = -2; x < 60; ++x)
      for (int y = -2; y < 60; ++y)
        drawHex(sb, 1, x, y);
  }

  public Vector2 getMousePos()
  {
    return new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
  }

  public void checkPos(int off, int x, int y)
  {
    Vector2 mousePos = getMousePos();
    Vector2 tilePos = new Vector2((widthGap)*x+(off==1?xOffset:0)+width/2, heightGap*y+(off==1?yOffset:0)+height/2);
    float currDist = mousePos.cpy().sub(tilePos).len();
    if (currDist < closestDist || closestTile == null)
    {
      closestTile = new JTTile(off, x, y);
      closestDist = currDist;
    }
  }
  public void drawHex(SpriteBatch sb, int off, int x, int y)
  {
    if (selectedTile != null && selectedTile.check(off, x, y))
      sb.setColor(1, .2f, .2f, 1);
    else if (closestTile != null && closestTile.check(off, x, y))
      sb.setColor(1, 0, 0, 1);
    else
      sb.setColor(0, 0, 0, 1);

    sb.draw(JTactics.assets.hex, (widthGap)*x+(off==1?xOffset:0), heightGap*y+(off==1?yOffset:0), width, height);
  }
  public Vector2 getPos(JTTile t)
  {
    return new Vector2((widthGap)*t.x+(t.off==1?xOffset:0), heightGap*t.y+(t.off==1?yOffset:0));
    
  }
}
