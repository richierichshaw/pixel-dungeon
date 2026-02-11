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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndCustomDifficulty extends Window {

	private static final int WIDTH = 120;
	private static final int TTL_HEIGHT = 16;
	private static final int SLIDER_HEIGHT = 24;
	private static final int GAP = 2;
	private static final int BTN_HEIGHT = 18;

	public WndCustomDifficulty() {

		super();

		RenderedTextBlock title = PixelScene.renderTextBlock(
				Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos(
				(WIDTH - title.width()) / 2,
				(TTL_HEIGHT - title.height()) / 2
		);
		PixelScene.align(title);
		add( title );

		float pos = TTL_HEIGHT + GAP;

		OptionSlider hpSlider = new OptionSlider(
				Messages.get(this, "hp", SPDSettings.customHP()),
				"5", "50", 5, 50) {
			@Override
			protected void onChange() {
				SPDSettings.customHP( getSelectedValue() );
				this.title.text(Messages.get(WndCustomDifficulty.class, "hp", getSelectedValue()));
			}
		};
		hpSlider.setSelectedValue( SPDSettings.customHP() );
		hpSlider.setRect( 0, pos, WIDTH, SLIDER_HEIGHT );
		add( hpSlider );

		pos = hpSlider.bottom() + GAP;

		OptionSlider strSlider = new OptionSlider(
				Messages.get(this, "str", SPDSettings.customSTR()),
				"5", "20", 5, 20) {
			@Override
			protected void onChange() {
				SPDSettings.customSTR( getSelectedValue() );
				this.title.text(Messages.get(WndCustomDifficulty.class, "str", getSelectedValue()));
			}
		};
		strSlider.setSelectedValue( SPDSettings.customSTR() );
		strSlider.setRect( 0, pos, WIDTH, SLIDER_HEIGHT );
		add( strSlider );

		pos = strSlider.bottom() + GAP;

		OptionSlider goldSlider = new OptionSlider(
				Messages.get(this, "gold", SPDSettings.customGold()),
				"0", "500", 0, 10) {
			@Override
			protected void onChange() {
				SPDSettings.customGold( getSelectedValue() * 50 );
				this.title.text(Messages.get(WndCustomDifficulty.class, "gold", getSelectedValue() * 50));
			}
		};
		goldSlider.setSelectedValue( SPDSettings.customGold() / 50 );
		goldSlider.setRect( 0, pos, WIDTH, SLIDER_HEIGHT );
		add( goldSlider );

		pos = goldSlider.bottom() + GAP + 2;

		RedButton btnReset = new RedButton( Messages.get(this, "reset") ) {
			@Override
			protected void onClick() {
				SPDSettings.customHP( 20 );
				SPDSettings.customSTR( 10 );
				SPDSettings.customGold( 0 );
				hide();
				ShatteredPixelDungeon.scene().addToFront(new WndCustomDifficulty());
			}
		};
		btnReset.setRect( 0, pos, WIDTH, BTN_HEIGHT );
		add( btnReset );

		pos = btnReset.bottom();

		resize( WIDTH, (int)pos );
	}
}
