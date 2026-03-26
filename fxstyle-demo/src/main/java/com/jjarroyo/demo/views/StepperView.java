package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JStepper;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import com.jjarroyo.components.JInput;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class StepperView extends ScrollPane {

    public StepperView() {
        VBox content = new VBox();
        content.setPadding(new Insets(24));
        content.setSpacing(24);
        
        setFitToWidth(true);
        setContent(content);

        Label title = new Label("Steppers");
        title.getStyleClass().add("j-text-h2");
        
        Label subtitle = new Label("Progress wizards and step-by-step navigation.");
        subtitle.getStyleClass().add("j-text-body");

        content.getChildren().addAll(title, subtitle, createBasicStepper(), createSimpleStepper(), createVerticalStepper());
    }

    private JCard createBasicStepper() {
        JCard card = new JCard();
        card.setTitle("Account Setup Wizard");

        JStepper stepper = new JStepper();
        
        // Step 1: Account
        VBox step1 = new VBox(12);
        JInput emailInput = new JInput("ejemplo@correo.com");
        JInput passInput = new JInput("********");
        step1.getChildren().addAll(
            emailInput.createWithLabel("Email Address", true),
            passInput.createWithLabel("Password", true)
        );
        
        // Step 2: Profile
        VBox step2 = new VBox(12);
        JInput firstNameInput = new JInput("John");
        JInput lastNameInput = new JInput("Doe");
        step2.getChildren().addAll(
            firstNameInput.createWithLabel("First Name", true),
            lastNameInput.createWithLabel("Last Name", false)
        );
        
        // Step 3: Verification
        VBox step3 = new VBox(12);
        step3.getChildren().addAll(
            new Label("Please upload your ID document to verify your identity.")
        );
        
        stepper.addStep("Account", "Basic details", step1);
        stepper.addStep("Profile", "Personal info", step2);
        stepper.addStep("Verification", "ID Upload", step3);
        
        // Callbacks
        stepper.setOnStepChange((oldStep, newStep) -> {
            System.out.println("Changed from step " + oldStep + " to " + newStep);
        });
        
        stepper.setOnFinish(() -> {
            JToast.show(getScene().getWindow(), "Account setup complete!", JToast.Type.SUCCESS, JToast.Position.TOP_RIGHT, 3000);
            // reset stepper as example
            stepper.setCurrentStepIndex(0);
        });

        card.setBody(stepper);
        return card;
    }

    private JCard createSimpleStepper() {
        JCard card = new JCard();
        card.setTitle("Simple Stepper");
        card.setSubtitle("A simpler stepper without subtitles");

        JStepper stepper = new JStepper();
        
        stepper.addStep("Cart", null, new VBox(new Label("Review your items.")));
        stepper.addStep("Shipping", null, new VBox(new Label("Select shipping method.")));
        stepper.addStep("Payment", null, new VBox(new Label("Enter payment info.")));
        stepper.addStep("Confirm", null, new VBox(new Label("Order confirmation.")));
        
        stepper.setOnFinish(() -> {
            com.jjarroyo.components.JToast.show(getScene().getWindow(), "Order placed!", com.jjarroyo.components.JToast.Type.SUCCESS, com.jjarroyo.components.JToast.Position.TOP_RIGHT, 3000);
            stepper.setCurrentStepIndex(0);
        });

        card.setBody(stepper);
        return card;
    }

    private JCard createVerticalStepper() {
        JCard card = new JCard();
        card.setTitle("Vertical Stepper");
        card.setSubtitle("A stepper with vertical orientation");

        JStepper stepper = new JStepper();
        stepper.setOrientation(javafx.geometry.Orientation.VERTICAL);
        
        stepper.addStep("Step 1", "Configure the basics", new VBox(new Label("Basic settings here.")));
        stepper.addStep("Step 2", "Add advanced options", new VBox(new Label("Advanced settings here.", new com.jjarroyo.components.JInput("Option A").createWithLabel("Option", false))));
        stepper.addStep("Step 3", "Review and Finish", new VBox(new Label("Review your configuration.")));
        
        stepper.setOnFinish(() -> {
            com.jjarroyo.components.JToast.show(getScene().getWindow(), "Configuration saved!", com.jjarroyo.components.JToast.Type.SUCCESS, com.jjarroyo.components.JToast.Position.TOP_RIGHT, 3000);
            stepper.setCurrentStepIndex(0);
        });

        card.setBody(stepper);
        return card;
    }
}
