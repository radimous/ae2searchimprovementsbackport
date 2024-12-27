package com.radimous.ae2searchimprovementsbackport.mixin;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.widgets.AETextField;
import appeng.core.localization.GuiText;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = MEStorageScreen.class, remap = false)
public class MixinMEStorageScreen {

    @Redirect(method = "updateSearch", at = @At(value = "INVOKE", target = "Lappeng/client/gui/widgets/AETextField;setTooltipMessage(Ljava/util/List;)V"))
    private void newTooltipMessage(AETextField instance, List<Component> tooltipMessage) {
        List<Component> newtt = List.of(
            GuiText.SearchTooltip.text(),
            GuiText.SearchTooltipModId.text(),
            new TranslatableComponent("gui.ae2searchimprovements.SearchTooltipTag"),
            new TranslatableComponent("gui.ae2searchimprovements.SearchTooltipToolTips"),
            GuiText.SearchTooltipItemId.text()
        );
        instance.setTooltipMessage(newtt);
    }

}
