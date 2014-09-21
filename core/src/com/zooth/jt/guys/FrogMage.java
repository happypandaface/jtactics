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

public class FrogMage extends BlackMage
{
  public FrogMage()
  {
    super();
    maxHp = 2;
    removeAction(Action.DEFEND);
  }
  @Override
  public float characterHeight()
  {
    return 1.2f;
  }
  @Override
  public Texture getTexture()
  {
    return JTactics.assets.frogMage;
  }
}
