/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Render.TESR.Dimension;

import java.util.Collection;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;

import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Base.ChromaRenderBase;
import Reika.ChromatiCraft.Block.Dimension.Structure.Water.BlockRotatingLock.TileEntityRotatingLock;
import Reika.ChromatiCraft.Models.ModelWaterLock;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import Reika.DragonAPI.Interfaces.TileEntity.RenderFetcher;
import Reika.DragonAPI.Libraries.ReikaDirectionHelper;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.Java.ReikaGLHelper.BlendMode;
import Reika.DragonAPI.Libraries.Rendering.ReikaColorAPI;
import Reika.DragonAPI.Libraries.Rendering.ReikaRenderHelper;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;


public class RenderWaterLock extends ChromaRenderBase {

	private final ModelWaterLock model = new ModelWaterLock();

	@Override
	public String getImageFileName(RenderFetcher te) {
		return null;
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
		TileEntityRotatingLock te = (TileEntityRotatingLock)tile;
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glPushMatrix();
		GL11.glTranslated(par2, par4+0.95, par6);

		if (MinecraftForgeClient.getRenderPass() == 0) {
			this.bindTextureByName(this.getTextureFolder()+"waterlock.png");
			this.preRenderModel();
			//double s = 0.85; //clipping avoidance
			//GL11.glScaled(s, s, s);
			Collection<ForgeDirection> li = ReikaJavaLibrary.makeSetFromArray(ForgeDirection.VALID_DIRECTIONS);
			li.remove(ForgeDirection.UP);
			li.remove(ForgeDirection.DOWN);
			li.removeAll(te.getOpenEndsForRender());
			for (ForgeDirection dir : li) {
				GL11.glPushMatrix();
				GL11.glRotated(ReikaDirectionHelper.getHeading(dir)+te.getRotationProgress(), 0, 1, 0);
				model.renderChannel(te);
				GL11.glPopMatrix();
			}
			for (int i = 2; i < 6; i++) {
				GL11.glPushMatrix();
				GL11.glRotated(ReikaDirectionHelper.getHeading(ForgeDirection.VALID_DIRECTIONS[i])+te.getRotationProgress(), 0, 1, 0);
				model.renderSegment(te);
				GL11.glPopMatrix();
			}
			this.postRenderModel(tile);
		}

		if ((te.isCheckpoint() || te.isEndpoint()) && MinecraftForgeClient.getRenderPass() == 1) {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(false);
			ReikaRenderHelper.disableEntityLighting();
			BlendMode.ADDITIVEDARK.apply();
			GL11.glDisable(GL11.GL_CULL_FACE);

			GL11.glTranslated(0.5, 0, 0.5);
			double s = 2;
			GL11.glScaled(s, s, s);
			ReikaTextureHelper.bindTexture(ChromatiCraft.class, "Textures/lightcone-foggy.png");
			long tick = Minecraft.getMinecraft().theWorld.getTotalWorldTime()+te.hashCode();
			int idx = (int)(tick%48);
			double u = (idx%12)/12D;
			double v = (idx/12)/4D;
			double du = u+1/12D;
			double dv = v+1/4D;
			Tessellator v5 = Tessellator.instance;

			double h = 2.5;
			double f = 0.5+0.5*Math.sin(tick/8D)+0.125*Math.sin((tick+300)/3D)+0.25*Math.cos((tick+20)/7D);
			int c1;
			int c2;
			if (te.isEndpoint()) {
				c1 = 0xffff00;
				c2 = 0xaaaa44;
			}
			else {
				boolean fluid = te.worldObj.getBlock(te.xCoord, te.yCoord+1, te.zCoord) == ChromaBlocks.EVERFLUID.getBlockInstance();
				c1 = fluid ? 0x00ff00 : 0xff0000;//0x22aaff;
				c2 = fluid ? 0x44aa44 : 0xaa4444;//0x5588ff;
			}
			int c = ReikaColorAPI.mixColors(c1, c2, (float)MathHelper.clamp_double(f, 0, 1));

			//for (double a = 0; a < 360; a += 120) {
			GL11.glPushMatrix();
			//GL11.glRotated(a, 0, 1, 0);
			GL11.glRotated(-RenderManager.instance.playerViewY, 0, 1, 0);
			v5.startDrawingQuads();
			v5.setColorOpaque_I(c);
			v5.addVertexWithUV(-0.5, h, 0, u, v);
			v5.addVertexWithUV(0.5, h, 0, du, v);
			v5.addVertexWithUV(0.5, 0, 0, du, dv);
			v5.addVertexWithUV(-0.5, 0, 0, u, dv);
			v5.draw();
			GL11.glPopMatrix();
		}

		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

}
