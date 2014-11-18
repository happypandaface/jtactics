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
import com.zooth.jt.objs.*;

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
    Texture blueMage = JTactics.assets.blueMage;
    Texture headPriest = JTactics.assets.headPriest;
    Texture thief = JTactics.assets.thief;
    Texture frogMage = JTactics.assets.frogMage;
    Texture frogKnight = JTactics.assets.frogKnight;
    Texture scaryEyes = JTactics.assets.scaryEyes;
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
    afterFightTrack.events.add(new TrackEvent(oldGuy, "I defeated the dragon-lord Balthazar!"));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "I banished the 7 world gems to the corners of the earth!"));
    afterFightTrack.events.add(new TrackEvent(oldGuy, "How can you not know of the wizard who saved the world from 300 years of demon reign?"));
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
      track.events.add(new TrackEvent(headPriest, "Who is he?"));
      track.events.add(new TrackEvent(oldGuy, "Oh just the rogue wizard that saved the world."));
      track.events.add(new TrackEvent(oldGuy, "You remember last year during the weeks of darkness?"));
      track.events.add(new TrackEvent(oldGuy, "And the army of the undead?"));
      track.events.add(new TrackEvent(headPriest, "I do not recall such events."));
      track.events.add(new TrackEvent(oldGuy, "What!? Are you an imbecil?"));
      track.events.add(new TrackEvent(headPriest, "Do not insult the head of the local temple!"));
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
      track.events.add(new TrackEvent(headPriest, "It seems you have some skill."));
      track.events.add(new TrackEvent(headPriest, "You may aid the temple in it's efforts."));
      track.events.add(new TrackEvent(headPriest, "Take this priestess with you!"));
      track.events.add(new TrackEvent(null, "Priestess joined your party!"));
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
            g.tile = new JTTile(1, 1, 4);
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
            g.tile = new JTTile(1, 1, 4);
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
      track.events.add(new TrackEvent(frogMage, "*croak* We hail from the great frog queen!"));
      track.events.add(new TrackEvent(frogMage, "Prepare to die! *croak*"));
      track.events.add(new TrackEvent(youngGirl, "There's a ton of them."));
      track.events.add(new TrackEvent(thief, "Looks like we'll have to team up for this one, amigos!"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.darkForest;
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
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
    // leading into the forest
    {
      Track track = new Track();
      track.bg = JTactics.assets.forestRoad;
      track.events.add(new TrackEvent(frogMage, "*croak* The frog queen will hear about this"));
      track.events.add(new TrackEvent(youngGirl, "Oh no!"));
      track.events.add(new TrackEvent(oldGuy, "Tell your frog queen I'm coming for her!"));
      track.events.add(new TrackEvent(thief, "Looks like those frogs croaked!"));
      track.events.add(new TrackEvent(oldGuy, "..."));
      track.events.add(new TrackEvent(thief, "Hey we really work well as a team!"));
      track.events.add(new TrackEvent(oldGuy, "No, I work well by myself. You guys are just here."));
      track.events.add(new TrackEvent(thief, "With your help, I'm sure we could raid the castle in the big city!"));
      track.events.add(new TrackEvent(oldGuy, "Join you? Hahahaha! I'd rather eat my wizard hat."));
      track.events.add(new TrackEvent(youngGirl, "I'd never join a scoundrel like you!"));
      track.events.add(new TrackEvent(thief, "I guess I'll take my leave..."));
      track.events.add(new TrackEvent(thief, "...but I have a feeling we'll be meeting again soon, mes amis!"));
      story.add(track);
    }
    // later... in the forest swamp
    {
      Track track = new Track();
      track.bg = JTactics.assets.swampBG;
      track.events.add(new TrackEvent(youngGirl, "Be wary. This is the swamp of misery!"));
      track.events.add(new TrackEvent(oldGuy, "... really? What challenge could this swamp possible have to offer?"));
      track.events.add(new TrackEvent(youngGirl, "It's used as a proving ground for master priests."));
      track.events.add(new TrackEvent(youngGirl, "I'm only an apprentice so I have never even entered this area."));
      track.events.add(new TrackEvent(thief, "Only an apprentice?"));
      track.events.add(new TrackEvent(thief, "But your control of magics is so powerful, m'lady."));
      track.events.add(new TrackEvent(oldGuy, "You again!"));
      track.events.add(new TrackEvent(thief, "I told you we'd meet again soon..."));
      track.events.add(new TrackEvent(youngGirl, "You followed us!"));
      track.events.add(new TrackEvent(thief, "Yes! And this time, I've brought my new compadres!"));
      track.events.add(new TrackEvent(frogKnight, "This swamp is now under control of the frog queen *croak*"));
      track.events.add(new TrackEvent(oldGuy, "Don't worry, priestess. I'll just blast them away with a meteor strike!"));
      track.events.add(new TrackEvent(youngGirl, "..."));
      track.events.add(new TrackEvent(thief, "..."));
      track.events.add(new TrackEvent(frogKnight, "*croak*"));
      track.events.add(new TrackEvent(thief, "Hmmm... so where's this meteor?"));
      track.events.add(new TrackEvent(oldGuy, "What is this!? My spellbook is nearly empty!"));
      track.events.add(new TrackEvent(oldGuy, "What happened to my vast arsenal of deadly spells?"));
      track.events.add(new TrackEvent(frogKnight, "Leave this forest or die! *croak*"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.swampFight;
        }
        public void endGame()
        {
          fl.endedFight();
        }
        // this is to make sure we only call endedFight once
        boolean extEnding = false;
        public boolean checkPause()
        {
          List<Guy> humanGuys = getObjs(players.get(0));
          int health = 0;
          for (int i = 0; i < humanGuys.size(); ++i)
          {
            Guy obj = humanGuys.get(i);
            health += obj.hp;
          }
          if (health < 2)
          {
            if (!extEnding)
              fl.endedFight();
            extEnding = true;
            return true;
          }
          return false;
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new BlackMage().setController(players.get(0)).setTile(1, 1, 5));
          addObj(new WhiteMage().setController(players.get(0)).setTile(0, 1, 5));
          addObj(new Thief().setController(players.get(1)).setTile(1, 4, 5));
          addObj(new Thief().setController(players.get(1)).setTile(0, 4, 5));
          addObj(new Thief().setController(players.get(1)).setTile(1, 4, 4));
          addObj(new FrogKnight().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(1, 4, 3));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.swampBG;
      track.events.add(new TrackEvent(oldGuy, "I've been sabotaged! This fight is a farce!"));
      track.events.add(new TrackEvent(youngGirl, "We surrender! Don't hurt us!"));
      track.events.add(new TrackEvent(thief, "I couldn't hurt a pretty thing like you."));
      track.events.add(new TrackEvent(thief, "But I can definitely rob you. Hand over all your valubles."));
      track.events.add(new TrackEvent(frogKnight, "Leave this land or die. *croak*"));
      track.events.add(new TrackEvent(headPriest, "Not so fast!"));
      track.events.add(new TrackEvent(frogKnight, "Who are you? *croak*"));
      track.events.add(new TrackEvent(headPriest, "I am the head priest of the local temple..."));
      track.events.add(new TrackEvent(headPriest, "...and you are encroaching on our land."));
      track.events.add(new TrackEvent(thief, "Doesn't matter, we can take you and your lousy temple!"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.swampFight;
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new BlackMage().setController(players.get(0)).setTile(1, 1, 5));
          addObj(new WhiteMage().setController(players.get(0)).setTile(0, 1, 5));
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(0, 1, 4));
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(1, 1, 3));
          addObj(new HeadPriest().setController(players.get(0)).setTile(1, 1, 4));
          addObj(new Thief().setController(players.get(1)).setTile(1, 4, 5));
          addObj(new Thief().setController(players.get(1)).setTile(0, 4, 5));
          addObj(new Thief().setController(players.get(1)).setTile(1, 4, 4));
          addObj(new FrogKnight().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(1, 4, 3));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.swampBG;
      track.events.add(new TrackEvent(headPriest, "That is the power of the local temple!"));
      track.events.add(new TrackEvent(frogKnight, "*croak* You may have defeated us,"));
      track.events.add(new TrackEvent(frogKnight, "But the frog queen will destroy you!"));
      track.events.add(new TrackEvent(thief, "Looks like I've underestimated you fellows again."));
      track.events.add(new TrackEvent(thief, "My guild is still looking for more members..."));
      track.events.add(new TrackEvent(oldGuy, "Are you seriously recruiting us again?"));
      track.events.add(new TrackEvent(youngGirl, "You are worthless!"));
      track.events.add(new TrackEvent(thief, "Okay, but this isn't the last time our paths will cross, I'm sure."));
      track.events.add(new TrackEvent(oldGuy, "Get out of my sight!"));
      story.add(track);
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.swampBG;
      track.events.add(new TrackEvent(youngGirl, "That guy is really becoming a nuisance."));
      track.events.add(new TrackEvent(headPriest, "I'm glad I showed up when I did."));
      track.events.add(new TrackEvent(oldGuy, "My spells... I can't remember them..."));
      track.events.add(new TrackEvent(youngGirl, "Are you sure you ever knew more spells."));
      track.events.add(new TrackEvent(oldGuy, "Don't mock me!"));
      track.events.add(new TrackEvent(oldGuy, "I need to regain my spells..."));
      track.events.add(new TrackEvent(headPriest, "You need to fulfil your promise..."));
      track.events.add(new TrackEvent(headPriest, "And help us defeat the frog queen!"));
      track.events.add(new TrackEvent(oldGuy, "Screw you and your frog queen!"));
      track.events.add(new TrackEvent(oldGuy, "I'm going to find who has sapped my powers!"));
      track.events.add(new TrackEvent(oldGuy, "And tear them limb from limb!"));
      track.events.add(new TrackEvent(null, "Wizard has left your party!"));
      story.add(track);
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.swampBG;
      track.events.add(new TrackEvent(headPriest, "It seems your friend is not as trustworthy as I thought."));
      track.events.add(new TrackEvent(youngGirl, "Yes, it seems the temple is alone in our efforts."));
      track.events.add(new TrackEvent(headPriest, "Let us press on!"));
      story.add(track);
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.swampTemple;
      track.events.add(new TrackEvent(headPriest, "Good, it looks like the swamp temple has not fallen to the frogs."));
      track.events.add(new TrackEvent(youngGirl, "What is so important about the swamp temple?"));
      track.events.add(new TrackEvent(headPriest, "You have not yet proved yourself a master..."));
      track.events.add(new TrackEvent(headPriest, "...so you wouldn't be given that information."));
      track.events.add(new TrackEvent(youngGirl, "I think our current crisis changes things."));
      track.events.add(new TrackEvent(headPriest, "Yes maybe you are right. Let me tell you-"));
      track.events.add(new TrackEvent(youngGirl, "Look! Over there! Frogs!"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.swampFight;
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new WhiteMage().setController(players.get(0)).setTile(0, 1, 5));
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(0, 1, 4));
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(1, 1, 3));
          addObj(new HeadPriest().setController(players.get(0)).setTile(1, 1, 4));
          addObj(new FrogMage().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new FrogMage().setController(players.get(1)).setTile(1, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(1, 3, 3));
          addObj(new Frog().setController(players.get(1)).setTile(1, 4, 3));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.swampTemple;
      track.events.add(new TrackEvent(youngGirl, "More of them! To the north!"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.swampFight;
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new WhiteMage().setController(players.get(0)).setTile(0, 1, 5));
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(0, 1, 4));
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(1, 1, 3));
          addObj(new HeadPriest().setController(players.get(0)).setTile(1, 1, 4));
          addObj(new FrogMage().setController(players.get(1)).setTile(0, 4, 5));
          addObj(new FrogMage().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new FrogMage().setController(players.get(1)).setTile(1, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(1, 3, 5));
          addObj(new Frog().setController(players.get(1)).setTile(1, 3, 3));
          addObj(new Frog().setController(players.get(1)).setTile(1, 4, 3));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.swampTemple;
      track.events.add(new TrackEvent(headPriest, "This is an allout attack!"));
      track.events.add(new TrackEvent(youngGirl, "There's another wave!"));
      track.events.add(new TrackEvent(blueMage, "Mr. Head Priest!"));
      track.events.add(new TrackEvent(headPriest, "I'm busy right now!"));
      track.events.add(new TrackEvent(blueMage, "It's very urgent! It's a message from the forest temple!"));
      track.events.add(new TrackEvent(headPriest, "What is it?"));
      track.events.add(new TrackEvent(blueMage, "Our home temple is under attack!"));
      track.events.add(new TrackEvent(headPriest, "My lord, this is a catastrophe!"));
      track.events.add(new TrackEvent(headPriest, "EVERYONE!"));
      track.events.add(new TrackEvent(headPriest, "FALL BACK AND DEFEND THE TEMPLE!"));
      track.events.add(new TrackEvent(youngGirl, "But sir, the swamp temple!"));
      track.events.add(new TrackEvent(headPriest, "The frog queen is not an intelligent leader."));
      track.events.add(new TrackEvent(headPriest, "We'll have some time before she discovers the swamp temple's power."));
      story.add(track);
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.forestCave;
      track.events.add(new TrackEvent(null, "Meanwhile, the wizard wanders into the forest in search of new magics!"));
      track.events.add(new TrackEvent(oldGuy, "Those guys think they're so smart."));
      track.events.add(new TrackEvent(oldGuy, "What FOOLS!"));
      track.events.add(new TrackEvent(oldGuy, "Where am I!"));
      track.events.add(new TrackEvent(oldGuy, "What is this some kind of magical cave?"));
      track.events.add(new TrackEvent(scaryEyes, "Who has come to my magical cave?"));
      track.events.add(new TrackEvent(oldGuy, "So it IS a magical cave!"));
      track.events.add(new TrackEvent(scaryEyes, "Yes. This is MY magical cave."));
      track.events.add(new TrackEvent(scaryEyes, "Those who seek its magic must first bear through a test of strength."));
      track.events.add(new TrackEvent(scaryEyes, "Enter and begin!"));
      track.events.add(new TrackEvent(scaryEyes, "But be weary! Many have died seeking my magic."));
      track.events.add(new TrackEvent(oldGuy, "There's a lot of idiots out there."));
      //storyIdx = story.size();
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.caveFloor;
        }
        public void setupObstacles()
        {
          addBoulder(new Boulder(1, 2, 5));
          addBoulder(new Boulder(1, 2, 4));
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new BlackMage().setController(players.get(0)).setTile(0, 1, 5));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 5));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.forestCave;
      track.events.add(new TrackEvent(oldGuy, "There, I beat your stupid test."));
      track.events.add(new TrackEvent(oldGuy, "Where's my magic?"));
      track.events.add(new TrackEvent(scaryEyes, "FOOL! That was not the test!"));
      track.events.add(new TrackEvent(scaryEyes, "Those were just some frogs!"));
      track.events.add(new TrackEvent(oldGuy, "Are you serious?"));
      track.events.add(new TrackEvent(scaryEyes, "THIS is the test!"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.caveFloor;
        }
        public void setupObstacles()
        {
          addBoulder(new Boulder(1, 2, 5));
          addBoulder(new Boulder(1, 2, 4));
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new BlackMage().setController(players.get(0)).setTile(0, 1, 5));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
          addObj(new FrogMage().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 5));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.forestCave;
      track.events.add(new TrackEvent(oldGuy, "Okay, now give me your stupid spell. I bet it's crap anyway."));
      track.events.add(new TrackEvent(scaryEyes, "Congradulations!"));
      track.events.add(new TrackEvent(oldGuy, "Finally..."));
      track.events.add(new TrackEvent(scaryEyes, "You have beaten the first test!"));
      track.events.add(new TrackEvent(oldGuy, "There's more!?"));
      track.events.add(new TrackEvent(scaryEyes, "Remember! Boulders can get in your way! But they can also crush your enemies!"));
      track.events.add(new TrackEvent(scaryEyes, "Also you must have 2 actions left to push them!"));
      track.events.add(new TrackEvent(scaryEyes, "(actions are the white things below your character)"));
      track.events.add(new TrackEvent(oldGuy, "What? Actions"));
      track.events.add(new TrackEvent(scaryEyes, "I wasn't talking to you, I was talking to the guy playing the game."));
      track.events.add(new TrackEvent(oldGuy, "What guy?"));
      track.events.add(new TrackEvent(scaryEyes, "You can't see him? Hmmm... very interesting."));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.caveFloor;
        }
        public void setupObstacles()
        {
          addBoulder(new Boulder(1, 2, 5));
          addBoulder(new Boulder(1, 2, 4));
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new BlackMage().setController(players.get(0)).setTile(0, 1, 5));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
          addObj(new FrogKnight().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 5));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.forestCave;
      track.events.add(new TrackEvent(oldGuy, "Am I done yet?"));
      track.events.add(new TrackEvent(scaryEyes, "You have done well!"));
      track.events.add(new TrackEvent(scaryEyes, "Prepare for the final test!"));
      track.events.add(new TrackEvent(oldGuy, "This is lame. I'm out."));
      track.events.add(new TrackEvent(scaryEyes, "No wait! Come back!"));
      track.events.add(new TrackEvent(scaryEyes, "I was umm... joking... yes. You have done well! I will teach you the spell."));
      track.events.add(new TrackEvent(scaryEyes, "But be weary."));
      track.events.add(new TrackEvent(scaryEyes, "This spell is powerful but may cause you as much pain as your enemies."));
      track.events.add(new TrackEvent(scaryEyes, "For it uses the life force of either you or your friends to cast!"));
      track.events.add(new TrackEvent(oldGuy, "Ooh, very nice!"));
      //storyIdx = story.size();
      story.add(track);
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.temple;
      track.events.add(new TrackEvent(null, "Back at the local temple, the head priest holds off waves of frogs"));
      track.events.add(new TrackEvent(headPriest, "This is an onslaught!"));
      track.events.add(new TrackEvent(blueMage, "Many of our fighters are wounded and need rest!"));
      track.events.add(new TrackEvent(headPriest, "Send them back in!"));
      track.events.add(new TrackEvent(youngGirl, "But sir, they may die in battle!"));
      track.events.add(new TrackEvent(headPriest, "They'll die as prisoners if we lose this fight!"));
      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.pinkRoom;
        }
        public void setupObstacles()
        {
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new HeadPriest().setController(players.get(0)).setTile(0, 1, 5)).hp = 2;
          addObj(new WhiteMage().setController(players.get(0)).setTile(1, 1, 5)).hp = 2;
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(0, 1, 4)).hp = 2;
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(1, 1, 4)).hp = 1;
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
          addObj(new Frog().setController(players.get(1)).setTile(1, 4, 3));
          addObj(new FrogKnight().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new FrogKnight().setController(players.get(1)).setTile(1, 3, 4));
          addObj(new Frog().setController(players.get(1)).setTile(1, 3, 3));
          addObj(new FrogMage().setController(players.get(1)).setTile(1, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 5));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.temple;
      track.events.add(new TrackEvent(headPriest, "Another wave, there!"));
      track.events.add(new TrackEvent(blueMage, "Who's that with them?"));
      track.events.add(new TrackEvent(youngGirl, "Oh no, it's-"));
      track.events.add(new TrackEvent(thief, "Hello friends! It's me again!"));
      story.add(track);
    }

    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.pinkRoom;
        }
        public void endGame()
        {
          fl.endedFight();
        }
        // this is to make sure we only call endedFight once
        boolean extEnding = false;
        public boolean checkPause()
        {
          List<Guy> humanGuys = getObjs(players.get(0));
          int health = 0;
          for (int i = 0; i < humanGuys.size(); ++i)
          {
            Guy obj = humanGuys.get(i);
            health += obj.hp;
          }
          if (health < 2)
          {
            if (!extEnding)
              fl.endedFight();
            extEnding = true;
            return true;
          }
          return false;
        }
        public void setupObstacles()
        {
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new HeadPriest().setController(players.get(0)).setTile(0, 1, 5)).hp = 2;
          addObj(new WhiteMage().setController(players.get(0)).setTile(1, 1, 5)).hp = 2;
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(0, 1, 4)).hp = 2;
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(1, 1, 4)).hp = 1;
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
          addObj(new Frog().setController(players.get(1)).setTile(1, 4, 3));
          addObj(new FrogKnight().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(1, 3, 3));
          addObj(new FrogMage().setController(players.get(1)).setTile(1, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 5));
          addObj(new Thief().setController(players.get(1)).setTile(0, 5, 6));
          addObj(new Thief().setController(players.get(1)).setTile(0, 5, 4));
          addObj(new Thief().setController(players.get(1)).setTile(0, 5, 5));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.temple;
      track.events.add(new TrackEvent(frogKnight, "Surrender the secrets of the swamp temple to the frog queen! *croak*"));
      track.events.add(new TrackEvent(headPriest, "Never!"));
      track.events.add(new TrackEvent(oldGuy, "What's this secret everyone's talking about?"));
      track.events.add(new TrackEvent(youngGirl, "You came back!"));
      track.events.add(new TrackEvent(oldGuy, "Yeah, I gotta test this new spell on someone!"));
      track.events.add(new TrackEvent(headPriest, "What is this new knowledge you've aquired?"));
      track.events.add(new TrackEvent(oldGuy, "I learned a spell to make a teammate explode!"));
      track.events.add(new TrackEvent(youngGirl, "Great."));
      track.events.add(new TrackEvent(thief, "Hahaha! Have fun with that!"));
      track.events.add(new TrackEvent(oldGuy, "It can also catch enemies in the explosion, of course."));
      track.events.add(new TrackEvent(headPriest, "I'm willing to try anything! Let's do this!"));

      story.add(track);
    }
    {
      story.add(new ExtendableFight()
      {
        public Texture getBg()
        {
          return JTactics.assets.pinkRoom;
        }
        public void setupInPlay()
        {
          addHex(0, 2, 5, 2);
          addHex(0, 3, 5, 1);
          addHex(0, 4, 5, 2);
        }
        public void setupObjs()
        {
          addObj(new HeadPriest().setController(players.get(0)).setTile(0, 1, 5)).hp = 2;
          addObj(new WhiteMage().setController(players.get(0)).setTile(1, 1, 5)).hp = 2;
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(0, 1, 4)).hp = 2;
          addObj(new GenericWhiteMage().setController(players.get(0)).setTile(1, 1, 4)).hp = 1;
          addObj(new BlackMage().setController(players.get(0)).setTile(1, 1, 3).addAction(Action.SPIRIT_BURST));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 3));
          addObj(new Frog().setController(players.get(1)).setTile(1, 4, 3));
          addObj(new FrogKnight().setController(players.get(1)).setTile(0, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(1, 3, 3));
          addObj(new FrogMage().setController(players.get(1)).setTile(1, 4, 4));
          addObj(new Frog().setController(players.get(1)).setTile(0, 4, 5));
          addObj(new Thief().setController(players.get(1)).setTile(0, 5, 6));
          addObj(new Thief().setController(players.get(1)).setTile(0, 5, 4));
          addObj(new Thief().setController(players.get(1)).setTile(0, 5, 5));
        }
      }.setFightListener(this));
    }
    {
      Track track = new Track();
      track.bg = JTactics.assets.temple;
      track.events.add(new TrackEvent(thief, "Looks like I chose the wrong side."));
      track.events.add(new TrackEvent(frogKnight, "*croak* It seems you have bested us today."));
      track.events.add(new TrackEvent(frogKnight, "But the frogs will be back. And in greater numbers!"));
      track.events.add(new TrackEvent(headPriest, "Never!"));
      track.events.add(new TrackEvent(oldGuy, "What's this secret everyone's talking about?"));
      track.events.add(new TrackEvent(youngGirl, "You came back!"));
      track.events.add(new TrackEvent(oldGuy, "Yeah, I gotta test this new spell on someone!"));
      track.events.add(new TrackEvent(headPriest, "What is this new knowledge you've aquired?"));
      track.events.add(new TrackEvent(oldGuy, "I learned a spell to make a teammate explode!"));
      track.events.add(new TrackEvent(youngGirl, "Great."));
      track.events.add(new TrackEvent(thief, "Hahaha! Have fun with that!"));
      track.events.add(new TrackEvent(oldGuy, "It can also catch enemies in the explosion, of course."));
      track.events.add(new TrackEvent(headPriest, "I'm willing to try anything! Let's do this!"));

      story.add(track);
    }
    Track finalTrack = new Track();
    finalTrack.bg = JTactics.assets.openingBG;
    finalTrack.events.add(new TrackEvent("To be continued..."));
    story.add(finalTrack);
    //storyIdx = 0;
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
