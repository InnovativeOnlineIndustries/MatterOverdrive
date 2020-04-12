/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.handler.dialog;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import matteroverdrive.Reference;
import matteroverdrive.api.dialog.IDialogOption;
import matteroverdrive.api.exceptions.MORuntimeException;
import matteroverdrive.data.dialog.DialogMessage;
import matteroverdrive.data.dialog.DialogOptionReference;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOLog;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.io.*;

public class DialogAssembler {
    private static final ResourceLocation dialogLocation = new ResourceLocation(Reference.MOD_ID + ":" + "dialog/dialog.json");
    private static final String customDialogPath = "custom_dialog.json";
    private String currentDialogName;

    public DialogAssembler() {

    }

    public void loadDialogs(DialogRegistry dialogRegistry) {
        String path = "/assets/" + dialogLocation.getNamespace() + "/" + dialogLocation.getPath();
        InputStream inputStream = DialogAssembler.class.getResourceAsStream(path);
        loadDialogs(inputStream, dialogRegistry);
    }

    public void loadCustomDialogs(DialogRegistry dialogRegistry) {
        File file = new File(customDialogPath);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                loadDialogs(inputStream, dialogRegistry);
            } catch (FileNotFoundException e) {
                MOLog.log(Level.ERROR, e, "Could not find Custom quests file");
            }
        } else {
            MOLog.info("No custom dialog file found at: %s", file.getAbsolutePath());
        }
    }

    private void loadDialogs(InputStream inputStream, DialogRegistry dialogRegistry) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject mainQuestsObject = jsonParser.parse(inputStreamReader).getAsJsonObject();
        for (JsonElement element : mainQuestsObject.getAsJsonArray("dialogs")) {
            if (element.isJsonObject()) {
                currentDialogName = MOJsonHelper.getString(element.getAsJsonObject(), "id");
                DialogMessage dialogMessage = parseMessage(element.getAsJsonObject(), dialogRegistry);
                if (dialogMessage != null) {
                    dialogRegistry.registerMessage(new ResourceLocation(currentDialogName), dialogMessage);
                }
            }
        }
    }

    public DialogMessage parseMessage(JsonObject jsonObject, DialogRegistry dialogRegistry) {
        DialogMessage dialogMessage = new DialogMessage(jsonObject);
        if (jsonObject.has("options") && jsonObject.get("options").isJsonArray()) {
            JsonArray array = jsonObject.getAsJsonArray("options");
            for (int i = 0; i < array.size(); i++) {
                JsonElement element = array.get(i);
                IDialogOption option = parseOption(element, dialogRegistry);
                if (option != null) {
                    dialogMessage.addOption(option);
                }
            }
        }
        return dialogMessage;
    }

    public IDialogOption parseOption(JsonElement element, DialogRegistry dialogRegistry) {
        if (element.isJsonObject()) {
            DialogMessage optionMessage = parseMessage(element.getAsJsonObject(), dialogRegistry);
            if (element.getAsJsonObject().has("id")) {
                dialogRegistry.registerMessage(new ResourceLocation(MOJsonHelper.getString(element.getAsJsonObject(), "id")), optionMessage);
            } else {
                dialogRegistry.registerMessage(optionMessage);
            }
            return optionMessage;
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            ResourceLocation location = new ResourceLocation(element.getAsString());
            DialogOptionReference optionReference = new DialogOptionReference(location);
            return optionReference;
        }
        throw new MORuntimeException(String.format("Dialog Option must be of type string or Object at: '%s'"));
    }
}
