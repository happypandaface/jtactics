package com.zooth.jt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input.Keys;
import com.zooth.jt.game.*;
import com.zooth.jt.guys.*;
import com.zooth.jt.objs.*;

public class JTGame
{
  public JTField field;
  public List<Guy> objs;
  public List<Guy> currPlayersObjs;
  public SpriteBatch sb;
  public Guy selectedGuy;
  public List<JTPlayer> players;
  public JTPlayer currPlayer = null;
  public List<JTTile> obstacles;
  public List<Boulder> boulders;
  public List<JTTile> inPlay;// the tiles that are in play and drawn
  public boolean startedGame;
  // winner conditions
  // anything above "IN_PROGRESS"
  // is the idx of the winner in player
  public static int IN_PROGRESS = -1;
  public static int NO_WINNER = -2; // this means everyone lost
                                    // can be caused by explosions
  public int winner = IN_PROGRESS;
  public int turnsTaken = 0;
  // the bottom bar with gui options:
  static int NOTHING  = 0;
  static int END_TURN = 1;
  public int botBarAct = NOTHING;
  public float botBarHeight;

  public JTGame()
  {
    field = getField();
    field.setGame(this);
    objs = new ArrayList<Guy>();
  }
  
  
  // for sub classed games
  public JTField getField()
  {
    return new JTField();
  }

  public void create()
  {
    sb = new SpriteBatch();
    field.create();
  }
  
  public void endGame()
  {
    startGame();
  }
  public void startGame()
  {
    // essential variables
    botBarHeight = Gdx.graphics.getHeight()*.15f;
    winner = IN_PROGRESS;
    startedGame = true;
    selectedGuy = null;
    players = new ArrayList<JTPlayer>();
    obstacles = new ArrayList<JTTile>();
    boulders = new ArrayList<Boulder>();
    inPlay = new ArrayList<JTTile>();
    objs = new ArrayList<Guy>();
    // this will make the loop get the first player
    currPlayer = null;
    turnsTaken = 0;
    
    // custom commands
    setupPlayers();
    setupObstacles();
    setupInPlay();
    setupObjs();
  }
  
