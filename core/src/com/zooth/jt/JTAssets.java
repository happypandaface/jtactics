package com.zooth.jt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.graphics.g2d.*;

public class JTAssets
{
  public Texture hex;
  public Texture fullHex;
  public Texture guy;
  public Texture glow;
  public Texture white;
  public Texture fireball;
  public Texture mage;
  public Texture whiteMage;
  public Texture blueMage;
  public Texture openingBG;
  public Texture darkForest;
  public Texture frog;
  public Texture frogMage;
  public Texture chatBox;
  public Texture mouseRightClick;
  public Texture mouseLeftClick;
  public Texture mouseNoClick;
  public Texture mouseBothClick;
  public Texture temple;
  public Texture headPriest;
  public Texture pinkRoom;
  public Texture forestRoad;
  public Texture thief;
  public Texture swampBG;
  public Texture swampTemple;
  public Texture swampFight;
  public Texture frogKnight;
  public Texture shield;
  public Texture shieldR;
  public Texture shieldL;
  public Texture forestCave;
  public Texture scaryEyes;
  public Texture caveFloor;
  public Texture boulder;
  public Texture endTurn;
  public BitmapFont font;

  public void create()
  {
    hex = new Texture("hexTileOutline.png");
    fullHex = new Texture("hexTile.png");
    guy = new Texture("guy.png");
    glow = new Texture("glow.png");
    mage = new Texture("mage.png");
    fireball = new Texture("fireball.png");
    whiteMage = new Texture("whiteMage.png");
    blueMage = new Texture("blueMage.png");
    openingBG = new Texture("openingBG.png");
    darkForest = new Texture("darkForest.png");
    headPriest = new Texture("headPriest.png");
    frog = new Texture("frog.png");
    frogMage = new Texture("frogMage.png");
    chatBox = new Texture("chatBox.png");
    mouseRightClick = new Texture("mouseRightClick.png");
    mouseLeftClick = new Texture("mouseLeftClick.png");
    mouseNoClick = new Texture("mouseNoClick.png");
    pinkRoom = new Texture("buttonDownSmall.png");
    forestRoad = new Texture("forestRoad.png");
    thief = new Texture("pharaoh.png");
    temple = new Texture("temple.png");
    swampBG = new Texture("swampy.jpeg");
    swampFight = new Texture("swampBattle.png");
    frogKnight = new Texture("frogKnight.png");
    shield = new Texture("shield.png");
    shieldR = new Texture("shieldRight.png");
    shieldL = new Texture("shieldLeft.png");
    forestCave = new Texture("forestCave.png");
    swampTemple= new Texture("swampTemple.jpg");
    scaryEyes= new Texture("scaryEyes.png");
    caveFloor= new Texture("caveFloor.png");
    boulder= new Texture("boulder.png");
    endTurn= new Texture("endTurn.png");

    {
      Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
      p.setColor(1, 1, 1, 1);
      p.fill();
      white = new Texture(p);
    }
    {
      FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Chunkfive Ex.ttf"));
      font = generator.generateFont((int)(Gdx.graphics.getWidth()*.05f));
    }
  }
}
