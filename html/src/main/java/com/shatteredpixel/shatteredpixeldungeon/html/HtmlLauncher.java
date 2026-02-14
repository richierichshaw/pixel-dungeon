/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.html;

import com.badlogic.gdx.Files;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

public class HtmlLauncher {

	public static void main(String[] args) {

		Game.version = "3.3.5-BROWSER";
		Game.versionCode = 890;

		// Browser uses Local file type, backed by IndexedDB via gdx-teavm
		FileUtils.setDefaultFileProperties(Files.FileType.Local, "");

		WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
		config.width = 0;   // 0 = auto-size to fill browser window
		config.height = 0;
		config.showDownloadLogs = true;

		// Preload FreeType WASM before game starts
		config.preloadListener = assetLoader -> {
			assetLoader.loadScript("freetype.js");
		};

		new WebApplication(new ShatteredPixelDungeon(new HtmlPlatformSupport()), config);
	}
}
