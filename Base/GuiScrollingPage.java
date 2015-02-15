/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Base;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import Reika.ChromatiCraft.ChromatiCraft;
import Reika.DragonAPI.Libraries.IO.ReikaRenderHelper;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;

public abstract class GuiScrollingPage extends ChromaBookGui {

	protected static int offsetX = 0;
	protected static int offsetY = 0;

	protected int maxX = 0;
	protected int maxY = 0;

	protected final int paneWidth;
	protected final int paneHeight;

	protected GuiScrollingPage(EntityPlayer ep, int x, int y, int w, int h) {
		super(ep, x, y);
		paneWidth = w;
		paneHeight = h;
	}

	public static void resetOffset() {
		offsetX = 0;
		offsetY = 0;
	}

	@Override
	public void drawScreen(int x, int y, float f) {
		leftX = (width - xSize) / 2;
		topY = (height - ySize) / 2;

		int sp = Math.max(1, 180/Math.max(1, ReikaRenderHelper.getFPS()));
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			sp *= 2;
		}
		if (Minecraft.getMinecraft().gameSettings.keyBindForward.getIsKeyPressed() || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			offsetY -= sp;
		}
		if (Minecraft.getMinecraft().gameSettings.keyBindLeft.getIsKeyPressed() || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			offsetX -= sp;
		}
		if (Minecraft.getMinecraft().gameSettings.keyBindBack.getIsKeyPressed() || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			offsetY += sp;
		}
		if (Minecraft.getMinecraft().gameSettings.keyBindRight.getIsKeyPressed() || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			offsetX += sp;
		}

		if (offsetX < 0) {
			offsetX = 0;
		}
		if (offsetY < 0) {
			offsetY = 0;
		}
		if (offsetX > maxX && maxX >= 0) {
			offsetX = maxX;
		}
		if (offsetY > maxY && maxY >= 0) {
			offsetY = maxY;
		}

		ReikaTextureHelper.bindTexture(ChromatiCraft.class, this.getScrollingTexture());
		int u = offsetX%256;
		int v = offsetY%256;
		this.drawTexturedModalRect(leftX+7, topY-1, u, v, paneWidth, paneHeight);

		super.drawScreen(x, y, f);
	}

	protected abstract String getScrollingTexture();

}
