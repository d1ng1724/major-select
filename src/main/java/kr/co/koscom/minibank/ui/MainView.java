package kr.co.koscom.minibank.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {
    public MainView() {
        H1 title = new H1("미니 금융 ERP");
        Paragraph desc = new Paragraph("1일차에 만든 계좌 API를 화면으로 확인합니다.");

        // 1. 고객 관리 버튼 (주요 액션: Primary 색상 + 큰 사이즈 + 아이콘)
        Button goToCustomers = new Button("고객 관리", VaadinIcon.USERS.create(),
                e -> getUI().ifPresent(ui -> ui.navigate("customers")));
        goToCustomers.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);

        // 2. 계좌 목록 버튼 (보조 액션: 테두리만 있는 스타일 또는 큰 사이즈 + 아이콘)
        Button goToAccounts = new Button("계좌 목록 보기", VaadinIcon.WALLET.create(),
                e -> getUI().ifPresent(ui -> ui.navigate("accounts")));
        goToAccounts.addThemeVariants(ButtonVariant.LUMO_LARGE);

        HorizontalLayout buttons = new HorizontalLayout(goToCustomers, goToAccounts);
        buttons.setSpacing(true);

        add(title, desc, buttons);

        // 화면 전체를 채우고 완벽한 중앙 정렬
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }
}