  public void setupPlayers()
  {
    JTPlayer p = new JTPlayer();
    p.type = JTPlayer.HUMAN;
    addPlayer(p);
    JTPlayer pc = new JTPlayer();
    pc.type = JTPlayer.COMPUTER;
    addPlayer(pc);
  }
  public void setupInPlay()
  {
    int fieldW = 8;
    int xTopOff = 1;
    int xBotOff = 0;
    int fieldH = 10;
    int yTopOff = 0;
    int yBotOff = 0;
    for (int x = 0; x < fieldW; ++x)
      for (int y = 0; y < fieldH; ++y)
        for (int off = 0; off < 2; ++off)
          if (x+(xBotOff==1?off:0) > 0 && x+(xTopOff==1?off:0) < fieldW-1 && y+(yBotOff==1?off:0) > 0 && y+(yTopOff==1?off:0) < fieldH-1)
            inPlay.add(new JTTile(off, x, y));
  }
  // add a hex located at (off, x, y) with a size of (ring)
  // a ring of (0) will give one tile
  // the hex is added to inPlay tiles and will be drawn
  // an moveable
  public void addHex(int off, int x, int y, int ring)
  {
    // make the mid tile
    JTTile midTile = new JTTile(off, x, y);
    // make an array for all the nearby tiles
    List<JTTile> tiles = new ArrayList<JTTile>();
    // add the middle of the hex
    tiles.add(midTile);
    // iterate through the num of rings around the middle
    // wanted and add all to tiles
    for (int i = 1; i <= ring; ++i)
      tiles.addAll(midTile.getAdjTiles(i));
    // now check if each one has already been added
    for (int i = 0; i < tiles.size(); ++i)
    {
      JTTile tile = tiles.get(i);
      boolean alreadyInPlay = false;
      for (int c = 0; c < inPlay.size(); ++c)
      {
        if (inPlay.get(c).check(tiles.get(i)))
        {
          alreadyInPlay = true;
        }
      }
      if (!alreadyInPlay)
        inPlay.add(tile);
    }
  }
  public void setupObstacles()
  {
    
    /* we now use inPlay to make boundaries because it looks nicer with backgrounds
    int fieldW = 8;
    int xTopOff = 1;
    int xBotOff = 0;
    int fieldH = 10;
    int yTopOff = 0;
    int yBotOff = 0;
    for (int x = 0; x < fieldW; ++x)
      for (int y = 0; y < fieldH; ++y)
        for (int off = 0; off < 2; ++off)
          if (x+(xBotOff==1?off:0) == 0 || x+(xTopOff==1?off:0) == fieldW-1 || y+(yBotOff==1?off:0) == 0 || y+(yTopOff==1?off:0) == fieldH-1)
            obstacles.add(new JTTile(off, x, y));
    */
  }
  public void setupObjs()
  {
    {
      Guy g = new BlackMage();
      g.setController(players.get(0));
      g.tile = new JTTile(1, 1, 1);
      addObj(g);
    }
    {
      Guy g = new WhiteMage();
      g.setController(players.get(0));
      g.tile = new JTTile(0, 1, 2);
      addObj(g);
    }
    {
      Guy g = new WhiteMage();
      g.setController(players.get(1));
      g.tile = new JTTile(0, 4, 4);
      addObj(g);
    }
    {
      Guy g = new BlackMage();
      g.setController(players.get(1));
      g.tile = new JTTile(1, 4, 3);
      addObj(g);
    }
  }
  // setup and add a boulder correctly
  public void  addBoulder(Boulder b)
  {
    b.setGame(this);
    boulders.add(b);
  }
  // setup and add a player correctly
  public void  addPlayer(JTPlayer p)
  {
    p.setGame(this);
    players.add(p);
  }
  // setup and add a guyObject correctly
  public Guy addObj(Guy g)
  {
    g.setGame(this);
    g.reset();
    objs.add(g);
    return g;
  }
  
  public List<Guy> getObjs(JTPlayer p)
  {
    List<Guy> guys = new ArrayList<Guy>();
    for (int i = 0; i < objs.size(); ++i)
    {
      Guy obj = objs.get(i);
      if (obj.controller == p)
      {
        guys.add(obj);
      }
    }
    return guys;
  }
  public void switchPlayer(JTPlayer p)
  {
    // tell all the other players that it's not their turn
    // anymore
    if (currPlayer != null)
    {
      for (int i = 0; i < currPlayersObjs.size(); ++i)
      {
        Guy obj = currPlayersObjs.get(i);
        obj.setTurn(false);
      }
    }
    currPlayer = p;
    currPlayersObjs = new ArrayList<Guy>();
    for (int i = 0; i < objs.size(); ++i)
    {
      Guy obj = objs.get(i);
      if (obj.controller == currPlayer)
      {
        obj.newTurn();
        obj.setTurn(true);
        currPlayersObjs.add(obj);
      }
    }
  }
  
  public boolean checkEndTurn()
  {
    int left = 0;
    boolean inTransit = false;
    for (int i = 0; i < currPlayersObjs.size(); ++i)
    {
      Guy obj = currPlayersObjs.get(i);
      if (obj.inTransit)
        inTransit = true;
      if (!obj.okayForTurnEnd())
        left++;
    }
    // consume the bottom bar action if it's to end the turn
    int botBarActTemp = botBarAct;
    if (botBarAct == END_TURN)
      botBarAct = NOTHING;
    return (left == 0) || (!inTransit && currPlayer.type == JTPlayer.HUMAN && (Gdx.input.isKeyJustPressed(Keys.SPACE) || botBarActTemp == END_TURN));
  }

