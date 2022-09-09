package ziyue.tjmetro.packet;

import mtr.MTR;
import net.minecraft.resources.ResourceLocation;
import ziyue.tjmetro.Reference;

public interface IPacket
{
    ResourceLocation PACKET_OPEN_CUSTOM_CONTENT_SCREEN = new ResourceLocation(Reference.MOD_ID, "packet_open_custom_content_screen");
    ResourceLocation PACKET_UPDATE_CUSTOM_CONTENT = new ResourceLocation(Reference.MOD_ID, "packet_update_custom_content_screen");
    ResourceLocation PACKET_OPEN_CUSTOM_COLOR_SCREEN = new ResourceLocation(Reference.MOD_ID, "packet_open_custom_color_screen");
    ResourceLocation PACKET_UPDATE_CUSTOM_COLOR = new ResourceLocation(Reference.MOD_ID, "packet_update_custom_color_screen");
    ResourceLocation PACKET_OPEN_RAILWAY_SIGN_SCREEN = new ResourceLocation(Reference.MOD_ID, "packet_open_railway_sign_screen");
    ResourceLocation PACKET_SIGN_TYPES = new ResourceLocation(Reference.MOD_ID, "packet_sign_types");
}
