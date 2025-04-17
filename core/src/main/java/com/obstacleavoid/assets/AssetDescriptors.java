package com.obstacleavoid.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
  //describes an asset to be loaded by its file name. This is an attempt to reduce the possibility of mistakes when naming game files.

  public static final AssetDescriptor<BitmapFont> FONT =
          new AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT , BitmapFont.class) ;

    public static final AssetDescriptor<TextureAtlas> GAME_PLAY =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY , TextureAtlas.class);



    public static  final AssetDescriptor<Skin>  UI_SKIN = new AssetDescriptor<Skin>(AssetPaths.UI_SKIN , Skin.class);

    public static final AssetDescriptor<Sound>  HIT_SOUND = new AssetDescriptor<Sound>(AssetPaths.HIT , Sound.class);

    public static final AssetDescriptor<Sound>  INTRO_SOUND  = new AssetDescriptor<Sound>(AssetPaths.INTRO , Sound.class);


    private AssetDescriptors(){

    }
}
