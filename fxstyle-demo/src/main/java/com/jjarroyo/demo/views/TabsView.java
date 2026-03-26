package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JTab;
import com.jjarroyo.components.JTabs;
import com.jjarroyo.components.JTabs.TabStyle;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class TabsView extends ScrollPane {

    public TabsView() {
        getStyleClass().add("base-view");
        setFitToWidth(true);
        setPadding(new Insets(20));

        VBox content = new VBox(30);
        content.setPadding(new Insets(0, 0, 50, 0));
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Tabs")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Tab styles inspired by Tailwind/JJArroyo")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        
        content.getChildren().add(pageHeader);

        // 1. Tabs with underline
        JCard underlineCard = new JCard("Tabs with underline", "Standard tabs with underline style");
        
        JTabs underlineTabs = new JTabs();
        underlineTabs.setTabStyle(TabStyle.LINE);
        
        underlineTabs.getTabs().add(new JTab("My Account", new Label("My Account Content")));
        underlineTabs.getTabs().add(new JTab("Company", new Label("Company Content")));
        underlineTabs.getTabs().add(new JTab("Team Members", new Label("Team Members Content")));
        underlineTabs.getTabs().add(new JTab("Billing", new Label("Billing Content")));
        underlineTabs.selectTab(underlineTabs.getTabs().get(2)); // Select "Team Members"
        
        underlineCard.setBody(underlineTabs);
        content.getChildren().add(underlineCard);

        // 2. Tabs with underline and icons
        JCard iconsCard = new JCard("Tabs with underline and icons", "Tabs with an icon beside the text");
        
        JTabs iconTabs = new JTabs();
        iconTabs.setTabStyle(TabStyle.LINE);
        
        iconTabs.getTabs().add(new JTab("My Account", JIcon.USER.view(), new Label("My Account Content")));
        iconTabs.getTabs().add(new JTab("Company", JIcon.HOME.view(), new Label("Company Content")));
        iconTabs.getTabs().add(new JTab("Team Members", JIcon.USERS.view(), new Label("Team Members Content")));
        iconTabs.getTabs().add(new JTab("Billing", JIcon.CREDIT_CARD.view(), new Label("Billing Content")));
        iconTabs.selectTab(iconTabs.getTabs().get(2)); // Select "Team Members"
        
        iconsCard.setBody(iconTabs);
        content.getChildren().add(iconsCard);
        
        // 3. Tabs in pills
        JCard pillsCard = new JCard("Tabs in pills", "Tabs with a pill background for the active state");
        
        JTabs pillsTabs = new JTabs();
        pillsTabs.setTabStyle(TabStyle.PILLS);
        
        pillsTabs.getTabs().add(new JTab("My Account", new Label("My Account Content")));
        pillsTabs.getTabs().add(new JTab("Company", new Label("Company Content")));
        pillsTabs.getTabs().add(new JTab("Team Members", new Label("Team Members Content")));
        pillsTabs.getTabs().add(new JTab("Billing", new Label("Billing Content")));
        pillsTabs.selectTab(pillsTabs.getTabs().get(2)); // Select "Team Members"
        
        pillsCard.setBody(pillsTabs);
        content.getChildren().add(pillsCard);

        setContent(content);
    }
}


