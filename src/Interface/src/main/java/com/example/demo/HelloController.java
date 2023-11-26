package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class HelloController {

    @FXML
    private TextField insercionCodigo;
    @FXML
    private ChoiceBox seleccionLenguaje;
    @FXML
    private Button analizarCodigo;
    @FXML
    private TextArea codigoAnalisis;
    @FXML
    private TextField lineasCodigo;
    @FXML
    private TextField funcionesCodigo;
    @FXML
    private TextField variablesGlobales;
    @FXML
    private TextField numeroIfs;
    @FXML
    private TextField numeroFors;
    @FXML
    private TextField numeroWhiles;
    @FXML
    private TextField promedioPrograma;
    @FXML
    private TextField promedioVariables;
    @FXML
    private TextField promedioFunciones;
    @FXML
    private TextField volumen;
    @FXML
    private TextField dificultad;
    @FXML
    private TextField tiempo;
    @FXML
    private TextField AnalizarFuncion;
    @FXML
    private ChoiceBox funciones;
    @FXML
    private TextField Dependencias;
    @FXML
    private TextField complejidadCiclomatica;
    @FXML
    private TextField duplicidadCodigo;
    @FXML
    private TextField duplicidadOtrasFunciones;
    @FXML
    private Button generar;
    @FXML
    private Button analizarFuncion;
    @FXML
    private BorderPane vista1;
    @FXML
    private BorderPane vista2;
    @FXML
    private BorderPane vista3;
    @FXML
    private ImageView primerAtras;
    @FXML
    private ImageView segundoAtras;

    @FXML
    public void initialize() {

        analizarCodigo.setOnAction(this::analizarCodigo);
        primerAtras.setOnMouseClicked(this::primerAtras);
        analizarFuncion.setOnAction(this::analizarFuncion);
        segundoAtras.setOnMouseClicked(this::segundoAtras);
    }
    @FXML
    private void analizarCodigo(ActionEvent event) {
        vista1.setVisible(false);
        vista2.setVisible(true);
        vista3.setVisible(false);
    }
    @FXML
    private void primerAtras(MouseEvent event) {
        vista1.setVisible(true);
        vista2.setVisible(false);
        vista3.setVisible(false);
    }
    @FXML
    private void analizarFuncion(ActionEvent event) {
        vista1.setVisible(false);
        vista2.setVisible(false);
        vista3.setVisible(true);
    }
    @FXML
    private void segundoAtras(MouseEvent event) {
        vista1.setVisible(false);
        vista2.setVisible(true);
        vista3.setVisible(false);
    }

}