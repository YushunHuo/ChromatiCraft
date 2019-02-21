/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.Render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Auxiliary.ChromaStacks;
import Reika.ChromatiCraft.Auxiliary.Interfaces.OperationInterval;
import Reika.ChromatiCraft.Auxiliary.Interfaces.OperationInterval.OperationState;
import Reika.ChromatiCraft.Auxiliary.RecipeManagers.PoolRecipes.PoolRecipe;
import Reika.ChromatiCraft.Auxiliary.Render.ChromaFontRenderer.FontType;
import Reika.ChromatiCraft.Base.TileEntity.TileEntityAdjacencyUpgrade;
import Reika.ChromatiCraft.Block.BlockActiveChroma.TileEntityChroma;
import Reika.ChromatiCraft.Block.BlockDummyAux.TileEntityDummyAux;
import Reika.ChromatiCraft.Magic.ElementTagCompound;
import Reika.ChromatiCraft.Magic.Interfaces.LumenRequestingTile;
import Reika.ChromatiCraft.Magic.Interfaces.LumenTile;
import Reika.ChromatiCraft.ModInterface.Bees.ChromaBeeHelpers.ConditionalProductBee;
import Reika.ChromatiCraft.ModInterface.Bees.ChromaBeeHelpers.ConditionalProductProvider;
import Reika.ChromatiCraft.ModInterface.Bees.ProductChecks.ProductCondition;
import Reika.ChromatiCraft.ModInterface.ThaumCraft.NodeRecharger;
import Reika.ChromatiCraft.Registry.ChromaOptions;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.ChromatiCraft.TileEntity.Recipe.TileEntityChromaCrafter;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Instantiable.Data.Maps.ItemHashMap;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.DragonAPI.Libraries.IO.ReikaColorAPI;
import Reika.DragonAPI.Libraries.IO.ReikaGuiAPI;
import Reika.DragonAPI.Libraries.IO.ReikaRenderHelper;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.ModRegistry.InterfaceCache;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.AlleleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import thaumcraft.api.nodes.INode;



public class MouseoverOverlayRenderer {

	public static final MouseoverOverlayRenderer instance = new MouseoverOverlayRenderer();

	private final RenderItem itemRender = new RenderItem();

	private MouseoverOverlayRenderer() {

	}

