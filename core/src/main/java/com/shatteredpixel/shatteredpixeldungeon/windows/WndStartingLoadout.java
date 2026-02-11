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

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndStartingLoadout extends Window {

	private static final int WIDTH = 120;
	private static final int MAX_HEIGHT = 200;
	private static final int TTL_HEIGHT = 16;
	private static final int BTN_HEIGHT = 16;
	private static final int GAP = 1;
	private static final int SECTION_GAP = 4;

	// Weapon indices matching SPDSettings.customWeapon():
	// 0=class default
	// T1: 1=WornShortsword, 2=Dagger, 3=Gloves, 4=Rapier, 5=Cudgel, 6=MagesStaff
	// T2: 7=Shortsword, 8=HandAxe, 9=Spear, 10=Quarterstaff, 11=Dirk, 12=Sickle

	private static final int[] WEAPON_SPRITES = {
			0, // placeholder for "class default"
			ItemSpriteSheet.WORN_SHORTSWORD,
			ItemSpriteSheet.DAGGER,
			ItemSpriteSheet.GLOVES,
			ItemSpriteSheet.RAPIER,
			ItemSpriteSheet.CUDGEL,
			ItemSpriteSheet.MAGES_STAFF,
			ItemSpriteSheet.SHORTSWORD,
			ItemSpriteSheet.HAND_AXE,
			ItemSpriteSheet.SPEAR,
			ItemSpriteSheet.QUARTERSTAFF,
			ItemSpriteSheet.DIRK,
			ItemSpriteSheet.SICKLE
	};

	private static final String[] WEAPON_KEYS = {
			"default",
			"worn_shortsword",
			"dagger",
			"gloves",
			"rapier",
			"cudgel",
			"mages_staff",
			"shortsword",
			"hand_axe",
			"spear",
			"quarterstaff",
			"dirk",
			"sickle"
	};

	private ArrayList<StyledButton> buttons = new ArrayList<>();

	public WndStartingLoadout() {
		super();

		RenderedTextBlock title = PixelScene.renderTextBlock(
				Messages.get(this, "title"), 12);
		title.hardlight(TITLE_COLOR);
		title.setPos(
				(WIDTH - title.width()) / 2,
				(TTL_HEIGHT - title.height()) / 2
		);
		PixelScene.align(title);
		add(title);

		int selected = SPDSettings.customWeapon();

		Component content = new Component();

		float pos = 0;

		// "Class Default" button
		pos = addWeaponButton(content, 0, selected, pos);

		// Tier 1 header
		pos += SECTION_GAP;
		RenderedTextBlock t1Header = PixelScene.renderTextBlock(
				Messages.get(this, "tier1"), 7);
		t1Header.hardlight(0xCCCCCC);
		t1Header.setPos(2, pos);
		content.add(t1Header);
		pos = t1Header.bottom() + 2;

		// Tier 1 weapons (indices 1-6)
		for (int i = 1; i <= 6; i++) {
			pos = addWeaponButton(content, i, selected, pos);
		}

		// Tier 2 header
		pos += SECTION_GAP;
		RenderedTextBlock t2Header = PixelScene.renderTextBlock(
				Messages.get(this, "tier2"), 7);
		t2Header.hardlight(0xCCCCCC);
		t2Header.setPos(2, pos);
		content.add(t2Header);
		pos = t2Header.bottom() + 2;

		// Tier 2 weapons (indices 7-12)
		for (int i = 7; i <= 12; i++) {
			pos = addWeaponButton(content, i, selected, pos);
		}

		content.setSize(WIDTH, pos);

		int contentHeight = (int) Math.min(pos, MAX_HEIGHT - TTL_HEIGHT);

		ScrollPane pane = new ScrollPane(content);
		add(pane);
		pane.setRect(0, TTL_HEIGHT, WIDTH, contentHeight);

		resize(WIDTH, TTL_HEIGHT + contentHeight);
	}

	private float addWeaponButton(Component content, int index, int selected, float pos) {
		final int idx = index;

		String label = Messages.get(WndStartingLoadout.class, WEAPON_KEYS[index]);

		StyledButton btn = new StyledButton(Chrome.Type.GREY_BUTTON_TR, label, 6) {
			@Override
			protected void onClick() {
				SPDSettings.customWeapon(idx);
				for (StyledButton b : buttons) {
					b.icon().resetColor();
					b.textColor(WHITE);
				}
				icon().hardlight(TITLE_COLOR);
				textColor(TITLE_COLOR);
			}
		};
		btn.leftJustify = true;

		if (index == 0) {
			btn.icon(Icons.ENTER.get());
		} else {
			btn.icon(new ItemSprite(WEAPON_SPRITES[index], null));
		}

		if (index == selected) {
			btn.icon().hardlight(TITLE_COLOR);
			btn.textColor(TITLE_COLOR);
		}

		btn.setRect(0, pos, WIDTH, BTN_HEIGHT);
		content.add(btn);
		buttons.add(btn);
		return btn.bottom() + GAP;
	}
}
