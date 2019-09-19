/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Registry;

import Reika.DragonAPI.Auxiliary.PacketTypes;

public enum ChromaPackets {

	ENCHANTER(2),
	ENCHANTERRESET(),
	SPAWNERPROGRAM(1),
	SPAWNERDATA(6),
	CRYSTALEFFECT(),
	PLANTUPDATE(),
	ABILITY(2),
	ABILITYSEND(3),
	PYLONATTACK(6),
	PYLONATTACKRECEIVE(1),
	ABILITYCHOOSE(1),
	BUFFERSET(2),
	//BUFFERINC(1),
	TELEPUMP(1),
	//TRANSMIT(3),
	ASPECT(),
	LAMPCHANNEL(1),
	LAMPCONTROL(2),
	LAMPINVERT(),
	TNT(4),
	BOOKINVSCROLL(1),
	TICKER(1),
	SHARDBOOST(1),
	GIVERESEARCH(1),
	LEAFBREAK(1),
	GIVEPROGRESS(2),
	HEALTHSYNC(2),
	INVCYCLE(1),
	RELAYCONNECT(),
	RERESEARCH(1),
	BIOMEPAINT(3),
	LIGHTNINGDIE(1),
	CLOUDDIE(1),
	CLOUDATTACK(1),
	GLUON(2),
	AURAPOUCH(2),
	FARMERHARVEST(3),
	PYLONCACHE(-1),
	PYLONLINKCACHE(-1),
	PYLONCACHECLEAR(1),
	TRANSITIONWAND(1),
	TELEPORT(),
	MAPTELEPORT(4),
	NEWTELEPORT(),
	DELTELEPORT(),
	SENDTELEPORT(4),
	GROWTH(3),
	PROGRESSNOTE(1),
	PORTALRECIPE(1),
	PYLONTURBORECIPE(1),
	REPEATERTURBORECIPE(1),
	HEATLAMP(1),
	WANDCHARGE(16),
	BULKITEM(2),
	BULKNUMBER(1),
	CASTAUTOUPDATE(7),
	AUTORECIPE(1),
	AUTOCANCEL(),
	AUTORECIPEPRIORITY(),
	AUTORECURSE(),
	CHAINGUNHURT(1),
	CHAINGUNEND(1),
	METRANSFER(2),
	MEDISTRIBTHRESH(2),
	MEDISTRIBFUZZY(1),
	HOVERWAND(1),
	AURATTACK(1),
	AURAHEAL(1),
	AURAGROW(0),
	DESTROYNODE(),
	HURTNODE(),
	CHARGINGNODE(),
	NEWASPECTNODE(17),
	HEALNODE(),
	SPLASHGUNEND(1),
	SPLASHGUNATTACK(2),
	VACUUMGUNEND(1),
	RFSEND(4),
	DIMPING(3),
	STRUCTUREENTRY(1),
	CRYSTALMUS(4),
	CRYSTALMUSERROR(),
	MUSICNOTE(4),
	FIXEDMUSICNOTE(5),
	MUSICCLEAR(),
	MUSICCLEARCHANNEL(1),
	MUSICDEMO(),
	MUSICBKSP(1),
	//MUSICDISC(),
	PYLONTURBOSTART(),
	PYLONTURBOEVENT(2),
	PYLONTURBOCOMPLETE(),
	PYLONTURBOFAIL(1),
	MUSICPLAY(1),
	TURRETATTACK(1),
	MONUMENTSTART(),
	MONUMENTEVENT(6),
	MONUMENTCOMPLETE(3),
	RESETMONUMENT(3),
	MONUMENTEND(),
	DASH(1),
	FENCETRIGGER(2),
	//MAZEDISTREQ(),
	//MAZEDISTINFO(1);
	MINERJAM(),
	REPEATERCONN(),
	CHARGERTOGGLE(1),
	BOOKNOTESRESET(),
	BOOKNOTE(),
	REPEATERSURGE(1),
	FIREDUMP(1),
	FIREDUMPSHOCK(3),
	FIRECONSUMEITEM(1),
	ESSENTIAPARTICLE(7),
	INSERTERMODE(1),
	INSERTERCLEAR(1),
	INSERTERCONNECTION(2),
	INSERTERACTION(5),
	COBBLEGENEND(2),
	LIGHTERACT(3),
	LIGHTERDELTAY(1),
	LIGHTEREND(),
	POWERCRYSDESTROY(),
	PARTICLESPAWNER(),
	//PYLONJAR(),
	PYLONCRYSTALBREAK(),
	WIRELESS(5),
	METEORIMPACT(1),
	ORECREATE(2),
	THROWNGEM(2),
	FLAREMSG(),
	FLAREATTACK(1),
	ASPECTMODE(),
	FLUIDSEND(5),
	COLLECTORRANGE(2),
	LEAVEDIM(),
	DIMSOUND(),
	SKYRIVER_SYNC(),
	SKYRIVER_STATE(1),
	ACTIVEGATE(),
	GATECACHE(),
	TRIGGERTELEPORT(8),
	TELEPORTCONFIRM(1),
	BIOMELOCS(),
	RELAYPRESSUREBASE(1),
	RELAYPRESSUREVAR(1),
	RELAYCOPY(),
	RELAYCLEAR(),
	RELAYAUTO(),
	RELAYFLUID(1),
	RELAYFILTER(2),
	ROUTERFILTERFLAG(1),
	ROUTERLINK(3),
	STRUCTFIND(2),
	DATASCAN(),
	LORENOTE(1),
	LOREPUZZLECOMPLETE(),
	INSCRIBE(1),
	TOWERLOC(),
	//DIGARTEFACT(3),
	//ARTEFACTCONFIRM(3),
	ARTEFACTCLICK(),
	FERTILITYSEED(),
	NUKERLOC(4),
	BURNERINV(1),
	BROADCASTLINK(1),
	SPELLFAIL(),
	UAFX(),
	STRUCTPASSNOTE(1),
	MINERCATEGORY(1),
	BOTTLENECK(7),
	VOIDMONSTERRITUAL(2),
	ALVEARYEFFECT(2),
	SUPERBUILD(1),
	OPTIMIZE(1),
	ENDEREYESYNC(2),
	NODERECEIVERSYNC(),
	RFWEBSEND(4),
	STRUCTSEED(2),
	RAYBLENDPING(1),
	RAYBLENDMIX(2, PacketTypes.POS),
	CONNECTIVITY(3),
	STRUCTUREERROR(2),
	CLEARHOVERBOX(),
	;

	public final int numInts;
	public final PacketTypes type;

	private ChromaPackets() {
		this(0);
	}

	private ChromaPackets(int size) {
		this(size, PacketTypes.DATA);
	}

	private ChromaPackets(int size, PacketTypes t) {
		numInts = size;
		type = t;
	}

	public static final ChromaPackets getPacket(int id) {
		ChromaPackets[] list = values();
		id = Math.max(0, Math.min(id, list.length-1));
		return list[id];
	}

	public boolean hasData() {
		return numInts != 0;
	}

	public boolean variableData() {
		return numInts < 0;
	}

}
