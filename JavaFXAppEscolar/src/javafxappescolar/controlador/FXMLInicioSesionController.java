/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.dao.InicioSesionDAO;
import javafxappescolar.modelo.pojo.Usuario;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection conexionBD = ConexionBD.abrirConexion();
    }    

    @FXML
    private void btnClicVerificar(ActionEvent event) {
        String username = tfUsuario.getText();
        String password = tfPassword.getText();
        if (validarCampos(username, password)) {
            validarCredenciales(username, password);
        }
    }
    
    private boolean validarCampos(String username, String password) {
        lbErrorUsuario.setText("");
        lbErrorPassword.setText("");
        boolean camposValidos = true;
        if (username.isEmpty()) {
            lbErrorUsuario.setText("Usuario obligatorio.");
            camposValidos = false;
        }
        if (password.isEmpty()) {
            lbErrorPassword.setText("Contraseña obligatoria");
            camposValidos = false;
        }
        return camposValidos;
    }
    
    private void validarCredenciales(String username, String password) {
        try {
            Usuario usuarioSesion = InicioSesionDAO.verificarCredenciales(username, password);
            if (usuarioSesion != null) {
                //TODO flujo normal
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales Correctas",
                        "Bienvenido(a) " + usuarioSesion.toString() + " al sistema");
                irPantallaPrincipal(usuarioSesion);
            } else {
                //TODO flujo alterno 1
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Credenciales Incorrectas",
                        "Usuario y/o contraseña incorrectos. Verifica la información");
            }
        } catch (SQLException ex) {
            //TODO Flujo excepcion
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas de conexión",
                        ex.getMessage());
        }
        
    }
    
    private void irPantallaPrincipal(Usuario usuarioSesion) {
        try {
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            
            FXMLPrincipalController controlador = cargador.getController();
            controlador.inicializarInformacion(usuarioSesion);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Home");
            escenarioBase.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
