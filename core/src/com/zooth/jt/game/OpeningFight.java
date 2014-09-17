package com.zooth.jt.game;

import com.zooth.jt.*;

public class OpeningFight extends JTGame
{
  OpeningCutscene os;
  public void setController(OpeningCutscene os)
  {
    this.os = os;
  }
  @Override
  public void setupObjs()
  {
    {
      Guy g = new BlackMage();
      g.setController(players.get(0));
      g.tile = new JTTile(1, 1, 5);
      addObj(g);
    }
    {
      Guy g = new WhiteMage();
      g.setController(players.get(0));
      g.tile = new JTTile(1, 1, 4);
      addObj(g);
    }
    {
      Guy g = new WhiteMage();
      g.setController(players.get(1));
      g.tile = new JTTile(0, 4, 6);
      addObj(g);
    }
    {
      Guy g = new BlackMage();
      g.setController(players.get(1));
      g.tile = new JTTile(0, 4, 5);
      addObj(g);
    }
  }
  @Override
  public void endGame()
  {
    // if we're the winner, end the fight, otherwise
    // start over
    if (winner == 0)
      os.endedFight();
    else
    {
      os.lostFight();
    }
  }
}