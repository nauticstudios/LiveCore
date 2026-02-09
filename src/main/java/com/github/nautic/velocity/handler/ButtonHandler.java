package com.github.nautic.velocity.handler;

import com.github.nautic.velocity.LiveCoreVelocityPlugin;
import com.github.nautic.velocity.utils.YamlUtil;
import com.github.nautic.velocity.utils.addColorVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class ButtonHandler {

    private ButtonHandler() {}

    public static Component build(
            LiveCoreVelocityPlugin plugin,
            String platformId,
            String url
    ) {

        Map<String, Object> messages = plugin.messages();

        Map<String, Object> buttons =
                YamlUtil.getSection(messages, "buttons");
        if (buttons == null) return Component.empty();

        Map<String, Object> btn =
                YamlUtil.getSection(buttons, platformId);
        if (btn == null) return Component.empty();

        String rawText = YamlUtil.getString(btn, "text");
        if (rawText == null) return Component.empty();

        Component button =
                addColorVelocity.Set(rawText);

        List<String> hoverLines =
                YamlUtil.getStringList(btn, "hover");

        if (hoverLines != null && !hoverLines.isEmpty()) {

            Component hover =
                    addColorVelocity.Set(
                            String.join("\n", hoverLines)
                    );

            button = button.hoverEvent(
                    HoverEvent.showText(hover)
            );
        }

        Map<String, Object> click =
                YamlUtil.getSection(btn, "click");

        if (click != null) {

            String type =
                    YamlUtil.getString(click, "type");
            String value =
                    YamlUtil.getString(click, "value");

            if ("url".equalsIgnoreCase(type) && value != null) {
                button = button.clickEvent(
                        ClickEvent.openUrl(
                                value.replace("%url%", url)
                        )
                );
            }
        }

        return button;
    }
}
