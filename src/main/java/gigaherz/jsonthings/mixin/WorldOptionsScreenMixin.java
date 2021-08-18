package gigaherz.jsonthings.mixin;

import gigaherz.jsonthings.things.parsers.ThingResourceManager;
import net.minecraft.client.gui.screens.worldselection.WorldGenSettingsComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldGenSettingsComponent.class)
public class WorldOptionsScreenMixin
{
    @Redirect(method = "lambda$init$10(Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/components/Button;)V",
            at = @At(value = "NEW", target = "(Lnet/minecraft/server/packs/PackType;[Lnet/minecraft/server/packs/repository/RepositorySource;)Lnet/minecraft/server/packs/repository/PackRepository;")
    )
    public PackRepository redirectPackListCreation(PackType type, RepositorySource... finders)
    {
        PackRepository list = new PackRepository(type, finders);
        list.addPackFinder(ThingResourceManager.INSTANCE.getWrappedPackFinder());
        return list;
    }
}
