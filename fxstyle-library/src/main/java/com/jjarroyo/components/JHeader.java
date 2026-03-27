package com.jjarroyo.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class JHeader extends HBox {

    private HBox leftBox;
    private HBox centerBox;
    private HBox rightBox;
    
    public JHeader() {
        // getStylesheets().add(getClass().getResource("/header.css").toExternalForm());
        getStyleClass().add("j-header");
        
        // 1. Left: Branding
        leftBox = new HBox();
        leftBox.getStyleClass().add("j-header-brand");
        
        // 2. Center: Menu
        centerBox = new HBox();
        centerBox.getStyleClass().add("j-header-menu");
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // 3. Right: Profile
        rightBox = new HBox();
        rightBox.getStyleClass().add("j-header-toolbar");
        
        getChildren().addAll(leftBox, centerBox, spacer, rightBox);
    }
    
    public JHeader setBrand(Node logo, String title) {
        leftBox.getChildren().clear();
        if (logo != null) leftBox.getChildren().add(logo);
        
        if (title != null && !title.isEmpty()) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("j-header-logo-text");
            leftBox.getChildren().add(titleLabel);
        }
        return this;
    }
    
    public JHeader addMenuItem(String text, Runnable action) {
        Label item = new Label(text);
        item.getStyleClass().add("j-header-menu-item");
        item.setOnMouseClicked(e -> {
            if (action != null) action.run();
        });
        centerBox.getChildren().add(item);
        return this;
    }
    
    public JHeader addMenuDropdown(String text, java.util.Map<String, Runnable> items) {
        HBox itemBox = new HBox(4);
        itemBox.setAlignment(Pos.CENTER_LEFT);
        itemBox.getStyleClass().add("j-header-menu-item");
        
        Label label = new Label(text);
        // Add a small arrow icon if we had one, or just text
        // For now just text
        
        itemBox.getChildren().add(label);
        
        itemBox.setOnMouseClicked(e -> {
            JPopover pop = new JPopover();
            pop.setPosition(JPopover.Position.BOTTOM);
            
            VBox content = new VBox();
            content.setPadding(new javafx.geometry.Insets(4));
            content.setMinWidth(150);
            
            items.forEach((menuText, action) -> {
                content.getChildren().add(createDropdownItem(menuText, () -> {
                    pop.hide();
                    if (action != null) action.run();
                }));
            });
            
            pop.setContentNode(content);
            pop.show(itemBox);
        });
        
        centerBox.getChildren().add(itemBox);
        return this;
    }
    
    public JHeader addToolbarItem(Node node) {
        if (node != null) {
            rightBox.getChildren().add(0, node); // Add to the beginning of right box (left of profile)
            HBox.setMargin(node, new javafx.geometry.Insets(0, 16, 0, 0));
        }
        return this;
    }

    private Runnable onLogout; // Field to store logout action

    public JHeader setUserProfile(String name, String email, String initials, Runnable onLogout) {
        this.onLogout = onLogout;
        // Clear previous profile to avoid duplicates if called multiple times, 
        // but keep custom toolbar items if we designed it that way. 
        // Ideally rightBox structure should be: [Toolbar Items] [Profile]
        // For now, let's just append profile at the end.
        
        // Remove existing profile if any (checking via style class)
        rightBox.getChildren().removeIf(n -> n.getStyleClass().contains("j-header-user-profile"));
        
        HBox profileBox = new HBox();
        profileBox.getStyleClass().add("j-header-user-profile");
        profileBox.setAlignment(Pos.CENTER_RIGHT);
        
        // Text Info (Name/Email)
        VBox textBox = new VBox();
        textBox.getStyleClass().add("j-header-user-info");
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("j-header-user-name");
        Label emailLabel = new Label(email);
        emailLabel.getStyleClass().add("j-header-user-email");
        textBox.getChildren().addAll(nameLabel, emailLabel);
        
        // Avatar Circle
        HBox avatarBox = new HBox();
        avatarBox.getStyleClass().add("j-header-avatar");
        Label initialsLabel = new Label(initials);
        initialsLabel.getStyleClass().add("j-header-avatar-text");
        avatarBox.getChildren().add(initialsLabel);
        
        profileBox.getChildren().addAll(textBox, avatarBox);
        
        // Dropdown Interaction
        profileBox.setOnMouseClicked(e -> {
            showProfileDropdown(profileBox, name, email);
        });
        
        rightBox.getChildren().add(profileBox);
        return this;
    }
    
    // Overload for backward compatibility if needed, though we can just update usage
    public JHeader setUserProfile(String name, String email, String initials) {
        return setUserProfile(name, email, initials, null);
    }
    
    private void showProfileDropdown(Node target, String name, String email) {
        JPopover pop = new JPopover();
        pop.setPosition(JPopover.Position.BOTTOM);
        
        VBox content = new VBox(8);
        content.setPadding(new javafx.geometry.Insets(8));
        content.setMinWidth(200);
        
        // Header in dropdown
        VBox headerBox = new VBox(2);
        headerBox.getStyleClass().add("j-popover-header");
        
        Label nameLbl = new Label(name);
        nameLbl.getStyleClass().add("j-popover-title");
        
        Label emailLbl = new Label(email);
        emailLbl.getStyleClass().add("j-popover-subtitle");
        
        headerBox.getChildren().addAll(nameLbl, emailLbl);
        
        content.getChildren().add(headerBox);
        
        // Items
        // content.getChildren().add(createDropdownItem("My Profile", () -> {
        //     pop.hide();
        //     System.out.println("Profile clicked");
        // }));
        // content.getChildren().add(createDropdownItem("Settings", () -> {
        //     pop.hide();
        //     System.out.println("Settings clicked");
        // }));
        content.getChildren().add(createDropdownItem("Cerrar Sesión", () -> {
            pop.hide();
            if (onLogout != null) onLogout.run();
            else System.out.println("Logout clicked (no action set)");
        }));
        
        pop.setContentNode(content);
        pop.show(target);
    }
    
    private Label createDropdownItem(String text, Runnable action) {
        Label item = new Label(text);
        item.getStyleClass().add("j-popover-item");
        item.setOnMouseClicked(e -> {
            if (action != null) action.run();
        });
        return item;
    }
}