  // returns the first boulder at the tile
  public Boulder boulderAt(JTTile t)
  {
    for (int i = 0; i < boulders.size(); ++i)
    {
      Boulder b = boulders.get(i);
      if (b.checkAt(t))
        return b;
    }
    return null; 
  }
  // returns the first guy at the tile
  // who isn't dead
  public Guy guyAt(JTTile t)
  {
    for (int i = 0; i < objs.size(); ++i)
    {
      Guy obj = objs.get(i);
      if (!obj.isDead() && obj.checkAt(t))
        return obj;
    }
    return null; 
  }
  // sees if that tile has obstacles on it
  public boolean checkIsMovable(JTTile t)
  {
    for (int i = 0; i < obstacles.size(); ++i)
    {
      if (obstacles.get(i).check(t))
        return false;
    }
    for (int i = 0; i < inPlay.size(); ++i)
    {
      if (inPlay.get(i).check(t))
        return true;
    }
    return false;
  }
  // sees if that tile is pathable
  // which is just the above function
  // plus checking for boulders
  // (they take 2 ap to move)
  public boolean checkIsPathable(JTTile t)
  {
    for (int i = 0; i < boulders.size(); ++i)
    {
      if (boulders.get(i).checkAt(t))
        return false;
    }
    for (int i = 0; i < obstacles.size(); ++i)
    {
      if (obstacles.get(i).check(t))
        return false;
    }
    for (int i = 0; i < inPlay.size(); ++i)
    {
      if (inPlay.get(i).check(t))
        return true;
    }
    return false;
  }

