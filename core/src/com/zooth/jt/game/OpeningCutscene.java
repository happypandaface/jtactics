package com.zooth.jt.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input.Keys;
import com.zooth.jt.*;
import com.zooth.jt.cinematics.*;
import com.zooth.jt.guys.*;
import com.badlogic.gdx.graphics.*;

public class OpeningCutscene implements ExtendableFight.FightListener
{
  SpriteBatch sb;
  public float time = 0;
  float fadeInOutTime = .6f;
  public List<Object> story;
  int storyIdx;
  static int NOTHING = 0;
  static int PLAYING_SCENE = 1;
  static int ENDING_SCENE = 2;
  static int STARTING_SCENE = 3;
  static int WAITING_SCENE = 4;
  int sceneState = STARTING_SCENE;
  static int GO_FORWARD = 1;
  static int GO_BACKWARD = 2;
  int sceneStateAction = NOTHING;
  /*
  JTGame currGame;
  OpeningFight game;
  OpeningFight2 game2;
  public Track lastTrack = null;
  public Track currTrack = null;
  public Track introTrack;
  public Track afterFightTrack;
  public Track afterFight2Track;
  public Track finalTrack;*/
  
  public OpeningCutscene()
  {
  }
  
  public void create()
  {
    sb = new SpriteBatch();
    story = new ArrayList<Object>();
    Track introTrack = new Track();
    introTrack.bg = JTactics.assets.openingBG;
    Texture oldGuy = JTactics.assets.mage;
    Texture youngGirl = JTactics.assets.whiteMage;
    Texture headPriest = JTactics.assets.headPriest;
    Texture thief = JTactics.assets.thief;
    Texture frogMage = JTactics.assets.frogMage;
    introTrack.events.add(new TrackEvent(oldGuy, "Boy, it sure is peaceful around here in my quiet forest home!"));
    introTrack.events.add(new TrackEvent(oldGuy, "It sure was hard using my wizard powers to save the world last year."));
    introTrack.events.add(new TrackEvent(oldGuy, "But now I'm going to enjoy some well-earned rest!"));
    introTrack.events.add(new TrackEvent(youngGirl, "Sir! Please let me in!"));
    introTrack.events.add(new TrackEvent(oldGuy, "Ugh... No soliciting!"));
    introTrack.events.add(new TrackEvent(youngGirl, "Please sir I'm being attacked!"));
    introTrack.events.add(new TrackEvent(oldGuy, "What? Attacked? But this is a peaceful forest."));
    introTrack.events.add(new TrackEvent(oldGuy, "I was told that I wouldn't have to defend my home if I bought this house."));
    introTrack.events.add(new TrackEvent(youngGirl, "I'm sorry you got scammed, but you have to save me! There's frogs!"));
    introTrack.events.add(new TrackEvent(oldGuy, "Frogs? Are you serious? Hahahaha!"));
    introTrack.events.add(new TrackEvent(youngGirl, "Warrior frogs, sir. I'm just a priestess from the temple nearby."));
    introTrack.events.add(new TrackEvent(youngGirl, "I wasn't trained to fight... please hurry!"));
    introTrack.events.add(new TrackEvent(oldGuy, "..."));
    introTrack.events.add(new TrackEvent(oldGuy, "Fine I'll open the door!"));
    introTrack.events.add(new TrackEvent(youngGirl, "They're right here!"));
    story.add(introTrack);
    OpeningFight game = new OpeningFight();
    game.create();
    game.setController(this);
    story.add(game);
    //currTrack = introTrack;
    Track afterFightTrack = new Track();
    afterFightTrack.bg = JTactics.assets.openingBG;
    afterFightTrack.events.add(new TrackEvent(youngGirl, "Thank you so much!"));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "Bah, don't mention it. Now go away."));
    afterFightTrack.events.add(new TrackEvent(youngGirl, "Sir, please. I've come from the nearby temple... We need help!"));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "Listen, I know you've probably heard of my vast skills..."));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "But I'm too old to save the world again."));
    afterFightTrack.events.add(new TrackEvent(youngGirl, "...sorry, I don't know who you are, but you seem to have skill enough to help."));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "WHAT!?"));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "You really have never heard of me?"));
    afterFightTrack.events.add(new TrackEvent(youngGirl, "I'm afraid not, sir."));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "I defeated the dragon-lord Balthazar and banished the 7 gems to the corners of the earth!"));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "How can you not know of the person who saved the world from 300 years of demon reign?"));
    afterFightTrack.events.add(new TrackEvent(youngGirl, "...Dragons...demons...What are you talking about?"));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "Ugh..."));
    afterFightTrack.events.add(new TrackEvent(youngGirl, "Here comes another wave!"));
    story.add(afterFightTrack);
    OpeningFight2 game2 = new OpeningFight2();
    game2.create();
    game2.setController(this);
    story.add(game2);
    Track afterFight2Track = new Track();
    afterFight2Track.bg = JTactics.assets.openingBG;
    afterFight2Track.events.add(new TrackEvent(youngGirl, "Wow! You're really something!"));
    afterFight2Track.events.add(new TrackEvent(oldGuy, "To hell with these frogs!"));
    afterFight2Track.events.add(new TrackEvent(oldGuy, "I'm going to destroy every last one of them!"));
    afterFight2Track.events.add(new TrackEvent(youngGirl, "I will help you, but first you must come to the temple."));
    afterFight2Track.events.add(new TrackEvent(oldGuy, "I'M GOING TO FIND THEIR LEADER"));
    afterFight2Track.events.add(new TrackEvent(oldGuy, "AND BANISH HIM TO THE 5TH DIMENSION"));
    afterFight2Track.events.add(new TrackEvent(oldGuy, "HE CAN ROT IN HELL WITH BALTHIZAR"));
    afterFight2Track.events.add(new TrackEvent(youngGirl, "Please calm down and follow me."));
    story.add(afterFight2Track);
    {// at the temple
      Track track = new Track();
      track.bg = JTactics.assets.temple;
      track.events.add(new TrackEvent(youngGirl, "Welcome to the temple!"));
      track.events.add(new TrackEvent(headPriest, "I am the leader of this temple!"));
      track.events.add(new TrackEvent(youngGirl, "This man wishes to aid us in our fight against the frog kingdom!"));
      track.events.add(new TrackEvent(headPriest, "Ah! But is he worthy?"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.pinkRoom;
        }
        public void setupObjs()
        {
          {
            Guy g = new BlackMage();
            g.setController(players.get(0));
            g.tile = new JTTile(1, 1, 5);
            addObj(g);
          }
          {
            Guy g = new HeadPriest();
            g.setController(players.get(1));
            g.tile = new JTTile(1, 4, 5);
            addObj(g);
          }
        }
      }.setFightListener(this));
    }
    {// at the temple
      Track track = new Track();
      track.bg = JTactics.assets.temple;
      track.events.add(new TrackEvent(headPriest, "So be it! Time to begin your quest!"));
      track.events.add(new TrackEvent(headPriest, "Take this priestess with you!"));
      track.events.add(new TrackEvent(null, "Wizard aquired priestess!"));
      story.add(track);
    }
    {// in the forest while on their quest
      Track track = new Track();
      track.bg = JTactics.assets.forestRoad;
      track.events.add(new TrackEvent(oldGuy, "Well great, I have to drag you along."));
      track.events.add(new TrackEvent(youngGirl, "You'll need me sooner or later."));
      track.events.add(new TrackEvent(youngGirl, "You're not going to get through this wihtout a scratch."));
      track.events.add(new TrackEvent(youngGirl, "How did you manage to save the world again?"));
      track.events.add(new TrackEvent(oldGuy, "You could read about it in the great libraries."));
      track.events.add(new TrackEvent(thief, "Halt! You've troven into the wrong place today, buddy."));
      track.events.add(new TrackEvent(oldGuy, "Who the hell are you guys and what the hell do you want?"));
      track.events.add(new TrackEvent(thief, "We're theives and we're going to rob you of course!"));
      track.events.add(new TrackEvent(youngGirl, "I'm scared!"));
      track.events.add(new TrackEvent(oldGuy, "Don't worry, we'll take these guys down!"));
      storyIdx = story.size();
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.darkForest;
        }
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
            g.tile = new JTTile(0, 1, 5);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(1));
            g.maxAp = 3;
            g.tile = new JTTile(1, 4, 5);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(1));
            g.maxAp = 3;
            g.tile = new JTTile(1, 4, 4);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(1));
            g.tile = new JTTile(0, 4, 5);
            addObj(g);
          }
        }
      }.setFightListener(this));
    }
    {// at the temple
      Track track = new Track();
      track.bg = JTactics.assets.forestRoad;
      track.events.add(new TrackEvent(thief, "Don't think you can win that easily!"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.darkForest;
        }
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
            g.tile = new JTTile(0, 1, 5);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(1));
            g.tile = new JTTile(1, 4, 5);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(1));
            g.tile = new JTTile(1, 4, 4);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(1));
            g.tile = new JTTile(0, 4, 5);
            addObj(g);
          }
        }
      }.setFightListener(this));
    }
    {// at the temple
      Track track = new Track();
      track.bg = JTactics.assets.forestRoad;
      track.events.add(new TrackEvent(thief, "You guys are good!"));
      track.events.add(new TrackEvent(oldGuy, "You guys are crap!"));
      track.events.add(new TrackEvent(youngGirl, "Look out! Frogs!"));
      track.events.add(new TrackEvent(thief, "I guess we'll have to team up for this one!"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.darkForest;
        }
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
            g.tile = new JTTile(0, 1, 5);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(0));
            g.tile = new JTTile(1, 1, 4);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(0));
            g.tile = new JTTile(1, 1, 3);
            addObj(g);
          }
          {
            Guy g = new Thief();
            g.setController(players.get(0));
            g.tile = new JTTile(0, 1, 4);
            addObj(g);
          }
          {
            Guy g = new Frog();
            g.setController(players.get(1));
            g.tile = new JTTile(0, 4, 5);
            addObj(g);
          }
          {
            Guy g = new Frog();
            g.setController(players.get(1));
            g.tile = new JTTile(1, 4, 5);
            addObj(g);
          }
          {
            Guy g = new FrogMage();
            g.setController(players.get(1));
            g.tile = new JTTile(0, 4, 4);
            addObj(g);
          }
          {
            Guy g = new Frog();
            g.setController(players.get(1));
            g.tile = new JTTile(0, 4, 3);
            addObj(g);
          }
          {
            Guy g = new Frog();
            g.setController(players.get(1));
            g.tile = new JTTile(1, 4, 3);
            addObj(g);
          }
        }
      }.setFightListener(this));
    }
    {// at the temple
      Track track = new Track();
      track.bg = JTactics.assets.forestRoad;
      track.events.add(new TrackEvent(frogMage, "*croak* The frog queen will hear about this"));
      track.events.add(new TrackEvent(youngGirl, "Oh no!"));
      track.events.add(new TrackEvent(oldGuy, "Tell your frog queen I'm coming for her!"));
      track.events.add(new TrackEvent(thief, "Wow we really work well as a team!"));
      track.events.add(new TrackEvent(oldGuy, "No, I work well by myself. You guys are just here."));
      track.events.add(new TrackEvent(thief, "Maybe you'd consider joining us?"));
      track.events.add(new TrackEvent(youngGirl, "I'd never join scoundrel like you!"));
      track.events.add(new TrackEvent(thief, "I guess I'll take my leave..."));
      track.events.add(new TrackEvent(thief, "...but I have a feeling we'll be meeting again soon!"));
      story.add(track);
    }
    Track finalTrack = new Track();
    finalTrack.bg = JTactics.assets.openingBG;
    finalTrack.events.add(new TrackEvent("To be continued..."));
    story.add(finalTrack);
    storyIdx = 0;
  }
  public void endedFight()
  {
    // this means we won, increment the story
    // reset the scene state to ending
    time = 0;
    sceneStateAction = GO_FORWARD;
    sceneState = ENDING_SCENE;
  }
  public void lostFight()
  {
    // we lost the fight
    // display the previous dialogue again
    // doing this should automatically lead into the correct fight
    // and provide some backstory
    
    time = 0;
    sceneStateAction = GO_BACKWARD;
    sceneState = ENDING_SCENE;
  }
  public void nextPart()
  {
    // reset the scene state
    time = 0;
    sceneStateAction = NOTHING;
    sceneState = STARTING_SCENE;
    // logically increment the scene
    if (storyIdx < story.size()-1)
      storyIdx++;
  }
  public void lastPart()
  {
    // reset the current and last scene (so we can play both again)
    for (int i = storyIdx-1; i <= storyIdx; ++i)
    {
      Object obj = story.get(i);
      if (obj instanceof JTGame)
      {
        JTGame currGame = (JTGame)obj;
        currGame.startedGame = false;
      }else
      if (obj instanceof Track)
      {
        Track currTrack = (Track)obj;
        currTrack.reset();
      }
    }
    // reset the scene state
    time = 0;
    sceneStateAction = NOTHING;
    sceneState = STARTING_SCENE;
    // logically decrement the scene
    storyIdx--;
  }
  public void render()
  {
    time += Gdx.graphics.getDeltaTime();
    // if we're done fading out, do the fade out action
    if (time >= fadeInOutTime && sceneState == ENDING_SCENE)
    {
      if (sceneStateAction == GO_FORWARD)
        nextPart();
      if (sceneStateAction == GO_BACKWARD)
        lastPart();
    }
    Track currTrack = null;
    JTGame currGame = null;
    Object currEvent = story.get(storyIdx);
    if (currEvent instanceof Track)
      currTrack = (Track)currEvent;
    if (currEvent instanceof JTGame)
      currGame = (JTGame)currEvent;
    // start drawing and do logic dependent on what the current scene iss
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    sb.begin();
    if (currTrack != null)
    {
      sb.setColor(1, 1, 1, 1);
      sb.draw(currTrack.bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      TrackEvent currTrackEvent = currTrack.events.get(currTrack.currIdx);
      drawCharacterText(sb, currTrackEvent.charImage, currTrackEvent.dialogue);
      if (Gdx.input.justTouched() && sceneState != ENDING_SCENE)
      {
        if (currTrack.currIdx >= currTrack.events.size()-1)
        {
          time = 0;
          sceneState = ENDING_SCENE;
          sceneStateAction = GO_FORWARD;
        }else
        {
          // skip fading in
          if (time <= fadeInOutTime)
            time = fadeInOutTime;
          else// increment the scene track
            currTrack.currIdx++;
        }
      }
    }else
    if (currGame != null)
    {
      if (!currGame.startedGame)
        currGame.startGame();
      currGame.render(sb);
    }
    // draw fades if we're starting or ending a scene
    if (time < fadeInOutTime)
    {
      if (sceneState == STARTING_SCENE)
      {
        sb.setColor(0, 0, 0, (fadeInOutTime-time)/fadeInOutTime);
        sb.draw(JTactics.assets.white, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      }else
      if (sceneState == ENDING_SCENE)
      {
        sb.setColor(0, 0, 0, time/fadeInOutTime);
        sb.draw(JTactics.assets.white, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      }
    }
    sb.end();
    // dev
    if (Gdx.input.isKeyJustPressed(Keys.S))
      nextPart();
  }
  public void drawCharacterText(SpriteBatch sb, Texture tex, String str)
  {
    if (tex == null)
      tex = JTactics.assets.white;// should be something indicating question mark? (default)
    float width = Gdx.graphics.getWidth()*.8f;
    float textWidth = Gdx.graphics.getWidth()*.5f;
    BitmapFont.TextBounds tb = JTactics.assets.font.getWrappedBounds(str, textWidth);
    float midX = Gdx.graphics.getWidth()/2f;
    float pad = width*.1f;
    float imgWidth = width*.2f;
    float height = Gdx.graphics.getHeight()*.2f;
    height = tb.height>height?tb.height:height;
    float midY = pad/2f+height/2f;
    float textMidX = midX-(width-textWidth)/2f-tb.width/2f+imgWidth;
    float startX = midX-width/2f-pad/2f;
    //float startY = midY-pad/2f;
    sb.setColor(.4f, .5f, .9f, .7f);
    sb.draw(JTactics.assets.chatBox, startX, 0, width+pad, height+pad);
    float mouseWidth = width*.07f;
    float mouseRatio = (float)JTactics.assets.mouseNoClick.getHeight()/(float)JTactics.assets.mouseNoClick.getWidth();
    float mouseHeight = mouseWidth*mouseRatio;
    sb.setColor(1, 1, 1, .9f);
    if (storyIdx < story.size()-1)
      sb.draw(
        ((int)(time*2.5f))%2 == 0?JTactics.assets.mouseNoClick:JTactics.assets.mouseLeftClick,
        midX+width/2f-pad/2f-mouseWidth/2f, midY-mouseHeight/2f, mouseWidth, mouseHeight);
    sb.draw(tex, startX+pad/2f, midY-imgWidth/2, imgWidth, imgWidth);
    JTactics.assets.font.setColor(1f, 1f, 1, .9f);
    JTactics.assets.font.drawWrapped(sb, str, startX+pad+imgWidth, height+pad/2f, tb.width, BitmapFont.HAlignment.LEFT);
  }
  public void drawMiddleText(SpriteBatch sb, String str)
  {
    BitmapFont.TextBounds tb = JTactics.assets.font.getWrappedBounds(str, Gdx.graphics.getWidth());
    float midX = Gdx.graphics.getWidth()/2f;
    float midY = Gdx.graphics.getHeight()/2f;
    float pad = Gdx.graphics.getWidth()*.29f;
    sb.setColor(.4f, .5f, .9f, .9f);
    sb.draw(JTactics.assets.glow, midX-tb.width/2f-pad/2f, midY-tb.height/2f-pad/2f, tb.width+pad, tb.height+pad);
    JTactics.assets.font.setColor(1f, 1f, 1, 1);
    JTactics.assets.font.drawWrapped(sb, str, midX-tb.width/2f, midY+tb.height/2f, tb.width, BitmapFont.HAlignment.CENTER);
  }
}