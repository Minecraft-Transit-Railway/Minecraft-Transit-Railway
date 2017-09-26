package MTR;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Recipes {

	public static void addShapelessRecipes() {
		// GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemtrain), new
		// Object[] { Items.compass, Items.MINECART });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blocklogo),
				new Object[] { new ItemStack(Items.DYE, 1, 1), Blocks.STONE });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itempsd, 8),
				new Object[] { Blocks.GLASS, Items.REDSTONE, Items.OAK_DOOR });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itempsd, 1, 1),
				new Object[] { new ItemStack(MTRItems.itempsd, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itempsd, 1, 2),
				new Object[] { new ItemStack(MTRItems.itempsd, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itempsd, 1, 0),
				new Object[] { new ItemStack(MTRItems.itempsd, 1, 2) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemapg, 8),
				new Object[] { Blocks.GLASS, Items.REDSTONE, Blocks.OAK_FENCE_GATE });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemapg, 1, 1),
				new Object[] { new ItemStack(MTRItems.itemapg, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemapg, 1, 0),
				new Object[] { new ItemStack(MTRItems.itemapg, 1, 1) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 1),
				new Object[] { new ItemStack(MTRItems.itemmtrpane, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 2),
				new Object[] { new ItemStack(MTRItems.itemmtrpane, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 3),
				new Object[] { new ItemStack(MTRItems.itemmtrpane, 1, 2) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 4),
				new Object[] { new ItemStack(MTRItems.itemmtrpane, 1, 3) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 5),
				new Object[] { new ItemStack(MTRItems.itemmtrpane, 1, 4) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 6),
				new Object[] { new ItemStack(MTRItems.itemmtrpane, 1, 5) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 7),
				new Object[] { new ItemStack(MTRItems.itemmtrpane, 1, 6) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 0),
				new Object[] { new ItemStack(MTRItems.itemmtrpane, 1, 7) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemminecartspecial),
				new Object[] { new ItemStack(Items.MINECART), new ItemStack(Items.IRON_PICKAXE) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemlightrail1, 1, 1),
				new Object[] { new ItemStack(MTRItems.itemlightrail1, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemlightrail1, 1, 0),
				new Object[] { new ItemStack(MTRItems.itemlightrail1, 1, 1) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemsp1900, 1, 3),
				new Object[] { new ItemStack(MTRItems.itemsp1900, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemsp1900, 1, 0),
				new Object[] { new ItemStack(MTRItems.itemsp1900, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemsp1900, 1, 1),
				new Object[] { new ItemStack(MTRItems.itemsp1900, 1, 2) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemsp1900, 1, 2),
				new Object[] { new ItemStack(MTRItems.itemsp1900, 1, 3) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemkilltrain),
				new Object[] { new ItemStack(Items.MINECART), new ItemStack(Items.LAVA_BUCKET) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockadside, 4), new Object[] { Items.SIGN });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockpids1, 2),
				new Object[] { Items.SIGN, Blocks.REDSTONE_BLOCK, Items.IRON_INGOT });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockplatform), new Object[] { Blocks.STONEBRICK });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockroutechanger, 2),
				new Object[] { Blocks.STONE, Blocks.REDSTONE_BLOCK });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockstationnamewall1, 8),
				new Object[] { Items.SIGN, Items.SIGN });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockceiling, 2),
				new Object[] { Blocks.STONE_SLAB, Blocks.TORCH });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockmtrmap),
				new Object[] { new ItemStack(MTRBlocks.blockmtrmapside) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockmtrmapside),
				new Object[] { new ItemStack(MTRBlocks.blockmtrmap) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockrubbishbin1, 8),
				new Object[] { new ItemStack(Items.CAULDRON) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockgoldenrepeateroff, 8),
				new Object[] { new ItemStack(Items.REPEATER), new ItemStack(Items.REPEATER),
						new ItemStack(Items.REPEATER), new ItemStack(Items.REPEATER), new ItemStack(Items.REPEATER),
						new ItemStack(Items.REPEATER), new ItemStack(Items.REPEATER), new ItemStack(Items.REPEATER),
						new ItemStack(Items.GOLD_NUGGET) });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemescalator),
				new Object[] { Blocks.STONE_STAIRS, Blocks.PISTON, Blocks.REDSTONE_BLOCK });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemescalator, 1, 1),
				new Object[] { new ItemStack(MTRItems.itemescalator, 1, 0) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemescalator, 1, 2),
				new Object[] { new ItemStack(MTRItems.itemescalator, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemescalator, 1, 3),
				new Object[] { new ItemStack(MTRItems.itemescalator, 1, 2) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemescalator, 1, 4),
				new Object[] { new ItemStack(MTRItems.itemescalator, 1, 3) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRItems.itemescalator, 1, 0),
				new Object[] { new ItemStack(MTRItems.itemescalator, 1, 4) });
		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockescalatorlanding, 8),
				new Object[] { Blocks.STONE_SLAB, Blocks.IRON_BLOCK, Blocks.REDSTONE_BLOCK });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockmtrsignpost, 16),
				new Object[] { Blocks.IRON_BARS, Items.IRON_INGOT });

		GameRegistry.addShapelessRecipe(new ItemStack(MTRBlocks.blockrailswitch, 16),
				new Object[] { MTRItems.itemrail, Blocks.LEVER });
	}

	public static void addShapedRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(MTRBlocks.blocklogo, 8), "xxx", "xyx", "xxx", 'x', Blocks.STONE, 'y',
				new ItemStack(Items.DYE, 1, 1));
		GameRegistry.addShapedRecipe(new ItemStack(MTRBlocks.blockclock, 16), " x ", "xyx", " x ", 'x',
				Items.IRON_INGOT, 'y', Items.CLOCK);
		GameRegistry.addShapedRecipe(new ItemStack(MTRBlocks.blockmtrmap, 16), " x ", "xyx", " x ", 'x',
				Items.IRON_INGOT, 'y', Items.MAP);
		GameRegistry.addShapedRecipe(new ItemStack(MTRBlocks.blockticketmachine, 4), "xxx", "xyx", "xxx", 'x',
				Items.IRON_INGOT, 'y', Items.MAP);
		GameRegistry.addShapedRecipe(new ItemStack(MTRBlocks.blocktraintimer, 2), "   ", "zyz", "xxx", 'x',
				Blocks.STONE, 'y', Items.COMPASS, 'z', Blocks.REDSTONE_TORCH);

		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemrail, 1, 0), " x ", " x ", " x ", 'x', Blocks.RAIL);
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemrail, 1, 1), " x ", " x ", " x ", 'x',
				Blocks.GOLDEN_RAIL);
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemrail, 1, 2), "  x", " xx", "xxx", 'x', Blocks.RAIL);
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemrail, 1, 3), "xxx", "xx ", "x  ", 'x', Blocks.RAIL);
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemrail, 1, 4), " x ", " x ", " x ", 'x',
				Blocks.DETECTOR_RAIL);
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemrail, 1, 5), " x ", "yxy", " x ", 'x', Blocks.RAIL, 'y',
				Items.REDSTONE);
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemrail, 1, 6), " x ", "x x", " x ", 'x', Blocks.RAIL);
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemrail, 1, 7), " x ", "xxx", " x ", 'x', Blocks.RAIL);

		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemlightrail1), "x  ", "   ", "   ", 'x',
				new ItemStack(Items.MINECART));
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemsp1900), " x ", "   ", "   ", 'x',
				new ItemStack(Items.MINECART));

		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itembrush), "x", "y", 'x', Blocks.WOOL, 'y', Items.STICK);

		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 0), "x  ", "   ", "   ", 'x',
				new ItemStack(Blocks.GLASS_PANE));
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 1), " x ", "   ", "   ", 'x',
				new ItemStack(Blocks.GLASS_PANE));
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 2), "  x", "   ", "   ", 'x',
				new ItemStack(Blocks.GLASS_PANE));
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 3), "   ", "x  ", "   ", 'x',
				new ItemStack(Blocks.GLASS_PANE));
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 4), "   ", " x ", "   ", 'x',
				new ItemStack(Blocks.GLASS_PANE));
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 5), "   ", "  x", "   ", 'x',
				new ItemStack(Blocks.GLASS_PANE));
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 6), "   ", "   ", "x  ", 'x',
				new ItemStack(Blocks.GLASS_PANE));
		GameRegistry.addShapedRecipe(new ItemStack(MTRItems.itemmtrpane, 1, 7), "   ", "   ", " x ", 'x',
				new ItemStack(Blocks.GLASS_PANE));
	}
}
