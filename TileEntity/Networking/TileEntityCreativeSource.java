/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.TileEntity.Networking;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import Reika.ChromatiCraft.Base.TileEntity.CrystalTransmitterBase;
import Reika.ChromatiCraft.Magic.ElementTagCompound;
import Reika.ChromatiCraft.Magic.Interfaces.CrystalReceiver;
import Reika.ChromatiCraft.Magic.Interfaces.CrystalSource;
import Reika.ChromatiCraft.Registry.ChromaTiles;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper;

public class TileEntityCreativeSource extends CrystalTransmitterBase implements CrystalSource {

	@Override
	public int getSendRange() {
		return 48;
	}

	@Override
	public boolean needsLineOfSight() {
		return true;
	}

	@Override
	public boolean isConductingElement(CrystalElement e) {
		return true;
	}

	@Override
	public int maxThroughput() {
		return 10000;
	}

	@Override
	public boolean canConduct() {
		return true;
	}

	@Override
	public int getEnergy(CrystalElement e) {
		return 1000000;
	}

	@Override
	public ElementTagCompound getEnergy() {
		return ElementTagCompound.getUniformTag(1000000);
	}

	@Override
	public int getMaxStorage(CrystalElement e) {
		return 1000000;
	}

	@Override
	public int getTransmissionStrength() {
		return 500;
	}

	@Override
	public boolean drain(CrystalElement e, int amt) {
		return true;
	}

	@Override
	public int getSourcePriority() {
		return 0;
	}

	@Override
	public ChromaTiles getTile() {
		return ChromaTiles.CREATIVEPYLON;
	}

	@Override
	public boolean canTransmitTo(CrystalReceiver te) {
		return true;
	}

	@Override
	public boolean canSupply(CrystalReceiver te) {
		EntityPlayer ep = this.getPlacer();
		UUID other = te.getPlacerUUID();
		if (DragonAPICore.isReikasComputer() && ReikaObfuscationHelper.isDeObfEnvironment())
			return true;
		return ep != null && other != null && ep.getUniqueID().equals(other) && ep.capabilities.isCreativeMode;
	}

	@Override
	public void onUsedBy(EntityPlayer ep, CrystalElement e) {

	}

	@Override
	public boolean playerCanUse(EntityPlayer ep) {
		return ep.capabilities.isCreativeMode;
	}

}
