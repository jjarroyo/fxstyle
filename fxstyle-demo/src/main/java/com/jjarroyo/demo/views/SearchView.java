package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JSearchInput;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class SearchView extends ScrollPane {

    private final List<String> COUNTRIES = Arrays.asList(
        "Argentina", "Bolivia", "Brasil", "Chile", "Colombia", "Costa Rica",
        "Cuba", "Ecuador", "El Salvador", "Guatemala", "Honduras", "México",
        "Nicaragua", "Panamá", "Paraguay", "Perú", "Puerto Rico",
        "República Dominicana", "Uruguay", "Venezuela", "España", "Estados Unidos"
    );

    public SearchView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        setFitToWidth(true);
        setContent(content);

        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Search Input")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Campo de búsqueda con autocompletado y botón clear")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        content.getChildren().add(new JCard("Búsqueda Libre (sin sugerencias)", createFreeSearch()));
        content.getChildren().add(new JCard("Búsqueda con Lista Estática", createStaticSelect()));
        content.getChildren().add(new JCard("Búsqueda con Proveedor de Sugerencias Dinámico", createDynamicSearch()));
    }

    private javafx.scene.Node createFreeSearch() {
        VBox container = new VBox(12);
        
        JSearchInput search = new JSearchInput();
        search.setPromptText("Buscar producto (ej. Laptop)...");
        search.setMaxWidth(300);
        
        search.setOnSearch(query -> {
            if (getScene() != null && getScene().getWindow() != null) {
                JToast.show(getScene().getWindow(), "Buscando: " + query, JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 2000);
            }
        });

        container.getChildren().add(search);
        return container;
    }

    private javafx.scene.Node createStaticSelect() {
        VBox container = new VBox(12);
        
        JSearchInput search = new JSearchInput();
        search.setPromptText("Buscar país hispanohablante...");
        search.setMaxWidth(300);
        
        search.setSuggestions(COUNTRIES);
        search.setOnSearch(query -> {
            if (getScene() != null && getScene().getWindow() != null) {
                JToast.show(getScene().getWindow(), "País seleccionado: " + query, JToast.Type.INFO, JToast.Position.BOTTOM_RIGHT, 2000);
            }
        });

        container.getChildren().add(search);
        return container;
    }

    private javafx.scene.Node createDynamicSearch() {
        VBox container = new VBox(12);
        
        JSearchInput search = new JSearchInput();
        search.setPromptText("Escribe para simular una API...");
        search.setMaxWidth(300);
        
        search.setSuggestionProvider(query -> {
            if (query == null || query.length() < 2) return List.of();
            // Simular resultados
            return List.of(
                query + " en tecnología",
                query + " en ofertas",
                query + " barato",
                "Ver todos los resultados de " + query
            );
        });
        
        search.setOnSearch(query -> {
            if (getScene() != null && getScene().getWindow() != null) {
                JToast.show(getScene().getWindow(), "Resultado API: " + query, JToast.Type.WARNING, JToast.Position.BOTTOM_RIGHT, 2000);
            }
        });

        container.getChildren().add(search);
        return container;
    }
}
