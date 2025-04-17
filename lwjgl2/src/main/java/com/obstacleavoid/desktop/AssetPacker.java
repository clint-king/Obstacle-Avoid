package com.obstacleavoid.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {

    private static final boolean DRAW_DEBUG_OUTLINE = false;
    private static final String RAW_ASSET_PATH = "lwjgl2/assets-raw";
    private static final String ASSETS_PATH  = "assets";


    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.debug = DRAW_DEBUG_OUTLINE;
        //settings.pot = false; //power off tool , is better to set pot to true


        TexturePacker.process(settings , RAW_ASSET_PATH + "/gamePlay" , ASSETS_PATH + "/gameplay" , "gameplay");


        TexturePacker.process(RAW_ASSET_PATH + "/skin" , ASSETS_PATH + "/ui" , "uiskin");




    }

}
