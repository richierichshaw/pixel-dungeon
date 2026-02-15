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

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.compiler.TeaCompiler;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;
import org.teavm.vm.TeaVMOptimizationLevel;

import java.io.File;

public class BuildHtml {

	public static void main(String[] args) {

		WebBackend backend = new WebBackend();
		backend.setHtmlTitle("Pixel Dungeon");
		backend.setHtmlWidth(960);
		backend.setHtmlHeight(640);
		backend.setStartJettyAfterBuild(false);

		TeaCompiler compiler = new TeaCompiler(backend);
		compiler.addAssets(new AssetFileHandle("../core/src/main/assets"));
		compiler.setMainClass(HtmlLauncher.class.getName());
		compiler.setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE);
		compiler.setObfuscated(false);

		// Register packages that use Reflection.forName() for Bundlable deserialization.
		// The .** glob suffix is required so TeaVM preserves all classes in these packages.
		compiler.addReflectionClass("com.shatteredpixel.shatteredpixeldungeon.**");
		compiler.addReflectionClass("com.watabou.**");

		compiler.build(new File("build/dist"));
	}
}
