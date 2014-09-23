package com.zooth.jt.game;

import com.zooth.jt.*;
import com.zooth.jt.guys.*;

public class OpeningFight2 extends OpeningFight
{
  @Override
  public void setupObjs()
  {
    super.setupObjs();
    //objs.remove(objs.size()-1);
    {
      Guy g = new FrogMage();
      g.setController(players.get(1));
      g.tile = new JTTile(0, 3, 5);
      addObj(g);
    }
  }
}
