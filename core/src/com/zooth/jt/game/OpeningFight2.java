package com.zooth.jt.game;

import com.zooth.jt.*;

public class OpeningFight2 extends OpeningFight
{
  @Override
  public void setupObjs()
  {
    super.setupObjs();
    {
      Guy g = new BlackMage();
      g.setController(players.get(1));
      g.tile = new JTTile(0, 4, 4);
      addObj(g);
    }
  }
}