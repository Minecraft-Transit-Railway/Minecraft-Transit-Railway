package MTR;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPSDDoor extends BlockDoorBase {

	private static final String name = "BlockPSDDoor";

	protected BlockPSDDoor() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	public String getName() {
		return name;
	}
}