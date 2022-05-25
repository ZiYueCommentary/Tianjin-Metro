package ziyue.tjmetro.packet;

import net.minecraft.resources.ResourceLocation;
import ziyue.tjmetro.Reference;

public interface IPacket
{
    ResourceLocation PACKET_OPEN_CUSTOM_CONTENT_SCREEN = new ResourceLocation(Reference.MOD_ID, "packet_open_custom_content_screen");
    ResourceLocation PACKET_UPDATE_CUSTOM_CONTENT = new ResourceLocation(Reference.MOD_ID, "packet_update_custom_screen");
}
