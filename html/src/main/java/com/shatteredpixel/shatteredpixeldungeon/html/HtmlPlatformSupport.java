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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.watabou.utils.PlatformSupport;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlPlatformSupport extends PlatformSupport {

	@Override
	public void updateDisplaySize() {
		// Browser handles display size via canvas, nothing to persist
	}

	@Override
	public void updateSystemUI() {
		// No system UI changes needed in browser
	}

	@Override
	public boolean connectedToUnmeteredNetwork() {
		return true; // Assume browser connection is unmetered
	}

	@Override
	public boolean supportsVibration() {
		return false;
	}

	/* FONT SUPPORT - uses FreeType via Emscripten/WASM (gdx-freetype-teavm) */

	private static FreeTypeFontGenerator basicFontGenerator;
	private static FreeTypeFontGenerator asianFontGenerator;

	@Override
	public void setupFontGenerators(int pageSize, boolean systemfont) {
		if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
			return;
		}
		this.pageSize = pageSize;
		this.systemfont = systemfont;

		resetGenerators(false);
		fonts = new HashMap<>();

		if (systemfont) {
			basicFontGenerator = asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		} else {
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
			asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		}

		fonts.put(basicFontGenerator, new HashMap<>());
		fonts.put(asianFontGenerator, new HashMap<>());

		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}

	// TeaVM's regex doesn't support Java's \p{InXxx_Yyy} syntax (underscored block names),
	// so we use explicit Unicode codepoint ranges instead.
	// Hangul Syllables: U+AC00-U+D7A3, CJK Unified Ideographs: U+4E00-U+9FFF,
	// CJK Symbols and Punctuation: U+3000-U+303F, Halfwidth and Fullwidth Forms: U+FF00-U+FFEF,
	// Hiragana: U+3040-U+309F, Katakana: U+30A0-U+30FF
	private static Matcher asianMatcher = Pattern.compile(
			"[\\uAC00-\\uD7A3]|[\\u4E00-\\u9FFF]|[\\u3000-\\u303F]|[\\uFF00-\\uFFEF]|" +
			"[\\u3040-\\u309F]|[\\u30A0-\\u30FF]").matcher("");

	@Override
	protected FreeTypeFontGenerator getGeneratorForString( String input ){
		if (asianMatcher.reset(input).find()){
			return asianFontGenerator;
		} else {
			return basicFontGenerator;
		}
	}

	private Pattern regularsplitter = Pattern.compile(
			"(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
					"(?<=[\\u3040-\\u309F])|(?=[\\u3040-\\u309F])|" +
					"(?<=[\\u30A0-\\u30FF])|(?=[\\u30A0-\\u30FF])|" +
					"(?<=[\\u4E00-\\u9FFF])|(?=[\\u4E00-\\u9FFF])|" +
					"(?<=[\\u3000-\\u303F])|(?=[\\u3000-\\u303F])");

	private Pattern regularsplitterMultiline = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
					"(?<=[\\u3040-\\u309F])|(?=[\\u3040-\\u309F])|" +
					"(?<=[\\u30A0-\\u30FF])|(?=[\\u30A0-\\u30FF])|" +
					"(?<=[\\u4E00-\\u9FFF])|(?=[\\u4E00-\\u9FFF])|" +
					"(?<=[\\u3000-\\u303F])|(?=[\\u3000-\\u303F])");

	@Override
	public String[] splitforTextBlock(String text, boolean multiline) {
		if (multiline) {
			return regularsplitterMultiline.split(text);
		} else {
			return regularsplitter.split(text);
		}
	}
}