	void renderTileOverlays(EntityPlayer ep, int gsc) {
		MovingObjectPosition pos = ReikaPlayerAPI.getLookedAtBlock(ep, 4, false);
		if (pos != null) {
			TileEntity te = ep.worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);

			if (te instanceof TileEntityDummyAux) {
				te = ((TileEntityDummyAux)te).getLinkedTile();
			}

			if (te instanceof LumenTile) {
				if (te instanceof TileEntityAdjacencyUpgrade && !ChromaOptions.POWEREDACCEL.getState())
					return;
				GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
				GL11.glPushMatrix();
				this.renderStorageOverlay(ep, gsc, (LumenTile)te);
				GL11.glPopMatrix();
				GL11.glPopAttrib();
			}
			if (te instanceof OperationInterval) {
				GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
				GL11.glPushMatrix();
				this.renderStatusOverlay(ep, gsc, (OperationInterval)te);
				GL11.glPopMatrix();
				GL11.glPopAttrib();
			}
			if (te instanceof TileEntityChromaCrafter) {
				GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
				GL11.glPushMatrix();
				this.renderItemContents(ep, gsc, (TileEntityChromaCrafter)te);
				GL11.glPopMatrix();
				GL11.glPopAttrib();
			}
			if (ModList.FORESTRY.isLoaded() && InterfaceCache.BEEHOUSE.instanceOf(te)) {
				GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
				GL11.glPushMatrix();
				this.renderConditionalBeeProductOverlay(ep, gsc, (IBeeHousing)te);
				GL11.glPopMatrix();
				GL11.glPopAttrib();
			}
			if (ModList.THAUMCRAFT.isLoaded() && InterfaceCache.NODE.instanceOf(te)) {
				if (NodeRecharger.instance.hasLocation(new WorldLocation(te))) {
					GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
					GL11.glPushMatrix();
					NodeRecharger.instance.renderNodeOverlay(ep, gsc, (INode)te);
					GL11.glPopMatrix();
					GL11.glPopAttrib();
				}
			}
		}
	}

	private void renderStatusOverlay(EntityPlayer ep, int gsc, OperationInterval te) {
		int ar = 12;
		int ox = Minecraft.getMinecraft().displayWidth/(gsc*2)+ar-8;
		int oy = Minecraft.getMinecraft().displayHeight/(gsc*2)+ar-8;
		OperationState state = te.getState();
		ReikaTextureHelper.bindTexture(ChromatiCraft.class, "Textures/infoicons.png");
		int idx = state.ordinal();
		double u = 0.125*idx;
		double v = 0.25;

		double u2 = 0.125*4;
		double v2 = 0.25;

		double s = 0.125;
		double r = 32;
		Tessellator v5 = Tessellator.instance;
		v5.startDrawingQuads();
		int sh = 3;

		v5.addVertexWithUV(ox+0-sh, oy+r+sh, 0, u2, v2+s);
		v5.addVertexWithUV(ox+r+sh, oy+r+sh, 0, u2+s, v2+s);
		v5.addVertexWithUV(ox+r+sh, oy+0-sh, 0, u2+s, v2);
		v5.addVertexWithUV(ox+0-sh, oy+0-sh, 0, u2, v2);

		v5.addVertexWithUV(ox+0, oy+r, 0, u, v+s);
		v5.addVertexWithUV(ox+r, oy+r, 0, u+s, v+s);
		v5.addVertexWithUV(ox+r, oy+0, 0, u+s, v);
		v5.addVertexWithUV(ox+0, oy+0, 0, u, v);

		v5.draw();
		if (state == OperationState.RUNNING) {
			idx = state.ordinal()+1;
			u = 0.125*idx;
			v = 0.25;
			v5.startDrawing(GL11.GL_TRIANGLE_FAN);
			v5.addVertexWithUV(ox+r/2, oy+r/2, 0, u+0.0625, v+0.0625);
			float f = te.getOperationFraction();
			double ma = 360*f;
			double da = 0.25;
			for (double a = 0; a < ma; a += da) {
				double dx = Math.sin(Math.toRadians(a+90));
				double dy = Math.cos(Math.toRadians(a+90));
				double x = ox+r/2+r/2*dx;
				double y = oy+r/2+r/2*dy;
				double du = u+0.0625+dx*s/2;
				double dv = v+0.0625+dy*s/2;
				//ReikaJavaLibrary.pConsole(a+">"+x+","+y+" @ "+du+","+dv+" from "+u+","+v);
				v5.addVertexWithUV(x, y, 0, du, dv);
			}
			v5.draw();
		}
	}

	public void renderStorageOverlay(EntityPlayer ep, int gsc, LumenTile lt) {
		ElementTagCompound tag = lt.getEnergy();
		if (lt instanceof LumenRequestingTile) {
			LumenRequestingTile lrt = (LumenRequestingTile)lt;
			tag = lrt.getRequestedTotal();
			if (tag == null)
				return;
		}
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);

		Tessellator v5 = Tessellator.instance;
		int ar = 12;
		int r = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? ar*2 : ar;
		int rb = r;
		int ox = Minecraft.getMinecraft().displayWidth/(gsc*2)-ar-8;
		int oy = Minecraft.getMinecraft().displayHeight/(gsc*2)-ar-8;

		int hash = System.identityHashCode(lt);
		double oa = hash+2*lt.getTicksExisted()*(hash%2 == 0 ? 1 : -1);

		int n = tag.tagCount();
		int i = 0;
		double angleStep = ReikaMathLibrary.isInteger(360D/n) ? 2 : 1;
		for (CrystalElement e : tag.elementSet()) {
			double min = i*360D/n;
			double max = (i+1)*360D/n;
			double maxe = lt.getMaxStorage(e);
			if (lt instanceof LumenRequestingTile) {
				maxe = ((LumenRequestingTile)lt).getRequestedTotal().getValue(e);
			}

			v5.startDrawing(GL11.GL_TRIANGLE_STRIP);
			int color = ReikaColorAPI.mixColors(e.getColor(), 0, 0.25F);
			v5.setColorOpaque_I(color);
			v5.setBrightness(240);
			for (double a = min; a <= max; a += angleStep) {
				double x = ox+r*Math.cos(Math.toRadians(oa+a));
				double y = oy+r*Math.sin(Math.toRadians(oa+a));
				//ReikaJavaLibrary.pConsole(x+", "+y);
				v5.addVertex(x, y, 0);
				v5.addVertex(ox, oy, 0);
			}
			v5.draw();

			v5.startDrawing(GL11.GL_TRIANGLE_STRIP);
			color = e.getColor();
			v5.setColorOpaque_I(color);
			v5.setBrightness(240);
			double dr = Math.min(r, r*lt.getEnergy(e)/maxe);
			for (double a = min; a <= max; a += angleStep) {
				double x = ox+dr*Math.cos(Math.toRadians(oa+a));
				double y = oy+dr*Math.sin(Math.toRadians(oa+a));
				//ReikaJavaLibrary.pConsole(x+", "+y);
				v5.addVertex(x, y, 0);
				v5.addVertex(ox, oy, 0);
			}
			v5.draw();
			i++;
		}

		float wide = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
		GL11.glLineWidth(1);
		if (n > 1) {
			v5.startDrawing(GL11.GL_LINES);
			v5.setColorOpaque_I(0x000000);
			v5.setBrightness(240);
			for (double a = 0; a < 360; a += 360D/n) {
				double x = ox+rb*Math.cos(Math.toRadians(oa+a));
				double y = oy+rb*Math.sin(Math.toRadians(oa+a));
				//ReikaJavaLibrary.pConsole(x+", "+y);
				v5.addVertex(x, y, 0);
				v5.addVertex(ox, oy, 0);
			}
			v5.draw();
		}

		v5.startDrawing(GL11.GL_LINE_LOOP);
		v5.setColorOpaque_I(0x000000);
		v5.setBrightness(240);
		for (double a = 0; a <= 360; a += 5) {
			double x = ox+r*Math.cos(Math.toRadians(oa+a));
			double y = oy+r*Math.sin(Math.toRadians(oa+a));
			//ReikaJavaLibrary.pConsole(x+", "+y);
			v5.addVertex(x, y, 0);
		}
		v5.draw();

		GL11.glLineWidth(2);
		if (n > 1) {
			v5.startDrawing(GL11.GL_LINES);
			v5.setColorRGBA_I(0x000000, 180);
			v5.setBrightness(240);
			for (double a = 0; a < 360; a += 360D/n) {
				double x = ox+rb*Math.cos(Math.toRadians(oa+a));
				double y = oy+rb*Math.sin(Math.toRadians(oa+a));
				//ReikaJavaLibrary.pConsole(x+", "+y);
				v5.addVertex(x, y, 0);
				v5.addVertex(ox, oy, 0);
			}
			v5.draw();
		}

		v5.startDrawing(GL11.GL_LINE_LOOP);
		v5.setColorRGBA_I(0x000000, 180);
		v5.setBrightness(240);
		for (double a = 0; a <= 360; a += 5) {
			double x = ox+r*Math.cos(Math.toRadians(oa+a));
			double y = oy+r*Math.sin(Math.toRadians(oa+a));
			//ReikaJavaLibrary.pConsole(x+", "+y);
			v5.addVertex(x, y, 0);
		}
		v5.draw();

		GL11.glLineWidth(wide);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glDisable(GL11.GL_BLEND);
		/*
				CrystalElement e = CrystalElement.elements[(int)(System.currentTimeMillis()/500%16)];
				int amt = tag.getValue(e);
				String s = String.format("%.0f%s", ReikaMathLibrary.getThousandBase(amt), ReikaEngLibrary.getSIPrefix(amt));
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, ox, oy+r/2, ReikaColorAPI.mixColors(e.getColor(), 0xffffff, 0.5F));
		 */

	}

	private void renderItemContents(EntityPlayer ep, int gsc, TileEntityChromaCrafter te) {
		ItemHashMap<Integer> c = te.getCurrentItems();
		if (c == null || c.isEmpty())
			return;
		int ar = 16;
		int ox = Minecraft.getMinecraft().displayWidth/(gsc*2)+ar-8;
		int oy = Minecraft.getMinecraft().displayHeight/(gsc*2)-ar-8;
		int i = 0;
		int h = 3;
		int mx = ox;
		int my = oy;
		ReikaRenderHelper.disableLighting();
		ReikaRenderHelper.disableEntityLighting();
		GL11.glColor4f(1, 1, 1, 1);
		Tessellator.instance.setBrightness(240);
		Tessellator.instance.setColorOpaque_I(0xffffff);
		for (ItemStack is : c.keySet()) {
			int x = ox+(i/h)*18;
			int y = oy-(i%h)*16;
			mx = x;
			my = Math.min(my, y);
			ReikaGuiAPI.instance.drawItemStack(itemRender, Minecraft.getMinecraft().fontRenderer, ReikaItemHelper.getSizedItemStack(is, c.get(is)), x, y);
			i++;
		}
		int berries = te.getBerryCount();
		if (berries > 0) {
			int r = 2;
			int clr = ReikaColorAPI.mixColors(0xffff0000, 0xffffff, Math.min(1, berries/(float)TileEntityChroma.ETHER_SATURATION));
			ReikaGuiAPI.instance.drawRectFrame(ox-r, my-r+1, mx-ox+18+r*2, oy-my+16+r*2, clr);
			String s = String.valueOf(berries)+" "+ChromaStacks.etherBerries.getDisplayName();
			int ds = 1;
			FontType.GUI.renderer.drawString(s, mx+19-FontType.GUI.renderer.getStringWidth(s)+ds, oy+21+ds, ReikaColorAPI.getColorWithBrightnessMultiplier(clr, 0.65F));
			FontType.GUI.renderer.drawString(s, mx+19-FontType.GUI.renderer.getStringWidth(s), oy+21, ReikaColorAPI.mixColors(clr, 0xffffffff, 0.45F));
		}
		PoolRecipe r = te.getActiveRecipe();
		if (r != null) {
			ItemStack out = r.getOutput();
			if (r.allowDoubling() && berries >= TileEntityChroma.ETHER_SATURATION) {
				out.stackSize *= 2;
			}
			String s = "Crafting: "+out.getDisplayName();
			int x = Minecraft.getMinecraft().displayWidth/(gsc*2);
			int y = Minecraft.getMinecraft().displayHeight/(gsc*2)+40;
			ReikaGuiAPI.instance.drawCenteredString(FontType.GUI.renderer, s, x-3, y, 0xffffff);
			ReikaGuiAPI.instance.drawItemStack(itemRender, FontType.GUI.renderer, out, x+FontType.GUI.renderer.getStringWidth(s)/2+3, y-4);
		}
	}

	@ModDependent(ModList.FORESTRY)
	private void renderConditionalBeeProductOverlay(EntityPlayer ep, int gsc, IBeeHousing te) {
		ItemStack queen = te.getBeeInventory().getQueen();
		if (queen != null) {
			IBee bee = (IBee)AlleleManager.alleleRegistry.getIndividual(queen);
			if (bee != null) {
				IAlleleBeeSpecies type = (IAlleleBeeSpecies)bee.getGenome().getActiveAllele(EnumBeeChromosome.SPECIES);
				if (type instanceof ConditionalProductBee) {

					int ox = Minecraft.getMinecraft().displayWidth/(gsc*2)-8;
					int oy = Minecraft.getMinecraft().displayHeight/(gsc*2)-8;

					ConditionalProductProvider p = ((ConditionalProductBee)type).getProductProvider();
					ItemHashMap<ProductCondition> map = p.getConditions();
					for (ItemStack is : map.keySet()) {
						ProductCondition c = map.get(is);

					}
				}
			}
		}
	}

}
