/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Blocks;

import java.util.Collection;

import Reika.ChromatiCraft.Auxiliary.Interfaces.CoreRecipe;
import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.TempleCastingRecipe;
import Reika.ChromatiCraft.Magic.Progression.ProgressStage;
import Reika.ChromatiCraft.Registry.CrystalElement;

public class EnhancedRuneRecipe extends TempleCastingRecipe implements CoreRecipe {

	public EnhancedRuneRecipe(CrystalElement e) {
		super(RuneRecipe.genOutput(e), RuneRecipe.getRecipe(e, true));
		this.addRuneRingRune(e);
	}

	@Override
	public int getExperience() {
		return 4*super.getExperience();
	}

	@Override
	public void getRequiredProgress(Collection<ProgressStage> c) {
		super.getRequiredProgress(c);
		c.add(ProgressStage.ALLCOLORS);
	}

	@Override
	public int getNumberProduced() {
		return 8;
	}

	@Override
	public boolean canGiveDoubleOutput() {
		return true;
	}

}
