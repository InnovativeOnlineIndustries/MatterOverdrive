package com.hrznstudio.matteroverdrive.util;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class MOJsonHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger("MO:JsonHelper");

    /**
     * Credit for the following Methods goes to Darkhax and his library mod Bookshelf <3
     *
     * We just stole this from Essence :) Which is Lanse's mod so is it really stealing?
     */
    public static Object getRegistryEntry(JsonElement json, String memberName, IForgeRegistry registry) {
        if (json == null) {
            throw new JsonSyntaxException("The property " + memberName + " is missing.");
        }
        if (json.isJsonPrimitive()) {
            final String rawId = json.getAsString();
            final ResourceLocation registryId = ResourceLocation.tryParse(rawId);
            if (registryId != null) {
                final Object registryEntry = registry.getValue(registryId);
                if (registryEntry != null) {
                    return registryEntry;
                } else {
                    throw new JsonSyntaxException("No entry found for id " + rawId);
                }
            } else {
                throw new JsonSyntaxException("Registry id " + rawId + " for property " + memberName + " was not a valid format.");
            }
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be a JSON primitive. was " + GsonHelper.getType(json));
        }
    }

    public static Block getBlock(JsonObject json, String memberName) {
        return (Block) getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.BLOCKS);
    }

    public static MobEffect getMobEffect(JsonObject json, String memberName) {
        return (MobEffect) getRegistryEntry(json.get(memberName), memberName, ForgeRegistries.MOB_EFFECTS);
    }

    public static JsonElement serializeBlockState(BlockState state) {
        final JsonObject object = new JsonObject();
        object.addProperty("block", ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString());
        final JsonObject propertiesElement = new JsonObject();
        ImmutableMap<Property<?>, Comparable<?>> map = state.getValues();
        for (Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {
            propertiesElement.addProperty(entry.getKey().getName(), entry.getValue().toString());
        }
        object.add("properties", propertiesElement);
        return object;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BlockState deserializeBlockState(JsonObject json) {
        // Read the block from the forge registry.
        final Block block = getBlock(json, "block");
        // Start off with the default state.
        BlockState state = block.defaultBlockState();
        // If the properties member exists, attempt to assign properties to the block state.
        if (json.has("properties")) {
            final JsonElement propertiesElement = json.get("properties");
            if (propertiesElement.isJsonObject()) {
                final JsonObject props = propertiesElement.getAsJsonObject();
                // Iterate each member of the properties object. Expecting a simple key to
                // primitive string structure.
                for (final Map.Entry<String, JsonElement> property : props.entrySet()) {
                    // Check the block for the property. Keys = property names.
                    final Property blockProperty = block.getStateDefinition().getProperty(property.getKey());
                    if (blockProperty != null) {
                        if (property.getValue().isJsonPrimitive()) {
                            // Attempt to parse the value with the the property.
                            final String valueString = property.getValue().getAsString();
                            final Optional<Comparable> propValue = blockProperty.getValue(valueString);
                            if (propValue.isPresent()) {
                                // Update the state with the new property.
                                try {
                                    state = state.setValue(blockProperty, propValue.get());
                                } catch (final Exception e) {
                                    LOGGER.error("Failed to update state for block {}. The mod that adds this block has issues.", ForgeRegistries.BLOCKS.getKey(block).toString());
                                    LOGGER.error(e.toString());
                                }
                            } else {
                                throw new JsonSyntaxException("The property " + property.getKey() + " with value " + valueString + " coul not be parsed!");
                            }
                        } else {
                            throw new JsonSyntaxException("Expected property value for " + property.getKey() + " to be primitive string. Got " + GsonHelper.getType(property.getValue()));
                        }
                    } else {
                        throw new JsonSyntaxException("The property " + property.getKey() + " is not valid for block " + ForgeRegistries.BLOCKS.getKey(block).toString());
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected properties to be an object. Got " + GsonHelper.getType(propertiesElement));
            }
        }
        return state;
    }

    public static JsonElement serializeEntityType(EntityType<?> type) {
        JsonObject object = new JsonObject();
        object.addProperty("entityType", ForgeRegistries.ENTITY_TYPES.getKey(type).toString());
        return object;
    }

    public static EntityType<?> deserializeEntityType(JsonObject json) {
        return (EntityType<?>) getRegistryEntry(json.get("entityType"), "entityType", ForgeRegistries.ENTITY_TYPES);
    }

    public static JsonElement serializeResourceLocation(ResourceLocation type) {
        JsonObject object = new JsonObject();
        object.addProperty("rl", type.toString());
        return object;
    }

    public static ResourceLocation deserializeResourceLocation(JsonObject json) {
        return new ResourceLocation(json.get("rl").getAsString());
    }

}
