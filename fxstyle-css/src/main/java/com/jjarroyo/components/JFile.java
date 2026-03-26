package com.jjarroyo.components;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class JFile extends VBox {

    public enum Mode {
        BUTTON,
        AVATAR,
        SQUARE,
        DROP_ZONE
    }

    private ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.BUTTON);
    private ObservableList<File> selectedFiles = FXCollections.observableArrayList();
    private ObservableList<String> validExtensions = FXCollections.observableArrayList();
    private LongProperty maxFileSize = new SimpleLongProperty(10 * 1024 * 1024); // 10 MB default
    private IntegerProperty maxFileCount = new SimpleIntegerProperty(1);
    
    private BooleanProperty showRemove = new SimpleBooleanProperty(false);
    private BooleanProperty showEdit = new SimpleBooleanProperty(false);

    // UI Elements
    private StackPane previewContainer;
    private Label errorLabel;
    
    // For Avatar/Square
    private StackPane imageWrapper;
    private Circle avatarClip;
    private Rectangle squareClip;
    private Rectangle avatarRect; // Used for ImagePattern
    
    // For DropZone
    private VBox dropZoneContent;

    public JFile() {
        init();
        buildUI();
    }
    
    public JFile(Mode mode) {
        this.mode.set(mode);
        init();
        buildUI();
    }

    private void init() {
        getStyleClass().add("j-file");
        
        // Mode listener
        mode.addListener((obs, oldVal, newVal) -> rebuildUI());
        
        // File changes
        selectedFiles.addListener((javafx.collections.ListChangeListener.Change<? extends File> c) -> updatePreview());
        
        // Drag & Drop
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                getStyleClass().add("drag-over");
            }
            event.consume();
        });

        setOnDragExited(event -> {
            getStyleClass().remove("drag-over");
            event.consume();
        });
        
        setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasFiles()) {
                List<File> files = event.getDragboard().getFiles();
                handleFiles(files);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void buildUI() {
        rebuildUI();
    }

    public void rebuildUI() {
        getChildren().clear();
        getStyleClass().removeAll("button", "avatar", "square", "drop-zone");
        
        switch (mode.get()) {
            case BUTTON:
                getStyleClass().add("button");
                buildButtonUI();
                break;
            case AVATAR:
                getStyleClass().add("avatar");
                buildImageUI(true);
                break;
            case SQUARE:
                getStyleClass().add("square");
                buildImageUI(false);
                break;
            case DROP_ZONE:
                getStyleClass().add("drop-zone");
                buildDropZoneUI();
                break;
        }
        
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-text");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        getChildren().add(errorLabel);
    }
    
    private void buildButtonUI() {
        Button btn = new Button("Upload File");
        btn.getStyleClass().addAll("btn", "btn-primary");
        btn.setGraphic(JIcon.UPLOAD.view());
        btn.setOnAction(e -> chooseFile());
        getChildren().add(btn);
        
        // Simple file name display
        Label fileName = new Label("No file selected");
        fileName.getStyleClass().add("text-gray-500");
        selectedFiles.addListener((javafx.collections.ListChangeListener.Change<? extends File> c) -> {
            if (selectedFiles.isEmpty()) fileName.setText("No file selected");
            else fileName.setText(selectedFiles.stream().map(File::getName).collect(Collectors.joining(", ")));
        });
        getChildren().add(fileName);
    }

    private void buildImageUI(boolean isAvatar) {
        imageWrapper = new StackPane();
        imageWrapper.getStyleClass().add("j-file-preview-wrapper");
        imageWrapper.setPrefSize(120, 120);
        imageWrapper.setMaxSize(120, 120);
        
        // Placeholder Icon
        Node placeholder = JIcon.USER.view(); 
        if (!isAvatar) placeholder = JIcon.IMAGE.view();
        placeholder.getStyleClass().add("placeholder-icon");
        
        // Image Rect (filled with ImagePattern or Placeholder)
        avatarRect = new Rectangle(120, 120);
        avatarRect.setFill(javafx.scene.paint.Color.TRANSPARENT);
        
        // Clip
        if (isAvatar) {
            avatarClip = new Circle(60, 60, 60);
            imageWrapper.setClip(avatarClip);
        } else {
            // CSS handles border radius for container, but we might need clip for Image
           squareClip = new Rectangle(120, 120);
           squareClip.setArcWidth(12);
           squareClip.setArcHeight(12);
           imageWrapper.setClip(squareClip);
        }
        
        imageWrapper.getChildren().addAll(placeholder, avatarRect);
        
        // Overlay (Edit/Remove)
        HBox overlay = new HBox();
        overlay.getStyleClass().addAll("j-file-overlay", "j-file-actions");
        
        // Click to upload if no buttons enabled, or edit button
        imageWrapper.setOnMouseClicked(e -> {
            if (selectedFiles.isEmpty()) chooseFile();
        });
        
        if (showEdit.get()) {
            Region editIcon = new Region();
            // editIcon.setGraphic(JIcon.EDIT.view()); // Need button wrapper
            Button editBtn = new Button();
            editBtn.setGraphic(JIcon.EDIT.view());
            editBtn.getStyleClass().add("j-file-action-btn");
            editBtn.setOnAction(e -> chooseFile());
            overlay.getChildren().add(editBtn);
        }
        
        if (showRemove.get()) {
             Button removeBtn = new Button();
             removeBtn.setGraphic(JIcon.TRASH.view());
             removeBtn.getStyleClass().add("j-file-action-btn");
             removeBtn.setOnAction(e -> clear());
             overlay.getChildren().add(removeBtn);
        }
        
        imageWrapper.getChildren().add(overlay);
        getChildren().add(imageWrapper);
        
        // Hint text
        Label hint = new Label("Allowed *.png, *.jpg");
        hint.getStyleClass().addAll("text-xs", "text-gray-500");
        getChildren().add(hint);
    }

    private void buildDropZoneUI() {
        dropZoneContent = new VBox();
        dropZoneContent.setAlignment(Pos.CENTER);
        dropZoneContent.setSpacing(10);
        
        Node icon = JIcon.UPLOAD_CLOUD.view();
        icon.getStyleClass().add("icon");
        
        Label title = new Label("Drop files here or click to upload.");
        title.getStyleClass().add("title");
        
        Label subtitle = new Label("Upload up to " + getMaxFileCount() + " files");
        subtitle.getStyleClass().add("subtitle");
        
        dropZoneContent.getChildren().addAll(icon, title, subtitle);
        
        // Make entire area clickable
        this.setOnMouseClicked(e -> chooseFile());
        
        getChildren().add(dropZoneContent);
    }
    
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        if (!validExtensions.isEmpty()) {
             fileChooser.getExtensionFilters().add(
                 new FileChooser.ExtensionFilter("Allowed Files", validExtensions.stream().map(e -> "*" + e).collect(Collectors.toList()))
             );
        }
        
        List<File> files;
        if (maxFileCount.get() > 1) {
            files = fileChooser.showOpenMultipleDialog(getScene().getWindow());
        } else {
            File f = fileChooser.showOpenDialog(getScene().getWindow());
            files = f != null ? List.of(f) : null;
        }
        
        if (files != null) handleFiles(files);
    }
    
    private void handleFiles(List<File> files) {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        getStyleClass().remove("error");

        // Validate count
        if (selectedFiles.size() + files.size() > maxFileCount.get()) {
            showError("Maximum file count exceeded.");
            return;
        }
        
        // Validate size and extensions
        for (File f : files) {
            if (f.length() > maxFileSize.get()) {
                showError("File " + f.getName() + " is too large.");
                return;
            }
            // Check extension
        }
        
        if (maxFileCount.get() == 1) selectedFiles.clear();
        selectedFiles.addAll(files);
    }
    
    private void updatePreview() {
        if (mode.get() == Mode.AVATAR || mode.get() == Mode.SQUARE) {
            if (!selectedFiles.isEmpty()) {
                File f = selectedFiles.get(0);
                try {
                    Image img = new Image(f.toURI().toString());
                    avatarRect.setFill(new ImagePattern(img));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                avatarRect.setFill(javafx.scene.paint.Color.TRANSPARENT);
            }
        } else if (mode.get() == Mode.DROP_ZONE) {
             // Maybe show list of files in DropZone? or keep generic
             if (!selectedFiles.isEmpty()) {
                 Label title = (Label) dropZoneContent.getChildren().get(1);
                 title.setText(selectedFiles.size() + " files selected");
             }
        }
    }
    
    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        getStyleClass().add("error");
    }
    
    public void clear() {
        selectedFiles.clear();
    }

    // Getters/Setters
    public ObjectProperty<Mode> modeProperty() { return mode; }
    public Mode getMode() { return mode.get(); }
    public void setMode(Mode mode) { this.mode.set(mode); }
    
    public ObservableList<File> getSelectedFiles() { return selectedFiles; }
    
    public void setPreviewImage(Image img) {
        if (mode.get() == Mode.AVATAR || mode.get() == Mode.SQUARE) {
            if (img != null) {
                avatarRect.setFill(new ImagePattern(img));
            } else {
                avatarRect.setFill(javafx.scene.paint.Color.TRANSPARENT);
            }
        }
    }
    
    public void setPreviewFromFile(File file) {
        if (file != null && file.exists()) {
             try {
                 setPreviewImage(new Image(file.toURI().toString()));
             } catch (Exception e) {
                 e.printStackTrace();
             }
        } else {
             setPreviewImage(null);
        }
    }
    
    public LongProperty maxFileSizeProperty() { return maxFileSize; }
    public long getMaxFileSize() { return maxFileSize.get(); }
    public void setMaxFileSize(long size) { this.maxFileSize.set(size); }
    
    public IntegerProperty maxFileCountProperty() { return maxFileCount; }
    public int getMaxFileCount() { return maxFileCount.get(); }
    public void setMaxFileCount(int count) { this.maxFileCount.set(count); }
    
    public BooleanProperty showRemoveProperty() { return showRemove; }
    public void setShowRemove(boolean show) { this.showRemove.set(show); }
    
    public BooleanProperty showEditProperty() { return showEdit; }
    public void setShowEdit(boolean show) { this.showEdit.set(show); }
    
    public ObservableList<String> getValidExtensions() { return validExtensions; }
}

