package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JCheckBox;
import com.jjarroyo.components.JRadioButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class ChecksRadiosView extends VBox {

    public ChecksRadiosView() {
        setSpacing(20);
        setPadding(new Insets(20));

        // Title
        Label title = new Label("Checkboxes & Radios");
        title.getStyleClass().add("text-2xl");
        title.getStyleClass().add("font-bold");
        title.getStyleClass().add("text-slate-800");
        getChildren().add(title);

        // 1. Basic Checkboxes
        JCard basicChecks = new JCard();
        basicChecks.setTitle("Checkboxes - Basic");
        basicChecks.setSubtitle("Custom styled checkboxes with various states");

        FlowPane checkFlow = new FlowPane(20, 20);
        checkFlow.getChildren().addAll(
            new JCheckBox("Default"),
            new JCheckBox("Checked") {{ setSelected(true); }},
            new JCheckBox("Disabled") {{ setDisable(true); }},
            new JCheckBox("Disabled & Checked") {{ setDisable(true); setSelected(true); }}
        );
        basicChecks.setBody(checkFlow);
        getChildren().add(basicChecks);

        // 2. Checkbox Colors
        JCard colorChecks = new JCard();
        colorChecks.setTitle("Checkboxes - Colors");
        colorChecks.setSubtitle("Support for state colors");

        FlowPane colorFlow = new FlowPane(20, 20);
        for (JCheckBox.CheckBoxStyle style : JCheckBox.CheckBoxStyle.values()) {
            JCheckBox cb = new JCheckBox(style.name());
            cb.setColorStyle(style);
            cb.setSelected(true);
            colorFlow.getChildren().add(cb);
        }
        colorChecks.setBody(colorFlow);
        getChildren().add(colorChecks);

        // 3. Radio Buttons
        JCard basicRadios = new JCard();
        basicRadios.setTitle("Radio Buttons - Basic");
        basicRadios.setSubtitle("Custom styled radio buttons");

        ToggleGroup group1 = new ToggleGroup();
        JRadioButton r1 = new JRadioButton("Option 1"); r1.setToggleGroup(group1);
        JRadioButton r2 = new JRadioButton("Option 2"); r2.setToggleGroup(group1); r2.setSelected(true);
        JRadioButton r3 = new JRadioButton("Disabled"); r3.setDisable(true);
        
        FlowPane radioFlow = new FlowPane(20, 20);
        radioFlow.getChildren().addAll(r1, r2, r3);
        basicRadios.setBody(radioFlow);
        getChildren().add(basicRadios);

        // 4. Radio Colors
        JCard colorRadios = new JCard();
        colorRadios.setTitle("Radio Buttons - Colors");
        colorRadios.setSubtitle("Support for state colors");

        FlowPane colorRadioFlow = new FlowPane(20, 20);
        ToggleGroup group2 = new ToggleGroup();
        
        for (JRadioButton.RadioStyle style : JRadioButton.RadioStyle.values()) {
            JRadioButton rb = new JRadioButton(style.name());
            rb.setColorStyle(style);
            rb.setToggleGroup(group2);
            rb.setSelected(true); // Logic would unselect others, but visually we want to see them
            // Actually, in a group only one can be selected. Let's make them separate for demo or use separate groups
             ToggleGroup tempGroup = new ToggleGroup();
             rb.setToggleGroup(tempGroup);
             // Wait, effectively let's just show them selected without grouping for demo visuals
             // Or better, let's keep them in one group and select the Primary one
        }
        
        // Re-doing loop for visual demo - independent toggles to show colors
        for (JRadioButton.RadioStyle style : JRadioButton.RadioStyle.values()) {
            JRadioButton rb = new JRadioButton(style.name());
            rb.setColorStyle(style);
            rb.setSelected(true);
            // Hack to allow multiple radios selected for demo purposes (don't set toggle group)
            colorRadioFlow.getChildren().add(rb);
        }
        
        colorRadios.setBody(colorRadioFlow);
        getChildren().add(colorRadios);

        // 5. Switches (Toggle)
        JCard switchCard = new JCard();
        switchCard.setTitle("Switches (Toggle)");
        switchCard.setSubtitle("JSwitch component (extends JCheckBox)");

        FlowPane switchFlow = new FlowPane(20, 20);
        switchFlow.getChildren().addAll(
            new com.jjarroyo.components.JSwitch("Toggle Me"),
            new com.jjarroyo.components.JSwitch("Checked") {{ setSelected(true); }},
            new com.jjarroyo.components.JSwitch("Disabled") {{ setDisable(true); }},
            new com.jjarroyo.components.JSwitch("Disabled & On") {{ setDisable(true); setSelected(true); }}
        );
        
        switchCard.setBody(switchFlow);
        getChildren().add(switchCard);

        // 6. Switch Colors
        JCard colorSwitches = new JCard();
        colorSwitches.setTitle("Switch Colors");
        colorSwitches.setSubtitle("Support for state colors");

        FlowPane colorSwitchFlow = new FlowPane(20, 20);
        for (JCheckBox.CheckBoxStyle style : JCheckBox.CheckBoxStyle.values()) {
            com.jjarroyo.components.JSwitch s = new com.jjarroyo.components.JSwitch(style.name());
            s.setColorStyle(style);
            s.setSelected(true);
            colorSwitchFlow.getChildren().add(s);
        }
        colorSwitches.setBody(colorSwitchFlow);
        getChildren().add(colorSwitches);
    }
}


