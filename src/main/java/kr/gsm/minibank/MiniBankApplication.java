package kr.gsm.minibank;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.COMPACT_STYLESHEET)
@StyleSheet("styles.css")
@SpringBootApplication
public class MiniBankApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(MiniBankApplication.class, args);
	}

}
