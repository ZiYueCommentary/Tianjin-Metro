package org.mtr.mapping.registry;

import org.mtr.mapping.holder.Identifier;

// This is a very hacky way but I have no choice.
public interface RegistryHelper
{
    static ItemRegistryObject RegistryObjectBlock2Item(BlockRegistryObject fabric, Identifier forge) {
        return new ItemRegistryObject(forge);
    }
}
