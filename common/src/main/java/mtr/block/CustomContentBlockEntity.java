package mtr.block;

import mtr.mappings.BlockEntityClientSerializableMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CustomContentBlockEntity extends BlockEntityClientSerializableMapper
{
    public final float yOffset;
    public final float zOffset;

    public final String CONTENT_ID = "content";
    public String content = "";

    public CustomContentBlockEntity(BlockEntityType<?> entity, BlockPos pos, BlockState state, float yOffset, float zOffset) {
        super(entity, pos, state);
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    @Override
    public void readCompoundTag(CompoundTag compoundTag) {
        content = compoundTag.getString(CONTENT_ID);
        super.readCompoundTag(compoundTag);
    }

    @Override
    public void writeCompoundTag(CompoundTag compoundTag) {
        compoundTag.putString(CONTENT_ID, content);
        super.writeCompoundTag(compoundTag);
    }

    public void setData(String content) {
        this.content = content;
        setChanged();
        syncData();
    }

    public boolean shouldRender() {
        return true;
    }
}
