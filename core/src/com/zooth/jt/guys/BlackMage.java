package com.zooth.jt.guys;

import com.zooth.jt.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;

public class BlackMage extends Guy
{
  public BlackMage()
  {
    super();
    possibleActions.add(Action.FIREBALL);
    //possibleActions.add(Action.SPIRIT_BURST);
    //possibleActions.add(Action.DEFEND);
  }
  @Override
  public Texture getTexture()
  {
    return JTactics.assets.mage;
  }
  @Override
  public boolean canRange()
  {
    return true;
  }
}