  // this function makes an object try to 'target' a tile
  // this could mean healing the unit there, or 
  // attacking an enemy at the tile, or simple
  // moving to the tile
  public boolean target(Guy guy, JTTile t)
  {// TODO: return whether the target was legal
    boolean stillAMove = true;
    for (int i = 0; i < objs.size(); ++i)
    {
      Guy obj = objs.get(i);
      if (obj.tile.check(t))
      {
        if (!obj.isDead())
        {
          stillAMove = false;
          if (obj.controller != guy.controller)
          {
            // try an attack
            guy.attack(obj);
          }else
          {
            // it's a friendly, try a heal
            guy.heal(obj);
          }
          break;
        }
      }
    }
    if (stillAMove)
      guy.moveTo(t);
    return true;
  }
  // guys will add projectiles during their step (each frame)
  // they draw over everything
  List<Fireball> projectiles;
  public void addProj(Fireball fb)
  {
    fb.setGame(this);
    projectiles.add(fb);
  }
  public void render()
  {
    Gdx.gl.glClearColor(1, 1, 1, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    sb.begin();
    render(sb);
    sb.end();
  }
  public boolean checkPause()
  {
    return false;
  }
  public int checkGUIClick(Vector2 v)
  {
    float width = botBarHeight;
    float height = botBarHeight;
    if (v.x > Gdx.graphics.getWidth()-width &&
        v.y < height)
    {
      // do logic
      return END_TURN;
    }
    return NOTHING;
  }
  public String getEndMessage()
  {
    String endStr = "A Glitch Occurred!";//default string
    // this was the old text:
    // endStr = "Player "+(winner+1)+" wins!";
    // it makes more sense if you know what player you
    // are (which you don't in campaign, which is the
    // only mode right now)

    if (winner == JTGame.NO_WINNER)
    {
      endStr = "Everyone loses!";
    }else
    if (winner == 0)
    {
      endStr = "You win!"; 
    }else
    {
      endStr = "You lose!"; 
    }
    return endStr;
  }
  public void render(SpriteBatch sb)
  {
    field.render(sb, null);
    // check state (for extending)
    boolean paused = checkPause();
    boolean gameOver = false;
    // get the players left
    List<JTPlayer> playersLeft = new ArrayList<JTPlayer>();
    for (int i = 0; i < players.size(); ++i)
    {
      JTPlayer p = players.get(i);
      int left = 0;
      for (int c = 0; c < objs.size(); ++c)
      {
        Guy obj = objs.get(c);
        if (obj.controller == p && !obj.isDead())
        {
          ++left;
          break;// we just care if one is still alive
        }
      }
      if (left != 0)
      {
        playersLeft.add(p);
      }
    }
    if (!paused)
    {
      // check if the game was won:
      // this should check teams eventually
      if (playersLeft.size() < 2)// not enough players to continue
        gameOver = true;
    }
    if (!gameOver && !paused)
    {
      float dt = Gdx.graphics.getDeltaTime();
      // if we haven't gotten a player (start of game)
      if (currPlayer == null)
      {
        switchPlayer(players.get(0));
      }else
      {
        // check to see if it should be someone else's turn
        boolean shouldEndTurn = checkEndTurn();
        if (shouldEndTurn || Gdx.input.isKeyJustPressed(Keys.D))
        {
          // this guy's turn is over, switch players
          int idx = players.indexOf(currPlayer);
          switchPlayer(players.get((idx+1)%players.size()));
          // increment turns taken for events, tutorials and cutscenes
          ++turnsTaken;
        }
      }
      if (Gdx.input.justTouched())
      {
        if (field.selectedTile != null)
        {
          boolean usedMove = false;
          if (Gdx.input.isButtonPressed(1) && selectedGuy != null)
          {
            if (currPlayer.type == JTPlayer.HUMAN)
            {// only issue commands for human player
              // check if this is an attack (or legal):
              boolean stillAMove = true;
              for (int i = 0; i < objs.size(); ++i)
              {
                Guy obj = objs.get(i);
                if (!obj.isDead())
                {
                  if (obj.tile.check(field.selectedTile))
                  {
                    stillAMove = false;
                    if (obj.controller != selectedGuy.controller)
                    {
                      // try an attack
                      usedMove = selectedGuy.attack(obj);
                    }else
                    {
                      // it's a friendly, try a heal
                      usedMove = selectedGuy.heal(obj);
                    }
                    break;
                  }
                }
              }
              // if it wasn't a heal or an attack, it may be a move:
              if (stillAMove)
                usedMove = selectedGuy.moveTo(field.selectedTile);
            }
          }else
          if (Gdx.input.isButtonPressed(0))// && !usedMove)
          {
            // go through possible guis to click,
            // if none were clicked try to select a character
            boolean guiClicked = false;
            // check if the normal GUI was clicked:
            botBarAct = checkGUIClick(new Vector2(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY()));
            if (botBarAct != 0)
              guiClicked = true;
            // check if the selectedGuy's GUI was clicked
            if (!guiClicked)
            {
              if (selectedGuy != null)
                guiClicked = selectedGuy.checkGUIClick(new Vector2(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY()));
            }
            if (!guiClicked)
            {
              // alert the last selected guy
              if (selectedGuy != null)
                selectedGuy.setSelected(false);
              // find the next one (default: null)
              selectedGuy = null;
              for (int i = 0; i < objs.size(); ++i)
              {
                Guy obj = objs.get(i);
                if (!obj.isDead() && obj.checkSelect(field.selectedTile))
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
      }
      if (currPlayer.type == JTPlayer.COMPUTER)
      {// it's a computer, do AI
        currPlayer.doAi(objs);
        
      }
      for (int i = 0; i < objs.size(); ++i)
      {
        objs.get(i).step(dt); 
      }
    }
    // now for drawing!!!
    // draw obstables:
    for (int i = 0; i < obstacles.size(); ++i)
    {
      JTTile t = obstacles.get(i);
      Vector2 pos = field.getPos(t);
      sb.setColor(.5f, .2f, .3f, 1);
      sb.draw(JTactics.assets.hex, pos.x, pos.y, field.width, field.height);
    }
    // only not null if a guy is selected
    List<JTTile> actionTiles = null;
    if (selectedGuy != null)
    {
      actionTiles = selectedGuy.getAdjTiles(1);
      sb.setColor(0, 0, 1f, .4f);
      if (actionTiles != null)
        for (int i = 0; i < actionTiles.size(); ++i)
        {
          JTTile tile = actionTiles.get(i);
          if (checkIsMovable(tile))
          {
            Vector2 pos = field.getPos(tile);
            sb.draw(JTactics.assets.hex, pos.x, pos.y, field.width, field.height);
          }
        }
      actionTiles = selectedGuy.getAdjTiles(2);
      sb.setColor(0, 1f, 0f, .4f);
      if (actionTiles != null)
        for (int i = 0; i < actionTiles.size(); ++i)
        {
          JTTile tile = actionTiles.get(i);
          if (checkIsMovable(tile))
          {
            Vector2 pos = field.getPos(tile);
            sb.draw(JTactics.assets.hex, pos.x, pos.y, field.width, field.height);
          }
        }

    }
    // reset projectiles array so objs can add them
    projectiles = new ArrayList<Fireball>();
    // now for drawing objects:
    // order for draw (lower Y objs in front)
    List<Guy> objsCpy = new ArrayList<Guy>(objs);
    float highestY = 0;
    int drawIdx = -1;
    while(objsCpy.size() > 0)
    {
      highestY = 0;
      drawIdx = -1;
      for (int i = 0; i < objsCpy.size(); ++i)
      {
        Guy obj = objsCpy.get(i);
        // minus .5 if they're dead
        float currY = obj.tile.y*2+obj.tile.off+(obj.isDead()?.5f:0);
        if (currY > highestY || drawIdx == -1)
        {
          drawIdx = i;
          highestY = currY;
        }
      }
      Guy closestObj = objsCpy.remove(drawIdx);
      closestObj.draw(sb, null);
    }
    // draw boulders
    for (int i = 0; i < boulders.size(); ++i)
    {
      Boulder b = boulders.get(i);
      b.draw(sb);
    }
    // done calling the draw funct on objects
    // (which adds projectiles)
    // this means all the projectiles have been added
    // just draw them, same order as added
    for (int i = 0; i < projectiles.size(); ++i)
    {
      Fireball fb = projectiles.get(i);
      fb.draw(sb);
    }
    // now draw the selected guy's GUI
    if (selectedGuy != null)
      selectedGuy.drawGUI(sb, null);
    /* old draw funct
    for (int i = 0; i < objs.size(); ++i)
    {
      objs.get(i).draw(sb, null); 
    }*/
    if (playersLeft.size() < 2)
    {
      // tell who won:
      String endStr = "";
      if (playersLeft.size() == 0)
      {
        // no one won:
        winner = -2;
        endStr = getEndMessage();
      }else
      {
        // figure out who won
        int playerNum = -1;
        for (int i = 0; i < players.size(); ++i)
        {
          if (playersLeft.get(0) == players.get(i))
          {
            playerNum = i;
          }
        }
        winner = playerNum;
        endStr = getEndMessage();
      }
      BitmapFont.TextBounds tb = JTactics.assets.font.getWrappedBounds(endStr, Gdx.graphics.getWidth());
      float midX = Gdx.graphics.getWidth()/2f;
      float midY = Gdx.graphics.getHeight()/2f;
      float pad = Gdx.graphics.getWidth()*.29f;
      sb.setColor(.4f, .5f, .9f, .9f);
      sb.draw(JTactics.assets.glow, midX-tb.width/2f-pad/2f, midY-tb.height/2f-pad/2f, tb.width+pad, tb.height+pad);
      JTactics.assets.font.setColor(1f, 1f, 1, 1);
      JTactics.assets.font.drawWrapped(sb, endStr, midX-tb.width/2f, midY+tb.height/2f, Gdx.graphics.getWidth());
      if (Gdx.input.justTouched())
      {
        endGame();
      }
    }else
    if (currPlayer == players.get(0))
    {
      // draw the end turn button
      float width = botBarHeight;
      float height = botBarHeight;
      sb.setColor(1, 1, 1, 1);
      sb.draw(JTactics.assets.endTurn, Gdx.graphics.getWidth()-width, 0, width, height);
    }
  }
}
