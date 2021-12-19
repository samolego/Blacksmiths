package org.samo_lego.blacksmiths.profession;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.samo_lego.blacksmiths.gui.RepairGUI;
import org.samo_lego.blacksmiths.inventory.RepairInventory;
import org.samo_lego.taterzens.api.professions.TaterzenProfession;
import org.samo_lego.taterzens.npc.TaterzenNPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.samo_lego.blacksmiths.Blacksmiths.MOD_ID;

public class BlacksmithProfession implements TaterzenProfession {
    public static final ResourceLocation ID = new ResourceLocation(MOD_ID, "blacksmith");

    public TaterzenNPC npc;

    private final HashMap<UUID, List<RepairInventory>> inventories = new HashMap<>();

    public BlacksmithProfession() {
    }

    public BlacksmithProfession(TaterzenNPC npc) {
        this.npc = npc;
    }


    @Override
    public InteractionResult interactAt(Player player, Vec3 pos, InteractionHand hand) {
        List<RepairInventory> invs = this.inventories.computeIfAbsent(player.getUUID(), k -> new ArrayList<>());
        new RepairGUI((ServerPlayer) player, this.npc.getName(), invs).open();
        return InteractionResult.CONSUME;
    }

    @Override
    public TaterzenProfession create(TaterzenNPC taterzen) {
        BlacksmithProfession profession = new BlacksmithProfession();
        profession.npc = taterzen;
        return profession;
    }

    @Override
    public void readNbt(CompoundTag tag) {
        ListTag listTag = (ListTag) tag.get("Inventory");
        if (listTag != null) {
            listTag.forEach((player) -> {
                CompoundTag playerTag = (CompoundTag) player;
                ListTag items = (ListTag) playerTag.get("Items");

                if (items != null) {
                    List<RepairInventory> invs = new ArrayList<>(items.size());
                    for (Tag item : items) {
                        RepairInventory inv = new RepairInventory();
                        inv.fromTag((CompoundTag) item);
                        if (!inv.peek().isEmpty())
                            invs.add(inv);
                    }
                    this.inventories.put(playerTag.getUUID("UUID"), invs);
                }

            });
        }
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        ListTag data = new ListTag();
        this.inventories.forEach((uuid, invs) -> {
           CompoundTag playerTag = new CompoundTag();
           playerTag.putUUID("UUID", uuid);

           ListTag listTag = new ListTag();
           invs.forEach((inv) -> {
               if (!inv.peek().isEmpty())
                   listTag.add(inv.toTag());
           });

           if (!invs.isEmpty()) {
               playerTag.put("Items", listTag);
               data.add(playerTag);
           }
        });
        tag.put("Inventory", data);
    }
}
