package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class KalkulatorVAT extends Application {

    private TextField poleWartosciBazowej;
    private ComboBox<Double> wyborStawkiVAT;
    private Label labelWynikNetto;
    private Label labelWynikVAT;
    private Label labelWynikBrutto;
    private RadioButton nettoDoBrutto, bruttoDoNetto, dopasujDoVAT;


    @Override
    public void start(Stage glownaScena) {
        glownaScena.setTitle("Kalkulator VAT netto-brutto");

        VBox glownyUklad = new VBox(10);
        glownyUklad.setPadding(new Insets(15, 15, 15, 15));

        // Sekcja Metoda obliczeń
        VBox ukladMetody = new VBox(5);
        ToggleGroup grupaMetody = new ToggleGroup();

        nettoDoBrutto = new RadioButton("Od netto do brutto");
        nettoDoBrutto.setToggleGroup(grupaMetody);
        nettoDoBrutto.setSelected(true);

        bruttoDoNetto = new RadioButton("Od brutto do netto");
        bruttoDoNetto.setToggleGroup(grupaMetody);

        dopasujDoVAT = new RadioButton("Dopasuj do kwoty VAT");
        dopasujDoVAT.setToggleGroup(grupaMetody);

        ukladMetody.getChildren().addAll(nettoDoBrutto, bruttoDoNetto, dopasujDoVAT);
        TitledPane sekcjaMetody = new TitledPane("Metoda obliczeń:", ukladMetody);
        sekcjaMetody.setCollapsible(false);
        glownyUklad.getChildren().add(sekcjaMetody);

        // Sekcja Dane
        GridPane ukladDanych = new GridPane();
        ukladDanych.setHgap(10);
        ukladDanych.setVgap(10);

        Label labelWartosciBazowej = new Label("Wartość bazowa:");
        poleWartosciBazowej = new TextField("2000.00");
        Label labelStawkiVAT = new Label("Stawka VAT:");
        wyborStawkiVAT = new ComboBox<>();
        wyborStawkiVAT.getItems().addAll(5.0, 8.0, 23.0);
        wyborStawkiVAT.setValue(23.0);

        ukladDanych.add(labelWartosciBazowej, 0, 0);
        ukladDanych.add(poleWartosciBazowej, 1, 0);
        ukladDanych.add(labelStawkiVAT, 0, 1);
        ukladDanych.add(wyborStawkiVAT, 1, 1);

        TitledPane sekcjaDanych = new TitledPane("Dane:", ukladDanych);
        sekcjaDanych.setCollapsible(false);
        glownyUklad.getChildren().add(sekcjaDanych);

        HBox ukladPrzyciskow = new HBox(10);
        ukladPrzyciskow.setAlignment(Pos.CENTER);
        ukladPrzyciskow.setPadding(new Insets(10, 0, 10, 0));

        Button przyciskOblicz = new Button("OBLICZ");
        Button przyciskZamknij = new Button("ZAMKNIJ");

        przyciskOblicz.setOnAction(e -> obliczVAT());
        przyciskZamknij.setOnAction(e -> glownaScena.close());

        HBox.setHgrow(przyciskOblicz, Priority.ALWAYS);
        HBox.setHgrow(przyciskZamknij, Priority.ALWAYS);

        przyciskOblicz.setMaxWidth(Double.MAX_VALUE);
        przyciskZamknij.setMaxWidth(Double.MAX_VALUE);

        ukladPrzyciskow.getChildren().addAll(przyciskOblicz, przyciskZamknij);
        glownyUklad.getChildren().add(ukladPrzyciskow);

        GridPane ukladWynikow = new GridPane();
        ukladWynikow.setHgap(10);
        ukladWynikow.setVgap(5);

        Label labelNetto = new Label("Netto:");
        labelWynikNetto = new Label("0.00");
        Label labelVAT = new Label("VAT:");
        labelWynikVAT = new Label("0.00");
        Label labelBrutto = new Label("Brutto:");
        labelWynikBrutto = new Label("0.00");

        ukladWynikow.add(labelNetto, 0, 0);
        ukladWynikow.add(labelWynikNetto, 1, 0);
        ukladWynikow.add(labelVAT, 0, 1);
        ukladWynikow.add(labelWynikVAT, 1, 1);
        ukladWynikow.add(labelBrutto, 0, 2);
        ukladWynikow.add(labelWynikBrutto, 1, 2);

        TitledPane sekcjaWynikow = new TitledPane("Wyniki:", ukladWynikow);
        sekcjaWynikow.setCollapsible(false);
        glownyUklad.getChildren().add(sekcjaWynikow);

        Scene scena = new Scene(glownyUklad, 400, 500);
        glownaScena.setScene(scena);
        glownaScena.show();
    }

    private void obliczVAT() {
        try {
            double wartoscBazowa = Double.parseDouble(poleWartosciBazowej.getText());
            double stawkaVAT = wyborStawkiVAT.getValue() / 100.0;
            double netto, vat, brutto;

            if (nettoDoBrutto.isSelected()) {
                netto = wartoscBazowa;
                vat = netto * stawkaVAT;
                brutto = netto + vat;
                aktualizujWyniki(netto, vat, brutto);
            } else if (bruttoDoNetto.isSelected()) {
                brutto = wartoscBazowa;
                netto = brutto / (1 + stawkaVAT);
                vat = brutto - netto;
                aktualizujWyniki(netto, vat, brutto);
            } else if (dopasujDoVAT.isSelected()) {
                vat = wartoscBazowa;
                netto = vat / stawkaVAT;
                brutto = netto + vat;
                aktualizujWyniki(netto, vat, brutto);
            }
        } catch (NumberFormatException ex) {
            labelWynikNetto.setText("Błędna");
            labelWynikVAT.setText("wartość");
            labelWynikBrutto.setText("bazowa");
        }
    }

    private void aktualizujWyniki(double netto, double vat, double brutto) {
        labelWynikNetto.setText(String.format("%.2f", netto));
        labelWynikVAT.setText(String.format("%.2f @ %.0f%%", vat, wyborStawkiVAT.getValue()));
        labelWynikBrutto.setText(String.format("%.2f", brutto));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
