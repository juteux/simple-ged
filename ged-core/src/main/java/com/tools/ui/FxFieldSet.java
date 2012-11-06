package com.tools.ui;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * @see http://code.google.com/p/javafx-demos/source/browse/trunk/javafx-demos/src/main/java/com/ezest/javafx/demogallery/FieldSetDemo.java
 *
 * FieldSet Component.
 * @author Sai.Dandem
 * 
 */
public class FxFieldSet extends StackPane {

	 private Label legend = new Label("");
     //private Node legendNode;
     private StackPane contentBox = new StackPane();
     private StackPane legendBox = new StackPane();
     
     public FxFieldSet(String legendStr){
             super();
             this.legend.setText(legendStr);
             legendBox.getChildren().add(legend);
             configureFieldSet();
     }
     
     public FxFieldSet(Node legendNode){
             super();
             //this.legendNode = legendNode;
             legendBox.getChildren().add(legendNode);
             configureFieldSet();
     }
     
     private void configureFieldSet(){
             super.setPadding(new Insets(10,0,0,0));
             super.setAlignment(Pos.TOP_LEFT);
             super.getStyleClass().add("fieldSetDefault");
             
             contentBox.getStyleClass().add("fieldSet");
             contentBox.setAlignment(Pos.TOP_LEFT);
             contentBox.setPadding(new Insets(8, 2, 2, 2));
             
             legendBox.setPadding(new Insets(0, 5, 0, 5));
             
             Group gp = new Group();
             gp.setTranslateX(20);
             gp.setTranslateY(-8);
             gp.getChildren().add(legendBox);
             
             super.getChildren().addAll(contentBox, gp);
             setBackGroundColor("#FFFFFF");
             
             /* Adding listeners for styles. */
             getStyleClass().addListener(new ListChangeListener<String>() {
                     @Override
                     public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> paramChange) {
                             System.out.println("-->"+getStyleClass());
                             System.out.println(contentBox.getStyleClass());
                             contentBox.getStyleClass().clear();
                             contentBox.getStyleClass().addAll("fieldSet");
                             for (String clazz : getStyleClass()) {
                                     if (!clazz.equals("fieldSetDefault")) {
                                             contentBox.getStyleClass().add(clazz);
                                     }
                             }
                     }
             });
     }
     
     /*
      * PUBLIC METHODS
      */
     
     public void setContent(Node content){
             contentBox.getChildren().add(content);
     }
     
     public void setBackGroundColor(String color){
             super.setStyle("-fx-background-color:"+color+";");
             contentBox.setStyle("-fx-background-color:"+color+";");
             legendBox.setStyle("-fx-background-color:"+color+";");
     }
     
     public void setStyleClassForBorder(String claz){
             contentBox.getStyleClass().add(claz);
     }
     public void removeStyleClassForBorder(String claz){
             contentBox.getStyleClass().remove(claz);
     }
}
