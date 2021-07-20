package mtr.mixin;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelPredicateProviderRegistry.class)
public interface ModelPredicateRegisterInvoker {

	@Invoker("register")
	static void invokeRegister(Item item, Identifier id, UnclampedModelPredicateProvider provider) {
	}
}
