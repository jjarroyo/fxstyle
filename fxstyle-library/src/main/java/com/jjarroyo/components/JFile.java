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

    // New Properties for custom texts and layout
    private StringProperty titleText = new SimpleStringProperty("Drop files here or click to upload.");
    private StringProperty subtitleText = new SimpleStringProperty("");
    private StringProperty descriptionText = new SimpleStringProperty("");
    private BooleanProperty fillParent = new SimpleBooleanProperty(false);
    private ObjectProperty<File> initialDirectory = new SimpleObjectProperty<>();
    private BooleanProperty supportDirectory = new SimpleBooleanProperty(false);

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

    public JFile(Mode mode, String title, String subtitle) {
        this.mode.set(mode);
        this.titleText.set(title);
        this.subtitleText.set(subtitle);
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
                updateHoverStyle(true);
            }
            event.consume();
        });

        setOnDragExited(event -> {
            getStyleClass().remove("drag-over");
            updateHoverStyle(false);
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

        // Fix white background in dark mode when hovered
        hoverProperty().addListener((obs, old, isHover) -> updateHoverStyle(isHover));
        
        fillParent.addListener((obs, old, newVal) -> applyFillParent(newVal));

         
    }

    private void updateHoverStyle(boolean active) {
        if (getMode() != Mode.DROP_ZONE) return;
        
        if (active) {
            // If in dark mode, override the light blue background from CSS
            if (isDark()) {
                setStyle("-fx-background-color: #1e293b; -fx-border-color: -color-primary-500;");
            }
        } else {
            setStyle("");
        }
    }

    private boolean isDark() {
        if (getScene() == null) return false;
        javafx.scene.Node root = getScene().getRoot();
        return root.getStyleClass().contains("dark") || root.getStyleClass().contains("dark-mode");
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

        applyFillParent(fillParent.get());
    }

    private void applyFillParent(boolean fill) {
        if (fill) {
            VBox.setVgrow(this, Priority.ALWAYS);
            setMaxWidth(Double.MAX_VALUE);
            setMaxHeight(Double.MAX_VALUE);
        } else {
            VBox.setVgrow(this, Priority.NEVER);
            setMaxWidth(Region.USE_COMPUTED_SIZE);
            setMaxHeight(Region.USE_COMPUTED_SIZE);
        }
    }
    
    private void buildButtonUI() {
        Button btn = new Button(titleText.get());
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
        javafx.scene.Node placeholder = JIcon.USER.view(); 
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
        VBox.setVgrow(dropZoneContent, Priority.ALWAYS);
        
        javafx.scene.Node icon = JIcon.UPLOAD_CLOUD.view();
        icon.getStyleClass().add("icon");
        
        Label titleLbl = new Label();
        titleLbl.getStyleClass().add("title");
        titleLbl.textProperty().bind(titleText);
        
        Label subtitleLbl = new Label();
        subtitleLbl.getStyleClass().add("subtitle");
        subtitleLbl.textProperty().bind(subtitleText);

        Label descLbl = new Label();
        descLbl.getStyleClass().add("subtitle"); // Reuse subtitle style or similar
        descLbl.textProperty().bind(descriptionText);
        descLbl.setWrapText(true);
        descLbl.setAlignment(Pos.CENTER);
        descLbl.setStyle("-fx-text-fill: -color-slate-500; -fx-font-size: 12px; -fx-text-alignment: center;");
        
        dropZoneContent.getChildren().addAll(icon, titleLbl, subtitleLbl, descLbl);
        
        // Make entire area clickable
        this.setOnMouseClicked(e -> chooseFile());
        
        getChildren().add(dropZoneContent);
    }
    
    private void chooseFile() {
        if (supportDirectory.get()) {
            showSelectionTypeModal();
        } else {
            openFileChooser();
        }
    }

    private void showSelectionTypeModal() {
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-padding: 40;");
        
        Label title = new Label("Importar Imágenes");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: -color-text-primary;");
        
        Label subtitle = new Label("Seleccione el origen de sus archivos");
        subtitle.setStyle("-fx-text-fill: -color-text-secondary; -fx-font-size: 14px;");
        
        HBox options = new HBox(25);
        options.setAlignment(Pos.CENTER);
        
        // Icon Paths (Lucide Style)
        String pathImage = "M3 3h18v18H3z M3 15l4-4 4 4 5-5 5 5 M8 8.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z";
        String pathFolder = "M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z";
        
        // Files Button
        Button btnFiles = new Button("Seleccionar Archivos");
        btnFiles.getStyleClass().addAll("btn", "btn-primary", "btn-lg");
        btnFiles.setPrefWidth(240);
        btnFiles.setGraphicTextGap(15);
        btnFiles.setGraphic(createSVGIcon(pathImage));
        
        // Folder Button
        Button btnFolder = new Button("Seleccionar Carpeta");
        btnFolder.getStyleClass().addAll("btn", "btn-outline", "btn-lg");
        btnFolder.setPrefWidth(240);
        btnFolder.setGraphicTextGap(15);
        btnFolder.setGraphic(createSVGIcon(pathFolder));
        
        options.getChildren().addAll(btnFiles, btnFolder);
        content.getChildren().addAll(title, subtitle, options);
        
        JModal modal = new JModal(content, JModal.Size.MEDIUM);
        
        btnFiles.setOnAction(e -> {
            modal.close();
            openFileChooser();
        });
        
        btnFolder.setOnAction(e -> {
            modal.close();
            openDirectoryChooser();
        });
        
        modal.show();
    }

    private javafx.scene.Node createSVGIcon(String pathData) {
        javafx.scene.shape.SVGPath path = new javafx.scene.shape.SVGPath();
        path.setContent(pathData);
        path.setFill(javafx.scene.paint.Color.TRANSPARENT);
        path.setStroke(javafx.scene.paint.Color.web("#94a3b8"));
        path.setStrokeWidth(1.5);
        path.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        path.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        
        // Wrap in a pane for scaling/alignment
        StackPane iconBox = new StackPane(path);
        iconBox.setPadding(new javafx.geometry.Insets(0, 5, 0, 0));
        return iconBox;
    }

    private void openDirectoryChooser() {
        javafx.stage.DirectoryChooser directoryChooser = new javafx.stage.DirectoryChooser();
        directoryChooser.setTitle("Seleccionar Carpeta");
        
        if (initialDirectory.get() != null && initialDirectory.get().exists()) {
            directoryChooser.setInitialDirectory(initialDirectory.get());
        }
        
        File dir = directoryChooser.showDialog(getScene().getWindow());
        if (dir != null) {
            initialDirectory.set(dir);
            handleFiles(List.of(dir));
        }
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        
        if (initialDirectory.get() != null && initialDirectory.get().exists()) {
            fileChooser.setInitialDirectory(initialDirectory.get());
        }
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
        
        if (files != null) {
            initialDirectory.set(files.get(0).getParentFile());
            handleFiles(files);
        }
    }

    private boolean isValidExtension(File f) {
        if (validExtensions.isEmpty()) return true;
        String name = f.getName().toLowerCase();
        return validExtensions.stream().anyMatch(ext -> name.endsWith(ext.toLowerCase()));
    }
    
    private void handleFiles(List<File> files) {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        getStyleClass().remove("error");

        List<File> allFiles = new java.util.ArrayList<>();
        
        for (File f : files) {
            if (f.isDirectory() && supportDirectory.get()) {
                scanDirectory(f, allFiles);
            } else {
                allFiles.add(f);
            }
        }

        // Filter valid files
        List<File> validFiles = allFiles.stream()
            .filter(f -> !f.isDirectory())
            .filter(this::isValidExtension)
            .filter(f -> f.length() <= maxFileSize.get())
            .collect(Collectors.toList());

        if (validFiles.isEmpty() && !allFiles.isEmpty()) {
            showError("0 valid files found in selection.");
            return;
        }

        if (maxFileCount.get() == 1) {
            selectedFiles.clear();
        }

        if (selectedFiles.size() + validFiles.size() > maxFileCount.get()) {
            showError("Límite de cantidad de archivos excedido.");
            return;
        }
        
        selectedFiles.addAll(validFiles);
    }

    private void scanDirectory(File dir, List<File> list) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) scanDirectory(f, list);
                else list.add(f);
            }
        }
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
             if (!selectedFiles.isEmpty()) {
                  // Option: could update subtitle with count
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

    public void setFillParent(boolean fill) {
        this.fillParent.set(fill);
    }

    public StringProperty titleTextProperty() { return titleText; }
    public void setTitleText(String text) { this.titleText.set(text); }

    public StringProperty subtitleTextProperty() { return subtitleText; }
    public void setSubtitleText(String text) { this.subtitleText.set(text); }

    public StringProperty descriptionTextProperty() { return descriptionText; }
    public void setDescriptionText(String text) { this.descriptionText.set(text); }
    
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

    public ObjectProperty<File> initialDirectoryProperty() { return initialDirectory; }
    public File getInitialDirectory() { return initialDirectory.get(); }
    public void setInitialDirectory(File dir) { this.initialDirectory.set(dir); }

    public BooleanProperty supportDirectoryProperty() { return supportDirectory; }
    public boolean isSupportDirectory() { return supportDirectory.get(); }
    public void setSupportDirectory(boolean support) { this.supportDirectory.set(support); }
}

