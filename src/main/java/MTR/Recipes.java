package MTR;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Recipes extends MTR {

	public static void addShapelessRecipes() {
		// GameRegistry.addShapelessRecipe(new ItemStack(itemtrain), new
		// Object[] { Items.compass, Items.minecart });
		GameRegistry.addShapelessRecipe(new ItemStack(blocklogo),
				new Object[] { new ItemStack(Items.dye, 1, 1), Blocks.stone });

		GameRegistry.addShapelessRecipe(new ItemStack(itempsd, 8),
				new Object[] { Blocks.glass, Items.redstone, Items.oak_door });
		GameRegistry.addShapelessRecipe(new ItemStack(itempsd, 1, 1), new Object[] { new ItemStack(itempsd, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(itempsd, 1, 2), new Object[] { new ItemStack(itempsd, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(itempsd, 1, 0), new Object[] { new ItemStack(itempsd, 1, 2) });

		GameRegistry.addShapelessRecipe(new ItemStack(itemapg, 8),
				new Object[] { Blocks.glass, Items.redstone, Blocks.oak_fence_gate });
		GameRegistry.addShapelessRecipe(new ItemStack(itemapg, 1, 1), new Object[] { new ItemStack(itemapg, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemapg, 1, 0), new Object[] { new ItemStack(itemapg, 1, 1) });

		GameRegistry.addShapelessRecipe(new ItemStack(itemmtrpane, 1, 1),
				new Object[] { new ItemStack(itemmtrpane, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemmtrpane, 1, 2),
				new Object[] { new ItemStack(itemmtrpane, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemmtrpane, 1, 3),
				new Object[] { new ItemStack(itemmtrpane, 1, 2) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemmtrpane, 1, 4),
				new Object[] { new ItemStack(itemmtrpane, 1, 3) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemmtrpane, 1, 5),
				new Object[] { new ItemStack(itemmtrpane, 1, 4) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemmtrpane, 1, 6),
				new Object[] { new ItemStack(itemmtrpane, 1, 5) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemmtrpane, 1, 7),
				new Object[] { new ItemStack(itemmtrpane, 1, 6) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemmtrpane, 1, 0),
				new Object[] { new ItemStack(itemmtrpane, 1, 7) });

		GameRegistry.addShapelessRecipe(new ItemStack(itemminecartspecial),
				new Object[] { new ItemStack(Items.minecart), new ItemStack(Items.iron_pickaxe) });

		GameRegistry.addShapelessRecipe(new ItemStack(itemlightrail1, 1, 1),
				new Object[] { new ItemStack(itemlightrail1, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemlightrail1, 1, 0),
				new Object[] { new ItemStack(itemlightrail1, 1, 1) });

		GameRegistry.addShapelessRecipe(new ItemStack(itemkilltrain),
				new Object[] { new ItemStack(Items.minecart), new ItemStack(Items.lava_bucket) });

		GameRegistry.addShapelessRecipe(new ItemStack(blockadside, 4), new Object[] { Items.sign });
		GameRegistry.addShapelessRecipe(new ItemStack(blockpids1, 2),
				new Object[] { Items.sign, Blocks.redstone_block, Items.iron_ingot });
		GameRegistry.addShapelessRecipe(new ItemStack(blockplatform), new Object[] { Blocks.stonebrick });
		GameRegistry.addShapelessRecipe(new ItemStack(blockroutechanger, 2),
				new Object[] { Blocks.stone, Blocks.redstone_block });
		GameRegistry.addShapelessRecipe(new ItemStack(blockstationnamewall1, 8),
				new Object[] { Items.sign, Items.sign });
		GameRegistry.addShapelessRecipe(new ItemStack(blockceiling, 2),
				new Object[] { Blocks.stone_slab, Blocks.torch });

		GameRegistry.addShapelessRecipe(new ItemStack(blockmtrmap), new Object[] { new ItemStack(blockmtrmapside) });
		GameRegistry.addShapelessRecipe(new ItemStack(blockmtrmapside), new Object[] { new ItemStack(blockmtrmap) });

		GameRegistry.addShapelessRecipe(new ItemStack(itemescalator),
				new Object[] { Blocks.stone_stairs, Blocks.piston, Blocks.redstone_block });
		GameRegistry.addShapelessRecipe(new ItemStack(itemescalator, 1, 1),
				new Object[] { new ItemStack(itemescalator, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemescalator, 1, 2),
				new Object[] { new ItemStack(itemescalator, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemescalator, 1, 3),
				new Object[] { new ItemStack(itemescalator, 1, 2) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemescalator, 1, 4),
				new Object[] { new ItemStack(itemescalator, 1, 3) });
		GameRegistry.addShapelessRecipe(new ItemStack(itemescalator, 1, 0),
				new Object[] { new ItemStack(itemescalator, 1, 4) });
		GameRegistry.addShapelessRecipe(new ItemStack(blockescalatorlanding),
				new Object[] { Blocks.stone_slab, Blocks.iron_block, Blocks.redstone_block });
	}

	public static void addShapedRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(blocklogo, 8), "xxx", "xyx", "xxx", 'x', Blocks.stone, 'y',
				new ItemStack(Items.dye, 1, 1));
		GameRegistry.addShapedRecipe(new ItemStack(blockclock, 16), " x ", "xyx", " x ", 'x', Items.iron_ingot, 'y',
				Items.clock);
		GameRegistry.addShapedRecipe(new ItemStack(blockmtrmap, 16), " x ", "xyx", " x ", 'x', Items.iron_ingot, 'y',
				Items.map);
		GameRegistry.addShapedRecipe(new ItemStack(blockrubbishbin1, 8), "x x", "x x", "xxx", 'x', Items.iron_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(blockticketmachine, 4), "xxx", "xyx", "xxx", 'x', Items.iron_ingot,
				'y', Items.map);
		GameRegistry.addShapedRecipe(new ItemStack(blocktraintimer, 2), "   ", "zyz", "xxx", 'x', Blocks.stone, 'y',
				Items.compass, 'z', Blocks.redstone_torch);

		GameRegistry.addShapedRecipe(new ItemStack(itemrail, 1, 0), " x ", " x ", " x ", 'x', Blocks.rail);
		GameRegistry.addShapedRecipe(new ItemStack(itemrail, 1, 1), " x ", " x ", " x ", 'x', Blocks.golden_rail);
		GameRegistry.addShapedRecipe(new ItemStack(itemrail, 1, 2), "  x", " xx", "xxx", 'x', Blocks.rail);
		GameRegistry.addShapedRecipe(new ItemStack(itemrail, 1, 3), "xxx", "xx ", "x  ", 'x', Blocks.rail);
		GameRegistry.addShapedRecipe(new ItemStack(itemrail, 1, 4), " x ", " x ", " x ", 'x', Blocks.detector_rail);
		GameRegistry.addShapedRecipe(new ItemStack(itemrail, 1, 5), " x ", "yxy", " x ", 'x', Blocks.rail, 'y',
				Items.redstone);

		GameRegistry.addShapedRecipe(new ItemStack(itemlightrail1), "x  ", "   ", "   ", 'x',
				new ItemStack(Items.minecart));
		GameRegistry.addShapedRecipe(new ItemStack(itemsp1900), " x ", "   ", "   ", 'x',
				new ItemStack(Items.minecart));

		GameRegistry.addShapedRecipe(new ItemStack(itembrush), "x", "y", 'x', Blocks.wool, 'y', Items.stick);

		GameRegistry.addShapedRecipe(new ItemStack(itemmtrpane, 1, 0), "x  ", "   ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(itemmtrpane, 1, 1), " x ", "   ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(itemmtrpane, 1, 2), "  x", "   ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(itemmtrpane, 1, 3), "   ", "x  ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(itemmtrpane, 1, 4), "   ", " x ", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(itemmtrpane, 1, 5), "   ", "  x", "   ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(itemmtrpane, 1, 6), "   ", "   ", "x  ", 'x',
				new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapedRecipe(new ItemStack(itemmtrpane, 1, 7), "   ", "   ", " x ", 'x',
				new ItemStack(Blocks.glass_pane));
	}
}
