package org.vertex.bukkit.gui;

import lombok.Getter;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.vertex.bukkit.protocol.Protocol;

import java.util.List;

public abstract class PaginatedContainer {

    @Getter private boolean keepOpen;
    @Getter private List<Container> pages;
    @Getter private int index = 0;
    @Getter private boolean loop;
    @Getter private Player holder;

    public void next() {
        if (index + 1 > pages.size()) {
            if(loop) {
                index = 0;
                return;
            }
            index = pages.size() - 1;
            return;
        }
        index = index - 1;
    }

    public void previous() {
        if (index - 1 < 0) {
            if(loop) {
                index = pages.size() - 1;
                return;
            }
            index = 0;
            return;
        }
        index = index - 1;
    }

    public Container getCurrentPage() {
        return pages.get(index);
    }

    public void openCurrentPage() {
        this.pages = buildPages();
        if(keepOpen) {

            EntityPlayer ep = ((CraftPlayer)holder).getHandle();
            Protocol.UpdateScreen.builder()
                    .containerId(ep.bR.j)
                    .title("")
                    .items(null).build().send(holder);

            holder.updateInventory();
        }
        this.holder.closeInventory();
        this.holder.openInventory(this.getCurrentPage().getInventory());
    }

    public abstract List<Container> buildPages();

}